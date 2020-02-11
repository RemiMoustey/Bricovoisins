package com.bricovoisins.clientui.controller;

import com.bricovoisins.clientui.beans.UserBean;
import com.bricovoisins.clientui.proxies.MicroserviceUsersProxy;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
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
import java.util.List;

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

    @GetMapping(value = "/demands")
    public String getDemandsPage(HttpServletRequest request, Model model) {
        if(request.getSession().getAttribute("search") != null) {
            model.addAttribute("users", request.getSession().getAttribute("users"));
            model.addAttribute("emailLoggedUser", ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
            model.addAttribute("idLoggedUser", new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), UserBean.class).getId());
            request.getSession().removeAttribute("search");
            request.getSession().removeAttribute("users");
        }
        return "Demands";
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "Login";
    }

    @PostMapping(value = "/results")
    public void getResults(@RequestParam String search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<UserBean> users = UsersProxy.getSearchedUsers(search);
        request.getSession().setAttribute("search", search);
        request.getSession().setAttribute("users", users);
        response.sendRedirect("/demands");
    }

    @GetMapping(value = "/send_message/{senderId}/{recipientId}")
    public String getMessagePage(@PathVariable int senderId, @PathVariable int recipientId, Model model) {
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/utilisateurs/" + senderId, UserBean.class);
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/utilisateurs/" + recipientId, UserBean.class);
        model.addAttribute("sender", sender);
        model.addAttribute("recipient", recipient);
        return "SendMessage";
    }

    @PostMapping(value = "/insert_message")
    public void insertMessageInConvention() {

    }
}
