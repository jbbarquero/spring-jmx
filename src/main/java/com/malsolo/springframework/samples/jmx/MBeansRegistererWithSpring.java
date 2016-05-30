package com.malsolo.springframework.samples.jmx;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.malsolo.springframework.samples.FileReplicatorConfig;
import com.malsolo.springframework.samples.JmxAnnotationConfig;

public class MBeansRegistererWithSpring {

    public static void main(String[] args) throws IOException {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FileReplicatorConfig.class,
                // JmxConfig.class,
                JmxAnnotationConfig.class)) {
            System.in.read();
        }
    }

}
