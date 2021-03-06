package com.cedar.web.admin;

import com.cedar.po.Blog;
import com.cedar.po.User;
import com.cedar.service.BlogService;
import com.cedar.service.LinkService;
import com.cedar.service.TagService;
import com.cedar.service.TypeService;
import com.cedar.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT="admin/blogs-input";
    private static final String LIST="admin/blogs";
    private static final String REDIRECT_LIST="redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private LinkService linkService;
    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                   Pageable pageable,
                       BlogQuery blog,
                       Model model,HttpSession session){
        User user= (User) session.getAttribute("user");
        Long userId=user.getId();
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog,userId));
        model.addAttribute("links",linkService.listLink() );

        return LIST;
    }


    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC)
                               Pageable pageable,
                         BlogQuery blog,
                       Model model,HttpSession session ){
        User user= (User) session.getAttribute("user");
        Long userId=user.getId();
        System.out.println(blog.getTitle());
        System.out.println(blog.isRecommend());
        model.addAttribute("page",blogService.listBlog(pageable,blog,userId));
        model.addAttribute("links",linkService.listLink() );
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model){

        model.addAttribute("blog",new Blog());
        model.addAttribute("links",linkService.listLink() );
        setTypeAndTag(model);
        return INPUT;
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("/blogs/{id}/input")
    public String editinput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog=blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        model.addAttribute("links",linkService.listLink() );
        return INPUT;
    }


    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){

        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if(blog.getId()==null){
            b=blogService.saveBlog(blog);
        }else{
            b=blogService.updateBlog(blog.getId(), blog);
        }
        if(b==null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }

        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功" );
        return REDIRECT_LIST;
    }

}
