package com.cedar.web;

import com.cedar.po.Reader;
import com.cedar.service.ReaderService;
import com.cedar.util.MD5Utils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping("/goLogin")
    public String gologin(){
        return "/readerLogin";
    }
    @GetMapping("/goRegister")
    public String goRegister(){
        return "/register";
    }
    @GetMapping("/goCorrpass")
    public String goCorrpass(){
        return "/corrpass";
    }

    @PostMapping("/findReader")
    public String findReader(@RequestParam String name,
                              @RequestParam String password,
                              HttpSession session,
                             Model model){
        Reader reader = readerService.findReader(name, MD5Utils.code(password));
        if(reader!=null){
            System.out.println(reader.toString());
            reader.setPassword(null);
            session.setAttribute("reader",reader);
            return "redirect:/";
        }else{
            model.addAttribute("msg","登录失败，用户名或密码不正确！" );
            return "/readerLogin";
        }
    }

    @PostMapping("/saveReader")
    public String saveReader(@RequestParam String name,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String phone,
                             Reader reader,
                             HttpSession session,
                             Model model){
        reader.setName(name);
        reader.setPassword(MD5Utils.code(password));
        reader.setEmail(email);
        reader.setPhone(phone);

        Reader reader1 = readerService.saveReader(reader);
        if(reader1!=null){
            session.setAttribute("reader",reader );
            return "redirect:/";
        }else{
            model.addAttribute("msg", "注册失败，请联系管理员处理");
        }
        return "/register";
    }

    @PostMapping("/updateReader")
    public String updateReader(@RequestParam String name,
                               @RequestParam String phone,
                               @RequestParam String password,
                               Model model){
        int i = readerService.updateReader(name, phone, MD5Utils.code(password));
        if(i != 0){
            return "/readerLogin";
        }else{
            model.addAttribute("msg", "密码修改失败，请联系管理员处理");
        }

        return "/corrpass";
    }

}
