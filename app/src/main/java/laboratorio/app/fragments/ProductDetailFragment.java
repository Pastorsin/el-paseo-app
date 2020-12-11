package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import laboratorio.app.R;
import laboratorio.app.models.Product;

public class ProductDetailFragment extends Fragment {

    private static final String PRODUCT_ARG = "product_arg";

    private Serializable productArg;

    private Product product;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    public static ProductDetailFragment newInstance(Product aProduct) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(PRODUCT_ARG, aProduct);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productArg = getArguments().getSerializable(PRODUCT_ARG);
            product = (Product) productArg;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        addImage(view);
        addTitle(view);
        addPrice(view);
        addBrand(view);
        addUnitQuantity(view);
        addUnitDescription(view);
        addDescription(view);
        addTabs(view);

        return view;
    }

    private void addUnitDescription(View view) {
        TextView unitsView = view.findViewById(R.id.product_unit_description);
        String unitsToShow = product.getUnit().getCode();
        unitsView.setText(unitsToShow);
    }

    private void addUnitQuantity(View view) {
        TextView unitsView = view.findViewById(R.id.product_unit_quantity);
        unitsView.setText(product.getUnitQuantity().toString());
    }

    private void addImage(View view) {
        if (product.hasMainImage()) {
            ImageView imageView = view.findViewById(R.id.product_image);
            imageView.setImageBitmap(product.getMainImage().bitmap());
        }
    }

    private void addTabs(View view) {
        TabLayout productTab = view.findViewById(R.id.product_tab);

        productTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private final TextView tabContentView = view.findViewById(R.id.product_tab_content);
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        tabContentView.setText(product.getDescription());
                        break;
                    case 1:
                        tabContentView.setText(product.getProducer().getName());
                        break;
                    case 2:
                        tabContentView.setText("Adicional");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void addDescription(View view) {
        TextView tabContentView = view.findViewById(R.id.product_tab_content);
        tabContentView.setText(product.getDescription());
    }

    private void addBrand(View view) {
        TextView brandView = view.findViewById(R.id.product_brand);
        brandView.setText(product.getBrand());
    }

    private void addPrice(View view) {
        TextView priceView = view.findViewById(R.id.product_price);
        String priceToShow = String.format(getString(R.string.product_price_format),
                product.getBuyPrice());
        priceView.setText(priceToShow);
    }

    private void addTitle(View view) {
        TextView titleView = view.findViewById(R.id.product_title);
        titleView.setText(product.getTitle());
    }
}