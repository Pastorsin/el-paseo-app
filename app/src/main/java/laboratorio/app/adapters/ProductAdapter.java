package laboratorio.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import laboratorio.app.R;
import laboratorio.app.activities.ProductDetailActivity;
import laboratorio.app.models.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    private void setTitle(Product product, View view) {
        TextView titleView = (TextView) view.findViewById(R.id.product_listitem_title);
        titleView.setText(product.getTitle());
    }

    private void setDescription(Product product, View view) {
        TextView descriptionView = (TextView) view.findViewById(R.id.product_listitem_description);
        descriptionView.setText(product.getDescription());
    }

    private void setPricePerUnit(Product product, View view) {
        TextView priceView = (TextView) view.findViewById(R.id.product_lisitem_price_per_unit);
        String priceText = String.format(
                getContext().getString(R.string.product_price_per_unit_format),
                product.getPrice(),
                product.getUnitQuantity()
        );

        priceView.setText(priceText);
    }

    private void setImage(Product product, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.product_listitem_image);

        if (product.hasMainImage()) {
            Bitmap bitmap = product.getMainImage().bitmap();
            imageView.setImageBitmap(bitmap);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.product_list_item,
                    parent,
                    false
            );
        }

        Product product = getItem(position);

        setTitle(product, convertView);
        setDescription(product, convertView);
        setPricePerUnit(product, convertView);
        setImage(product, convertView);

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT", product);

            getContext().startActivity(intent);
        });

        return convertView;
    }

}
