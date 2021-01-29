package laboratorio.app.fragments.forms;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentAccountFormBinding;
import laboratorio.app.viewmodels.SignUpViewModel;

public class AccountFormFragment extends FormFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account_form;
    }

    @Override
    protected ViewModel setViewmodelToDataBinding(ViewDataBinding binding, ViewModelProvider viewModelProvider) {
        SignUpViewModel viewModel = viewModelProvider.get(SignUpViewModel.class);
        ((FragmentAccountFormBinding) binding).setViewmodel(viewModel);
        return viewModel;
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