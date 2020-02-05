package com.bricovoisins.clientui.proxies;

import com.bricovoisins.clientui.beans.UserBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "usersClient", name = "zuul-server")
@RibbonClient(name = "musers")
public interface MicroserviceUsersProxy {
    @PostMapping(value = "/musers/validation")
    ResponseEntity<Void> insertUser(@RequestBody UserBean user);
}
