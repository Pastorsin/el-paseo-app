package laboratorio.app.controllers;

import java.util.List;

import laboratorio.app.models.Product;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductController {

    @GET("api/product")
    Call<List<Product>> getProducts();
}
