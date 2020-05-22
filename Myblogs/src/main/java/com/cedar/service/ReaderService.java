package com.cedar.service;

import com.cedar.po.Reader;

public interface ReaderService {


    Reader findReader(String name, String password);

    Reader saveReader(Reader reader);

    int updateReader(String name,String phone,String password);
}
