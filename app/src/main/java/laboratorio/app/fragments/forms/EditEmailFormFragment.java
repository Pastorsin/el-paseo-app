package laboratorio.app.fragments.forms;

import androidx.databinding.ViewDataBinding;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentEditEmailFormBinding;
import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.viewmodels.EmailViewModel;
import laboratorio.app.viewmodels.FormViewModel;
import laboratorio.app.viewmodels.UserViewModel;

public class EditEmailFormFragment extends FormFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_email_form;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider) {
        FragmentEditEmailFormBinding emailBinding = (FragmentEditEmailFormBinding) binding;

        UserViewModel userViewModel = provider.get(UserViewModel.class);
        emailBinding.setUserViewmodel(userViewModel);

        EmailViewModel emailViewModel = provider.get(EmailViewModel.class);
        emailBinding.setEmailViewmodel(emailViewModel);

        FormViewModel formViewModel = provider.get(FormViewModel.class);
        formViewModel.isValid.observe(getViewLifecycleOwner(), (isValid) -> putEmail(isValid, emailViewModel));
        emailBinding.setFormViewmodel(formViewModel);
    }

    private void putEmail(Boolean isValid, EmailViewModel emailViewModel) {
        if (isValid) {
            // TODO: Make Request
            ErrorFragment fragment = new ErrorFragment();
            ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
        }
    }


    @Override
    protected void initValidators(View view) {
        EditText oldEmailInput = view.findViewById(R.id.old_update_email_input);
        validator.addValidation(oldEmailInput, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));

        EditText newEmailinput = view.findViewById(R.id.new_update_email_input);
        validator.addValidation(newEmailinput, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));
    }
}