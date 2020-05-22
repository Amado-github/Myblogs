package com.cedar.web;


import com.cedar.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutShowController {

    @Autowired
    private LinkService linkService;
    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("links",linkService.listLink() );

        return "about";
    }

}
