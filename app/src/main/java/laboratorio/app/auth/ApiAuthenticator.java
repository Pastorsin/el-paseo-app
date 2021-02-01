package laboratorio.app.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import laboratorio.app.R;
import laboratorio.app.activities.MainActivity;
import laboratorio.app.controllers.API;
import laboratorio.app.models.LoginUser;
import laboratorio.app.models.Token;
import retrofit2.Response;

public class ApiAuthenticator extends AbstractAccountAuthenticator {
    private Context mContext;

    public ApiAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(MainActivity.ARG_NAV_ELEMENT_ID_SELECTED, R.id.account_nav);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        final AccountManager am = AccountManager.get(mContext);
        final Bundle result = new Bundle();

        String authToken = null;

        final String password = am.getPassword(account);
        if (password != null) {
            LoginUser loginUser = new LoginUser(account.name, password);

            try {
                Response<Token> tokenResponse = API.instance.getService().signIn(loginUser).execute();

                if (tokenResponse.isSuccessful()) {
                    Token token = tokenResponse.body();
                    authToken = String.format("Bearer %s", token.getValue());
                    am.setAuthToken(account, authTokenType, authToken);

                    Log.d("TOKEN AUTH", String.format("Success response: %s", authToken));
                } else {
                    Log.e("TOKEN AUTH", String.format("Response failed: %s", tokenResponse.code()));
                }
            } catch (IOException e) {
                Log.e("TOKEN AUTH", "Network error");
                throw new NetworkErrorException();
            }
        }

        result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        result.putString(AccountManager.KEY_AUTH_FAILED_MESSAGE, "Error al obtener el token");

        return result;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        return null;
    }
}
