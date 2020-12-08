package laboratorio.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import laboratorio.app.R;
import laboratorio.app.adapters.CategoryAdapter;
import laboratorio.app.controllers.APIService;
import laboratorio.app.controllers.API;
import laboratorio.app.models.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryListActivity extends ListActivity<Category> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
    }

    @Override
    protected void showList(List<Category> categories) {
        ArrayAdapter categoryAdapter = new CategoryAdapter(this, categories);
        GridView categoriesList = (GridView) findViewById(R.id.categories_list);

        categoriesList.setAdapter(categoryAdapter);
    }

    @Override
    protected List elementsToShow(List<Category> allCategories) {
        return  allCategories.stream()
                .filter(category -> !category.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    protected Call<List<Category>> getCall() {
        return service.getCategories();
    }

}