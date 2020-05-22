package com.cedar.service;

import com.cedar.dao.ReaderRepository;
import com.cedar.po.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReaderServiceImpl implements ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    public Reader findReader(String name, String password) {
        return readerRepository.findByNameAndPassword(name, password);
    }

    @Override
    public Reader saveReader(Reader reader) {
        return readerRepository.save(reader);
    }

    @Override
    public int updateReader(String name, String phone, String password) {
        return readerRepository.updateReader(name, phone, password);
    }
}
