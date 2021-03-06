package laboratorio.app.fragments.forms.user.items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.viewmodels.FormViewModel;

abstract public class ItemMultiSteperFormFragment extends Fragment {
    protected AwesomeValidation validator = new AwesomeValidation(ValidationStyle.BASIC);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View view = binding.getRoot();

        ViewModelProvider viewModelProvider = getViewModelProvider();

        setViewmodelsToDataBinding(binding, viewModelProvider);
        binding.setLifecycleOwner(this);

        initValidators(view);

        setOnClickSubmitButton(viewModelProvider);

        return view;
    }

    @NotNull
    protected ViewModelProvider getViewModelProvider() {
        return new ViewModelProvider(requireActivity());
    }


    private void setOnClickSubmitButton(ViewModelProvider viewModelProvider) {
        FormViewModel formViewModel = viewModelProvider.get(FormViewModel.class);

        formViewModel.submitButtonPressed.observe(getViewLifecycleOwner(), buttonPressed -> {
            System.out.println("Listen: " + this + " | State: " + getViewLifecycleOwner().getLifecycle().getCurrentState());
            boolean isValid = validator.validate();
            System.out.println(isValid);
            formViewModel.isValid.setValue(isValid);
        });
    }

    protected abstract int getLayoutId();

    protected abstract void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider);

    protected abstract void initValidators(View view);
}
