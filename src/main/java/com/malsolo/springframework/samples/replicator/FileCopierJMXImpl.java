package com.malsolo.springframework.samples.replicator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "bean:name=fileCopier,type=FileCopierJMXImpl", description = "File Copier")
public class FileCopierJMXImpl implements FileCopier {

    @Override
    @ManagedOperationParameter(name = "filename", description = "File to copy")
    public File copyFile(String srcDir, String destDir, String filename) throws IOException {
        Path source = Paths.get(srcDir, filename);
        Path target = Paths.get(destDir, filename);
        return Files.copy(source, target).toFile();
    }

}
