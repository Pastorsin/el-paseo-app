package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import laboratorio.app.R;
import laboratorio.app.adapters.CartAdapter;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.Cart;
import laboratorio.app.models.CartProduct;

public class CartFragment extends Fragment {
    private ArrayAdapter adapter;
    private TextView totalPriceView;

    private Cart cart = Cart.instance;
    private List<CartProduct> cartProducts = cart.getCartProducts();

    private static boolean isActionMode = false;
    private static List<CartProduct> cartProductsSelected = new ArrayList<>();
    private static ActionMode mode = null;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (cart.isEmpty()) {
            return inflater.inflate(R.layout.fragment_cart_empty_cart, container, false);
        } else {
            View view =  inflater.inflate(R.layout.fragment_cart, container, false);

            Button buy_button = view.findViewById(R.id.cart_buy_button);

            buy_button.setOnClickListener(v -> Snackbar.make(v, "Compra realizada!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());

            ListView cartProductsView = view.findViewById(R.id.cart_products_list);

            // CONTEXTUAL MENU
            cartProductsView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            cartProductsView.setMultiChoiceModeListener(modeListener);

            adapter = new CartAdapter(getContext(), cartProducts);
            cartProductsView.setAdapter(adapter);

            addTotalPrice(view);

            return view;
        }
    }

    private void addTotalPrice(View view){
        totalPriceView = (TextView) view.findViewById(R.id.cart_total_price_text);
        String priceFormat = getContext().getString(R.string.product_price_format);
        Double totalPrice = cart.getTotal();

        totalPriceView.setText(String.format(priceFormat, totalPrice));
    }

    public static boolean isActionMode(){
        return isActionMode;
    }

    public static ActionMode getActionMode(){return mode;}

    public static List<CartProduct> getUserSelectionList(){return cartProductsSelected;}

    private void loadCartFragment() {
        FragmentLoader loader = (FragmentLoader) getContext();
        loader.replaceFragmentOnMainContainer(new CartFragment());
    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {}

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.cart_context_menu,menu);

            isActionMode = true;
            mode = actionMode;

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.cart_remove:
                    cart.remove(cartProductsSelected);
                    actionMode.finish();
                    loadCartFragment();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            isActionMode = false;
            mode = null;
            cartProductsSelected.clear();
        }
    };

}