package com.bricovoisins.clientui.controller;

import com.bricovoisins.clientui.beans.UserBean;
import com.bricovoisins.clientui.proxies.MicroserviceUsersProxy;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceUsersProxy UsersProxy;

    @GetMapping(value = "/home")
    public String getHomePage() {
        return "Home";
    }

    @GetMapping(value = "/explications")
    public String getExplicationsPage() {
        return "Explications";
    }

    @GetMapping(value = "/inscription")
    public String getRegistrationPage() {
        return "Register";
    }

    @PostMapping(value = "/validation")
    public void insertUser(HttpServletRequest request, HttpServletResponse response, @RequestParam("avatar") MultipartFile imageFile) throws IOException {
        UserBean newUser = new UserBean();
        newUser.setFirstName(request.getParameter("firstName"));
        newUser.setLastName(request.getParameter("lastName"));
        newUser.setAge(Integer.parseInt(request.getParameter("age")));
        newUser.setEmail(request.getParameter("email"));
        newUser.setPassword(new BCryptPasswordEncoder().encode(request.getParameter("password")));
        newUser.setTown(request.getParameter("town"));
        if (!imageFile.getOriginalFilename().equals("")) {
            try {
                String imageName = saveImage(imageFile);
                newUser.setAvatar("/avatars/" + imageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newUser.setAvatar("/avatars/default.png");
        }
        newUser.setPoints(10);
        newUser.setLevelGardening(request.getParameter("level-gardening"));
        newUser.setLevelElectricity(request.getParameter("level-electricity"));
        newUser.setLevelPlumbing(request.getParameter("level-plumbing"));
        newUser.setLevelCarpentry(request.getParameter("level-carpentry"));
        newUser.setLevelPainting(request.getParameter("level-painting"));
        newUser.setLevelMasonry(request.getParameter("level-masonry"));
        newUser.setLevelDiy(request.getParameter("level-diy"));
        newUser.setDescription(request.getParameter("description"));
        UsersProxy.insertUser(newUser, imageFile);
        response.sendRedirect("/home");
    }

    private String saveImage(MultipartFile imageFile) throws Exception {
        String folder = "./src/main/resources/static/avatars/";
        byte[] bytes = imageFile.getBytes();
        String currentDate = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        Path path = Paths.get(folder + imageFile.getOriginalFilename().replace(imageFile.getOriginalFilename(), FilenameUtils.getBaseName(imageFile.getOriginalFilename()).concat(currentDate) + "." + FilenameUtils.getExtension(imageFile.getOriginalFilename())).toLowerCase());
        Files.write(path, bytes);
        return FilenameUtils.removeExtension(imageFile.getOriginalFilename()) + currentDate + "." + FilenameUtils.getExtension(imageFile.getOriginalFilename()).toLowerCase();
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "Login";
    }
}
