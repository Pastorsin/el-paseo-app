package laboratorio.app.fragments.forms;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aceinteract.android.stepper.StepperNavListener;
import com.aceinteract.android.stepper.StepperNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import laboratorio.app.R;
import laboratorio.app.fragments.SignInFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.User;
import laboratorio.app.viewmodels.FormViewModel;
import laboratorio.app.viewmodels.SignUpViewModel;

public class SignUpFragment extends Fragment implements StepperNavListener {

    private static final int TOTAL_STEPS = 4;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);

        StepperNavigationView stepper = view.findViewById(R.id.stepper);

        InitNavController(view, stepper);
        InitPreviousButton(view, stepper);
        InitNextButton(view, stepper);

        stepper.setStepperNavListener(this);

        return view;
    }

    private void InitNextButton(View view, StepperNavigationView stepper) {
        FloatingActionButton nextButton = view.findViewById(R.id.button_next);
        FormViewModel viewmodel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);

        nextButton.setOnClickListener(buttonView -> viewmodel.submitButtonPressed.call());

        viewmodel.isValid.observe(getViewLifecycleOwner(), isFormValid -> {
            if (isFormValid)
                stepper.goToNextStep();
        });
    }

    private void InitPreviousButton(View view, StepperNavigationView stepper) {
        FloatingActionButton previousButton = view.findViewById(R.id.button_previous);
        previousButton.setOnClickListener(buttonView -> stepper.goToPreviousStep());
    }

    private void InitNavController(View view, StepperNavigationView stepper) {
        View frameView = view.findViewById(R.id.frame_stepper);
        navController = Navigation.findNavController(frameView);
        stepper.setupWithNavController(navController);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCompleted() {
        Toast.makeText(getContext(), "Completado", Toast.LENGTH_SHORT).show();

        SignUpViewModel viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
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
    public void onStepChanged(int step) {
        setUpPreviousButtonVisibility(step);
        setUpNextButtonVisibility(step);
    }

    private void setUpNextButtonVisibility(int step) {
        FloatingActionButton nextButton = getView().findViewById(R.id.button_next);
        if (step == TOTAL_STEPS - 1) {
            nextButton.setImageResource(R.drawable.ic_baseline_check_24);
        } else {
            nextButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
        }
    }

    private void setUpPreviousButtonVisibility(int step) {
        FloatingActionButton previousButton = getView().findViewById(R.id.button_previous);
        previousButton.setVisibility(step == 0 ? View.GONE : View.VISIBLE);
    }
}