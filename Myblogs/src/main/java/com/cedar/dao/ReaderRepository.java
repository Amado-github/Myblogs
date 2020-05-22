package com.cedar.dao;

import com.cedar.po.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReaderRepository extends JpaRepository<Reader,Long> {

    Reader findByNameAndPassword(String name,String password);

    @Transactional
    @Modifying
    @Query("update Reader r set r.password=?3 where r.name=?1 and r.phone=?2")
    int updateReader(String name,String phone,String password);
}
