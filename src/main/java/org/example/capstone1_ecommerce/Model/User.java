package org.example.capstone1_ecommerce.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class User {


    @NotEmpty(message = "id must not be empty")
    private String id;

    @NotEmpty(message = "Username must not be empty")
    @Size(min = 5, message = "Username must be more than 5 characters long")
    private String userName;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be more than 6 characters long")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain both letters and digits")
    private String password;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotEmpty(message = "Role must not be empty")
    @Pattern(regexp = "^(Admin|Customer)$", message = "Role must be either 'Admin' or 'Customer'")
    private String role;

    @NotNull(message = "Balance must not be empty")
    @Positive(message = "Balance must be a positive value")
    private Double balance;


    private ArrayList<Product> orders;

    public void addOrder(Product product){
        orders.add(product);
    }
}
