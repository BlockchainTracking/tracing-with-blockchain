package cn.edu.nju.software.ui;

import cn.edu.nju.software.fabricservice.bean.SampleUser;
import cn.edu.nju.software.ui.bizservice.impl.ServerCache;
import cn.edu.nju.software.ui.dao.UserEntityDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UiApplicationTests {
    @Autowired
    UserEntityDao userDao;

    @Autowired
    ServerCache serverCache;

    @Test
    public void contextLoads() {
        SampleUser sampleUser = serverCache.getUser("ttff1f");
        System.out.println(sampleUser.toString());
    }

}
