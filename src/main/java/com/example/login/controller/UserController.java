package com.example.login.controller;

import com.example.login.database.*;
import com.example.login.service.ProductService;
import com.example.login.service.UserEXception;
import com.example.login.security.UserSession;
import com.example.login.service.UserService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static javax.swing.UIManager.get;

@Controller
public class UserController {


    @Autowired
    UserSession userSession;
    int item = 0;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderLinesDao orderLinesDao;

    @GetMapping("/register-form")
    public ModelAndView registerAction(@RequestParam("email") String email ,
                                       @RequestParam("password1")String password1,
                                       @RequestParam("password2") String password2){
        ModelAndView modelAndView = new ModelAndView("register");

        try {
            userService.registerUser(email, password1, password2);
        } catch (UserEXception e) {
            modelAndView.addObject("message", e.getMessage());
            return modelAndView;
        }
        return new ModelAndView("redirect:index.html");
    }

    @GetMapping("/register")
    public ModelAndView register(){
        return new ModelAndView("register");
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password){
        ModelAndView modelAndView = new ModelAndView("index");

        List<User> userList;
        try {
            userList= userService.loginUser(email, password);
        } catch (UserEXception e) {
            modelAndView.addObject("message", e.getMessage());
            return modelAndView;
        }


        userSession.setId(userList.get(0).getId());
        return new ModelAndView("redirect:dashboard");
    }

    @GetMapping("dashboard")
    public ModelAndView dashboard(){
        ModelAndView modelAndView = new ModelAndView("index");
        if (userSession.getId()<=0){
            return modelAndView;
        }
        List<Product> productList = productService.findAllProducts();
        modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("productList", productList);

        item=userSession.getCartSize();
        modelAndView.addObject("items", item);
        return  modelAndView;
    }

    @GetMapping("/add-to-cart")
    public ModelAndView addToCart(@RequestParam("productId") int productId){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        if (userSession.getId()<=0){
            return new ModelAndView("index");
        }
        List<Product> productList = productService.findAllProducts();
        modelAndView.addObject("productList", productList);

    userSession.addToCart(productId);
        System.out.println(userSession.getCart());
        item= userSession.getCartSize();
        modelAndView.addObject("items", item);
        return new ModelAndView("redirect:dashboard");
    }

    @GetMapping("/cos")
    public ModelAndView getCart(){
        ModelAndView modelAndView = new ModelAndView("cos");
        if (userSession.getId()<=0){
            return modelAndView;
        }
        List<Product> produseBD = productService.findAllProducts();
        List<CosProduct> produseCos = new ArrayList<>();
        double totalOrderAmount=0;

        for(int idProdusCos: userSession.getCart().keySet()){
        for (Product product: produseBD){
            if (product.getId() == idProdusCos){
//                produseCos.add(product);
                CosProduct cosProduct = new CosProduct();
                cosProduct.setCantitate(userSession.getCart().get(idProdusCos));
                cosProduct.setId(product.getId());
                cosProduct.setCategory(product.getCategory());
                cosProduct.setName(product.getName());
                cosProduct.setPrice(product.getPrice());
                cosProduct.setPretTotal(userSession.getCart().get(idProdusCos) * product.getPrice());
                totalOrderAmount = totalOrderAmount + userSession.getCart().get(idProdusCos) * product.getPrice();
                produseCos.add(cosProduct);
            }
        }
        }
        modelAndView.addObject("itemList", produseCos);
        modelAndView.addObject("totalPretComanda", totalOrderAmount);
        return modelAndView;


        }
    @GetMapping("/logout")
    public ModelAndView logout(){
        userSession.setId(0);
        return new ModelAndView("index");
    }


    @PostMapping("/sendOrder")
    public ModelAndView sendOrder(){

        ModelAndView modelAndView = new ModelAndView("orderSuccess");
        List<Product> produseBD = productService.findAllProducts();

        for(int idProdusCos: userSession.getCart().keySet()) {
            for (Product product: produseBD){
                if (product.getId() == idProdusCos){
                    OrderLiners  orderLiner = new OrderLiners();
                    orderLiner.setProductId(idProdusCos); //id produs
                    orderLiner.setQuantity(userSession.getCart().get(idProdusCos)); //cantitate din cos
                    orderLiner.setTotalPrice(userSession.getCart().get(idProdusCos) * product.getPrice());

                    Order order = new Order();
                    order.setUser_id(userSession.getId());
                    order.setAddress("strada cu flor");
                    orderLiner.setOrder(order);
                    orderLinesDao.save(orderLiner);
                }
        }}
        userSession.getCart().clear();
        return modelAndView;
    }





}
