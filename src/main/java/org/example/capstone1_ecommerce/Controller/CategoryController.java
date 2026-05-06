package org.example.capstone1_ecommerce.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1_ecommerce.Api.ApiResponse;
import org.example.capstone1_ecommerce.Model.Category;
import org.example.capstone1_ecommerce.Model.Product;
import org.example.capstone1_ecommerce.Model.User;
import org.example.capstone1_ecommerce.Service.CategoryService;
import org.example.capstone1_ecommerce.Service.ProductService;
import org.example.capstone1_ecommerce.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.status(200).body(categoryService.getCategorys());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody @Valid Category category, Errors errors){
        if (errors.hasErrors()){
            String m = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(m);
        }
        if (!categoryService.addCategory(category)){
            return ResponseEntity.status(400).body(new ApiResponse("Category name already exists"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Category added successfully"));
    }


    @PutMapping("/update/{index}")
    public ResponseEntity<?> updateCategory(@PathVariable int index, @RequestBody @Valid Category category, Errors errors) {
        if (errors.hasErrors()) {
            String m = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(m);
        }
        if (!categoryService.updateCategory(index, category)) {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Category updated successfully"));
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity<?> deleteCategory(@PathVariable int index) {
        if (!categoryService.deleteCategory(index)) {
            return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Category deleted successfully"));
    }


    @GetMapping("/popular-category")
    public ResponseEntity<?> mostPopularCategory(){
        ArrayList<User> users = userService.getUsers();
        ArrayList<Product> products = productService.getProducts();
        return ResponseEntity.ok().body(new ApiResponse(categoryService.mostPopularCategory(users, products)));
    }
}