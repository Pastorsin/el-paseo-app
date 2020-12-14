package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import laboratorio.app.models.Cart;
import laboratorio.app.models.CartProduct;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

     */

    private Cart cart = Cart.instance;
    private ArrayAdapter adapter;
    private TextView totalPrice;

    private static boolean isActionMode = false;
    private static List<CartProduct> userSelection = new ArrayList<>();
    private static ActionMode mode = null;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

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

            adapter = new CartAdapter(getContext(),cart.getCartProducts());

            ListView cartProductsView = view.findViewById(R.id.cart_products_list);

            // CONTEXTUAL MENU
            cartProductsView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            cartProductsView.setMultiChoiceModeListener(modeListener);

            cartProductsView.setAdapter(adapter);

            addTotalPrice(view);

            return view;
        }
    }

    private void addTotalPrice(View view){
        totalPrice = (TextView) view.findViewById(R.id.cart_total_price_text);
        totalPrice.setText(totalPrice.getText() + Cart.instance.getTotal().toString());
    }

    public static boolean isActionMode(){
        return isActionMode;
    }

    public static ActionMode getActionMode(){return mode;}

    public static List<CartProduct> getUserSelectionList(){return userSelection;}

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
                    for (CartProduct cartProduct : userSelection) {
                        Cart.instance.removeProduct(cartProduct);
                    }
                    totalPrice.setText("Total:" + Cart.instance.getTotal().toString());
                    actionMode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            isActionMode = false;
            mode = null;
            userSelection.clear();
        }
    };

}