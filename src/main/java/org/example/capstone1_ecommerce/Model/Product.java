package org.example.capstone1_ecommerce.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    @NotEmpty(message = "id must not be empty")
    private String id;

    @NotEmpty(message = "name must not be empty")
    @Size(min = 4, message = "name must be more than 3 digits")
    private String name;

    @NotNull(message = "price must not be empty")
    @Positive
    private double price;

    @NotEmpty(message = "category ID must not be empty")
    private String categoryID;
}
