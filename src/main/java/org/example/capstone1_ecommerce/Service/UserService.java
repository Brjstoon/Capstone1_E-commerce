package org.example.capstone1_ecommerce.Service;

import org.example.capstone1_ecommerce.Model.MerchantStock;
import org.example.capstone1_ecommerce.Model.Product;
import org.example.capstone1_ecommerce.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {


    ArrayList<User> users = new ArrayList<>();



    public ArrayList<User> getUsers(){
        return users;
    }

    public boolean addUser(User user){
        for (int i=0;i<users.size();i++){
            if (users.get(i).getId().equalsIgnoreCase(user.getId())){
                return false;
            }
        }
        users.add(user);
        return true;
    }

    public boolean updateUser(String id, User user){
        for (int i=0;i< users.size();i++){
            if (users.get(i).getId().equalsIgnoreCase(id)){
                user.setId(id);
                users.set(i, user);
                return true;
            }
        }
        return false;
    }


    public boolean deleteUser(String id){
        for (int i=0;i< users.size();i++){
            if (users.get(i).getId().equalsIgnoreCase(id)){
                users.remove(i);
                return true;
            }
        }
        return false;
    }



    public int buyProduct(String userID, String productID, String merchantID, ArrayList<MerchantStock> merchantStocks, ArrayList<Product> products){
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getId().equalsIgnoreCase(userID)){
                for (int z = 0; z < merchantStocks.size(); z++){
                    if (merchantStocks.get(z).getProductID().equalsIgnoreCase(productID)
                            && merchantStocks.get(z).getMerchantID().equalsIgnoreCase(merchantID)){
                        if (merchantStocks.get(z).getStock() <= 0){
                            return 1; // product out of stock
                        }
                        for (int j = 0; j < products.size(); j++){
                            if (products.get(j).getId().equalsIgnoreCase(productID)){
                                if (users.get(i).getBalance() >= products.get(j).getPrice()){
                                    users.get(i).setBalance(users.get(i).getBalance() - products.get(j).getPrice());
                                    merchantStocks.get(z).setStock( merchantStocks.get(z).getStock() - 1 );
                                    users.get(i).addOrder(products.get(j));
                                    return 0;
                                }
                                return 2; // Not enough balance
                            }
                        }
                        return 3; // product not found in product list
                    }
                }
                return 4; // Product or Merchant not found in MerchantStock
            }
        }
        return 5; // User not found
    }



    public ArrayList<Product> recomendProducts(String id, ArrayList<MerchantStock> merchantStocks, ArrayList<Product> products){

        ArrayList<Product> recommendations = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) { // Loop through user list
            if (users.get(i).getId().equalsIgnoreCase(id)) { // User id verify
                for (int j = 0; j < users.get(i).getOrders().size(); j++) { // Loop through orders list
                    Product currentOrder = users.get(i).getOrders().get(j);
                    for (int z = 0; z < products.size(); z++) { // Loop through products list
                        Product currentProduct = products.get(z);

                        if (currentOrder.getId().equalsIgnoreCase(currentProduct.getId())) { // To eliminate products already bought
                            continue;
                        }
                        if (currentOrder.getCategoryID().equalsIgnoreCase(currentProduct.getCategoryID())) { // To check if any product is in the same category as any product bought previously
                            recommendations.add(currentProduct);
                            continue;
                        }
                        if (currentOrder.getPrice() >= (currentProduct.getPrice() - 1500)
                                && currentOrder.getPrice() <= (currentProduct.getPrice() + 1500)) { // To check if any product is in the range (1500) of any product bought previously
                            recommendations.add(currentProduct);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < recommendations.size(); i++) { // Loop through recommendations to remove duplicates
            for (int x = 0; x < recommendations.size(); x++) {
                if (recommendations.get(i).getId().equalsIgnoreCase(recommendations.get(x).getId()) && x != i) {
                    recommendations.remove(i);
                    i--;
                    break;
                }
            }
        }

        return recommendations;
    }




    public int resellProduct(String sellerID, String buyerID, String productID, int condition){

        User seller = null;
        User buyer = null;
        Product item = null;
        double percentage = 0;

        for (int i=0;i< users.size();i++){
            if (users.get(i).getId().equalsIgnoreCase(sellerID)){
                seller = users.get(i);
            } else if (users.get(i).getId().equalsIgnoreCase(buyerID)) {
                buyer = users.get(i);
            }
        }
        if (seller == null){
            return 1; // seller not found
        } else if (buyer == null) {
            return 2; // buyer not found
        }
        for (int i=0;i<seller.getOrders().size();i++){
            if (seller.getOrders().get(i).getId().equalsIgnoreCase(productID)){
                item = seller.getOrders().get(i);
            }
        }
        if (item == null){
            return 3; // seller don't have this product
        }
        switch (condition){
            case 1:
                percentage = 0.9;
                break;
            case 2:
                percentage = 0.65;
                break;
            case 3:
                percentage = 0.4;
                break;
            default:
                return 4; // wrong condition choice
        }

        if (buyer.getBalance() < item.getPrice()){
            return 5; // insufficient balance
        }

        buyer.setBalance( buyer.getBalance() - (item.getPrice() * percentage ) );
        seller.setBalance( seller.getBalance() + (item.getPrice() * percentage ) );
        buyer.addOrder(item);
        for (int i=0;i<seller.getOrders().size();i++){
            if (seller.getOrders().get(i).getId().equalsIgnoreCase(item.getId())){
                seller.getOrders().remove(i);
                break;
            }
        }
        return 0; // Success

    }



    public int applyDiscount(String userID, String category, double discount, ArrayList<Product> products){

        boolean catFlag=false;
        boolean found=false;
        for (int i=0;i< users.size();i++){
            if (users.get(i).getId().equalsIgnoreCase(userID)) {
                found=true;
                if (!users.get(i).getRole().equalsIgnoreCase("Admin")){
                    return 1; // User is not an admin
                }
                break;
            }
        }
        if (!found){
            return 2; // user not found
        }
        if (discount > 100 || discount <0){
            return 3; // invalid discount amount
        }
        discount = discount/100;
        if (category.equalsIgnoreCase("all")){
            for (int i=0;i<products.size();i++){
                products.get(i).setPrice(products.get(i).getPrice() - (discount * products.get(i).getPrice()));
            }
            return 0; // Discount applied successfully
        }else {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getCategoryID().equalsIgnoreCase(category)){
                    products.get(i).setPrice(products.get(i).getPrice() - (discount * products.get(i).getPrice()));
                    catFlag=true;
                }
            }
            if (catFlag){
                return 111; // Discount applied to category successfully
            }
        }
        return 4; // category entered not found
    }



    public String topSpender(){

        double[] totalSpent = new double[users.size()];
        double highest=0;
        User topSpender=users.get(0);

        for (int i=0;i< users.size();i++){
            for (int j=0;j<users.get(i).getOrders().size();j++){
                totalSpent[i] += users.get(i).getOrders().get(j).getPrice();
            }
        }
        for (int i=0;i<totalSpent.length;i++){
            if (highest > totalSpent[i]){
                continue;
            }
            highest = totalSpent[i];
            topSpender = users.get(i);
        }
        return "Top spender: " + topSpender.getUserName() + ", Total spent: " + highest;
    }


    public int loyaltyDiscount(String id, int count, double gift){
        boolean found=false;
        if (gift > 100 || gift <0){
            return 3; // invalid discount amount
        }
        gift/=100;
        for (int i=0;i< users.size();i++){
            if (users.get(i).getId().equalsIgnoreCase(id)) {
                found=true;
                if (!users.get(i).getRole().equalsIgnoreCase("Admin")){
                    return 1; // User is not an admin
                }
                break;
            }
        }
        if (!found){
            return 2; // user not found
        }

        for (int i=0;i< users.size();i++){
            if (users.get(i).getOrders().size() >= count){
                users.get(i).setBalance( users.get(i).getBalance() + (gift * users.get(i).getBalance()));
            }
        }
        return 0; // Gift added successfully
    }


    public int deactivateUser(String reqID, String targetID){

        double threshold = 10000;
        double totalSpent = 0;
        User requister = null;
        User target = null;

        for (int i=0;i<users.size();i++){
            if (users.get(i).getId().equalsIgnoreCase(reqID)){
                requister = users.get(i);
            }
            if (users.get(i).getId().equalsIgnoreCase(targetID)){
                target = users.get(i);
            }
        }
        if (requister == null){
            return 1; // requister not found
        } else if (!requister.getRole().equalsIgnoreCase("Admin")) {
            return 2; // requister is not an admin
        }
        if (target == null){
            return 3; // target not found
        }

        for (int i=0;i<target.getOrders().size();i++){
            totalSpent += target.getOrders().get(i).getPrice();
        }
        if (totalSpent >= threshold){
            return 222; // Target has a total spend value more than 10,000 so was not deactivated
        }
        for (int i=0;i<users.size();i++){
            if (users.get(i).getId().equalsIgnoreCase(targetID)) {
                if (target.getRole().equalsIgnoreCase("Admin")) {
                    users.get(i).setRole("Customer");
                    return 111; // Target was demoted from admin to customer
                }
            }
            if (users.get(i).getId().equalsIgnoreCase(targetID)){
                users.remove(i);
                break;
            }
        }
        return 0; // Target was removed
    }
}