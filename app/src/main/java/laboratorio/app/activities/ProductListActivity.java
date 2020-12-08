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

public class ProductListActivity extends AppCompatActivity {

    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        category = (Category) getIntent().getSerializableExtra("CATEGORY");

        APIService service = API.instance.getService();

        service.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> allProducts = response.body();
                List<Product> productsToShow = productsToShow(allProducts);
                showProductList(productsToShow);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                System.out.println("Falló");
            }
        });
    }

    private List<Product> productsToShow(List<Product> allProducts) {
        return allProducts.stream()
                .filter(product -> product.hasCategory(category))
                .collect(Collectors.toList());
    }

    private void showProductList(List<Product> products) {
        ArrayAdapter productAdapter = new ProductAdapter(this, products);
        ListView productList = (ListView) findViewById(R.id.products_list);

        productList.setAdapter(productAdapter);
    }
}