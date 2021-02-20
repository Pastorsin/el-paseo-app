package laboratorio.app.viewmodels;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>();

    public final MutableLiveData<String> firstName = new MutableLiveData<>();
    public final MutableLiveData<String> lastName = new MutableLiveData<>();

    public final MutableLiveData<String> age = new MutableLiveData<>();

    public final MutableLiveData<String> phone = new MutableLiveData<>();

    public final AddressViewModel residencyAddress = new AddressViewModel();
    public final AddressViewModel deliveryAddress = new AddressViewModel();

    private User user;

    public UserViewModel() {
        super();
        reset();
    }

    private boolean isCurrentUserEqualsToInitUser() {
        User currentUser = createUserWithCurrentFields();
        return Objects.equals(currentUser, user);
    }

    public void reset() {
        if (user == null) {
            email.postValue(null);
            password.postValue(null);
            confirmPassword.postValue(null);
            firstName.postValue("");
            lastName.postValue("");
            age.postValue("");
            phone.postValue("");
            residencyAddress.reset();
            deliveryAddress.reset();
        } else {
            init(user);
        }
    }

    public void clearUser() {
        this.user = null;
        reset();
    }

    public void init(User user) {
        this.user = user;
        initCredentials(user);
        initPersonalInformation(user);
        initAddresses(user);
    }

    public MutableLiveData<User> signUp() {
        MutableLiveData<User> signUpResponse = new MutableLiveData<>();

        User userToSignUp = createUserWithCurrentFields();

        API.instance.getService().signUp(userToSignUp).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.d("Signup", "Signup successfully");
                        signUpResponse.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("Signup", "Error to signup user", t);
                        signUpResponse.setValue(null);
                    }
                }
        );

        return signUpResponse;
    }

    @Nullable
    public Integer getAgeNumber() {
        if (age.getValue() == null)
            return null;
        return age.getValue().equals("") ? null : Integer.parseInt(age.getValue());
    }

    private User createUserWithCurrentFields() {
        User newUser = new User(email.getValue(),
                password.getValue(),
                firstName.getValue(),
                lastName.getValue(),
                getAgeNumber(),
                phone.getValue(),
                residencyAddress.getAddress(),
                deliveryAddress.getAddress());

        return newUser;
    }

    private void initCredentials(User user) {
        email.setValue(user.getEmail());
    }

    private void initPersonalInformation(User user) {
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        age.setValue(user.getAge() == null ? "" : "" + user.getAge());
        phone.setValue(user.getPhone());
    }

    private void initAddresses(User user) {
        initResidencyAddress(user);
        initDeliveryAddress(user);
    }

    private void initDeliveryAddress(User user) {
        deliveryAddress.init(user.getDeliveryAddress());
    }

    private void initResidencyAddress(User user) {
        residencyAddress.init(user.getAddress());
    }

    public User getUser() {
        return user;
    }

    private MutableLiveData<User> putUser(String token) {
        MutableLiveData<User> userResponse = new MutableLiveData<>();

        API.instance.getService().putUser(user, token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    reset();

                    userResponse.setValue(user);
                } else {
                    userResponse.setValue(null);
                    Log.e("PUT USER", String.format("Request error %s", response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userResponse.setValue(null);
                Log.e("PUT USER", "Network error", t);
            }
        });

        return userResponse;
    }

    public MutableLiveData<User> putEmail(String newEmail, String token) {
        user.setEmail(newEmail);

        return putUser(token);
    }

    public MutableLiveData<User> putNonCredentialInformation(String token) {
        user.setNonCredentialInformation(
                firstName.getValue(),
                lastName.getValue(),
                getAgeNumber(),
                phone.getValue(),
                residencyAddress.getAddress(),
                deliveryAddress.getAddress()
        );

        return putUser(token);
    }
}