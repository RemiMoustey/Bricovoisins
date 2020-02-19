package com.bricovoisins.clientui.proxies;

import com.bricovoisins.clientui.beans.OpinionBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@FeignClient(contextId = "opinionsClient", name = "zuul-server")
@RibbonClient(name = "mopinions")
public interface MicroserviceOpinionsProxy {
    @PostMapping(value = "/mopinions/add_opinion")
    ResponseEntity<Void> insertOpinion(@RequestBody OpinionBean opinionBean) throws IOException;

    @GetMapping(value = "/mopinions/remove_opinion/{id}")
    void deleteOpinion(@PathVariable int id);
}
