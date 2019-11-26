package com.kazanexpress.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EventPublisherConfig.class,
                      initializers = ConfigFileApplicationContextInitializer.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

    @Test
    public void contextLoads() {
        //Context loads
    }
}
