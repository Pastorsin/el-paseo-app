package laboratorio.app.controllers;

import java.util.List;

import laboratorio.app.models.Category;
import laboratorio.app.models.LoginUser;
import laboratorio.app.models.Product;
import laboratorio.app.models.Token;
import laboratorio.app.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @GET("api/category")
    Call<List<Category>> getCategories();

    @GET("api/product")
    Call<List<Product>> getProducts();

    @POST("api/token/generate-token")
    Call<Token> signIn(@Body LoginUser loginUser);

    @POST("api/user/signup")
    Call<User> signUp(@Body User user);
}
