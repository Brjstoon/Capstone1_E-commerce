package org.example.capstone1_ecommerce.Service;

import org.example.capstone1_ecommerce.Model.MerchantStock;
import org.example.capstone1_ecommerce.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantStockService {


    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();


    public ArrayList<MerchantStock> getMerchantStock(){
        return merchantStocks;
    }

    public void addMerchantStock(MerchantStock merchantStock){
        merchantStocks.add(merchantStock);
    }

    public boolean updateMerchantStock(String id, MerchantStock merchantStock){
        for (int i=0;i< merchantStocks.size();i++){
            if (merchantStocks.get(i).getId().equalsIgnoreCase(id)){
                merchantStock.setId(id);
                merchantStocks.set(i, merchantStock);
                return true;
            }
        }
        return false;
    }


    public boolean deleteMerchantStock(String id){
        for (int i=0;i< merchantStocks.size();i++){
            if (merchantStocks.get(i).getId().equalsIgnoreCase(id)){
                merchantStocks.remove(i);
                return true;
            }
        }
        return false;
    }


    public int addStockToMerchantStock(String productID, String merchantID, int stock){
        if (stock < 0){
            return 2; // stock must be positive
        }
        for (int i=0;i<merchantStocks.size();i++){
            if (merchantStocks.get(i).getProductID().equalsIgnoreCase(productID)
            && merchantStocks.get(i).getMerchantID().equalsIgnoreCase(merchantID)){
                merchantStocks.get(i).setStock(merchantStocks.get(i).getStock() + stock);
                return 0; // stock added successfully
            }
        }
        return 1; // no product matching given IDs found
    }


    public ArrayList<MerchantStock> lowStockAlert(){
        final int threshold = 10;
        ArrayList<MerchantStock> bellowThreshold = new ArrayList<>();

        for (int i=0;i<merchantStocks.size();i++){
            if (merchantStocks.get(i).getStock() < threshold){
                bellowThreshold.add(merchantStocks.get(i));
            }
        }

        return bellowThreshold;
    }


    public MerchantStock mostStockedProduct(){

        MerchantStock highest = merchantStocks.get(0);

        for (int i=0;i<merchantStocks.size();i++){
            if (merchantStocks.get(i).getStock() > highest.getStock()){
                highest = merchantStocks.get(i);
            }
        }
        return highest;
    }

}
