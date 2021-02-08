package laboratorio.app.fragments.forms;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentRecoveryPasswordBinding;
import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.fragments.forms.FormFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.RecoveryPasswordResponse;
import laboratorio.app.viewmodels.FormViewModel;
import laboratorio.app.viewmodels.RecoveryPasswordViewModel;
import laboratorio.app.viewmodels.UserViewModel;

public class RecoveryPasswordFormFragment extends FormFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recovery_password;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider) {
        FragmentRecoveryPasswordBinding passwordBinding = (FragmentRecoveryPasswordBinding) binding;

        UserViewModel userViewModel = provider.get(UserViewModel.class);

        RecoveryPasswordViewModel recoveryViewModel = provider.get(RecoveryPasswordViewModel.class);
        recoveryViewModel.init(userViewModel.email.getValue());
        passwordBinding.setRecoveryViewmodel(recoveryViewModel);

        FormViewModel formViewModel = provider.get(FormViewModel.class);
        formViewModel.isValid.observe(getViewLifecycleOwner(), (isValid) -> postSolicitCode(isValid, recoveryViewModel));
        passwordBinding.setFormViewmodel(formViewModel);
    }

    private void postSolicitCode(Boolean formIsValid, RecoveryPasswordViewModel viewmodel) {
        if (formIsValid) {
            MutableLiveData<RecoveryPasswordResponse> response = viewmodel.postSolicitCode();
            response.observe(getViewLifecycleOwner(), (recoveryPasswordResponse) -> {
                        if (recoveryPasswordResponse == null) {
                            loadFragment(new ErrorFragment());
                        } else {
                            loadFragment(new ConfirmRecoveryPasswordFormFragment());
                            Log.d("POST CODE RECOVERY PASSWORD", recoveryPasswordResponse.toString());
                        }
                    }
            );
        }
    }

    private void loadFragment(Fragment fragment) {
        ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
    }

    @Override
    protected void initValidators(View view) {
        EditText emailInput = view.findViewById(R.id.recovery_email_input);
        validator.addValidation(emailInput, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));
    }
}