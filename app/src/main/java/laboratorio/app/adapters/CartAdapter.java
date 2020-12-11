package laboratorio.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import laboratorio.app.R;
import laboratorio.app.models.CartProduct;
import laboratorio.app.models.Product;

public class CartAdapter extends ArrayAdapter<CartProduct> {

    public CartAdapter(Context context, List<CartProduct> cartProductList){
        super(context,0,cartProductList);
    }

    private void setTitle(Product product, View view) {
        TextView titleView = (TextView) view.findViewById(R.id.cart_product_name);
        titleView.setText(product.getTitle());
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
        priceView.setText("$" + cartProduct.getTotalCartProductPrice().toString());
    }

    private void setQuantity(CartProduct cartProduct, View view){
        TextView quantityView = (TextView) view.findViewById(R.id.cart_product_quantity);
        quantityView.setText(cartProduct.getQuantity());
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

        return convertView;
    }

}
