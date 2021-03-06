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

import java.util.ArrayList;
import java.util.List;

import laboratorio.app.R;
import laboratorio.app.adapters.CartAdapter;
import laboratorio.app.fragments.forms.purchase.PurchaseFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.UserCart;
import laboratorio.app.models.CartProduct;

public class CartFragment extends Fragment {
    private ArrayAdapter adapter;
    private TextView totalPriceView;

    private UserCart userCart = UserCart.instance;
    private List<CartProduct> cartProducts = userCart.getCartProducts();

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
        if (userCart.isEmpty()) {
            return inflater.inflate(R.layout.fragment_cart_empty_cart, container, false);
        } else {
            View view =  inflater.inflate(R.layout.fragment_cart, container, false);

            initBuyButton(view);

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

    private void initBuyButton(View view) {
        Button buy_button = view.findViewById(R.id.cart_buy_button);
        buy_button.setOnClickListener(buttonView -> loadFragment(new PurchaseFragment()));
    }

    private void loadFragment(Fragment fragment) {
        ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
    }

    private void addTotalPrice(View view){
        totalPriceView = (TextView) view.findViewById(R.id.cart_total_price_text);
        String priceFormat = getContext().getString(R.string.product_price_format);
        Double totalPrice = userCart.getTotal();

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
                    userCart.remove(cartProductsSelected);
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