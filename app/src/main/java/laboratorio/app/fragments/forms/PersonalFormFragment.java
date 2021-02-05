package laboratorio.app.fragments.forms;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentPersonalFormBinding;
import laboratorio.app.viewmodels.UserViewModel;

public class PersonalFormFragment extends FormFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_form;
    }

    @Override
    protected ViewModel setViewmodelToDataBinding(ViewDataBinding binding, ViewModelProvider viewModelProvider) {
        UserViewModel viewmodel = viewModelProvider.get(UserViewModel.class);
        ((FragmentPersonalFormBinding) binding).setViewmodel(viewmodel);
        return viewmodel;
    }

    @Override
    protected void initValidators(View view) {
        EditText firstNameInput = view.findViewById(R.id.signup_first_name);
        validator.addValidation(firstNameInput, getNameValidator(), getString(R.string.first_name_error));

        EditText lastNameInput = view.findViewById(R.id.signup_last_name);
        validator.addValidation(lastNameInput, getNameValidator(), getString(R.string.last_name_error));

        EditText ageInput = view.findViewById(R.id.signup_age);
        validator.addValidation(ageInput,
                getAgeValidator(),
                getString(R.string.age_error));

        EditText phoneInput = view.findViewById(R.id.signup_phone);
        validator.addValidation(phoneInput, Patterns.PHONE, getString(R.string.phone_error));
    }

    @NotNull
    private SimpleCustomValidation getNameValidator() {
        String nameRegex = getString(R.string.name_regex);

        return input ->
            StringUtils.isNotBlank(input) &&
            input.matches(nameRegex) &&
            input.length() > 2;
    }

    @NotNull
    private SimpleCustomValidation getAgeValidator() {
        return input -> {
            if (StringUtils.isBlank(input))
                return false;

            int age = Integer.parseInt(input);

            return age > 14 && age < 99;
        };
    }
}