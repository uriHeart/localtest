package com.argo.collect.domain;

import com.argo.collect.domain.collector.SSgDataMapperTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class FileRead {

    static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {

                System.out.println(fileEntry.getPath());
                try (Stream<String> stream = Files.lines(Paths.get(fileEntry.getPath()))) {
                    stream.forEach(System.out::println);
                } catch (IOException e) {
                }
                System.out.println(fileEntry.getName());
                SSgDataMapperTest.class.getMethods();
            }
        }
    }



    public static void main(String[] args){
        final File folder = new File("C:/project/io.blocktracer/reciept/src");
        listFilesForFolder(folder);

    }

}
