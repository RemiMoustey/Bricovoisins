package com.bricovoisins.clientui.proxies;

import com.bricovoisins.clientui.beans.CommentBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@FeignClient(contextId = "commentsClient", name = "zuul-server")
@RibbonClient(name = "mcomments")
public interface MicroserviceCommentsProxy {
    @PostMapping(value = "/mcomments/add_comment")
    ResponseEntity<Void> insertComment(@RequestBody CommentBean commentBean) throws IOException;

    @GetMapping(value = "/mcomments/remove_comment/{id}")
    void deleteComment(@PathVariable int id);
}
