package com.example.login.service;

import com.example.login.database.User;
import com.example.login.database.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    public void registerUser(String email, String password1, String password2) throws UserEXception {

        if (!password1.equals(password2)) {
            throw new UserEXception("Parolele nu sunt identice");
        }
        User user = new User();
        user.setPassword(password1);
        user.setEmail(email);
        userDAO.save(user);
    }

    public List<User> loginUser(String email, String password) throws UserEXception {
        List<User> userlist = userDAO.findAllByEmail(email);


            if (userlist.isEmpty()) {
                throw new UserEXception("user/parola incorecte");
            }
        User user = userlist.get(0);

            if (!user.getPassword().equals(password)){
                throw new UserEXception("user/parola incorecte");
        }
            return userlist;
    }

}
