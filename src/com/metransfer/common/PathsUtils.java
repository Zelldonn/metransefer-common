package com.metransfer.common;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class PathsUtils {

    /**
     * This function return all paths which are or contain at least one file in a given path
     * If the given path is a file, then it return itself
     * @param p
     * @return
     */

    public static boolean isValidPath(java.nio.file.Path p){
        try {
            Paths.get(p.toString());
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }

    public static ArrayList<File> listFile(File file){
        ArrayList<File> fileList = new ArrayList<File>();
        if(file.isFile())
            fileList.add(file);
        else{
            try {
                DirectoryStream<java.nio.file.Path> directoryStream = Files.newDirectoryStream(file.toPath());
                for (java.nio.file.Path pa : directoryStream) {
                    if(pa.toFile().isDirectory()){
                        if(!isDirEmpty(pa)){
                            fileList.add(pa.toFile());
                        }
                    }
                    else fileList.add(pa.toFile());
                }
            } catch (IOException ex) {
            }
        }
        return fileList;
    }
    public static boolean isDirEmpty(final java.nio.file.Path directory) throws IOException {
        try(DirectoryStream<java.nio.file.Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }
    public static FileFilter withAtLeastOneFile = new FileFilter() {
        //Override accept method
        public boolean accept(File file) {
            //if the file extension is .log return true, else false
            if(file.isDirectory()){
                try {
                    if(isDirEmpty(file.toPath()))
                        return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    };
}
