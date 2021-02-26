package laboratorio.app.fragments;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import laboratorio.app.R;
import laboratorio.app.adapters.OrdersAdapter;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.helpers.OnItemListener;
import laboratorio.app.helpers.PageCallback;
import laboratorio.app.models.Cart;

import laboratorio.app.models.Pagination;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryOrdersListFragment extends Fragment implements OnItemListener {

    private OrdersAdapter adapter;
    private APIService service = API.instance.getService();
    private List<Cart> ordersList = new ArrayList<>();

    private ProgressBar progressBar;

    public DeliveryOrdersListFragment() {
        // Required empty public constructor
    }


    public static DeliveryOrdersListFragment newInstance() {
        DeliveryOrdersListFragment fragment = new DeliveryOrdersListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_orders_list, container, false);

        RecyclerView ordersView = view.findViewById(R.id.orders_recyclerView);

        progressBar = view.findViewById(R.id.progress_bar);

        boolean isUserLoggedIn = ApiSession.instance.isUserLoggedIn(getContext());

        adapter = new OrdersAdapter(ordersList, this::onItemClick);
        ordersView.setAdapter(adapter);
        ordersView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(isUserLoggedIn){
            ApiSession.instance.getToken(getContext(), fetchOrders(view));
        }else{
            getActivity().finish();
        }

        return view;
    }

    private AccountManagerCallback<Bundle> fetchOrders(View view){
        return accountManagerFuture -> {
            try {
                String token = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                API.instance.getService().getCart(token).enqueue(getResponseCallback(view));

            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            }
        };
    }

    private Callback<Pagination<Cart>> getResponseCallback(View view) {
        return new PageCallback<Pagination<Cart>>(progressBar, view, (FragmentLoader) getContext()) {
            @Override
            public void onResponse(Call<Pagination<Cart>> call, Response<Pagination<Cart>> response) {
                hideProgressBar();
                addOrders(response.body().getPage());
                adapter.notifyDataSetChanged();
            }
        };
    }

    private void addOrders(List<Cart> allOrders) {
        ordersList.clear();
        ordersList.addAll(ordersToShow(allOrders));
    }

    private List<Cart> ordersToShow(List<Cart> allOrders) {
        return allOrders.stream().filter(Cart::isDeliverable).collect(Collectors.toList());
    }

    @Override
    public void onItemClick(int position) {
        Cart cart = ordersList.get(position);

        FragmentLoader loader = (FragmentLoader) getContext();
        Fragment fragment = OrderDetailFragment.newInstance(cart);

        loader.replaceFragmentOnMainContainer(fragment);

    }

}