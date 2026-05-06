package org.example.capstone1_ecommerce.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1_ecommerce.Api.ApiResponse;
import org.example.capstone1_ecommerce.Model.Product;
import org.example.capstone1_ecommerce.Model.User;
import org.example.capstone1_ecommerce.Service.ProductService;
import org.example.capstone1_ecommerce.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;


    @GetMapping("/get")
    public ResponseEntity<?> getProducts(){
        return ResponseEntity.status(200).body(productService.getProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors()){
            String m = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(m);
        }
        if (!productService.addProduct(product)){
            return ResponseEntity.status(400).body(new ApiResponse("Product ID already exists"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id,@RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors()){
            String m = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(m);
        }

        if (!productService.updateProduct(id, product)){
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        if (!productService.deleteProduct(id)){
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully"));
    }



    @GetMapping("/trending")
    public ResponseEntity<?> trendingProducts(){

        ArrayList<User> users = userService.getUsers();
        ArrayList<Product> trending = productService.trendingProducts(users);

        if (trending.isEmpty()){
            return ResponseEntity.status(200).body(new ApiResponse("No trending products found"));
        }
        return ResponseEntity.ok().body(trending);
    }


}
