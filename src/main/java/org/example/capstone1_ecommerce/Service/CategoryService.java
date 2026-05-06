package org.example.capstone1_ecommerce.Service;

import org.example.capstone1_ecommerce.Model.Category;
import org.example.capstone1_ecommerce.Model.Product;
import org.example.capstone1_ecommerce.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {


    ArrayList<Category> categories = new ArrayList<>();


    public ArrayList<Category> getCategorys(){
        return categories;
    }

    public boolean addCategory(Category category){
        for (int i=0;i< categories.size();i++){
            if (categories.get(i).getName().equalsIgnoreCase(category.getName())){
                return false;
            }
        }
        categories.add(category);
        return true;
    }


    public boolean updateCategory(int index, Category categorie){
        if (categories.size()> index){
            categories.set(index, categorie);
            return true;
        }

        return false;
    }


    public boolean deleteCategory(int index){

        if (categories.size()> index){
            categories.remove(index);
            return true;
        }

        return false;
    }



    public String mostPopularCategory(ArrayList<User> users, ArrayList<Product> products){

        int[] counts = new int[categories.size()];
        int count=0;
        String popularCategory="";

        for (User user : users) {
            for (int j = 0; j < categories.size(); j++) {
                for (int z = 0; z < user.getOrders().size(); z++) {
                    if (categories.get(j).getName().equalsIgnoreCase(user.getOrders().get(z).getCategoryID())) {
                        counts[j]++;
                    }
                }
            }
        }

        for (int i=0;i<counts.length;i++){
            if (counts[i] > count){
                popularCategory = categories.get(i).getName();
                count=counts[i];
            }
        }
        return popularCategory;
    }



}
