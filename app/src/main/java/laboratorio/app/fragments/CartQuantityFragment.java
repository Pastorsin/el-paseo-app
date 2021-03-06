package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

import laboratorio.app.R;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.UserCart;
import laboratorio.app.models.CartProduct;
import laboratorio.app.models.Product;

public class CartQuantityFragment extends Fragment {

    private static final String PRODUCT_ARG = "product_arg";

    private Serializable product_arg;

    private Product product;
    private CartProduct cartProduct;
    private UserCart userCart = UserCart.instance;

    public CartQuantityFragment() {
        // Required empty public constructor
    }

    public static CartQuantityFragment newInstance(Product aProduct) {
        CartQuantityFragment fragment = new CartQuantityFragment();
        Bundle args = new Bundle();
        args.putSerializable(PRODUCT_ARG, aProduct);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product_arg = getArguments().getSerializable(PRODUCT_ARG);
            product = (Product) product_arg;
            cartProduct = userCart.getCartProductOrCreate(product);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart_quantity, container, false);

        TextView quantityInput = view.findViewById(R.id.cart_product_quantity_input);
        Button addToCartButton = view.findViewById(R.id.add_to_cart_button);

        onChangeQuantity(quantityInput, addToCartButton);

        view.findViewById(R.id.quantity_increment_button)
                .setOnClickListener(this.incrementQuantity(quantityInput, addToCartButton));

        view.findViewById(R.id.quantity_decrement_button)
                .setOnClickListener(this.decrementQuantity(quantityInput, addToCartButton));

        view.findViewById(R.id.add_to_cart_button)
                .setOnClickListener(this.addToCart(quantityInput, addToCartButton));

        return view;
    }

    private View.OnClickListener addToCart(TextView quantityInput, Button addToCartButton) {
        return view -> {
            userCart.addProduct(cartProduct);
            loadCartFragment();
        };
    }

    private void loadCartFragment() {
        FragmentLoader loader = (FragmentLoader) getContext();
        Fragment cartFragment = new CartFragment();

        loader.replaceFragmentOnMainContainer(cartFragment);
    }

    private View.OnClickListener decrementQuantity(TextView quantityInput, Button addToCartButton) {
        return view -> {
            if (cartProduct.decrementQuantity()){
                onChangeQuantity(quantityInput, addToCartButton);
            }
        };
    }

    private View.OnClickListener incrementQuantity(TextView quantityInput, Button addToCartButton) {
        return view -> {
            if (cartProduct.incrementQuantity()) {
                onChangeQuantity(quantityInput, addToCartButton);
            }
        };
    }

    private void onChangeQuantity(TextView quantityInput, Button addToCartButton) {
        renderQuantity(quantityInput);
        renderCartButton(addToCartButton);
    }

    private void renderQuantity(TextView view) {
        Integer quantity = cartProduct.getQuantity();
        view.setText(quantity.toString());
    }

    private void renderCartButton(Button button) {
        int formatForTextButton = userCart.contains(cartProduct) ?
                R.string.modify_product_cart_button_format :
                R.string.add_to_cart_button_format;

        double total = cartProduct.getTotalCartProductPrice();

        String textToShow = String.format(getString(formatForTextButton), total);

        button.setText(textToShow);
    }
}