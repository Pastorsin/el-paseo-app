package laboratorio.app.adapters;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import laboratorio.app.R;
import laboratorio.app.fragments.ProductDetailFragment;
import laboratorio.app.helpers.FragmentLoader;
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

    private void setPrice(Product product, View view) {
        TextView priceView = (TextView) view.findViewById(R.id.product_lisitem_price);

        String priceText = String.format(
                getContext().getString(R.string.product_price_format),
                product.getBuyPrice()
        );

        priceView.setText(priceText);
    }

    private void setUnitQuantity(Product product, View view) {
        TextView unitView = view.findViewById(R.id.product_listitem_unit_quantity);

        unitView.setText(product.getUnitQuantity().toString());
    }

    private void setUnitDescription(Product product, View view) {
        TextView unitView = view.findViewById(R.id.product_listitem_unit_description);

        unitView.setText(product.getUnit().getCode());
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
        setPrice(product, convertView);
        setUnitQuantity(product, convertView);
        setUnitDescription(product, convertView);
        setImage(product, convertView);

        convertView.setOnClickListener(view -> {
            FragmentLoader loader = (FragmentLoader) getContext();
            Fragment fragment = ProductDetailFragment.newInstance(product);

            loader.replaceFragmentOnMainContainer(fragment);
        });

        return convertView;
    }

}
