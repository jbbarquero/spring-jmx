package com.malsolo.springframework.samples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { FileReplicatorConfig.class })
public class FileReplicatorConfigTest {

    @Test
    public void contextLoads() {
    }

}