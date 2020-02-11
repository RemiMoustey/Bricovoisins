package com.bricovoisins.clientui.proxies;

import com.bricovoisins.clientui.beans.ConventionBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@FeignClient(contextId = "conventionsClient", name = "zuul-server")
@RibbonClient(name = "mconventions")
public interface MicroserviceConventionsProxy {
    @PostMapping(value = "/mconventions/insert_message")
    ResponseEntity<Void> insertUser(@RequestBody ConventionBean conventionBean) throws IOException;
}
