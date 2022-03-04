package com.tyu.servicetest;


import com.tyu.application.BaseTestApplication;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class ServiceTest extends BaseTestApplication {

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        String sql = "replace into stock (id,name, sale, count,version) values (1,'xr', 0, 50,0)";
        jdbcTemplate.execute(sql);
    }


    @Test
    @Transactional
    @Rollback(value = false)
    public void test1() {
        A();
        System.out.println(1/0);

    }

    private void A() {

//        B();
    }



    private void B() {
        String sql1 = "UPDATE tyu.stock SET name = 'sql2' WHERE id = 1";
        jdbcTemplate.execute(sql1);
    }
}
