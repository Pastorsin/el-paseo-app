package laboratorio.app.fragments.forms.user.items;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentAccountFormBinding;
import laboratorio.app.viewmodels.UserViewModel;

public class AccountFormFragment extends ItemMultiSteperFormFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account_form;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider viewModelProvider) {
        UserViewModel viewModel = viewModelProvider.get(UserViewModel.class);
        ((FragmentAccountFormBinding) binding).setViewmodel(viewModel);
    }

    @Override
    protected void initValidators(View view) {
        EditText emailInput = view.findViewById(R.id.signin_email);
        validator.addValidation(emailInput, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));

        EditText passwordInput = view.findViewById(R.id.signin_password);
        String regexPassword = getString(R.string.password_regex);
        validator.addValidation(passwordInput, regexPassword, getString(R.string.error_password));

        EditText passwordConfirm = view.findViewById(R.id.signup_confirm_password);
        validator.addValidation(passwordConfirm, passwordInput, getString(R.string.error_confirm_password));
    }
}