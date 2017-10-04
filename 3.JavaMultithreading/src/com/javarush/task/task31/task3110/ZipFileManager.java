package com.javarush.task.task31.task3110;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
        try (ZipOutputStream zipFile = new ZipOutputStream(Files.newOutputStream(this.zipFile))) {
            ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
            zipFile.putNextEntry(zipEntry);

            try (InputStream inputStream = Files.newInputStream(source)) {
                int amountRead;
                while ((amountRead = inputStream.read()) > 0)
                    zipFile.write(amountRead);
            }

            zipFile.closeEntry();
        }
    }
}

