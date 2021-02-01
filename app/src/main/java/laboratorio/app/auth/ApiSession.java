package laboratorio.app.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.Api;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.TransformationsKt;
import laboratorio.app.controllers.API;
import laboratorio.app.models.LoginUser;
import laboratorio.app.models.Token;
import laboratorio.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiSession {
    public final static ApiSession instance = new ApiSession();

    private final static String ACCOUNT_TYPE = "laboratorio.app.auth";
    private final static String TOKEN_TYPE = "_Manage your buys";
    private final static String USER_ID_KEY = "USER_ID";

    private ApiSession() {
    }

    public Account[] getAccounts(Context context) {
        return AccountManager.get(context).getAccountsByType(ACCOUNT_TYPE);
    }

    public Account getAccount(Context context) {
        Account[] accounts = getAccounts(context);

        if (accounts.length == 0)
            return null;

        return accounts[0];
    }

    public void login(Context context, LoginUser loginUser, Token token) throws UserAlreadyLoggedException {
        if (isUserLoggedIn(context))
            throw new UserAlreadyLoggedException();

        AccountManager am = AccountManager.get(context);

        Bundle userData = new Bundle();
        userData.putString(USER_ID_KEY, ""+token.getUser().getId());

        Account account = new Account(loginUser.getUserName(), ACCOUNT_TYPE);

        am.addAccountExplicitly(account, loginUser.getUserPassword(), userData);
        am.setAuthToken(account, TOKEN_TYPE, token.getValue());
    }

    public void logout(Context context) throws NoUserLoggedException {
        if (!isUserLoggedIn(context))
            throw new NoUserLoggedException();

        AccountManager am = AccountManager.get(context);

        //TODO: consider if account is removed from outside of app
        am.removeAccountExplicitly(getAccount(context));
    }

    public String getUserIdLogged(Context context) {
        AccountManager am = AccountManager.get(context);
        return am.getUserData(getAccount(context), USER_ID_KEY);
    }

    public MutableLiveData<User> getUserLogged(Context context) {
        MutableLiveData<User> userResponse = new MutableLiveData<>();

        getToken(context, accountManagerFuture -> {
            try {
                String token = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                String userId = getUserIdLogged(context);

                API.instance.getService().getUser(userId, token).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();
                        userResponse.setValue(user);
                        Log.d("GET USER LOGGED", "Response code: " + response.code());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        userResponse.setValue(null);
                        Log.e("GET USER LOGGED", "Error on request token", t);
                    }
                });
            } catch (Exception e) {
                userResponse.setValue(null);
                Log.e("GET USER LOGGED", "Error to access token", e);
            }
        });

        return userResponse;
    }

    public boolean isUserLoggedIn(Context context) {
        return getAccount(context) != null;
    }

    public AccountManagerFuture<Bundle> getToken(Context context, AccountManagerCallback<Bundle> onResponse) {
        AccountManager am = AccountManager.get(context);

        AccountManagerFuture<Bundle> authTokenCallback = am.getAuthToken(
                getAccount(context),
                TOKEN_TYPE,
                null,
                true,
                onResponse,
                null);

        return authTokenCallback;
    }
}
