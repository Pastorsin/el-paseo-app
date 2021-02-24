package laboratorio.app.fragments;

import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import laboratorio.app.R;
import laboratorio.app.adapters.ProductOrderAdapter;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.models.Cart;
import laboratorio.app.models.CartProduct;

import static java.util.stream.Collectors.summingDouble;


public class OrderDetailFragment extends Fragment {

    private static final String CART_ARG = "cart_arg";

    private Serializable cartArg;

    private Cart cart;

    private ProductOrderAdapter adapter;

    private List<CartProduct> cartProductList;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    public static OrderDetailFragment newInstance(Cart aCart) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(CART_ARG, aCart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cartArg = getArguments().getSerializable(CART_ARG);
            cart = (Cart) cartArg;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order_detail, container, false);

        RecyclerView products_view = view.findViewById(R.id.products_list);

        adapter = new ProductOrderAdapter(cart.getCartProducts());
        products_view.setAdapter(adapter);
        products_view.setLayoutManager(new LinearLayoutManager(getContext()));

        cartProductList = cart.getCartProducts();

        setTotalPrice(view);
        setPayMethod(view);
        setDeliveryAddress(view);
        setAcceptButton(view);

        return view;
    }

    private void setTotalPrice(View view){
        TextView total_price_text_view = view.findViewById(R.id.order_product_total_price_textView);
        Double sum = cartProductList.stream().map(cartProduct -> (Double) cartProduct.getTotalCartProductPrice())
                .collect(summingDouble(f -> f));
        total_price_text_view.setText(sum.toString());
    }

    private void setPayMethod(View view){
        TextView pay_method_text_view = view.findViewById(R.id.pay_method_input);
        TextView pay_description_text_view = view.findViewById(R.id.pay_method_description_input);
        ImageButton icon_button_view = view.findViewById(R.id.pay_method_icon);
        pay_method_text_view.setText("Efectivo");
        pay_description_text_view.setText(cart.getTotal().toString());
        Icon icon = Icon.createWithResource(getContext(), R.drawable.ic_round_money_24);//'drawable/ic_round_money_24 : drawable/ic_baseline_credit_card_24}
        icon_button_view.setImageIcon(icon);
    }

    private void setDeliveryAddress(View view){
        TextView address_text_view = view.findViewById(R.id.delivery_method_address_input);
        ApiSession.instance.getUserLogged(getContext()).observe(getViewLifecycleOwner(), user -> {
            address_text_view.setText(user.getAddress().fullAddress());
        });
    }

    private void setAcceptButton(View view){
        Button acceptButton = view.findViewById(R.id.order_accept_button);
        acceptButton.setOnClickListener(click -> {
            Toast.makeText(getContext(), "Orden tomada!", Toast.LENGTH_SHORT).show();
            acceptButton.setVisibility(View.GONE);
        });
    }

}