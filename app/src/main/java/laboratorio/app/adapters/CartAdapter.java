package laboratorio.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import laboratorio.app.R;

import laboratorio.app.fragments.ProductDetailFragment;
import laboratorio.app.helpers.FragmentLoader;

import laboratorio.app.fragments.CartFragment;
import laboratorio.app.models.Cart;

import laboratorio.app.models.CartProduct;
import laboratorio.app.models.Product;

public class CartAdapter extends ArrayAdapter<CartProduct> {

    public CartAdapter(Context context, List<CartProduct> cartProductList){
        super(context,0,cartProductList);
    }

    private void setTitle(Product product, View view) {
        TextView titleView = (TextView) view.findViewById(R.id.cart_product_name);
        titleView.setText(product.getTitle());
        titleView.setOnClickListener(v -> {
            loadProductFragment(product);
        });
    }

    private void setImage(Product product, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.cart_product_image);

        if (product.hasMainImage()) {
            Bitmap bitmap = product.getMainImage().bitmap();
            imageView.setImageBitmap(bitmap);
        }
    }

    private void setProductTotalPrice(CartProduct cartProduct, View view){
        TextView priceView = (TextView) view.findViewById(R.id.cart_item_price);
        String priceFormat = getContext().getString(R.string.product_price_format);
        Double totalPrice = cartProduct.getTotalCartProductPrice();

        priceView.setText(String.format(priceFormat, totalPrice));
    }

    private void setQuantity(CartProduct cartProduct, View view){
        TextView quantityView = (TextView) view.findViewById(R.id.cart_product_quantity);
        quantityView.setText(cartProduct.getQuantity().toString());
    }


    private void loadProductFragment(Product product) {
        FragmentLoader loader = (FragmentLoader) getContext();
        ProductDetailFragment fragment = ProductDetailFragment.newInstance(product);

        loader.replaceFragmentOnMainContainer(fragment);
    }

    private void setCheckBox(View view,int position){
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        checkBox.setTag(position);

        if(CartFragment.isActionMode()){
            checkBox.setVisibility(View.VISIBLE);
        }else{
            checkBox.setVisibility(View.GONE);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = (int) compoundButton.getTag();

                if(CartFragment.getUserSelectionList().contains(getItem(position))){
                    CartFragment.getUserSelectionList().remove(getItem(position));
                }else{
                    CartFragment.getUserSelectionList().add(getItem(position));
                }

                CartFragment.getActionMode().setTitle("Seleccionados: " + CartFragment.getUserSelectionList().size());
            }
        });
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fragment_cart_list_item,
                    parent,
                    false
            );
        }

        CartProduct cartProduct = getItem(position);
        Product product = cartProduct.getProduct();

        setTitle(product,convertView);
        setImage(product,convertView);
        setProductTotalPrice(cartProduct,convertView);
        setQuantity(cartProduct,convertView);
        setCheckBox(convertView,position);

        //convertView.setOnClickListener(view -> {
        //    loadProductFragment(product);
        //});

        return convertView;
    }
}
