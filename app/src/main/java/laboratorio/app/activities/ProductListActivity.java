package laboratorio.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import laboratorio.app.R;

import android.os.Bundle;

import laboratorio.app.adapters.ProductAdapter;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.models.Category;
import laboratorio.app.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.stream.Collectors;

public class ProductListActivity extends ListActivity<Product> {

    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        category = (Category) getIntent().getSerializableExtra("CATEGORY");
    }

    @Override
    protected void showList(List<Product> products) {
        ArrayAdapter productAdapter = new ProductAdapter(this, products);
        ListView productList = (ListView) findViewById(R.id.products_list);

        productList.setAdapter(productAdapter);
    }

    @Override
    protected List elementsToShow(List<Product> allProducts) {
        return allProducts.stream()
                .filter(product -> product.hasCategory(category))
                .collect(Collectors.toList());
    }

    @Override
    protected Call<List<Product>> getCall() {
        return service.getProducts();
    }

}