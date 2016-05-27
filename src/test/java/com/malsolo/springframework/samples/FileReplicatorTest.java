package com.malsolo.springframework.samples;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.malsolo.springframework.samples.replicator.FileReplicator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { FileReplicatorConfig.class })
public class FileReplicatorTest {

    @Value("${file.targetDir}")
    private String destDir;

    @Autowired
    FileReplicator fileReplicator;

    @Before
    public void deleteTargetDirectory() throws IOException {
        Path directory = Paths.get(destDir);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

        });

    }

    @Test
    public void testReplicate() throws IOException {
        fileReplicator.replicate();
    }

}
