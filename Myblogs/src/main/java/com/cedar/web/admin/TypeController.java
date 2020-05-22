package com.cedar.web.admin;

import com.cedar.po.Type;
import com.cedar.service.LinkService;
import com.cedar.service.TypeService;
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
public class TypeController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private LinkService linkService;

    @GetMapping("/types")
    public String list(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                        Pageable pageable, Model model){

        model.addAttribute("page",typeService.listType(pageable));
        model.addAttribute("links",linkService.listLink() );

        return "admin/types";
    }


    @GetMapping("/types/input")
    public String input(Model model){
        //增加对象第一步
        model.addAttribute("type",new Type() );
        model.addAttribute("links",linkService.listLink() );
        return "admin/types-input";
    }
    @PostMapping("/types")
    public String insertTypes(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        //增加对象第二步
        if(typeService.getType(type.getName())!=null){
            result.rejectValue("name","nameError","该分类已存在！");
        }
        //增加失败(数据库中有相同内容)
        if(result.hasErrors()){
            return "admin/types-input";
        }
        Type t=typeService.saveType(type);
        if(t==null){
            //其他原因未能成功添加对象
            attributes.addFlashAttribute("message","新增失败" );
        }else{
            attributes.addFlashAttribute("message","新增成功" );
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/types/{id}/input")
    public String editTypes(@PathVariable Long id, Model model){
        //根据ID获得对象
        model.addAttribute("type",typeService.getType(id) );
        model.addAttribute("links",linkService.listLink() );

        return "admin/types-input";
    }
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){

        //修改对象
        if(typeService.getType(type.getName())!=null){
            result.rejectValue("name","nameError","该分类已存在！");
        }
        if(result.hasErrors()){
            return "admin/types-input";
        }
        Type t=typeService.updateType(id,type);
        if(t==null){
            attributes.addFlashAttribute("message","修改失败" );

        }else{
            attributes.addFlashAttribute("message","修改成功" );
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        //删除对象
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功" );
        return "redirect:/admin/types";
    }
}
