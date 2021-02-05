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

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.auth.NoUserLoggedException;
import laboratorio.app.databinding.FragmentEditUserCredentialsBinding;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.viewmodels.UserViewModel;

public class EditUserCredentialsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        View view = initDataBinding(inflater, container, userViewModel);

        initLogoutButton(view, userViewModel);

        return view;
    }

    @NotNull
    private View initDataBinding(LayoutInflater inflater, ViewGroup container, UserViewModel viewmodel) {
        FragmentEditUserCredentialsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_user_credentials, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setViewmodel(viewmodel);
        return view;
    }

    private void initLogoutButton(View view, UserViewModel userViewModel) {
        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(buttonView -> {
            try {
                ApiSession.instance.logout(getContext());
                Log.d("LOGOUT", "User logged out successfully");

                userViewModel.clearUser();

                // TODO: Consider if fragment still live cause exist SingleEvents into FormVM

                Fragment fragment = new SignInFragment();
                ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);

                onDestroy();
            } catch (NoUserLoggedException e) {
                Log.e("LOGOUT", "No user logged");
                e.printStackTrace();
            }
        });
    }
}