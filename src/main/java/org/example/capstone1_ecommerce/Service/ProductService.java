package org.example.capstone1_ecommerce.Service;

import org.example.capstone1_ecommerce.Model.Product;
import org.example.capstone1_ecommerce.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductService {


    ArrayList<Product> products = new ArrayList<>();


    public ArrayList<Product> getProducts(){
        return products;
    }


    public boolean addProduct(Product product){
        for (int i=0;i< products.size();i++){
            if (products.get(i).getId().equalsIgnoreCase(product.getId())){
                return false;
            }
        }
        products.add(product);
        return true;
    }


    public boolean updateProduct(String id, Product product){
        for (int i=0;i< products.size();i++){
            if (products.get(i).getId().equalsIgnoreCase(id)){
                product.setId(id);
                products.set(i, product);
                return true;
            }
        }
        return false;
    }


    public boolean deleteProduct(String id){
        for (int i=0;i< products.size();i++){
            if (products.get(i).getId().equalsIgnoreCase(id)){
                products.remove(i);
                return true;
            }
        }
        return false;
    }







    public ArrayList<Product> trendingProducts(ArrayList<User> users){

        ArrayList<Product> trending = new ArrayList<>();
        int[] counts = new int[products.size()];
        int total=0;
        int count=0;

        for (User user : users) {
            for (int j = 0; j < products.size(); j++) {
                for (int z = 0; z < user.getOrders().size(); z++) {
                    if (products.get(j).getId().equalsIgnoreCase(user.getOrders().get(z).getId())) {
                        counts[j]++;
                    }
                }
            }
        }


        for (int j=0;j< products.size();j++) {
            total=0;
            count=0;
            for (int i=0;i< products.size();i++) {
                if (products.get(j).getCategoryID().equalsIgnoreCase(products.get(i).getCategoryID())) {
                    total += counts[i];
                    count++;
                }
            }

            if (counts[j] > (total / count)){
                trending.add(products.get(j));
            }
        }
        return trending;
    }





}
