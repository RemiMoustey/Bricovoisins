package com.bricovoisins.clientui.proxies;

import com.bricovoisins.clientui.beans.UserBean;
import org.apache.catalina.User;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@FeignClient(contextId = "usersClient", name = "zuul-server")
@RibbonClient(name = "musers")
public interface MicroserviceUsersProxy {
    @PostMapping(value = "/musers/validation")
    ResponseEntity<Void> insertUser(@RequestBody UserBean user, @RequestParam("avatar") MultipartFile imageFile) throws IOException;

    @PostMapping(value = "/musers/results")
    List<UserBean> getSearchedUsers(@RequestParam String search);
}
