package com.malsolo.springframework.samples.replicator;

import java.io.File;
import java.io.IOException;

public interface FileCopier {

    public File copyFile(String srcDir, String destDir, String filename) throws IOException;
}
