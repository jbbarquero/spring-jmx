package com.malsolo.springframework.samples.simple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
@PropertySource("classpath:file.properties")
public class Main {

    @Value("${file.targetDir}")
    private String targetDir;

    @Bean
    public String text() {
        return new String(targetDir);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class)) {
            ConfigurableEnvironment environment = context.getEnvironment();
            environment.getPropertySources().forEach(System.out::println);
            System.out.println("********* SYSTEM ENVIRONMENT *********");
            environment.getSystemEnvironment().forEach((k, v) -> System.out.printf("%s=%s%n", k, v));
            System.out.println("********* SYSTEM PROPERTIES *********");
            environment.getSystemProperties().forEach((k, v) -> System.out.printf("%s=%s%n", k, v));
            System.out.println("********* SOME PROPERTIES *********");
            System.out.println(environment.getProperty("file.srcDir"));
            System.out.println(context.getBean("text"));
            System.out.println(environment.getProperty("JAVA_HOME"));
            System.out.println(environment.getProperty("java.version"));
        }
    }

}
