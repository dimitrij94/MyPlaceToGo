/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dmitrij.myplacetogo.java_classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 *
 * @author Dmitrij
 */
public class Reciver<T> {
    Gson gson;
    Class<T> entityClass;
    public Reciver(){
        entityClass = (Class<T>) this.getClass();
    }

    public T recive(InputStream in) throws IOException{
        gson = new GsonBuilder().setPrettyPrinting().create();
        StringBuilder json = new StringBuilder();
        String inputLine;
        BufferedReader reader=null;
        try{
            reader = new BufferedReader(new InputStreamReader(in));
            while  ((inputLine=reader.readLine())!=null){
            json.append(inputLine);
        }}
        finally {
            reader.close();
        }
        T arg;
        arg  = (T)gson.fromJson(json.toString(),entityClass);

        return arg;
    }
    public String reciveString(InputStream in) throws IOException {
        StringBuilder resault= new StringBuilder();
        String inputLine;
        BufferedReader reader=null;
        try{
            reader = new BufferedReader(new InputStreamReader(in));
            while  ((inputLine=reader.readLine())!=null){
                resault.append(inputLine);
            }}
        finally {
            reader.close();
        }
        return resault.toString();
    }
    public byte[]reciveImage(InputStream in){
        byte[]body=new byte[1024];
        int ofset=0;
        BufferedInputStream bin = new BufferedInputStream(in);
        try {
            while((ofset+=bin.read(body,ofset,body.length))!=-1){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
