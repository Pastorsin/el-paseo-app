package laboratorio.app.fragments;

import android.accounts.AccountManager;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import laboratorio.app.R;
import laboratorio.app.adapters.PurchaseAdapter;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.fragments.forms.purchase.items.PurchaseDetailFormFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.Cart;
import laboratorio.app.models.User;
import laboratorio.app.viewmodels.PurchaseListViewModel;
import laboratorio.app.viewmodels.PurchaseViewModel;

public class PurchaseListFragment extends Fragment {

    private PurchaseListViewModel viewmodel;
    private ViewModelProvider provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_purchase_list, container, false);

        provider = new ViewModelProvider(requireActivity());
        viewmodel = provider.get(PurchaseListViewModel.class);

        if (ApiSession.instance.isUserLoggedIn(getContext())) {
            RecyclerView recyclerView = view.findViewById(R.id.purchase_list);

            loadPurchaseList(recyclerView);
        } else {
            onUserNotLogged();
        }

        return view;
    }

    private void loadPurchaseList(RecyclerView recyclerView) {
        getPurchaseListOfUserLogged().observe(getViewLifecycleOwner(), pair -> {
            List<Cart> purchaseList = pair.first;
            User userLogged = pair.second;

            if (purchaseList == null) {
                onError();
                return;
            }

            if (purchaseList.isEmpty()) {
                onEmptyList();
                return;
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            DividerItemDecoration separator = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
            recyclerView.addItemDecoration(separator);

            PurchaseAdapter adapter = new PurchaseAdapter(purchaseList, viewmodel);
            recyclerView.setAdapter(adapter);

            setOnClickListener(userLogged);
        });
    }

    private void setOnClickListener(User userLogged) {
        viewmodel.targetPurchase.observe(
                getViewLifecycleOwner(),
                purchase -> {
                    PurchaseViewModel viewmodelDetail = provider.get(PurchaseViewModel.class);
                    viewmodelDetail.initDetail(purchase, userLogged);
                    loadDetail();
                }
        );
    }

    private void loadDetail() {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragmentOnMainContainer(new PurchaseDetailFormFragment());
    }

    private void onEmptyList() {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragmentOnMainContainer(new EmptyListFragment());
    }

    private MutableLiveData<Pair<List<Cart>, User>> getPurchaseListOfUserLogged() {
        MutableLiveData<Pair<List<Cart>, User>> purchaseListResponse = new MutableLiveData<>();

        getToken().observe(getViewLifecycleOwner(), token ->
                getUserLogged().observe(getViewLifecycleOwner(), user -> {
                    if (user == null || token == null) {
                        onUserNotLogged();
                        return;
                    }

                    viewmodel.getPurchases(user, token).observe(
                            getViewLifecycleOwner(),
                            purchaseList -> purchaseListResponse.setValue(new Pair<>(purchaseList, user))
                    );
                }));

        return purchaseListResponse;
    }

    private MutableLiveData<User> getUserLogged() {
        return ApiSession.instance.getUserLogged(getContext());
    }

    private MutableLiveData<String> getToken() {
        MutableLiveData<String> tokenResponse = new MutableLiveData<>();

        ApiSession.instance.getToken(getContext(), accountManagerFuture -> {
            try {
                String token = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                tokenResponse.setValue(token);

            } catch (Exception e) {
                tokenResponse.setValue(null);
                Log.e("PURCHASE LIST", "Error getting token", e);
            }
        });

        return tokenResponse;
    }

    private void onUserNotLogged() {
        onError();
        Toast.makeText(getContext(), R.string.not_user_logged_error, Toast.LENGTH_SHORT);
    }

    private void onError() {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragmentOnMainContainer(new ErrorFragment());
    }
}