package org.example.capstone1_ecommerce.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone1_ecommerce.Api.ApiResponse;
import org.example.capstone1_ecommerce.Model.MerchantStock;
import org.example.capstone1_ecommerce.Service.MerchantStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("/get")
    public ResponseEntity<?> getMerchantStocks() {
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStock());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors){
        if (errors.hasErrors()){
            String m = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(m);
        }
        merchantStockService.addMerchantStock(merchantStock);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant stock added successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMerchantStock(@PathVariable String id, @RequestBody @Valid MerchantStock merchantStock, Errors errors) {
        if (errors.hasErrors()) {
            String m = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(m);
        }
        if (!merchantStockService.updateMerchantStock(id, merchantStock)) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Merchant stock updated successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMerchantStock(@PathVariable String id) {
        if (!merchantStockService.deleteMerchantStock(id)) {
            return ResponseEntity.status(400).body(new ApiResponse("Merchant stock not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Merchant stock deleted successfully"));
    }


    @PutMapping("/addstock-tomerchant/{productID}/{merchantID}/{stock}")
    public ResponseEntity<?> addStockToMerchantStock(@PathVariable String productID, @PathVariable String merchantID, @PathVariable int stock){

        int check = merchantStockService.addStockToMerchantStock(productID, merchantID, stock);
        if (check == 2){
            return ResponseEntity.status(400).body(new ApiResponse("stock must be positive"));
        }
        if (check == 1){
            return ResponseEntity.status(400).body(new ApiResponse("no product matching given IDs found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("stock added successfully"));
    }



    @GetMapping("/low-stock")
    public ResponseEntity<?> lowStockAlert(){
        ArrayList<MerchantStock> result = merchantStockService.lowStockAlert();
        if (result.isEmpty()){
            return ResponseEntity.status(200).body(new ApiResponse("All stocks are above threshold"));
        }
        return ResponseEntity.status(200).body(result);
    }


    @GetMapping("/most-stocked")
    public ResponseEntity<?> mostStockedProduct(){
        if (merchantStockService.getMerchantStock().isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No Merchant stocks found"));
        }
        return ResponseEntity.ok().body(merchantStockService.mostStockedProduct());
    }
}