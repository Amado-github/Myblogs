package com.cedar.web;

import com.cedar.po.Tag;
import com.cedar.service.BlogService;
import com.cedar.service.LinkService;
import com.cedar.service.TagService;
import com.cedar.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private LinkService linkService;
    @Autowired
    private TagService TagService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/tag/{id}")
    public String Tags(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model,@PathVariable Long id){
        List<Tag> Tags=TagService.listTagTop(100000);
        if(id==-1){
            id=Tags.get(0).getId();
        }
        model.addAttribute("tags",Tags );
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id );
        model.addAttribute("links",linkService.listLink() );

        return "tags";
    }
}
