package com.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Test
    public void testOne(){
        System.out.println(2 << 1);//运算高效首选位运算
    }

}
