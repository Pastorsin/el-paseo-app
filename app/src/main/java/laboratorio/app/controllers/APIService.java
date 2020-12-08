package laboratorio.app.controllers;

import java.util.List;

import laboratorio.app.models.Category;
import laboratorio.app.models.Product;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("api/category")
    Call<List<Category>> getCategories();

    @GET("api/product")
    Call<List<Product>> getProducts();
}
