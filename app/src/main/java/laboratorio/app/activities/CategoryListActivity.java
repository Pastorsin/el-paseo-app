package laboratorio.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import laboratorio.app.R;
import laboratorio.app.adapters.CategoryAdapter;
import laboratorio.app.controllers.CategoryController;
import laboratorio.app.controllers.Connection;
import laboratorio.app.models.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        CategoryController service = Connection.conn.getRetrofit().create(CategoryController.class);

        service.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> allCategories = response.body();
                List<Category> categoriesToShow = categoriesToShow(allCategories);

                showCategoriesList(categoriesToShow);

                System.out.println("Categories loaded successfully");
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                System.out.println("Error to load categories");
                showErrorMessage();
            }
        });
    }

    private List<Category> categoriesToShow(List<Category> allCategories) {
        return allCategories.stream()
                .filter(category -> !category.isDeleted())
                .collect(Collectors.toList());
    }

    private void showErrorMessage() {
        Intent intent = new Intent(this, ErrorActivity.class);
        startActivity(intent);
    }

    private void showCategoriesList(List<Category> categories) {
        ArrayAdapter categoryAdapter = new CategoryAdapter(this, categories);
        GridView categoriesList = (GridView) findViewById(R.id.categories_list);

        categoriesList.setAdapter(categoryAdapter);
    }
}