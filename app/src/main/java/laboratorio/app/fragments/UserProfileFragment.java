package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import laboratorio.app.R;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.auth.NoUserLoggedException;


public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        TextView emailLabel = view.findViewById(R.id.user_profile_email);
        String userIdLogged = ApiSession.instance.getUserIdLogged(getContext());
        emailLabel.setText(userIdLogged);

        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(buttonView -> {
            try {
                ApiSession.instance.logout(getContext());
                System.out.println("User logged out successfully");

                Fragment fragment = new SignInFragment();
                ((FragmentLoader) getActivity()).replaceFragmentOnMainContainer(fragment);
            } catch (NoUserLoggedException e) {
                System.out.println("Error - No user logged when click in logout button");
                e.printStackTrace();
            }

        });

        return view;
    }
}