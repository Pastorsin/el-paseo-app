package laboratorio.app.controllers;

import java.util.List;

import laboratorio.app.models.Category;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryController {

    @GET("api/category")
    Call<List<Category>> getCategories();
}
