package com.example.login.controller;

import com.example.login.database.Product;
import com.example.login.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    ProductService productService;

    @GetMapping("/admin/products")
    public ModelAndView getProducts(){

        ModelAndView modelAndView = new ModelAndView("/admin/products.html");
        List<Product> productsList = productService.findAllProducts();
        modelAndView.addObject("products", productsList);
        return modelAndView;
    }

    @PostMapping("/admin/products")
    @ResponseBody
    public String addProdusct(@RequestParam("name") String name,
                              @RequestParam("category") String category,
                              @RequestParam("price") Double price,
                              @RequestParam("quantity") Integer quantity){

      return   productService.addProduct(name, category, price, quantity);
    }

    @DeleteMapping("/admin/products/{id}")
    @ResponseBody
    public  String deleteProduct(@PathVariable("id") int id){
        return productService.deleteProduct(id);
    }
}
