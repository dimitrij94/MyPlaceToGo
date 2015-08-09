/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dmitrij.myplacetogo.java_classes;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author Dmitrij
 */
public class Sender<T> {

    public void send(OutputStream out, T o, Gson gson) throws IOException{
        String json = gson.toJson(o);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        try {
            writer.write(json);
        }
        finally{
            writer.flush();
            writer.close();
        }
    }
    public void send(OutputStream out, String data) throws IOException {
        try {
            out.write(data.getBytes());
        }
        finally{
            out.flush();
            out.close();
        }
    }
    public void sendPhoto(InputStream in,OutputStream out) throws IOException {
        BufferedOutputStream bout = new BufferedOutputStream(out);
        BufferedInputStream bin = new BufferedInputStream(in);
        int offset=0;
        int readed;
        byte[]buffer=new byte[512];
        while((readed=bin.read(buffer,offset,buffer.length))!=-1){
                bout.write(buffer,offset,buffer.length);
                offset+=readed;
        }
    }
}
