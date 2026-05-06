package org.example.capstone1_ecommerce.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1_ecommerce.Api.ApiResponse;
import org.example.capstone1_ecommerce.Model.MerchantStock;
import org.example.capstone1_ecommerce.Model.Product;
import org.example.capstone1_ecommerce.Model.User;
import org.example.capstone1_ecommerce.Service.MerchantStockService;
import org.example.capstone1_ecommerce.Service.ProductService;
import org.example.capstone1_ecommerce.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final MerchantStockService merchantStockService;


    @GetMapping("/get")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, Errors errors){
       if (errors.hasErrors()){
           String m = errors.getFieldError().getDefaultMessage();
           return ResponseEntity.status(400).body(m);
       }
       if (!userService.addUser(user)){
           return ResponseEntity.status(404).body(new ApiResponse("User ID is used"));
       }

        return ResponseEntity.status(200).body(new ApiResponse("User added successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id,@RequestBody @Valid User user, Errors errors){
        if (errors.hasErrors()){
            String m = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(m);
        }

        if (!userService.updateUser(id, user)){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        if (!userService.deleteUser(id)){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }





    @PutMapping("/buy/{userID}/{productID}/{merchantID}")
    public ResponseEntity<?> buyProduct(@PathVariable String userID, @PathVariable String productID, @PathVariable String merchantID){

        ArrayList<MerchantStock> merchantStocks = merchantStockService.getMerchantStock();
        ArrayList<Product> products = productService.getProducts();

        int check = userService.buyProduct(userID, productID, merchantID, merchantStocks, products);

        if (check == 5){
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }
        if (check == 4){
            return ResponseEntity.status(400).body(new ApiResponse("Product or Merchant not found in MerchantStock class"));
        }
        if (check == 3){
            return ResponseEntity.status(400).body(new ApiResponse("product not found in product class"));
        }
        if (check == 2){
            return ResponseEntity.status(400).body(new ApiResponse("Not enough balance"));
        }
        if (check == 1){
            return ResponseEntity.status(400).body(new ApiResponse("product out of stock"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Product bought successfully"));
    }



    @GetMapping("/recommend/{id}")
    public ResponseEntity<?> recommendProducts(@PathVariable String id){
        ArrayList<MerchantStock> merchantStocks = merchantStockService.getMerchantStock();
        ArrayList<Product> products = productService.getProducts();



        return ResponseEntity.status(200).body(userService.recomendProducts(id, merchantStocks, products));
    }



    @PutMapping("/resell/{sellerID}/{buyerID}/{productID}/{condition}")
    public ResponseEntity<?> resellProduct(@PathVariable String sellerID, @PathVariable String buyerID, @PathVariable String productID, @PathVariable int condition){
        switch (userService.resellProduct(sellerID, buyerID, productID, condition)){
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse("Seller not found"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("Buyer not found"));
            case 3:
                return ResponseEntity.status(400).body(new ApiResponse("Product not found in seller's orders"));
            case 4:
                return ResponseEntity.status(400).body(new ApiResponse("Wrong condition choice"));
            case 5:
                return ResponseEntity.status(400).body(new ApiResponse("Insufficient balance"));
        }
        return ResponseEntity.ok().body(new ApiResponse("Resell done successfully"));
    }



    @PutMapping("/applyDiscount/{userID}/{category}/{discount}")
    public ResponseEntity<?> applyDiscount(@PathVariable String userID, @PathVariable String category, @PathVariable double discount){
        ArrayList<Product> products = productService.getProducts();
        switch (userService.applyDiscount(userID, category, discount, products)){
            case 1:
                return ResponseEntity.status(400).body(new ApiResponse("User is not an admin"));
            case 2:
                return ResponseEntity.status(400).body(new ApiResponse("User not found"));
            case 3:
                return ResponseEntity.status(400).body(new ApiResponse("Invalid discount amount"));
            case 4:
                return ResponseEntity.status(400).body(new ApiResponse("Category not found"));
            case 111:
                return ResponseEntity.status(200).body(new ApiResponse("Discount applied to " + category + " category successfully"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Discount applied to all products successfully"));
    }


    @GetMapping("/top-spender")
    public ResponseEntity<?> topSpender(){
        if (userService.getUsers().isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No users found"));

        }
        return ResponseEntity.status(200).body(userService.topSpender());
    }

    @PutMapping("/loyalty-discount/{adminID}/{minOrders}/{gift}")
    public ResponseEntity<?> loyaltyDiscount(@PathVariable String adminID, @PathVariable int minOrders, @PathVariable double gift){
        switch (userService.loyaltyDiscount(adminID, minOrders, gift)){
            case 1:
                return ResponseEntity.status(403).body(new ApiResponse("User is not an admin"));
            case 2:
                return ResponseEntity.status(404).body(new ApiResponse("Admin not found"));
            case 3:
                return ResponseEntity.status(400).body(new ApiResponse("Invalid discount amount"));
            case 4:
                return ResponseEntity.status(400).body(new ApiResponse("Invalid order count"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Loyalty discount applied successfully"));
    }



    @DeleteMapping("/deactivate/{requesterID}/{targetID}")
    public ResponseEntity<?> deactivateUser(@PathVariable String requesterID, @PathVariable String targetID){
        switch (userService.deactivateUser(requesterID, targetID)){
            case 1:
                return ResponseEntity.status(404).body(new ApiResponse("Requester not found"));
            case 2:
                return ResponseEntity.status(403).body(new ApiResponse("Requester is not an admin"));
            case 3:
                return ResponseEntity.status(404).body(new ApiResponse("Target not found"));
            case 111:
                return ResponseEntity.ok().body(new ApiResponse("Target was demoted from admin to customer"));
            case 222:
                return ResponseEntity.status(400).body(new ApiResponse("Target has spent more than 10,000 and cannot be deactivated"));
        }
        return ResponseEntity.ok().body(new ApiResponse("Target was removed successfully"));
    }


}
