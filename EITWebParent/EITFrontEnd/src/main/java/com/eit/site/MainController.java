package com.eit.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/account")
    public String viewAccountPage(){
        return "account";
    }

    @GetMapping("/cart")
    public String viewCartPage(){
        return "cart";
    }

}
