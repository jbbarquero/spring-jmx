package com.malsolo.springframework.samples.replicator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCopierJMXImpl implements FileCopier {

    @Override
    public File copyFile(String srcDir, String destDir, String filename) throws IOException {
        Path source = Paths.get(srcDir, filename);
        Path target = Paths.get(destDir, filename);
        return Files.copy(source, target).toFile();
    }

}
