package com.bricovoisins.clientui.controller;

import com.bricovoisins.clientui.beans.OpinionBean;
import com.bricovoisins.clientui.beans.ConventionBean;
import com.bricovoisins.clientui.beans.UserBean;
import com.bricovoisins.clientui.proxies.MicroserviceOpinionsProxy;
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
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceUsersProxy UsersProxy;

    @Autowired
    private MicroserviceConventionsProxy ConventionsProxy;

    @Autowired
    private MicroserviceOpinionsProxy OpinionsProxy;

    public void catchLoggedUserIdPointsAndFirstName(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        RestTemplate restTemplate = new RestTemplate();
        UserBean loggedUser = restTemplate.getForObject("http://localhost:9001/user/" + email, UserBean.class);
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
    public void insertUser(HttpServletRequest request, HttpServletResponse response, @RequestParam("avatar") MultipartFile imageFile) throws Exception {
        if (request.getParameter("description").length() > 200) {
            throw new Exception("Description trop longue");
        }
        UserBean newUser = initializeUser(response, request, "post");
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

    private boolean isMailAlreadyInList(List<UserBean> users, String email) {
        for (UserBean user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping(value = "/email_already")
    public String getEmailAlreadyPage() {
        return "EmailAlready";
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
        UserBean user = new RestTemplate().getForObject("http://localhost:9001/userId/" + userId, UserBean.class);
        model.addAttribute("user", user);
        catchLoggedUserIdPointsAndFirstName(model);
        UserBean visitor = new RestTemplate().getForObject("http://localhost:9001/user/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        model.addAttribute("visitorIsAdmin", visitor.getIsAdmin());
        for (ConventionBean convention : Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions_recipient/" + userId, ConventionBean[].class).getBody())) {
            if (convention.getSenderId() == (int) model.getAttribute("userId")) {
                model.addAttribute("hasConvention", true);
                break;
            }
            model.addAttribute("hasConvention", false);
        }
        model.addAttribute("opinions", Arrays.asList(new RestTemplate().getForEntity("http://localhost:9003/opinions_user/" + userId, OpinionBean[].class).getBody()));
        model.addAttribute("titlePage", "Profil de " + user.getFirstName() + " " + user.getLastName());
        return "Profile";
    }

    @GetMapping(value = "/demands")
    public String getDemandsPage(HttpServletRequest request, Model model) {
        if(request.getSession().getAttribute("search") != null) {
            model.addAttribute("users", request.getSession().getAttribute("users"));
            model.addAttribute("emailLoggedUser", ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
            model.addAttribute("idLoggedUser", new RestTemplate().getForObject("http://localhost:9001/user/" + ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), UserBean.class).getId());
            model.addAttribute("search", request.getSession().getAttribute("search"));
            request.getSession().removeAttribute("search");
            request.getSession().removeAttribute("users");
        }
        catchLoggedUserIdPointsAndFirstName(model);
        return "Demands";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(Principal user) {
        if (user != null) {
            return "Home";
        }
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
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/userId/" + senderId, UserBean.class);
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/userId/" + recipientId, UserBean.class);
        model.addAttribute("sender", sender);
        model.addAttribute("recipient", recipient);
        catchLoggedUserIdPointsAndFirstName(model);
        return "SendMessage";
    }

    private String getGardening(UserBean sender) {
        String competence = "";
        if (sender.getLevelGardening() != null) {
            if (sender.getLevelGardening().equals("little-works-gardening")) {
                competence += "Jardinier petits travaux\n";
            } else if (sender.getLevelGardening().equals("connoisseur-gardening")) {
                competence += "Jardinier confirmé\n";
            } else if (sender.getLevelGardening().equals("expert-gardening")) {
                competence += "Jardinier expert\n";
            }
        }
        return competence;
    }

    private String getPlumbing(UserBean sender) {
        String competence = "";
        if (sender.getLevelPlumbing() != null) {
            if (sender.getLevelPlumbing().equals("little-works-plumbing")) {
                competence += "Plombier petits travaux\n";
            } else if (sender.getLevelPlumbing().equals("connoisseur-plumbing")) {
                competence += "Plombier confirmé\n";
            } else if (sender.getLevelPlumbing().equals("expert-plumbing")) {
                competence += "Plombier expert\n";
            }
        }
        return competence;
    }

    private String getCarpentry(UserBean sender) {
        String competence = "";
        if (sender.getLevelCarpentry() != null) {
            if (sender.getLevelCarpentry().equals("little-works-carpentry")) {
                competence += "Menuisier petits travaux\n";
            } else if (sender.getLevelCarpentry().equals("connoisseur-carpentry")) {
                competence += "Menuisier confirmé\n";
            } else if (sender.getLevelCarpentry().equals("expert-carpentry")) {
                competence += "Menuisier expert\n";
            }
        }
        return competence;
    }

    private String getPainting(UserBean sender) {
        String competence = "";
        if (sender.getLevelPainting() != null) {
            if (sender.getLevelPainting().equals("little-works-painting")) {
                competence += "Peintre petits travaux\n";
            } else if (sender.getLevelPainting().equals("connoisseur-painting")) {
                competence += "Peintre confirmé\n";
            } else if (sender.getLevelPainting().equals("expert-painting")) {
                competence += "Peintre expert\n";
            }
        }
        return competence;
    }

    private String getMasonry(UserBean sender) {
        String competence = "";
        if (sender.getLevelMasonry() != null) {
            if (sender.getLevelMasonry().equals("little-works-masonry")) {
                competence += "Maçon petits travaux\n";
            } else if (sender.getLevelMasonry().equals("connoisseur-masonry")) {
                competence += "Maçon confirmé\n";
            } else if (sender.getLevelMasonry().equals("expert-masonry")) {
                competence += "Maçon expert\n";
            }
        }
        return competence;
    }

    private String getDiy(UserBean sender) {
        String competence = "";
        if (sender.getLevelDiy() != null) {
            if (sender.getLevelDiy().equals("little-works-diy")) {
                competence += "Bricoleur petits travaux\n";
            } else if (sender.getLevelDiy().equals("connoisseur-diy")) {
                competence += "Bricoleur confirmé\n";
            } else if (sender.getLevelDiy().equals("expert-diy")) {
                competence += "Bricoleur expert\n";
            }
        }
        return competence;
    }

    private String getCompetences(UserBean sender) {
        String competences = "";
        competences += getGardening(sender);
        competences += getPlumbing(sender);
        competences += getCarpentry(sender);
        competences += getPainting(sender);
        competences += getMasonry(sender);
        competences += getDiy(sender);
        return competences;
    }

    @PostMapping(value = "/insert_message")
    public void insertMessageInConvention(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/userId/" + Integer.parseInt(request.getParameter("senderId")), UserBean.class);
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/userId/" + Integer.parseInt(request.getParameter("recipientId")), UserBean.class);
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
        catchLoggedUserIdPointsAndFirstName(model);
        if ((int) model.getAttribute("userId") != userId) {
            return "AccessDenied";
        }
        List<ConventionBean> allCurrentConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/conventions/" + userId, ConventionBean[].class).getBody());
        model.addAttribute("conventions", allCurrentConventionsUser);
        List<ConventionBean> allValidatedConventionUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/validated_conventions/" + userId, ConventionBean[].class).getBody());
        model.addAttribute("validatedConventions", allValidatedConventionUser);
        List<ConventionBean> allFinishedConventions = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions/" + userId, ConventionBean[].class).getBody());
        model.addAttribute("finishedConventions", allFinishedConventions);
        return "MyConventions";
    }

    @GetMapping(value = "/not_enough_points")
    public String getPageNotEnoughPoints(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            catchLoggedUserIdPointsAndFirstName(model);
        }
        return "NotEnoughPoints";
    }

    private ConventionBean buildConvention(UserBean sender, HttpServletRequest request) {
        ConventionBean conventionBean = new ConventionBean();
        conventionBean.setSenderId(sender.getId());
        conventionBean.setFirstNameSender(sender.getFirstName());
        conventionBean.setLastNameSender(sender.getLastName());
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/user/" + request.getParameter("helperEmail"), UserBean.class);
        conventionBean.setRecipientId(recipient.getId());
        conventionBean.setFirstNameRecipient(recipient.getFirstName());
        conventionBean.setLastNameRecipient(recipient.getLastName());
        conventionBean.setDateConvention(LocalDate.now().plusDays(1));
        conventionBean.setDateBeginning(LocalDate.of(Integer.parseInt(request.getParameter("year")), Integer.parseInt(request.getParameter("month")), Integer.parseInt(request.getParameter("day")) + 1));
        conventionBean.setBeginningHour(LocalTime.of(Integer.parseInt(request.getParameter("hour")) + 1, 0));
        conventionBean.setTimeIntervention(LocalTime.of(Integer.parseInt(request.getParameter("hours-intervention-time")) + 1, Integer.parseInt(request.getParameter("minutes-intervention-time"))));
        conventionBean.setPlace(request.getParameter("place"));
        conventionBean.setPhoneNumberHelped(request.getParameter("phone_number"));
        conventionBean.setMessage(request.getParameter("convention"));
        return conventionBean;
    }

    private void createConvention(UserBean sender, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (new RestTemplate().getForObject("http://localhost:9001/user/" + request.getParameter("helperEmail"), UserBean.class) == null) {
            response.sendRedirect("/email_not_found");
            return;
        }
        ConventionBean conventionBean = buildConvention(sender, request);
        ConventionsProxy.insertConvention(conventionBean);
        sender.setPoints(sender.getPoints() - ((conventionBean.getTimeIntervention().getHour() - 1) * 4) - conventionBean.getTimeIntervention().getMinute() / 15);
        new RestTemplate().put("http://localhost:9001/update_user/", sender);
        response.sendRedirect("/my_conventions/" + conventionBean.getSenderId() + "?sendConvention=true");
    }

    @PostMapping(value = "/send_convention")
    public void insertConvention(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getParameter("convention").length() > 200) {
            throw new Exception("Message trop longue");
        }
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/user/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        if (sender.getPoints() - Integer.parseInt(request.getParameter("hours-intervention-time")) * 4 - Integer.parseInt(request.getParameter("minutes-intervention-time")) / 15 < 0) {
            response.sendRedirect("/not_enough_points");
            return;
        }
        createConvention(sender, request, response);
    }

    @GetMapping(value = "/addressed_conventions/{recipientId}")
    public String getListAddressedConventions(@PathVariable int recipientId, Model model) {
        catchLoggedUserIdPointsAndFirstName(model);
        if ((int) model.getAttribute("userId") != recipientId) {
            return "AccessDenied";
        }
        List<ConventionBean> allNoValidatedConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/conventions_recipient/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("noValidatedConventions", allNoValidatedConventionsUser);
        List<ConventionBean> allValidatedConventionsUser = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/validated_conventions_recipient/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("validatedConventions", allValidatedConventionsUser);
        List<ConventionBean> allFinishedConventions = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions_recipient/" + recipientId, ConventionBean[].class).getBody());
        model.addAttribute("finishedConventions", allFinishedConventions);
        return "AddressedConventions";
    }

    public boolean verifyConvention(ConventionBean convention, String typeUser) throws Exception {
        UserBean loggedUser = new RestTemplate().getForObject("http://localhost:9001/user/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
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
            modifiedConvention.setDateConvention(modifiedConvention.getDateConvention().plusDays(1));
            modifiedConvention.setDateBeginning(modifiedConvention.getDateBeginning().plusDays(1));
            new RestTemplate().put("http://localhost:9002/update_convention", modifiedConvention);
            generateConvention(modifiedConvention.getId());
            response.sendRedirect("/addressed_conventions/" + modifiedConvention.getRecipientId() + "?acceptedConvention=true");
        } else {
            response.sendRedirect("/access_denied");
        }
    }

    @GetMapping(value = "/refuse_convention_recipient/{id}")
    public void updateIsRefusedByRecipient(@PathVariable int id, HttpServletResponse response) throws Exception {
        ConventionBean modifiedConvention = new RestTemplate().getForObject("http://localhost:9002/convention/" + id, ConventionBean.class);
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/userId/" + modifiedConvention.getSenderId(), UserBean.class);
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
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/userId/" + modifiedConvention.getRecipientId(), UserBean.class);
        recipient.setPoints(recipient.getPoints() + (modifiedConvention.getTimeIntervention().getHour() - 1) * 4 + modifiedConvention.getTimeIntervention().getMinute() / 15);
        new RestTemplate().put("http://localhost:9001/update_user/", recipient);
        if (verifyConvention(modifiedConvention, "sender")) {
            modifiedConvention.setEndedBySender(true);
            modifiedConvention.setDateConvention(modifiedConvention.getDateConvention().plusDays(1));
            modifiedConvention.setDateBeginning(modifiedConvention.getDateBeginning().plusDays(1));
            new RestTemplate().put("http://localhost:9002/update_convention", modifiedConvention);
            response.sendRedirect("/my_conventions/" + modifiedConvention.getSenderId() + "?acceptedConvention=true");
        } else {
            response.sendRedirect("/access_denied");
        }
    }

    private void generateConvention(int conventionId) throws DocumentException {
        ConventionBean convention = new RestTemplate().getForObject("http://localhost:9002/convention/" + conventionId, ConventionBean.class);
        Document document = new Document();
        String link = "src/main/resources/static/conventions/" + convention.getLastNameSender() + "-" + convention.getLastNameRecipient() + "-" + DateTimeFormatter.ofPattern("dd-MM-yyyy").format(convention.getDateBeginning()) + "-" + DateTimeFormatter.ofPattern("hh-mm").format(convention.getBeginningHour()) + "-Convention.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(link));
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

    private Paragraph writeParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        return paragraph;
    }

    private void writeHeader(Document document, ConventionBean convention) throws DocumentException {
        UserBean sender = new RestTemplate().getForObject("http://localhost:9001/userId/" + convention.getSenderId(), UserBean.class);
        UserBean recipient = new RestTemplate().getForObject("http://localhost:9001/userId/" + convention.getRecipientId(), UserBean.class);
        Paragraph title = new Paragraph("CONVENTION D'INTERVENTION");
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        addEmptyLine(document, 2);
        document.add(new Paragraph("ENTRE LES SOUSSIGNÉS :"));
        addEmptyLine(document, 1);
        document.add(new Paragraph(sender.getFirstName() + " " + sender.getLastName().toUpperCase() + " Domicilié(e) à " + sender.getTown()));
        document.add(new Paragraph("Et"));
        document.add(new Paragraph(recipient.getFirstName() + " " + recipient.getLastName().toUpperCase() + " Domicilié(e) à " + recipient.getTown()));
        addEmptyLine(document, 1);
    }

    private void writeBody(Document document, ConventionBean convention) throws DocumentException {
        document.add(writeParagraph("Il est conclu cette convention d'intervention fondée sur une confiance mutuelle, qui est la base de la mise en relation des membres du site BRICOVOISINS."));
        document.add(writeParagraph("L'aidant accomplira l'intervention décrite ci-dessous le " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(convention.getDateBeginning()) + " à partir de " + DateTimeFormatter.ofPattern("hh:mm").format(convention.getBeginningHour().minusHours(1)) + " à l'adresse qui lui a été communiquée par l'aidé."));
        document.add(writeParagraph("La signature de chacun des deux membres vaut acceptation de la description ci-dessous, qu'ils ont préalablement mise au point. À l'issue de l'intervention, il est convenu que l'aidé en validera l'achèvement sur le site BRICOVOISINS."));
        document.add(writeParagraph("Durée prévue de l'intervention : " + DateTimeFormatter.ofPattern("hh").format(convention.getTimeIntervention().minusHours(1)) + " h " + DateTimeFormatter.ofPattern("mm").format(convention.getTimeIntervention()) + " min."));
        document.add(writeParagraph("Valeur de l'intervention : " + ((convention.getTimeIntervention().getHour() - 1) * 4 + convention.getTimeIntervention().getMinute() / 15) + " points d'entraide." ));
        addEmptyLine(document, 1);
        Paragraph description = new Paragraph("DESCRIPTION DE L'INTERVENTION");
        description.setAlignment(Element.ALIGN_CENTER);
        document.add(description);
        addEmptyLine(document, 1);
        Paragraph message = new Paragraph(convention.getMessage());
        message.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(message);
        addEmptyLine(document, 1);
    }

    private void writeFooter(Document document) throws DocumentException {
        Paragraph signature = new Paragraph("Signatures");
        signature.setAlignment(Element.ALIGN_CENTER);
        document.add(signature);
        addEmptyLine(document, 1);
        Paragraph actors = new Paragraph("L'aidant,                                    L'aidé,");
        actors.setAlignment(Element.ALIGN_CENTER);
        document.add(actors);
    }

    private void addText(Document document, ConventionBean convention) throws DocumentException {
        writeHeader(document, convention);
        writeBody(document, convention);
        writeFooter(document);
    }

    private void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    @GetMapping(value = "/update_profile/{userId}")
    public String updateProfile(@PathVariable int userId, Model model) {
        catchLoggedUserIdPointsAndFirstName(model);
        if (userId == new RestTemplate().getForObject("http://localhost:9001/user/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class).getId()) {
            model.addAttribute("user", UsersProxy.getOneUser(userId));
            return "Register";
        } else {
            return "AccessDenied";
        }
    }

    public UserBean initializeUser(HttpServletResponse response, HttpServletRequest request, String method) throws Exception {
        List<UserBean> users = Arrays.asList(new RestTemplate().getForEntity("http://localhost:9001/users", UserBean[].class).getBody());
        if (isMailAlreadyInList(users, request.getParameter("email")) && method.equals("post")) {
            response.sendRedirect("/email_already");
            return null;
        } else {
            UserBean user = setAttributesUser(request);
            return user;
        }
    }

    @PostMapping(value = "/update_user")
    public void updateUser(HttpServletRequest request, HttpServletResponse response, @RequestParam("avatar") MultipartFile imageFile) throws Exception {
        if (request.getParameter("description").length() > 200) {
            throw new Exception("Description trop longue");
        }
        UserBean user = initializeUser(response, request, "put");
        UserBean existedUser = new RestTemplate().getForObject("http://localhost:9001/user/" + request.getParameter("email"), UserBean.class);
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

    private UserBean setAttributesUser(HttpServletRequest request) {
        UserBean user = new UserBean();
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
        return user;
    }

    @PostMapping(value = "/add_opinion")
    public void addOpinion(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OpinionBean newOpinion = new OpinionBean();
        UserBean author = new RestTemplate().getForObject("http://localhost:9001/user/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        newOpinion.setAuthor(author.getFirstName() + " " + author.getLastName());
        newOpinion.setOpinion(request.getParameter("opinion"));
        newOpinion.setUserId(Integer.parseInt(request.getParameter("userId")));
        newOpinion.setAuthorId(author.getId());
        for (ConventionBean convention : Arrays.asList(new RestTemplate().getForEntity("http://localhost:9002/ended_conventions_recipient/" + newOpinion.getUserId(), ConventionBean[].class).getBody())) {
            if (convention.getSenderId() == newOpinion.getAuthorId()) {
                OpinionsProxy.insertOpinion(newOpinion);
                response.sendRedirect("/profile/" + newOpinion.getUserId() + "?addOpinion=true");
                return;
            }
        }
        throw new Exception("Vous n'avez pas les droits pour ajouter cette opinion");
    }

    @GetMapping(value = "/users")
    public String getListUsers(Model model) {
        catchLoggedUserIdPointsAndFirstName(model);
        if (new RestTemplate().getForObject("http://localhost:9001/user/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class).getIsAdmin()) {
            List<UserBean> listUsers = UsersProxy.getListUsers();
            model.addAttribute("listUsers", listUsers);
            return "ListUsers";
        } else {
            return "AccessDenied";
        }
    }

    @GetMapping(value = "/delete_user/{id}")
    public void deleteUser(@PathVariable int id, HttpServletResponse response) throws IOException {
        if (new RestTemplate().getForObject("http://localhost:9001/user/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class).getIsAdmin()) {
            UsersProxy.deleteUser(id);
            response.sendRedirect("/users?deletedUser=true");
        } else {
            response.sendRedirect("/access_denied");
        }
    }

    @GetMapping(value = "/remove_opinion/{opinionId}")
    public void deleteOpinion(@PathVariable int opinionId, HttpServletResponse response) throws IOException {
        OpinionBean opinion = new RestTemplate().getForObject("http://localhost:9003/opinion/" + opinionId, OpinionBean.class);
        OpinionsProxy.deleteOpinion(opinionId);
        response.sendRedirect("/profile/" + opinion.getUserId() + "?deleteOpinion=true");
    }

    @GetMapping(value = "/chat")
    public String getChatPage(Model model) {
        catchLoggedUserIdPointsAndFirstName(model);
        UserBean user = new RestTemplate().getForObject("http://localhost:9001/user/" + SecurityContextHolder.getContext().getAuthentication().getName(), UserBean.class);
        model.addAttribute("name", user.getFirstName() + " " + user.getLastName());
        return "Chat";
    }

    @GetMapping(value = "/error")
    public String getErrorPage() {
        return "Error";
    }
}
