package laboratorio.app.models;

import java.io.Serializable;

import laboratorio.app.auth.Encryptor;

public class LoginUser implements Serializable {
    private String userName;
    private String userPassword;

    public LoginUser(String username, String userPassword) {
        this.userName = username;
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        String encryptedPassword = Encryptor.encryptToMD5(userPassword);
        return encryptedPassword;
    }
}
