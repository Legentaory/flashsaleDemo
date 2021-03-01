package com.wentaodemos.flashsale.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* This part is just for fun, nothing about flash sale */

@RestController
@RequestMapping("echo")
public class EchoController {

    @GetMapping("{text}")
    public String echo(@PathVariable("text") String text){
        return "Knock, knock.   Who is there?  " + text + "'s here!";
    }
}
