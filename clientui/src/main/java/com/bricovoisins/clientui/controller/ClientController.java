package com.bricovoisins.clientui.controller;

import com.bricovoisins.clientui.beans.ConventionBean;
import com.bricovoisins.clientui.beans.UserBean;
import com.bricovoisins.clientui.proxies.MicroserviceConventionsProxy;
import com.bricovoisins.clientui.proxies.MicroserviceUsersProxy;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceUsersProxy UsersProxy;

    @Autowired
    private MicroserviceConventionsProxy ConventionsProxy;

    public void catchLoggedUserIdPointsAndFirstName(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        RestTemplate restTemplate = new RestTemplate();
        UserBean loggedUser = restTemplate.getForObject("http://localhost:9001/utilisateur/" + email, UserBean.class);
        if(loggedUser != null) {
            model.addAttribute("firstName", loggedUser.getFirstName());
            model.addAttribute("points", loggedUser.getPoints());
            model.addAttribute("userId", loggedUser.getId());
        }
    }

    @GetMapping(value = "/home")
    public String getHomePage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            catchLoggedUserIdPointsAndFirstName(model);
        }
        return "Home";
    }

    @GetMapping(value = "/access_denied")
    public String getAccessDeniedPage() {
        return "AccessDenied";
    }

    @GetMapping(value = "/explications")
    public String getExplicationsPage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            catchLoggedUserIdPointsAndFirstName(model);
        }
        return "Explications";
    }

    @GetMapping(value = "/inscription")
    public String getRegistrationPage() {
        return "Register";
    }

    @GetMapping(value = "/email_not_found")
    public String getEmailNotFoundPage() {
        return "EmailNotFound";
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
        catchLoggedUserIdPointsAndFirstName(model);
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
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/utilisateurId/" + senderId, UserBean.class);
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/utilisateurId/" + recipientId, UserBean.class);
        model.addAttribute("sender", sender);
        model.addAttribute("recipient", recipient);
        catchLoggedUserIdPointsAndFirstName(model);
        return "SendMessage";
    }

    public String getCompetences(UserBean sender) {
        String competences = "";
        if (sender.getLevelGardening() != null) {
            if (sender.getLevelGardening().equals("little-works-gardening")) {
                competences += "Jardinier petits travaux\n";
            } else if (sender.getLevelGardening().equals("connoisseur-gardening")) {
                competences += "Jardinier confirmé\n";
            } else if (sender.getLevelGardening().equals("expert-gardening")) {
                competences += "Jardinier expert\n";
            }
        } if (sender.getLevelElectricity() != null) {
            if (sender.getLevelElectricity().equals("little-works-electricity")) {
                competences += "Électricien petits travaux\n";
            } else if (sender.getLevelElectricity().equals("connoisseur-electricity")) {
                competences += "Électricien confirmé\n";
            } else if (sender.getLevelElectricity().equals("expert-electricity")) {
                competences += "Électricien expert\n";
            }
        } if (sender.getLevelPlumbing() != null) {
            if (sender.getLevelPlumbing().equals("little-works-plumbing")) {
                competences += "Plombier petits travaux\n";
            } else if (sender.getLevelPlumbing().equals("connoisseur-plumbing")) {
                competences += "Plombier confirmé\n";
            } else if (sender.getLevelPlumbing().equals("expert-plumbing")) {
                competences += "Plombier expert\n";
            }
        } if (sender.getLevelCarpentry() != null) {
            if (sender.getLevelCarpentry().equals("little-works-carpentry")) {
                competences += "Menuisier petits travaux\n";
            } else if (sender.getLevelCarpentry().equals("connoisseur-carpentry")) {
                competences += "Menuisier confirmé\n";
            } else if (sender.getLevelCarpentry().equals("expert-carpentry")) {
                competences += "Menuisier expert\n";
            }
        } if (sender.getLevelPainting() != null) {
            if (sender.getLevelPainting().equals("little-works-painting")) {
                competences += "Peintre petits travaux\n";
            } else if (sender.getLevelPainting().equals("connoisseur-painting")) {
                competences += "Peintre confirmé\n";
            } else if (sender.getLevelPainting().equals("expert-painting")) {
                competences += "Peintre expert\n";
            }
        } if (sender.getLevelMasonry() != null) {
            if (sender.getLevelMasonry().equals("little-works-masonry")) {
                competences += "Maçon petits travaux\n";
            } else if (sender.getLevelMasonry().equals("connoisseur-masonry")) {
                competences += "Maçon confirmé\n";
            } else if (sender.getLevelMasonry().equals("expert-masonry")) {
                competences += "Maçon expert\n";
            }
        } if (sender.getLevelDiy() != null) {
            if (sender.getLevelDiy().equals("little-works-diy")) {
                competences += "Bricoleur petits travaux\n";
            } else if (sender.getLevelDiy().equals("connoisseur-diy")) {
                competences += "Bricoleur confirmé\n";
            } else if (sender.getLevelDiy().equals("expert-diy")) {
                competences += "Bricoleur expert\n";
            }
        }
        return competences;
    }

    @PostMapping(value = "/insert_message")
    public void insertMessageInConvention(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/utilisateurId/" + Integer.parseInt(request.getParameter("senderId")), UserBean.class);
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/utilisateurId/" + Integer.parseInt(request.getParameter("recipientId")), UserBean.class);
        String competences = getCompetences(sender);
        String message = request.getParameter("message") + "\n\nVous pouvez répondre à " + sender.getFirstName() + " " + sender.getLastName() + "à l'adresse email suivante : " + sender.getEmail() + ".\n" +
                sender.getFirstName() + " " + "habite à " + sender.getTown() + ". Ses compétences sont les suivantes :\n" + competences;
        readPropertiesAndSend(recipient.getEmail(), "Vous avez reçu un message de " + sender.getFirstName() + " " + sender.getLastName(), message);
        response.sendRedirect("/home?sendMessage=true");
    }

    private void readPropertiesAndSend(String email, String subject, String sentMessage) throws IOException, MessagingException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("configuration.properties");
        Properties properties = new Properties();
        if(inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("Le fichier de propriétés n'existe pas");
        }
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(properties.getProperty("mail.smtp.user")));
        message.setSubject(subject);
        message.setContent(sentMessage, "text/plain");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

        Transport transport = session.getTransport("smtp");
        transport.connect(properties.getProperty("mail.smtp.host"), properties.getProperty("mail.smtp.user"), properties.getProperty("mail.smtp.password"));
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    @GetMapping(value = "/create_convention")
    public String getPageCreateConvention(Model model) {
        catchLoggedUserIdPointsAndFirstName(model);
        return "CreateConvention";
    }

    @GetMapping(value = "/my_conventions/{userId}")
    public String getListConventionsUser(@PathVariable int userId, Model model) {
        List<ConventionBean> allCurrentConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/conventions/" + userId, ConventionBean[].class).getBody());
        model.addAttribute("conventions", allCurrentConventionsUser);
        List<ConventionBean> allValidatedConventionUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/validated_conventions/" + userId, ConventionBean[].class).getBody());
        model.addAttribute("validatedConventions", allValidatedConventionUser);
        List<ConventionBean> allFinishedConventions = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions/" + userId, ConventionBean[].class).getBody());
        model.addAttribute("finishedConventions", allFinishedConventions);
        catchLoggedUserIdPointsAndFirstName(model);
        return "MyConventions";
    }

    @PostMapping(value = "/send_convention")
    public void insertConvention(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConventionBean conventionBean = new ConventionBean();
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        conventionBean.setSenderId(sender.getId());
        conventionBean.setFirstNameSender(sender.getFirstName());
        conventionBean.setLastNameSender(sender.getLastName());
        if (new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + request.getParameter("helperEmail"), UserBean.class) == null) {
            response.sendRedirect("/email_not_found");
            return;
        }
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + request.getParameter("helperEmail"), UserBean.class);
        conventionBean.setRecipientId(recipient.getId());
        conventionBean.setFirstNameRecipient(recipient.getFirstName());
        conventionBean.setLastNameRecipient(recipient.getLastName());
        conventionBean.setDateConvention(LocalDate.of(Integer.parseInt(request.getParameter("year")), Integer.parseInt(request.getParameter("month")), Integer.parseInt(request.getParameter("day"))));
        conventionBean.setBeginningHour(LocalTime.of(Integer.parseInt(request.getParameter("hour")), 0));
        conventionBean.setTimeIntervention(LocalTime.of(Integer.parseInt(request.getParameter("hours-intervention-time")), Integer.parseInt(request.getParameter("minutes-intervention-time"))));
        conventionBean.setPlace(request.getParameter("place"));
        conventionBean.setPhoneNumberHelped(request.getParameter("phone_number"));
        conventionBean.setMessage(request.getParameter("convention"));
        ConventionsProxy.insertConvention(conventionBean);
        response.sendRedirect("/my_conventions/" + conventionBean.getSenderId() + "?sendConvention=true");
    }

    @GetMapping(value = "/addressed_conventions/{recipientId}")
    public String getListAddressedConventions(@PathVariable int recipientId, Model model) {
        List<ConventionBean> allNoValidatedConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/conventions_recipient/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("noValidatedConventions", allNoValidatedConventionsUser);
        List<ConventionBean> allValidatedConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/validated_conventions_recipient/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("validatedConventions", allValidatedConventionsUser);
        List<ConventionBean> allFinishedConventions = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("finishedConventions", allFinishedConventions);
        catchLoggedUserIdPointsAndFirstName(model);
        return "AddressedConventions";
    }

    public boolean verifyConvention(ConventionBean convention) {
        UserBean loggedUser = new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        List<ConventionBean> listConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/conventions/" + loggedUser.getId(), ConventionBean[].class).getBody());
        for (ConventionBean oneConventionOfUser : listConventionsUser) {
            if (oneConventionOfUser.getId() == convention.getId()) {
                return true;
            }
        }
        return false;
    }

    @GetMapping(value = "/validate_convention_recipient/{id}")
    public void updateIsValidatedByRecipient(@PathVariable int id, HttpServletResponse response) throws IOException {
        ConventionBean modifiedConvention = new RestTemplate().getForObject("http://localhost:9002/convention/" + id, ConventionBean.class);
        if (verifyConvention(modifiedConvention)) {
            modifiedConvention.setValidatedByRecipient(true);
            new RestTemplate().put("http://localhost:9002/update_convention", modifiedConvention);
            response.sendRedirect("/addressed_conventions/" + modifiedConvention.getRecipientId() + "?acceptedConvention=true");
        } else {
            response.sendRedirect("/access_denied");
        }
    }

    @GetMapping(value = "/refuse_convention_recipient/{id}")
    public void updateIsRefusedByRecipient(@PathVariable int id, HttpServletResponse response) throws IOException {
        ConventionBean modifiedConvention = new RestTemplate().getForObject("http://localhost:9002/convention/" + id, ConventionBean.class);
        new RestTemplate().delete("http://localhost:9002/delete_convention/" + modifiedConvention.getId());
        response.sendRedirect("/addressed_conventions/" + modifiedConvention.getRecipientId() + "?refusedConvention=true");
    }

    @GetMapping(value = "/validate_convention_sender/{id}")
    public void updateIsEndedBySender(@PathVariable int id, HttpServletResponse response) throws IOException {
        ConventionBean modifiedConvention = new RestTemplate().getForObject("http://localhost:9002/convention/" + id, ConventionBean.class);
        modifiedConvention.setEndedBySender(true);
        new RestTemplate().put("http://localhost:9002/update_convention", modifiedConvention);
        response.sendRedirect("/addressed_conventions/" + modifiedConvention.getRecipientId() + "?acceptedConvention=true");
    }
}
