package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import laboratorio.app.R;
import laboratorio.app.auth.Encryptor;
import laboratorio.app.controllers.API;
import laboratorio.app.fragments.forms.user.SignUpFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.LoginUser;
import laboratorio.app.models.Token;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.auth.UserAlreadyLoggedException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.jetbrains.annotations.NotNull;

public class SignInFragment extends Fragment {

    private AwesomeValidation validator = new AwesomeValidation(ValidationStyle.BASIC);

    private static final int LOGIN_INVALID_FIELDS_MESSAGE_ID = R.string.error_login_invalid_fields;
    private static final int LOGIN_DEFAULT_ERROR_MESSAGE_ID = R.string.error_login;

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

        Button signInButton = view.findViewById(R.id.signin_button);
        signInButton.setOnClickListener(handleSignInClick());

        Button signUpButton = view.findViewById(R.id.signin_register_button);
        signUpButton.setOnClickListener(handleSignUpClick());

        return view;
    }

    private View.OnClickListener handleSignUpClick() {
        return view -> {
            Fragment fragment = new SignUpFragment();
            ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
        };
    }

    private void initValidators(View view) {
        EditText emailInput = view.findViewById(R.id.signin_email);
        validator.addValidation(emailInput, Patterns.EMAIL_ADDRESS, getString(R.string.error_email));

        EditText passwordInput = view.findViewById(R.id.signin_password);
        String regexPassword = getString(R.string.password_regex);
        validator.addValidation(passwordInput, regexPassword, getString(R.string.error_password));
    }

    private View.OnClickListener handleSignInClick() {
        return buttonView -> {
            if (validator.validate()) {
                View view = getView();

                String email = ((EditText) view.findViewById(R.id.signin_email)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.signin_password)).getText().toString();

                String encryptedPassword = Encryptor.encrypt(password);
                LoginUser body = new LoginUser(email, encryptedPassword);

                API.instance.getService().signIn(body).enqueue(getSignInCallback(body));
            };
        };
    }

    @NotNull
    private Callback<Token> getSignInCallback(LoginUser body) {
        return new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    try {
                        ApiSession.instance.login(getContext(), body, token);
                        System.out.println("User logged in successfully");

                        Fragment fragment = new UserProfileFragment();
                        ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);

                    } catch (UserAlreadyLoggedException e) {
                        showError(LOGIN_DEFAULT_ERROR_MESSAGE_ID);
                        e.printStackTrace();
                        System.out.println("Error - User already logged");
                    }
                } else {
                    int errorMsgId = (response.code() == 401) ?
                            LOGIN_INVALID_FIELDS_MESSAGE_ID :
                            LOGIN_DEFAULT_ERROR_MESSAGE_ID;
                    showError(errorMsgId);
                    System.out.println("Request error - Login " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                showError(LOGIN_DEFAULT_ERROR_MESSAGE_ID);
                Log.e("Login", "Network error", t);
            }
        };
    }

    private void showError(int messageId) {
        ConstraintLayout errorLayout = getView().findViewById(R.id.signin_error_layout);
        errorLayout.setVisibility(View.VISIBLE);

        TextView errorMessageView = getView().findViewById(R.id.signin_error_message);
        errorMessageView.setText(messageId);
    }


}