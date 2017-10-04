package com.javarush.task.task31.task3110;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Archiver {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter the source path: ");
        String sourcePath = null;
        try {
            sourcePath = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path sPath = Paths.get(sourcePath);
        ZipFileManager zipFileManager = new ZipFileManager(sPath);

        System.out.print("Please enter the destination path: ");
        String destinationPath = null;
        try {
            destinationPath = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path dPath = Paths.get(destinationPath);
        try {
            zipFileManager.createZip(dPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
