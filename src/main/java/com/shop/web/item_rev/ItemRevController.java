package com.shop.web.item_rev;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemRevController {


    @GetMapping("/reviews/test")
    public String reviewTest(){
        System.out.println("review test");
        return "member/review";
    }

}
