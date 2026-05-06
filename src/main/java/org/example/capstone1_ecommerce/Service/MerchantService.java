package org.example.capstone1_ecommerce.Service;

import org.example.capstone1_ecommerce.Model.Merchant;
import org.example.capstone1_ecommerce.Model.MerchantStock;
import org.example.capstone1_ecommerce.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {


    ArrayList<Merchant> merchants = new ArrayList<>();


    public ArrayList<Merchant> getMerchant(){
        return merchants;
    }

    public boolean addMerchant(Merchant merchant){
        for (int i=0;i< merchants.size();i++){
            if (merchants.get(i).getId().equalsIgnoreCase(merchant.getId())){
                return false;
            }
        }
        merchants.add(merchant);
        return true;
    }


    public boolean updateMerchant(String id, Merchant merchant){
        for (int i=0;i< merchants.size();i++){
            if (merchants.get(i).getId().equalsIgnoreCase(id)){
                merchant.setId(id);
                merchants.set(i, merchant);
                return true;
            }
        }
        return false;
    }


    public boolean deleteMerchant(String id){
        for (int i=0;i< merchants.size();i++){
            if (merchants.get(i).getId().equalsIgnoreCase(id)){
                merchants.remove(i);
                return true;
            }
        }
        return false;
    }






}
