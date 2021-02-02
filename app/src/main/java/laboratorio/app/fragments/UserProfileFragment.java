package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentUserProfileBinding;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.auth.NoUserLoggedException;
import laboratorio.app.models.User;
import laboratorio.app.viewmodels.ApplicationViewModel;
import laboratorio.app.viewmodels.SignUpViewModel;


public class UserProfileFragment extends Fragment {

    private ApplicationViewModel appViewmodel;
    private SignUpViewModel signUpViewModel;

    private MutableLiveData[] personalInformationFields = {
            signUpViewModel.firstName,
            signUpViewModel.lastName,
            signUpViewModel.age,
            signUpViewModel.phone
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initViewModels();

        View view = initDataBinding(inflater, container);

        fetchUser();

        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(onClickLogoutButton());

        return view;
    }

    private void initViewModels() {
        appViewmodel = new ViewModelProvider(requireActivity()).get(ApplicationViewModel.class);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
    }

    @NotNull
    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentUserProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setViewmodel(signUpViewModel);
        return view;
    }

    private void fetchUser() {
        MutableLiveData<User> userRequest = ApiSession.instance.getUserLogged(getContext());

        appViewmodel.isLoading.setValue(true);
        userRequest.observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                appViewmodel.errorEvent.call();
            } else {
                signUpViewModel.init(user);
                initSaveChangesButton(user);
            }
            appViewmodel.isLoading.setValue(false);
        });
    }

    private void initSaveChangesButton(User user) {
        for (MutableLiveData field : personalInformationFields) {
            field.observe(getViewLifecycleOwner(), fieldValue -> {
                String firstName = signUpViewModel.firstName.getValue();
                String lastName = signUpViewModel.lastName.getValue();
                Integer age = signUpViewModel.getAgeNumber();
                String phone = signUpViewModel.phone.getValue();

                signUpViewModel.personalInformationChanged.setValue(
                        !user.isPersonalInformationEquals(firstName, lastName, age, phone)
                );
            });
        }
    }

    @NotNull
    private View.OnClickListener onClickLogoutButton() {
        return buttonView -> {
            try {
                ApiSession.instance.logout(getContext());
                Log.d("LOGOUT", "User logged out successfully");

                requireActivity().getViewModelStore().clear();

                Fragment fragment = new SignInFragment();
                ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
            } catch (NoUserLoggedException e) {
                Log.e("LOGOUT", "No user logged");
                e.printStackTrace();
            }
        };
    }
}