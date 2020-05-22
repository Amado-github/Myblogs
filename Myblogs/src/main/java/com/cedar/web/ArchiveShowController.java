package com.cedar.web;


import com.cedar.service.BlogService;
import com.cedar.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private LinkService linkService;

    @GetMapping("/archives")
    public String archives(Model model){
        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCount",blogService.CountBlog() );
        model.addAttribute("links",linkService.listLink() );

        return "archives";
    }

}
