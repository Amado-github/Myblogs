package com.cedar.service;

import com.cedar.dao.LinkRepository;
import com.cedar.po.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public List<Link> listLink() {
        return linkRepository.findAll();
    }
}
