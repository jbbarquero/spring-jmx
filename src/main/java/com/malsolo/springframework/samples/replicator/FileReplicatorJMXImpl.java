package com.malsolo.springframework.samples.replicator;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import lombok.Data;

@Data
@ManagedResource(description = "File replicator")
public class FileReplicatorJMXImpl implements FileReplicator {

    private String srcDir;

    private String destDir;

    private FileCopier fileCopier;

    @Override
    @ManagedOperation(description = "Replicate files")
    public synchronized void replicate() throws IOException {
        // @formatter:off
        try (Stream<Path> list = Files.list(Paths.get(srcDir))) {
            list.filter(path -> path.toFile().isFile())
            .map(path -> path.getFileName().toString())
            .forEach(filename -> unchecked(srcDir, destDir, filename));
        }
        // @formatter:on
    }

    private File unchecked(String srcDir, String destDir, String filename) {
        try {
            return fileCopier.copyFile(srcDir, destDir, filename);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
