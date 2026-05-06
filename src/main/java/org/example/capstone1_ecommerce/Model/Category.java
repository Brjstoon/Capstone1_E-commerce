package org.example.capstone1_ecommerce.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

    @NotEmpty(message = "name must not be empty")
    @Size(min = 4, message = "name must be more than 3 digits")
    private String name;

}
