package laboratorio.app.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import laboratorio.app.R;
import laboratorio.app.models.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product = (Product) getIntent().getSerializableExtra("PRODUCT");

        render();
    }

    private void render() {
        renderImage();
        renderTitle();
        renderPrice();
        renderBrand();
        renderUnits();
        renderDescription();
        renderTabs();
    }

    private void renderImage() {
        if (product.hasMainImage()) {
            ImageView imageView = findViewById(R.id.product_image);
            imageView.setImageBitmap(product.getMainImage().bitmap());
        }
    }

    private void renderTabs() {
        TabLayout productTab = findViewById(R.id.product_tab);

        productTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private final TextView tabContentView = findViewById(R.id.product_tab_content);
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

    private void renderDescription() {
        TextView tabContentView = findViewById(R.id.product_tab_content);
        tabContentView.setText(product.getDescription());
    }

    private void renderUnits() {
        TextView unitsView = findViewById(R.id.product_units);
        String unitsToShow = String.format(
                getString(R.string.product_units_format),
                product.getUnitQuantity()
        );
        unitsView.setText(unitsToShow);
    }

    private void renderBrand() {
        TextView brandView = findViewById(R.id.product_brand);
        brandView.setText(product.getBrand());
    }

    private void renderPrice() {
        TextView priceView = findViewById(R.id.product_price);
        String priceToShow = String.format(getString(R.string.product_price_format),
                product.getPrice());
        priceView.setText(priceToShow);
    }

    private void renderTitle() {
        TextView titleView = findViewById(R.id.product_title);
        titleView.setText(product.getTitle());
    }

}