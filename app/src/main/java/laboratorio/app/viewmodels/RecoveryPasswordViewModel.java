package laboratorio.app.viewmodels;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.auth.Encryptor;
import laboratorio.app.controllers.API;
import laboratorio.app.models.RecoveryPassword;
import laboratorio.app.models.RecoveryPasswordResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoveryPasswordViewModel extends ViewModel {
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> code = new MutableLiveData<>();
    public final MutableLiveData<String> newPassword = new MutableLiveData<>();
    public final MutableLiveData<String> oldPassword = new MutableLiveData<>();

    public void init(String email) {
        this.email.setValue(email);
    }

    public MutableLiveData<RecoveryPasswordResponse> postSolicitCode() {
        MutableLiveData<RecoveryPasswordResponse> recoveryPasswordResponse = new MutableLiveData<>();
        RecoveryPassword recoveryPassword = getRecoveryPassword();

        API.instance.getService().postSolicitCodeForRecoveryPassword(recoveryPassword).enqueue(
                new Callback<RecoveryPasswordResponse>() {
                    @Override
                    public void onResponse(Call<RecoveryPasswordResponse> call, Response<RecoveryPasswordResponse> response) {
                        if (response.isSuccessful()) {
                            recoveryPasswordResponse.setValue(response.body());
                        } else {
                            Log.e("RECOVERY PASSWORD", String.format("Request error %s", response.code()));
                            recoveryPasswordResponse.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecoveryPasswordResponse> call, Throwable t) {
                        Log.e("RECOVERY PASSWORD", "Network error", t);
                        recoveryPasswordResponse.setValue(null);
                    }
                }
        );

        return recoveryPasswordResponse;
    }

    @NotNull
    private RecoveryPassword getRecoveryPassword() {
        return new RecoveryPassword(email.getValue(),
                code.getValue(),
                Encryptor.encrypt(newPassword.getValue()),
                Encryptor.encrypt(oldPassword.getValue()));
    }

    public MutableLiveData<RecoveryPasswordResponse> postConfirmRecoveryPassword() {
        MutableLiveData<RecoveryPasswordResponse> recoveryPasswordResponse = new MutableLiveData<>();
        RecoveryPassword recoveryPassword = getRecoveryPassword();

        API.instance.getService().postConfirmRecoveryPassword(recoveryPassword).enqueue(
                new Callback<RecoveryPasswordResponse>() {
                    @Override
                    public void onResponse(Call<RecoveryPasswordResponse> call, Response<RecoveryPasswordResponse> response) {
                        if (response.isSuccessful()) {
                            recoveryPasswordResponse.setValue(response.body());
                        } else {
                            recoveryPasswordResponse.setValue(null);
                        }

                        Log.d("CONFIRM RECOVERY PASSWORD", response.toString());
                    }

                    @Override
                    public void onFailure(Call<RecoveryPasswordResponse> call, Throwable t) {
                        recoveryPasswordResponse.setValue(null);
                        Log.e("CONFIRM RECOVERY PASSWORD", "Network error", t);
                    }
                }
        );

        return recoveryPasswordResponse;
    }
}
