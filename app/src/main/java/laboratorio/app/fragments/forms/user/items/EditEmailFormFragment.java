package laboratorio.app.fragments.forms.user.items;

import androidx.databinding.ViewDataBinding;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.controllers.API;
import laboratorio.app.databinding.FragmentEditEmailFormBinding;
import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.User;
import laboratorio.app.viewmodels.EmailViewModel;
import laboratorio.app.viewmodels.FormViewModel;
import laboratorio.app.viewmodels.UserViewModel;

public class EditEmailFormFragment extends ItemMultiSteperFormFragment {

    private EmailViewModel emailViewModel;
    private UserViewModel userViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_email_form;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider) {
        FragmentEditEmailFormBinding emailBinding = (FragmentEditEmailFormBinding) binding;

        userViewModel = provider.get(UserViewModel.class);
        emailBinding.setUserViewmodel(userViewModel);

        emailViewModel = provider.get(EmailViewModel.class);
        emailBinding.setEmailViewmodel(emailViewModel);

        FormViewModel formViewModel = provider.get(FormViewModel.class);
        formViewModel.isValid.observe(getViewLifecycleOwner(), (isValid) ->
                putEmail(isValid));
        emailBinding.setFormViewmodel(formViewModel);
    }

    private void putEmail(Boolean isValid) {
        if (isValid) {
            ApiSession.instance.getToken(getContext(), accountManagerFuture -> {
                try {
                    String token = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);

                    putUser(token);

                } catch (AuthenticatorException | IOException | OperationCanceledException e) {
                    onError();
                    e.printStackTrace();
                    Log.e("PUT USER EMAIL", "User not logged");
                }

            });
        }
    }

    private void putUser(String token) {
        String email = emailViewModel.email.getValue();

        userViewModel.putEmail(email, token).observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                onPutError();
                return;
            }

            renameAccount(email);
        });
    }

    private void renameAccount(String email) {
        ApiSession.instance.editUsername(getContext(), email).observe(getViewLifecycleOwner(), isSuccess -> {

            if (!isSuccess) {
                onPutError();
                return;
            }

            onPutSuccess();
        });
    }

    private void onPutSuccess() {
        emailViewModel.reset();
        Toast.makeText(getContext(), R.string.put_user_success_message, Toast.LENGTH_LONG).show();
    }

    private void onPutError() {
        Toast.makeText(getContext(), R.string.try_again_error, Toast.LENGTH_SHORT).show();
    }

    private void onError() {
        ErrorFragment fragment = new ErrorFragment();
        ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
    }

    @Override
    protected void initValidators(View view) {
        EditText oldEmailInput = view.findViewById(R.id.old_update_email_input);
        validator.addValidation(oldEmailInput, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));

        EditText newEmailinput = view.findViewById(R.id.new_update_email_input);
        validator.addValidation(newEmailinput, getNewEmailValidator(), getString(R.string.error_email));
    }

    @NotNull
    private SimpleCustomValidation getNewEmailValidator() {
        return newEmail -> {
            String oldEmail = userViewModel.email.getValue();
            return Patterns.EMAIL_ADDRESS.matcher(newEmail).matches() && !Objects.equals(newEmail, oldEmail);
        };
    }
}