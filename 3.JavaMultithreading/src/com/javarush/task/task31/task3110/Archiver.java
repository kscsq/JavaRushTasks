package com.javarush.task.task31.task3110;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class Archiver {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please enter the destination path: ");//C:\Temp\dest\out.zip
        String destinationPath = null;
        try {
            destinationPath = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ZipFileManager zipFileManager = new ZipFileManager(Paths.get(destinationPath));

        System.out.print("Please enter the source path: ");//C:\Temp\source\111.pdf
        String sourcePath = null;
        try {
            sourcePath = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            zipFileManager.createZip(Paths.get(destinationPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
