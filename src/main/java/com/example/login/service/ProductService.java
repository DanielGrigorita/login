package com.example.login.service;

import com.example.login.database.Product;
import com.example.login.database.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductDao productDao;

    public List<Product> findAllProducts(){
        return (List<Product>) productDao.findAll();
    }

    public String addProduct(String name, String category, double price, int quantity){

        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        productDao.save(product);
        return "produsul " + name + "a fost adaugat";
    }

    public String deleteProduct(int id){
        productDao.deleteById(id);
        return "Produsul cu id " + id + "a fost sters";
    }

}
