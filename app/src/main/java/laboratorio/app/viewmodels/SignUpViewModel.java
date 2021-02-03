package laboratorio.app.viewmodels;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.controllers.API;
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

    public final MutableLiveData<Boolean> personalInformationChanged = new MutableLiveData<>(false);
    public final MutableLiveData[] personalInformationFields = {firstName, lastName, age, phone};

    private User user;

    public SignUpViewModel() {
        super();
        reset();
    }

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
        this.user = user;

        initCredentials(user);
        initPersonalInformation(user, viewLifecycleOwner);
        initAddresses(user);
    }

    public void reset() {
        email.postValue(null);
        password.postValue(null);
        confirmPassword.postValue(null);
        firstName.postValue("");
        lastName.postValue("");
        age.postValue("");
        phone.postValue("");
        residencyAddress.reset();
        deliveryAddress.reset();
    }

    private void initCredentials(User user) {
        email.setValue(user.getEmail());
    }

    private void initPersonalInformation(User user, LifecycleOwner viewLifecycleOwner) {
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

    private void initAddresses(User user) {
        residencyAddress.init(user.getAddress());
        deliveryAddress.init(user.getAddress());
    }

    private boolean isPersonalInformationEquals(User user) {
        return user.isPersonalInformationEquals(firstName.getValue(),
                lastName.getValue(),
                getAgeNumber(),
                phone.getValue());
    }

}