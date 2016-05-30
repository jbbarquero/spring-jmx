package com.malsolo.springframework.samples;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;

import com.malsolo.springframework.samples.replicator.FileReplicator;

//@Configuration
public class JmxConfig {

    @Autowired
    private FileReplicator fileReplicator;

    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        MBeanServerFactoryBean mbeanServer = new MBeanServerFactoryBean();
        mbeanServer.setLocateExistingServerIfPossible(true);
        return mbeanServer;
    }

    @Bean
    public MBeanExporter mbeanExporter() {
        MBeanExporter mbeanExporter = new MBeanExporter();
        mbeanExporter.setBeans(beansToExport());
        mbeanExporter.setServer(mbeanServer().getObject());
        return mbeanExporter;
    }

    private Map<String, Object> beansToExport() {
        Map<String, Object> beansToExport = new HashMap<>();
        beansToExport.put("bean:name=springDocumentReplicator", fileReplicator);
        return beansToExport;
    }
}
