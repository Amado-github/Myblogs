package com.cedar.web.admin;

import com.cedar.po.User;
import com.cedar.service.LinkService;
import com.cedar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private LinkService linkService;

    @GetMapping
    public String loginpage(){
        return "admin/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model){

        User user=userService.CheckUser(username, password);
        if(user != null){
            user.setPassword(null);
            session.setAttribute("user",user );
            model.addAttribute("links",linkService.listLink() );
            return "admin/index";
        }else{
            attributes.addFlashAttribute("message","用户名和密码不正确");
            return "redirect:/admin";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }


}
