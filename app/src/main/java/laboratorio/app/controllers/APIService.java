package laboratorio.app.controllers;

import java.util.List;

import laboratorio.app.models.Cart;
import laboratorio.app.models.Category;
import laboratorio.app.models.General;
import laboratorio.app.models.LoginUser;
import laboratorio.app.models.News;
import laboratorio.app.models.NewsletterSubscription;
import laboratorio.app.models.NewsletterSubscriptionResponse;
import laboratorio.app.models.Producer;
import laboratorio.app.models.Node;
import laboratorio.app.models.Pagination;
import laboratorio.app.models.Product;
import laboratorio.app.models.RecoveryPassword;
import laboratorio.app.models.RecoveryPasswordResponse;
import laboratorio.app.models.Token;
import laboratorio.app.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {
    @GET("api/category")
    Call<List<Category>> getCategories();

    @GET("api/product")
    Call<List<Product>> getProducts();

    @POST("api/token/generate-token")
    Call<Token> signIn(@Body LoginUser loginUser);

    @POST("api/user/signup")
    Call<User> signUp(@Body User user);

    @GET("api/producer")
    Call<List<Producer>> getProducers();

    @GET("api/user/{id}")
    Call<User> getUser(@Path("id") String id, @Header("Authorization") String token);

    @PUT("api/user")
    Call<User> putUser(@Body User user, @Header("Authorization") String token);

    @POST("/api/email/recovery")
    Call<RecoveryPasswordResponse> postSolicitCodeForRecoveryPassword(@Body RecoveryPassword recoveryPassword);

    @POST("/api/email/recovery/confirm")
    Call<RecoveryPasswordResponse> postConfirmRecoveryPassword(@Body RecoveryPassword recoveryPassword);

    @POST("api/newsletter")
    Call<NewsletterSubscriptionResponse> subscribeNewsletter(@Body NewsletterSubscription email, @Header("Authorization") String token);

    @GET("/api/node")
    Call<List<Node>> getNodes();

    @GET("/api/general")
    Call<Pagination<General>> getGeneral();

    @POST("/api/cart")
    Call<Cart> postCart(@Body Cart cart, @Header("Authorization") String token);

    @GET("/api/cart")
    Call<Pagination<Cart>> getCart(@Header("Authorization") String token);

    @GET("api/news")
    Call<Pagination<News>> getNews();

    @GET("api/general/active")
    Call<General> getActiveNodes();

}
