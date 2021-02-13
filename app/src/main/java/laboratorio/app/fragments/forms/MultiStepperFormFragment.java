package laboratorio.app.fragments.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aceinteract.android.stepper.StepperNavListener;
import com.aceinteract.android.stepper.StepperNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import laboratorio.app.R;
import laboratorio.app.viewmodels.FormViewModel;

public abstract class MultiStepperFormFragment extends Fragment implements StepperNavListener {
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multistep_form_fragment, container, false);

        StepperNavigationView stepper = view.findViewById(R.id.stepper);
        getActivity().getMenuInflater().inflate(getMenuId(), stepper.getMenu());

        InitNavController(view, stepper);
        InitPreviousButton(view, stepper);
        InitNextButton(view, stepper);

        stepper.setStepperNavListener(this);

        return view;
    }

    public abstract int getMenuId();

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
        navController.setGraph(getNavGraphId());

        stepper.setupWithNavController(navController);
    }

    public abstract int getNavGraphId();

    public abstract void onCompleted();

    @Override
    public void onStepChanged(int step) {
        setUpPreviousButtonVisibility(step);
        setUpNextButtonVisibility(step);
    }

    private void setUpNextButtonVisibility(int step) {
        FloatingActionButton nextButton = getView().findViewById(R.id.button_next);
        if (step == getTotalSteps() - 1) {
            nextButton.setImageResource(R.drawable.ic_baseline_check_24);
        } else {
            nextButton.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
        }
    }

    public abstract int getTotalSteps();

    private void setUpPreviousButtonVisibility(int step) {
        FloatingActionButton previousButton = getView().findViewById(R.id.button_previous);
        previousButton.setVisibility(step == 0 ? View.GONE : View.VISIBLE);
    }
}
