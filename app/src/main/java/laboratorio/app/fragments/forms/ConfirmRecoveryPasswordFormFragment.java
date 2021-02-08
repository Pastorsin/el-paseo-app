package laboratorio.app.fragments.forms;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.auth.NoUserLoggedException;
import laboratorio.app.databinding.FragmentConfirmRecoveryPasswordFormBinding;
import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.fragments.SignInFragment;
import laboratorio.app.fragments.UserProfileFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.RecoveryPasswordResponse;
import laboratorio.app.viewmodels.FormViewModel;
import laboratorio.app.viewmodels.RecoveryPasswordViewModel;
import laboratorio.app.viewmodels.UserViewModel;

public class ConfirmRecoveryPasswordFormFragment extends FormFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirm_recovery_password_form;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider) {
        FragmentConfirmRecoveryPasswordFormBinding recoveryBinding = (FragmentConfirmRecoveryPasswordFormBinding) binding;

        RecoveryPasswordViewModel recoveryPasswordViewModel = provider.get(RecoveryPasswordViewModel.class);
        recoveryBinding.setRecoveryViewmodel(recoveryPasswordViewModel);

        FormViewModel formViewModel = provider.get(FormViewModel.class);
        formViewModel.isValid.observe(getViewLifecycleOwner(),
                (isValid) -> postConfirmRecoveryPassword(isValid, recoveryPasswordViewModel, provider));
        recoveryBinding.setFormViewmodel(formViewModel);
    }

    private void postConfirmRecoveryPassword(Boolean formIsValid, RecoveryPasswordViewModel viewModel, ViewModelProvider provider) {
        if (formIsValid) {
            MutableLiveData<RecoveryPasswordResponse> response = viewModel.postConfirmRecoveryPassword();

            response.observe(getViewLifecycleOwner(), (recoveryPasswordResponse) -> {
                if (recoveryPasswordResponse == null) {
                    onResponseError();
                } else {
                    onResponseSuccess();
                }
            });
        }
    }

    private void onResponseSuccess() {
        Toast.makeText(getContext(), R.string.confirm_recovery_password_success_message, Toast.LENGTH_SHORT).show();

        try {
            ApiSession.instance.logout(getContext());
        } catch (NoUserLoggedException e) {
            onResponseError();
        }

        loadFragment(new SignInFragment());
    }

    private void onResponseError() {
        loadFragment(new ErrorFragment());
    }

    private void loadFragment(Fragment fragment) {
        ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
    }

    @Override
    protected void initValidators(View view) {
        EditText emailInput = view.findViewById(R.id.confirm_recovery_password_email_text_input);
        validator.addValidation(emailInput, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));

        EditText codeInput = view.findViewById(R.id.confirm_recovery_password_code_text_input);
        validator.addValidation(codeInput, StringUtils::isNotBlank, getString(R.string.empty_field_error));

        EditText oldPasswordInput = view.findViewById(R.id.confirm_recovery_password_old_password_text_input);
        validator.addValidation(oldPasswordInput, getString(R.string.password_regex), getString(R.string.error_password));

        EditText newPasswordInput = view.findViewById(R.id.confirm_recovery_password_new_password_text_input);
        validator.addValidation(newPasswordInput, getString(R.string.password_regex), getString(R.string.error_password));

        EditText confirmNewPasswordInput = view.findViewById(R.id.confirm_recovery_password_confirm_new_password_text_input);
        validator.addValidation(newPasswordInput, confirmNewPasswordInput, getString(R.string.error_confirm_password));
    }
}