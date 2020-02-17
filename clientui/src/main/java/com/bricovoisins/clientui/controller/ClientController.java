package com.bricovoisins.clientui.controller;

import com.bricovoisins.clientui.beans.CommentBean;
import com.bricovoisins.clientui.beans.ConventionBean;
import com.bricovoisins.clientui.beans.UserBean;
import com.bricovoisins.clientui.proxies.MicroserviceCommentsProxy;
import com.bricovoisins.clientui.proxies.MicroserviceConventionsProxy;
import com.bricovoisins.clientui.proxies.MicroserviceUsersProxy;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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

    @Autowired
    private MicroserviceCommentsProxy CommentsProxy;

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
    public String getAccessDeniedPage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            catchLoggedUserIdPointsAndFirstName(model);
        }
        return "AccessDenied";
    }

    @GetMapping(value = "/explanations")
    public String getExplicationsPage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            catchLoggedUserIdPointsAndFirstName(model);
        }
        return "Explanations";
    }

    @GetMapping(value = "/inscription")
    public String getRegistrationPage() {
        return "Register";
    }

    @GetMapping(value = "/email_not_found")
    public String getEmailNotFoundPage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            catchLoggedUserIdPointsAndFirstName(model);
        }
        return "EmailNotFound";
    }

    @PostMapping(value = "/validation")
    public void insertUser(HttpServletRequest request, HttpServletResponse response, @RequestParam("avatar") MultipartFile imageFile) throws IOException {
        UserBean newUser = new UserBean();
        setAttributesUser(newUser, request);
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

    @GetMapping(value = "/profile/{userId}")
    public String getProfilePage(@PathVariable int userId, Model model) {
        model.addAttribute("user", new RestTemplate().getForObject("http://localhost:9001/utilisateurId/" + userId, UserBean.class));
        catchLoggedUserIdPointsAndFirstName(model);
        for (ConventionBean convention : Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions_recipient/" + userId, ConventionBean[].class).getBody())) {
            if (convention.getSenderId() == (int) model.getAttribute("userId")) {
                model.addAttribute("hasConvention", true);
                break;
            }
            model.addAttribute("hasConvention", false);
        }
        model.addAttribute("comments", Arrays.asList(new RestTemplate().getForEntity("http://localhost:9003/comments_user/" + userId, CommentBean[].class).getBody()));
        return "Profile";
    }

    @GetMapping(value = "/demands")
    public String getDemandsPage(HttpServletRequest request, Model model) {
        if(request.getSession().getAttribute("search") != null) {
            model.addAttribute("users", request.getSession().getAttribute("users"));
            model.addAttribute("emailLoggedUser", ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
            model.addAttribute("idLoggedUser", new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), UserBean.class).getId());
            model.addAttribute("search", request.getSession().getAttribute("search"));
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
        String message = request.getParameter("message") + "\n\nVous pouvez répondre à " + sender.getFirstName() + " " + sender.getLastName() + " à l'adresse email suivante : " + sender.getEmail() + ".\n" +
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
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            catchLoggedUserIdPointsAndFirstName(model);
        }
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

    @GetMapping(value = "/not_enough_points")
    public String getPageNotEnoughPoints(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            catchLoggedUserIdPointsAndFirstName(model);
        }
        return "NotEnoughPoints";
    }

    @PostMapping(value = "/send_convention")
    public void insertConvention(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        if (sender.getPoints() - Integer.parseInt(request.getParameter("hours-intervention-time")) * 4 - Integer.parseInt(request.getParameter("minutes-intervention-time")) / 15 < 0) {
            response.sendRedirect("/not_enough_points");
            return;
        }
        ConventionBean conventionBean = new ConventionBean();
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
        conventionBean.setDateConvention(LocalDate.now().plusDays(1));
        conventionBean.setDateBeginning(LocalDate.of(Integer.parseInt(request.getParameter("year")), Integer.parseInt(request.getParameter("month")), Integer.parseInt(request.getParameter("day"))));
        conventionBean.setBeginningHour(LocalTime.of(Integer.parseInt(request.getParameter("hour")) + 1, 0));
        conventionBean.setTimeIntervention(LocalTime.of(Integer.parseInt(request.getParameter("hours-intervention-time")) + 1, Integer.parseInt(request.getParameter("minutes-intervention-time"))));
        conventionBean.setPlace(request.getParameter("place"));
        conventionBean.setPhoneNumberHelped(request.getParameter("phone_number"));
        conventionBean.setMessage(request.getParameter("convention"));
        ConventionsProxy.insertConvention(conventionBean);
        sender.setPoints(sender.getPoints() - ((conventionBean.getTimeIntervention().getHour() - 1) * 4) - conventionBean.getTimeIntervention().getMinute() / 15);
        new RestTemplate().put("http://localhost:9001/update_user/", sender);
        response.sendRedirect("/my_conventions/" + conventionBean.getSenderId() + "?sendConvention=true");
    }

    @GetMapping(value = "/addressed_conventions/{recipientId}")
    public String getListAddressedConventions(@PathVariable int recipientId, Model model) {
        List<ConventionBean> allNoValidatedConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/conventions_recipient/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("noValidatedConventions", allNoValidatedConventionsUser);
        List<ConventionBean> allValidatedConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/validated_conventions_recipient/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("validatedConventions", allValidatedConventionsUser);
        List<ConventionBean> allFinishedConventions = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions_recipient/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("finishedConventions", allFinishedConventions);
        catchLoggedUserIdPointsAndFirstName(model);
        return "AddressedConventions";
    }

    public boolean verifyConvention(ConventionBean convention, String typeUser) throws Exception {
        UserBean loggedUser = new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        if (typeUser.equals("sender")) {
            List<ConventionBean> listConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/validated_conventions/" + loggedUser.getId(), ConventionBean[].class).getBody());
            for (ConventionBean oneConventionOfUser : listConventionsUser) {
                if (oneConventionOfUser.getId() == convention.getId()) {
                    return true;
                }
            }
            return false;
        } else if (typeUser.equals("recipient")) {
            List<ConventionBean> listConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/conventions_recipient/" + loggedUser.getId(), ConventionBean[].class).getBody());
            for (ConventionBean oneConventionOfUser : listConventionsUser) {
                if (oneConventionOfUser.getId() == convention.getId()) {
                    return true;
                }
            }
            return false;
        } else {
            throw new Exception("Mauvais paramètre typeUser");
        }
    }

    @GetMapping(value = "/validate_convention_recipient/{id}")
    public void updateIsValidatedByRecipient(@PathVariable int id, HttpServletResponse response) throws Exception {
        ConventionBean modifiedConvention = new RestTemplate().getForObject("http://localhost:9002/convention/" + id, ConventionBean.class);
        if (verifyConvention(modifiedConvention, "recipient")) {
            modifiedConvention.setValidatedByRecipient(true);
            new RestTemplate().put("http://localhost:9002/update_convention", modifiedConvention);
            response.sendRedirect("/addressed_conventions/" + modifiedConvention.getRecipientId() + "?acceptedConvention=true");
        } else {
            response.sendRedirect("/access_denied");
        }
    }

    @GetMapping(value = "/refuse_convention_recipient/{id}")
    public void updateIsRefusedByRecipient(@PathVariable int id, HttpServletResponse response) throws Exception {
        ConventionBean modifiedConvention = new RestTemplate().getForObject("http://localhost:9002/convention/" + id, ConventionBean.class);
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/utilisateurId/" + modifiedConvention.getSenderId(), UserBean.class);
        sender.setPoints(sender.getPoints() + (modifiedConvention.getTimeIntervention().getHour() - 1) * 4 + modifiedConvention.getTimeIntervention().getMinute() / 15);
        new RestTemplate().put("http://localhost:9001/update_user/", sender);
        if (verifyConvention(modifiedConvention, "recipient")) {
            new RestTemplate().delete("http://localhost:9002/delete_convention/" + modifiedConvention.getId());
            response.sendRedirect("/addressed_conventions/" + modifiedConvention.getRecipientId() + "?refusedConvention=true");
        } else {
            response.sendRedirect("/access_denied");
        }
    }

    @GetMapping(value = "/validate_convention_sender/{id}")
    public void updateIsEndedBySender(@PathVariable int id, HttpServletResponse response) throws Exception {
        ConventionBean modifiedConvention = new RestTemplate().getForObject("http://localhost:9002/convention/" + id, ConventionBean.class);
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/utilisateurId/" + modifiedConvention.getRecipientId(), UserBean.class);
        recipient.setPoints(recipient.getPoints() + (modifiedConvention.getTimeIntervention().getHour() - 1) * 4 + modifiedConvention.getTimeIntervention().getMinute() / 15);
        new RestTemplate().put("http://localhost:9001/update_user/", recipient);
        if (verifyConvention(modifiedConvention, "sender")) {
            modifiedConvention.setEndedBySender(true);
            new RestTemplate().put("http://localhost:9002/update_convention", modifiedConvention);
            response.sendRedirect("/addressed_conventions/" + modifiedConvention.getRecipientId() + "?acceptedConvention=true");
        } else {
            response.sendRedirect("/access_denied");
        }
    }

    @GetMapping(value = "/print_convention/{conventionId}")
    public void generateConvention(@PathVariable int conventionId, HttpServletResponse response) throws DocumentException {
        ConventionBean convention = new RestTemplate().getForObject("http://localhost:9002/convention/" + conventionId, ConventionBean.class);
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/static/conventions/" + convention.getLastNameSender() + "-" + convention.getLastNameRecipient() + "-Convention.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();

        addImage(document);
        addText(document, convention);

        document.close();


    }

    private void addImage(Document document) {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("static/img/logo-title.png").toURI());
            Image img = Image.getInstance(path.toAbsolutePath().toString());
            img.setAlignment(Element.ALIGN_CENTER);
            document.add(img);
            addEmptyLine(document, 2);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void addText(Document document, ConventionBean convention) throws DocumentException {
        Paragraph title = new Paragraph("CONVENTION D'INTERVENTION");
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        addEmptyLine(document, 2);
        document.add(new Paragraph("ENTRE LES SOUSSIGNÉS :"));
        addEmptyLine(document, 1);
        document.add(new Paragraph(convention.getFirstNameSender() + " " + convention.getLastNameSender().toUpperCase() + " Domicilié(e) à "));
        document.add(new Paragraph("Et"));
        document.add(new Paragraph(convention.getFirstNameRecipient() + " " + convention.getLastNameRecipient().toUpperCase() + " Domicilié(e) à "));
        addEmptyLine(document, 1);
        document.add(new Paragraph("Il est conclu cette convention d'intervention basée sur une confiance mutuelle, qui est la base de la mise en relation des membres du site BRICOVOISINS."));
        addEmptyLine(document, 1);
        document.add(new Paragraph("L'aidant accomplira l'intervention décrite ci-dessous le (date) à partir de (heure) à l'adresse qui lui a été communiquée par l'aidé."));
        addEmptyLine(document, 1);
        document.add(new Paragraph("La signature des 2 membres vaut acceptation de la description ci-dessous, qu'ils ont préalablement mise au point. A l'issue de l'intervention, il est convenu que l'aidé en validera l'achèvement sur le site BRICOVOISINS."));
        addEmptyLine(document, 2);
        Paragraph description = new Paragraph("DESCRIPTION DE L'INTERVENTION");
        description.setAlignment(Element.ALIGN_CENTER);
        document.add(description);
        addEmptyLine(document, 1);
        document.add(new Paragraph(convention.getMessage()));
        addEmptyLine(document, 1);
        document.add(new Paragraph("Signatures"));
        Paragraph actors = new Paragraph("L'aidant,            L'aidé,");
        actors.setAlignment(Element.ALIGN_CENTER);
        document.add(actors);
    }

    private void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    @GetMapping(value = "/update_profile/{userId}")
    public String updateProfile(@PathVariable int userId, Model model) {
        catchLoggedUserIdPointsAndFirstName(model);
        if (userId == new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class).getId()) {
            model.addAttribute("user", UsersProxy.getOneUser(userId));
            return "Register";
        } else {
            return "AccessDenied";
        }
    }

    @PostMapping(value = "/update_user")
    public void updateUser(HttpServletRequest request, HttpServletResponse response, @RequestParam("avatar") MultipartFile imageFile) throws IOException {
        UserBean user = new UserBean();
        setAttributesUser(user, request);
        UserBean existedUser = new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + request.getParameter("email"), UserBean.class);
        user.setId(existedUser.getId());
        user.setPoints(existedUser.getPoints());
        if (!imageFile.getOriginalFilename().equals("")) {
            try {
                String imageName = saveImage(imageFile);
                user.setAvatar("/avatars/" + imageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            user.setAvatar(existedUser.getAvatar());
        }
        new RestTemplate().put("http://localhost:9001/update_user", user);
        response.sendRedirect("/home?updateUser=true");
    }

    private void setAttributesUser(UserBean user, HttpServletRequest request) {
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setAge(Integer.parseInt(request.getParameter("age")));
        user.setEmail(request.getParameter("email"));
        user.setPassword(new BCryptPasswordEncoder().encode(request.getParameter("password")));
        user.setTown(request.getParameter("town"));
        user.setLevelGardening(request.getParameter("level-gardening"));
        user.setLevelElectricity(request.getParameter("level-electricity"));
        user.setLevelPlumbing(request.getParameter("level-plumbing"));
        user.setLevelCarpentry(request.getParameter("level-carpentry"));
        user.setLevelPainting(request.getParameter("level-painting"));
        user.setLevelMasonry(request.getParameter("level-masonry"));
        user.setLevelDiy(request.getParameter("level-diy"));
        user.setDescription(request.getParameter("description"));
    }

    @PostMapping(value = "/add_comment")
    public void addComment(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommentBean newComment = new CommentBean();
        UserBean author = new RestTemplate().getForObject("http://localhost:9001/utilisateur/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        newComment.setAuthor(author.getFirstName() + " " + author.getLastName());
        newComment.setComment(request.getParameter("comment"));
        newComment.setUserId(Integer.parseInt(request.getParameter("userId")));
        newComment.setAuthorId(author.getId());
        for (ConventionBean convention : Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions_recipient/" + newComment.getUserId(), ConventionBean[].class).getBody())) {
            if (convention.getSenderId() == newComment.getAuthorId()) {
                CommentsProxy.insertComment(newComment);
                response.sendRedirect("/profile/" + newComment.getUserId() + "?addComment=true");
                return;
            }
        }
        throw new Exception("Vous n'avez pas les droits pour ajouter ce commentaire");
    }
}
