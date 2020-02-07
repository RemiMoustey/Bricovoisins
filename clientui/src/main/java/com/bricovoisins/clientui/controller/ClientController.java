package com.bricovoisins.clientui.controller;

import com.bricovoisins.clientui.beans.UserBean;
import com.bricovoisins.clientui.proxies.MicroserviceUsersProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceUsersProxy UsersProxy;

    @GetMapping(value = "/accueil")
    public String getHomePage() {
        return "Home";
    }

    @GetMapping(value = "/inscription")
    public String getRegistrationPage() {
        return "Register";
    }

    @PostMapping(value = "/validation")
    public void insertUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserBean newUser = new UserBean();
        newUser.setFirstName(request.getParameter("firstName"));
        newUser.setLastName(request.getParameter("lastName"));
        newUser.setEmail(request.getParameter("email"));
        newUser.setPassword(new BCryptPasswordEncoder().encode(request.getParameter("password")));
        newUser.setAddress(request.getParameter("address"));
        newUser.setPostalCode(Integer.parseInt(request.getParameter("postalCode")));
        newUser.setTown(request.getParameter("town"));
        newUser.setPoints(8);
        String qualifications = request.getParameter("qualification1");
        for (int i = 1; i < 6; i++) {
            if(request.getParameter("qualification" + (i + 1)) != null) {
                qualifications += ", " + request.getParameter("qualification" + (i + 1));
            } else {
                break;
            }
        }
        newUser.setQualifications(qualifications);
        newUser.setDescription(request.getParameter("description"));
        UsersProxy.insertUser(newUser);
        response.sendRedirect("/accueil");
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "Login";
    }
}
