package com.cedar.web.admin;


import com.cedar.po.Tag;
import com.cedar.service.LinkService;
import com.cedar.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private LinkService linkService;

    @GetMapping("/tags")
    public String list(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                        Pageable pageable, Model model){

        model.addAttribute("page",tagService.listTag(pageable));
        model.addAttribute("links",linkService.listLink() );

        return "admin/tags";
    }


    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        model.addAttribute("links",linkService.listLink() );
        return "admin/tags-input";
    }
    @PostMapping("/tags")
    public String insertTag(@Valid Tag tag, BindingResult result, RedirectAttributes redirectAttributes){
        if(tagService.getTag(tag.getName())!=null){
            result.rejectValue("name","nameError","该标签已存在");
        }
        if(result.hasErrors()){
            return "admin/tags-input";
        }
        if(tagService.saveTag(tag)==null){
            redirectAttributes.addFlashAttribute("massage", "添加失败");
        }else{
            redirectAttributes.addFlashAttribute("message", "添加成功");
        }
        return "redirect:/admin/tags";

    }


    @GetMapping("/tags/{id}/input")
    public String editTags(@PathVariable Long id, Model model){
        //根据ID获得对象
        model.addAttribute("tag",tagService.getTag(id));
        model.addAttribute("links",linkService.listLink() );
        return "admin/tags-input";
    }
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){

        //修改对象
        if(tagService.getTag(tag.getName())!=null){
            result.rejectValue("name","nameError","该标签已存在！");
        }
        if(result.hasErrors()){
            return "admin/tags-input";
        }
        if(tagService.updateTag(id,tag)==null){
            attributes.addFlashAttribute("message","修改失败" );
        }else{
            attributes.addFlashAttribute("message","修改成功" );
        }
        return "redirect:/admin/tags";
    }



    @GetMapping("tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        tagService.deleteTag(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
