package laboratorio.app.viewmodels;

import android.util.Log;

import org.jetbrains.annotations.Nullable;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.controllers.API;
import laboratorio.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends ViewModel {
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>();

    public final MutableLiveData<String> firstName = new MutableLiveData<>("");
    public final MutableLiveData<String> lastName = new MutableLiveData<>("");

    public final MutableLiveData<String> age = new MutableLiveData<>("");

    public final MutableLiveData<String> phone = new MutableLiveData<>("");

    public final AddressViewModel residencyAddress = new AddressViewModel();
    public final AddressViewModel deliveryAddress = new AddressViewModel();

    public final MutableLiveData<Boolean> personalInformationChanged = new MutableLiveData<>(false);
    public final MutableLiveData[] personalInformationFields = {firstName, lastName, age, phone};

    public MutableLiveData<User> signUp() {
        MutableLiveData<User> signUpResponse = new MutableLiveData<>();

        User user = new User(email.getValue(),
                password.getValue(),
                firstName.getValue(),
                lastName.getValue(),
                getAgeNumber(),
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

    @Nullable
    public Integer getAgeNumber() {
        return age.getValue().equals("") ? null : Integer.parseInt(age.getValue());
    }

    public void init(User user, LifecycleOwner viewLifecycleOwner) {
        initPersonalInformation(user, viewLifecycleOwner);
        initAddresses(user);
    }

    private void initPersonalInformation(User user, LifecycleOwner viewLifecycleOwner) {
        email.setValue(user.getEmail());
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        age.setValue(user.getAge() == null ? "" : "" + user.getAge());
        phone.setValue(user.getPhone());

        for (MutableLiveData field : personalInformationFields) {
            field.observe(viewLifecycleOwner, fieldValue ->
                personalInformationChanged.setValue(!isPersonalInformationEquals(user))
            );
        }
    }

    private boolean isPersonalInformationEquals(User user) {
        return user.isPersonalInformationEquals(firstName.getValue(), 
                lastName.getValue(), 
                getAgeNumber(), 
                phone.getValue());
    }

    private void initAddresses(User user) {
        residencyAddress.init(user.getAddress());
        deliveryAddress.init(user.getAddress());
    }

}