package com.metransfer.common;

import java.io.*;
import java.net.Socket;

public class NetworkInputOutput {

    protected Socket socket;

    protected BufferedInputStream bis;

    protected BufferedOutputStream bos;

    protected DataInputStream dis;

    protected DataOutputStream dos;

    public NetworkInputOutput(Socket soc) throws IOException {
        this.socket = soc;

        this.bis = new BufferedInputStream(soc.getInputStream());

        this.bos = new BufferedOutputStream(soc.getOutputStream());

        this.dis = new DataInputStream(bis);

        this.dos = new DataOutputStream(bos);
    }

    public String readString() throws IOException {
        int expectedLength = dis.readInt();
        //System.out.println(expectedLength +" String length expected");
        String s = "";
        int index;
        while(s.length() < expectedLength){
            index = bis.read();
            if(index == -1)
                throw new EOFException("Input stream end reached unexpectedly before a string could be read");

            s += (char)index;
        }
        //System.out.println("string returned" + s);
        return s;
    }
    public void sendInt(int v) throws IOException {
        dos.writeInt(v);
        dos.flush();
    }
    public void sendString(String s) throws IOException {
        byte[] data = s.getBytes();
        sendInt(data.length);
        bos.write(data);
        bos.flush();
    }
}
