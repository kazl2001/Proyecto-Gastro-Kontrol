package model;

import common.constants.Constants;
import io.vavr.collection.List;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
    private Credential credential;

    public Customer(String firstName, String lastName, LocalDate dateOfBirth, String email, String phone) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
    }

    public Customer(int id, String firstName, String lastName, LocalDate dateOfBirth, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
    }

    public Customer(int id, String firstName, String lastName, LocalDate dateOfBirth, String email, String phone, Credential credential) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.credential = credential;
    }

    public Customer(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  id +
                Constants.SPACE + Constants.HYPHEN + Constants.SPACE + firstName + Constants.SPACE + lastName;
    }
}
