package laboratorio.app.fragments;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentUserProfileBinding;
import laboratorio.app.fragments.forms.user.items.DeliveryAddressFormFragment;
import laboratorio.app.fragments.forms.user.items.PersonalFormFragment;
import laboratorio.app.fragments.forms.user.items.ResidencyAddressFormFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.auth.NoUserLoggedException;
import laboratorio.app.models.User;
import laboratorio.app.viewmodels.ApplicationViewModel;
import laboratorio.app.viewmodels.FormViewModel;
import laboratorio.app.viewmodels.UserViewModel;


public class UserProfileFragment extends Fragment {

    private ApplicationViewModel appViewmodel;
    private UserViewModel userViewModel;
    private FormViewModel formViewModel;

    private void selectFirstTabitem(@NonNull View view) {
        TabLayout tab = view.findViewById(R.id.profile_tab);
        tab.getTabAt(1).select();
        tab.getTabAt(0).select();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initViewModels();

        View view = initDataBinding(inflater, container);

        fetchUser();

        initTabView(view);
        initSaveChangesButton(view);
        selectFirstTabitem(view);

        return view;
    }


    private void initViewModels() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());

        appViewmodel = provider.get(ApplicationViewModel.class);

        userViewModel = provider.get(UserViewModel.class);
        userViewModel.reset();

        formViewModel = provider.get(FormViewModel.class);
    }

    @NotNull
    private View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentUserProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setViewmodel(userViewModel);
        return view;
    }

    private void fetchUser() {
        appViewmodel.isLoading.setValue(true);

        MutableLiveData<User> userRequest = ApiSession.instance.getUserLogged(getContext());
        userRequest.observe(getViewLifecycleOwner(), user -> {
            if (user == null) onFetchUserError();
            else onFetchUserSuccess(user);
            appViewmodel.isLoading.setValue(false);
        });
    }

    private void onFetchUserSuccess(User user) {
        userViewModel.init(user);
    }

    private void onFetchUserError() {
        try {
            ApiSession.instance.logout(getContext());
        } catch (NoUserLoggedException e) {
            e.printStackTrace();
        }

        loadFragment(new SignInFragment());
    }

    private void initTabView(View view) {
        TabLayout tab = view.findViewById(R.id.profile_tab);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                setVisibilityOfSaveChangesButton(position, view);

                switch (position) {
                    case 0:
                        loadFragmentOnTabContainer(new EditUserCredentialsFragment());
                        break;
                    case 1:
                        loadFragmentOnTabContainer(new PersonalFormFragment());
                        break;
                    case 2:
                        loadFragmentOnTabContainer(new ResidencyAddressFormFragment());
                        break;
                    case 3:
                        loadFragmentOnTabContainer(new DeliveryAddressFormFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                userViewModel.reset();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setVisibilityOfSaveChangesButton(int position, View view) {
        Button saveChangesButton = view.findViewById(R.id.profile_save_changes_button);
        saveChangesButton.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    }

    private void initSaveChangesButton(View view) {
        Button saveChangesButton = view.findViewById(R.id.profile_save_changes_button);

        saveChangesButton.setOnClickListener(buttonView -> formViewModel.submitButtonPressed.call());

        formViewModel.isValid.observe(getViewLifecycleOwner(), isValid -> {
                    if (isValid) {
                        appViewmodel.isLoading.setValue(true);

                        ApiSession.instance.getToken(getContext(), accountManagerFuture -> {
                            try {
                                String token = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                                putUser(token);
                            } catch (AuthenticatorException | IOException | OperationCanceledException e) {
                                appViewmodel.errorEvent.call();
                                Log.e("PUT USER", "Authentication error", e);
                            } finally {
                                appViewmodel.isLoading.setValue(false);
                            }
                        });

                    }
                }
        );

    }

    private void putUser(String token) {
        userViewModel.putNonCredentialInformation(token).observe(getViewLifecycleOwner(), user -> {
            if (user == null)
                appViewmodel.errorEvent.call();
            else
                onSuccessPutUser();
        });
    }

    private void onSuccessPutUser() {
        Toast.makeText(getContext(), R.string.put_user_success_message, Toast.LENGTH_SHORT).show();
    }

    private void loadFragmentOnTabContainer(Fragment fragment) {
        ((FragmentLoader) getActivity()).replaceFragment(fragment, R.id.profile_tab_fragment_container);
    }

    private void loadFragment(Fragment fragment) {
        ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
    }

}