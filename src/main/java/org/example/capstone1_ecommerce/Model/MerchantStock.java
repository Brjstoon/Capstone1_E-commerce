package org.example.capstone1_ecommerce.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {

    @NotEmpty(message = "id must not be empty")
    private String id;

    @NotEmpty(message = "product ID must not be empty")
    private String productID;

    @NotEmpty(message = "merchant ID must not be empty")
    private String merchantID;

    @NotNull(message = "stock must not be empty")
    @Min(value = 11, message = "stock must be more than 10 at start")
    private int stock;

}
