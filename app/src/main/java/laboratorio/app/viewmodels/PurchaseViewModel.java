package laboratorio.app.viewmodels;

import android.util.Log;

import com.zaferayan.creditcard.model.CreditCard;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.controllers.API;
import laboratorio.app.models.Address;
import laboratorio.app.models.AvailableNode;
import laboratorio.app.models.Cart;
import laboratorio.app.models.CartProduct;
import laboratorio.app.models.General;
import laboratorio.app.models.Node;
import laboratorio.app.models.Pagination;
import laboratorio.app.models.Product;
import laboratorio.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.stream.Collectors.groupingBy;

public class PurchaseViewModel extends ViewModel {
    public final MutableLiveData<Cart> cart = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isCashChecked = new MutableLiveData<>(true);
    public final MutableLiveData<String> cashValue = new MutableLiveData<>("");
    public final MutableLiveData<CreditCard> creditCard = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isDeliveryChecked = new MutableLiveData<>(false);
    public final MutableLiveData<AvailableNode> chosenNodeSchedule = new MutableLiveData<>();

    private final MutableLiveData<Map<Node, List<AvailableNode>>> nodes = new MutableLiveData<>();

    public final MutableLiveData<String> tipValue = new MutableLiveData<>("");
    public final MutableLiveData<String> observation = new MutableLiveData<>("");

    public final MutableLiveData<User> user = new MutableLiveData<>();
    public final AddressViewModel deliveryAddress = new AddressViewModel();

    public void resetAll() {
        resetCart();
        resetFields();
        resetUser();
    }

    private void resetCart() {
        cart.getValue().reset();
    }

    private void resetUser() {
        this.user.setValue(null);
        this.deliveryAddress.reset();
    }

    private void resetFields() {
        isCashChecked.setValue(true);
        cashValue.setValue("");
        creditCard.setValue(null);
        isDeliveryChecked.setValue(false);
        chosenNodeSchedule.setValue(null);
        tipValue.setValue("");
        observation.setValue("");
    }

    public void initDetail(@NotNull Cart cart, @NotNull User userLogged) {
        init(cart, userLogged);
        isCashChecked.setValue(true);
        cashValue.setValue(String.format("%.2f", cart.getTotal()));
        creditCard.setValue(null);
        isDeliveryChecked.setValue(cart.isDeliverable());
        chosenNodeSchedule.setValue(cart.getNodeDate());
        tipValue.setValue("");
        observation.setValue(cart.getObservation());
    }

    public void initCreate(@NotNull Cart cart, @NotNull User userLogged) {
        init(cart, userLogged);
    }

    private void init(@NotNull Cart cart, @NotNull User userLogged) {
        this.cart.setValue(cart);

        this.user.setValue(userLogged);

        if (userLogged.getDeliveryAddress() != null)
            deliveryAddress.init(userLogged.getDeliveryAddress());
        else
            Log.w("DELIVERY ADDRESS", "User hasn't delivery address");
    }

    public MutableLiveData<Cart> postCart(String token) {
        MutableLiveData<Cart> cartResponse = new MutableLiveData<>();
        Cart cart = getCartWithCurrentFields();

        API.instance.getService().postCart(cart, token).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    cartResponse.setValue(response.body());
                    Log.d("POST CART", "Post cart succesfully");
                } else {
                    cartResponse.setValue(null);
                    Log.d("POST CART", String.format("Post failed %s", response.code()));
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                cartResponse.setValue(null);
                Log.d("POST CART", "Network error", t);
            }
        });

        return cartResponse;
    }

    public Cart getCartWithCurrentFields() {
        Cart cart = this.cart.getValue();
        User user = this.user.getValue();

        cart.setUser(user);

        if (isDeliveryChecked.getValue()) {
            user.setDeliveryAddress(deliveryAddress.getAddress());
        } else {
            cart.setNodeDate(chosenNodeSchedule.getValue());
        }

        cart.setObservation(observation.getValue());

        return cart;
    }

    public MutableLiveData<Map<Node, List<AvailableNode>>> fetchNodes() {
        if (!isNodesLoaded()) {
            API.instance.getService().getActiveNodes().enqueue(new Callback<General>() {
                @Override
                public void onResponse(Call<General> call, Response<General> response) {
                    if (response.isSuccessful()) {
                        General general = response.body();

                        List<AvailableNode> allAvailableNodes = general.getActiveNodes();

                        Map<Node, List<AvailableNode>> nodeSchedules = allAvailableNodes.stream().collect(
                                groupingBy(AvailableNode::getNode)
                        );

                        nodes.setValue(nodeSchedules);
                        Log.d("GET NODES", String.format("Fetch nodes successfully %s", nodeSchedules));
                    } else {
                        Log.e("GET NODES", String.format("Error on fetch nodes %s", response.code()));
                        nodes.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<General> call, Throwable t) {
                    Log.e("GET NODES", "Network error", t);
                    nodes.setValue(null);
                }
            });
        }

        return nodes;
    }

    public boolean isNodesLoaded() {
        return nodes.getValue() != null;
    }

    public boolean isNodeChosen(@NotNull Node node) {
        return isAnyNodeScheduleChosen() && node.equals(chosenNodeSchedule.getValue().getNode());
    }

    public boolean isAnyNodeScheduleChosen() {
        return chosenNodeSchedule.getValue() != null;
    }

    public List<CartProduct> getPurchaseDetail() {
        List<CartProduct> detail = new ArrayList<>();

        detail.addAll(cart.getValue().getCartProducts());

        detail.add(new CartProduct(null, getTipProduct(), 1));
        
        return detail;
    }

    @NotNull
    private Product getTipProduct() {
        Product tip = new Product();
        tip.setTitle("Propina");
        tip.setPrice(getTipPrice());
        return tip;
    }

    private Double getTipPrice() {
        String tip = tipValue.getValue();
        return tip == "" ? 0 : Double.parseDouble(tip);
    }

    public String getTotalPrice() {
        Double total = cart.getValue().getTotal() + getTipPrice();
        return String.format("%.2f", total);
    }

    public String getChosenNodeAddress() {
        Node node = chosenNodeSchedule.getValue().getNode();
        return String.format("%s (%s)", node, node.getAddress().fullAddress());
    }

    public String getNodeTime() {
        AvailableNode schedule = chosenNodeSchedule.getValue();
        return String.format("Desde %s a %s", schedule.getDateTimeFrom(), schedule.getDateTimeTo());
    }

    public String getNodeDate() {
        Date date = chosenNodeSchedule.getValue().getDay();
        return String.format(DateFormat.getDateInstance().format(date));
    }

    public Address getAddress() {
        return isDeliveryChecked.getValue() ?
                deliveryAddress.getAddress() :
                chosenNodeSchedule.getValue().getNode().getAddress();
    }
}
