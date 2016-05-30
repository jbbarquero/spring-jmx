package com.malsolo.springframework.samples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;

@Configuration
public class JmxAnnotationConfig {

    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        MBeanServerFactoryBean mbeanServer = new MBeanServerFactoryBean();
        mbeanServer.setLocateExistingServerIfPossible(true);
        return mbeanServer;
    }

    @Bean
    public MBeanExporter mbeanExporter() {
        AnnotationMBeanExporter mbeanExporter = new AnnotationMBeanExporter();
        return mbeanExporter;
    }

}
