package com.cedar.service;

import com.cedar.NotFoundException;
import com.cedar.dao.BlogRepository;
import com.cedar.po.Blog;
import com.cedar.po.Type;
import com.cedar.util.MarkdownUtils;
import com.cedar.util.MyBeanUtils;
import com.cedar.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    @Transactional
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog=blogRepository.getOne(id);
        if(blog==null){
            throw new NotFoundException("博客不存在");
        }
        Blog b=new Blog();
        BeanUtils.copyProperties(blog, b);
        String content=b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return b;
    }

    //后台
    @Override
    @Transactional
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog,Long userId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId()!=null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                if(userId!=null){
                    predicates.add(cb.equal(root.<Long>get("user").get("id"), userId));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    //前端
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findByPublished(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join=root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {

        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public List<Blog> listBlogTop(Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable= PageRequest.of(0,size , sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years=blogRepository.findGropYear();
        Map<String,List<Blog>> map=new LinkedHashMap<>();
        for(String year : years){
            map.put(year,blogRepository.findByYear(year) );
        }
        return map;
    }

    @Override
    public Long CountBlog() {
        return blogRepository.count();
    }

    @Override
    @Transactional
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setCreateTime(blog.getCreateTime());
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Override
    @Transactional
    public Blog updateBlog(Long id, Blog blog) {
        Blog b=blogRepository.getOne(id);
        if(b==null){
            throw new NotFoundException("博客不存在");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        return blogRepository.save(b);
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
