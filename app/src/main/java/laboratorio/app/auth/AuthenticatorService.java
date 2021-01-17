package laboratorio.app.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    public AuthenticatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        ApiAuthenticator authenticator = new ApiAuthenticator(this);
        return authenticator.getIBinder();
    }
}