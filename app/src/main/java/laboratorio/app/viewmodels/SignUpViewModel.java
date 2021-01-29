package laboratorio.app.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends ViewModel {
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>();

    public final MutableLiveData<String> firstName = new MutableLiveData<>();
    public final MutableLiveData<String> lastName = new MutableLiveData<>();
    public final MutableLiveData<String> age = new MutableLiveData<>();

    public final MutableLiveData<String> phone = new MutableLiveData<>();

    public final AddressViewModel residencyAddress = new AddressViewModel();
    public final AddressViewModel deliveryAddress = new AddressViewModel();

    public MutableLiveData<User> signUp() {
        MutableLiveData<User> signUpResponse = new MutableLiveData<>();

        User user = new User(email.getValue(),
                password.getValue(),
                firstName.getValue(),
                lastName.getValue(),
                Integer.parseInt(age.getValue()),
                phone.getValue(),
                residencyAddress.getAddress(),
                deliveryAddress.getAddress());

        API.instance.getService().signUp(user).enqueue(
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
}