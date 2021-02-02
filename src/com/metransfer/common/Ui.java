package com.metransfer.common;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class Ui {
    /**
     * Converts byte to string with appropriate unit  * @param byte
     * @return human readable string
     */
    static public String byte2Readable(long _byte){
        String s = "";
        float mebibyte = 1_048_576F;

        if(_byte < mebibyte / 1024F)
            s = (int)_byte + " B";
        else if(_byte < mebibyte)
            s = String.format("%.2f",_byte/1024F) + " kB";
        else if(_byte < mebibyte * 1024F){
            s = String.format("%.2f",_byte/mebibyte) + " MB";
        }else if(_byte < mebibyte * mebibyte){
            s = String.format("%.2f",_byte/(mebibyte * 1024F)) + " GB";
        }
        return s;
    }

    public static long round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }

    static public long calculateThroughput(long transferredBytes, long oldTransferredBytes, long oldMillis){
        long throughput = 0L;
        long currentMillis = System.currentTimeMillis();
        long dB = transferredBytes - oldTransferredBytes;
        long dt = currentMillis - oldMillis;
        float dS = (float)dt/1_000F;
        if(dB != 0 && dS != 0)
            throughput = (long)(dB/dS);

        return throughput;
    }

    static public long calculateExpectedBytes(File[] fileList) {
        long l = 0L;
        for (File file : fileList) {
            if(!file.isDirectory())
                l += file.length();
            else{
                File[] filesInDir = file.listFiles();
                if(filesInDir == null)
                    return 0;
                l += calculateExpectedBytes(filesInDir);
            }
        }
        return l;
    }

    static public int calculateExpectedFiles(File[] fileList) {
        int size = 0;
        for (File file : fileList) {
            if(!file.isDirectory())
                size++;
            else{
                File[] filesInDir = file.listFiles();
                if(filesInDir == null)
                    return 0;
                size += calculateExpectedFiles(filesInDir);
            }
        }
        return size;
    }
    static public String displayFileInfo(File[] fileList){
        int numberOfFile = calculateExpectedFiles(fileList);
        long size = calculateExpectedBytes(fileList);
        return numberOfFile + " file(s) selected : "+ byte2Readable(size);
    }
    static public ArrayList<Path> pathListFromFileList(File[] fileList){
        ArrayList<Path> pathList = new ArrayList<>();
        for (File file : fileList) {
            pathList.add(file.toPath());
        }
        return pathList;
    }
}
