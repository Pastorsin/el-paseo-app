package laboratorio.app.models;

import java.io.Serializable;

public class User implements Serializable {
    private String id;

    private String email;
    private String encryptedPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
