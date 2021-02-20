package laboratorio.app.viewmodels;

import android.util.Log;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.controllers.API;
import laboratorio.app.helpers.SingleLiveEvent;
import laboratorio.app.models.Cart;
import laboratorio.app.models.CartProduct;
import laboratorio.app.models.Pagination;
import laboratorio.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseListViewModel extends ViewModel {
    private final MutableLiveData<List<Cart>> userPurchases = new MutableLiveData<>();
    public final SingleLiveEvent<Cart> targetPurchase = new SingleLiveEvent<>();

    public MutableLiveData<List<Cart>> getPurchases(User userLogged, String token) {
        if (userPurchases.getValue() == null) {

            API.instance.getService().getCart(token).enqueue(new Callback<Pagination<Cart>>() {
                @Override
                public void onResponse(Call<Pagination<Cart>> call, Response<Pagination<Cart>> response) {
                    if (response.isSuccessful()) {
                        List<Cart> allPurchases = response.body().getPage();
                        List<Cart> purchases = getFilterUserPurchases(userLogged, allPurchases);
                        userPurchases.setValue(purchases);

                        Log.d("GET CART", "Success get cart");
                    } else {
                        userPurchases.setValue(null);
                        Log.e("GET CART", String.format("Get failed %s", response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Pagination<Cart>> call, Throwable t) {
                    userPurchases.setValue(null);
                    Log.e("GET CART", "Network error", t);
                }
            });
        }
        return userPurchases;
    }

    private List<Cart> getFilterUserPurchases(User userLogged, List<Cart> allPurchases) {
        return allPurchases.stream()
                .filter(purchase -> Objects.equals(purchase.getUser().getId(), userLogged.getId()))
                .collect(Collectors.toList());
    }
}
