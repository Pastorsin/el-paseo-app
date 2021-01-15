package laboratorio.app.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import laboratorio.app.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputEditText;

import javax.xml.validation.Validator;

public class SignInFragment extends Fragment {

    AwesomeValidation validator = new AwesomeValidation(ValidationStyle.BASIC);

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        initValidators(view);

        Button registerButton = view.findViewById(R.id.signin_button);

        registerButton.setOnClickListener(buttonView -> {
            validator.validate();
        });

        return view;
    }

    private void initValidators(View view) {
        EditText emailInput = view.findViewById(R.id.signin_email);
        validator.addValidation(emailInput, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));

        EditText passwordInput = view.findViewById(R.id.signin_password);
        String regexPassword = getString(R.string.password_regex);
        validator.addValidation(passwordInput, regexPassword, getString(R.string.error_password));
    }

    private boolean isValidFields() {
        EditText emailInput = getView().findViewById(R.id.signin_email);
        EditText passwordInput = getView().findViewById(R.id.signin_password);

        return Patterns.EMAIL_ADDRESS.matcher(emailInput.getText()).matches();
    }
}