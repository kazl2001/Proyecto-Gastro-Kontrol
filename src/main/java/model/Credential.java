package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Credential {

    private int id;
    private String username;
    private String password;

    public Credential(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Credential(int id) {
        this.id = id;
    }
}
