package com.tyu.application;

import com.tyu.core.CoreApp;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApp.class, webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "dubbo.provider.version=1.0.0")
public class BaseTestApplication
{
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

}
