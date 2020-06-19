package project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/")
public class DefaultController {

    @GetMapping
    public String index(){
        return "index";
    }

//    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET}, value = "//{path:[^\\.]*}")
//    public String redirectToIndex() {
//        return "forward:/";
//    }
}