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
    @PostMapping(value = "/mconventions/send_convention")
    ResponseEntity<Void> insertConvention(@RequestBody ConventionBean conventionBean) throws IOException;
}
