package laboratorio.app.fragments;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import laboratorio.app.R;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.controllers.API;
import laboratorio.app.models.NewsletterSubscription;
import laboratorio.app.models.NewsletterSubscriptionResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsletterFragment extends Fragment {

    public NewsletterFragment() {
        // Required empty public constructor
    }

    public static NewsletterFragment newInstance() {
        NewsletterFragment fragment = new NewsletterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_newsletter, container, false);
        boolean isUserLoggedIn = ApiSession.instance.isUserLoggedIn(getContext());

        if(isUserLoggedIn){
            Button suscribeButton = view.findViewById(R.id.newsletter_suscribe_button);

            suscribeButton.setOnClickListener(button -> {
                String email = ((EditText) view.findViewById(R.id.newsletter_emailAddress)).getText().toString();
                if(email != null){
                    ApiSession.instance.getToken(getContext(), sendSubscription(email));
                }
            });
        }else{
            view.setVisibility(View.GONE);
        }

        return view;
    }

    private AccountManagerCallback<Bundle> sendSubscription(String email){
        return accountManagerFuture -> {
            try {
                NewsletterSubscription newsletterSubscription = new NewsletterSubscription();
                newsletterSubscription.setEmail(email);
                String token = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                API.instance.getService().subscribeNewsletter(newsletterSubscription,token).enqueue(getResponseCallback());

            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            }
        };
    }

    @NotNull
    private Callback<NewsletterSubscriptionResponse> getResponseCallback() {
        return new Callback<NewsletterSubscriptionResponse>() {
            @Override
            public void onResponse(Call<NewsletterSubscriptionResponse> call, Response<NewsletterSubscriptionResponse> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Suscripto correctamente!", Toast.LENGTH_SHORT).show();

                } else {
                    if(response.code() == 500){
                        Toast.makeText(getContext(), "Email ya está suscripto!", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("Request error - Suscribe newsletter " + response.code());
                }
            }

            @Override
            public void onFailure(Call<NewsletterSubscriptionResponse> call, Throwable t) {
                Log.e("Login", "Network error", t);
            }
        };
    }

}