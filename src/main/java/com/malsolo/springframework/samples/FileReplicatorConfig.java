package com.malsolo.springframework.samples;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.malsolo.springframework.samples.replicator.FileCopier;
import com.malsolo.springframework.samples.replicator.FileCopierJMXImpl;
import com.malsolo.springframework.samples.replicator.FileReplicator;
import com.malsolo.springframework.samples.replicator.FileReplicatorJMXImpl;

@Configuration
@PropertySource("classpath:file.properties")
public class FileReplicatorConfig {

    @Value("${file.srcDir}")
    private String srcDir;

    @Value("${file.targetDir}")
    private String destDir;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public FileCopier fileCopier() {
        FileCopier fCop = new FileCopierJMXImpl();
        return fCop;
    }

    @Bean
    public FileReplicator documentReplicator() {
        FileReplicator fRep = new FileReplicatorJMXImpl();
        fRep.setSrcDir(srcDir);
        fRep.setDestDir(destDir);
        fRep.setFileCopier(fileCopier());
        return fRep;
    }

    @PostConstruct
    public void verifyDirectoriesExist() {
        File src = new File(srcDir);
        File dest = new File(destDir);
        if (!src.exists()) {
            System.err.printf("%s does not exist %n", src.getAbsolutePath());
            src.mkdirs();
        }
        if (!dest.exists()) {
            System.err.printf("%s does not exist %n", dest.getAbsolutePath());
            dest.mkdirs();
        }
    }
}
