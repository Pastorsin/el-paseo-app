package laboratorio.app.fragments.forms.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.widget.Toast;

import laboratorio.app.R;
import laboratorio.app.fragments.SignInFragment;
import laboratorio.app.fragments.forms.MultiStepperFormFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.User;
import laboratorio.app.viewmodels.UserViewModel;

public class SignUpFragment extends MultiStepperFormFragment {

    private static final int TOTAL_STEPS = 4;

    @Override
    public int getMenuId() {
        return R.menu.menu_signup_stepform;
    }

    @Override
    public int getNavGraphId() {
        return R.menu.signup_nav_stepper;
    }

    @Override
    public void onCompleted() {
        UserViewModel viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        MutableLiveData<User> signUpResponse = viewModel.signUp();

        signUpResponse.observe(getViewLifecycleOwner(), user -> {
            if (user == null)
                Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getContext(), R.string.signup_success, Toast.LENGTH_SHORT).show();
                ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(new SignInFragment());
            }
        });

    }

    @Override
    public int getTotalSteps() {
        return TOTAL_STEPS;
    }
}