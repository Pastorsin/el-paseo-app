package laboratorio.app.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;

import laboratorio.app.models.LoginUser;
import laboratorio.app.models.Token;

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
        userData.putString(USER_ID_KEY, token.getUser().getId());

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

    public boolean isUserLoggedIn(Context context) {
        return getAccount(context) != null;
    }

    public AccountManagerFuture<Bundle> getToken(Context context, AccountManagerCallback<Bundle> onResponse) {
        AccountManager am = AccountManager.get(context);

        AccountManagerFuture<Bundle> authTokenCallback = am.getAuthToken(
                getAccount(context),
                TOKEN_TYPE,
                new Bundle(),
                true,
                onResponse,
                null);

        return authTokenCallback;
    }
}
