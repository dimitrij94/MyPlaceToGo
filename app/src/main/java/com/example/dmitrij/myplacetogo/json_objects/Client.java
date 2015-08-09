/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dmitrij.myplacetogo.json_objects;


import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author Dmitrij
 */

public class Client  implements Parcelable{


    private long id;
    public String clientEmail;
    public String clientName;
    public String clientPass;
    public Place[]clientPlaces;
    public String clientAddress;
    public String clientSurname;
    public Photo[] clientPhotos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client email(String email){
        clientEmail=email;
        return this;
    }
    public Client name(String name){
        clientName=name;
        return this;
    }
    public Client pass(String pass){
        clientPass = pass;
        return this;
    }
    
    public Client address(String address){
        clientAddress = address;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private static final Parcelable.Creator<Client>CREATOR=new Parcelable.Creator<Client>(){

        @Override
        public Client createFromParcel(Parcel source) {
            return new Client(source);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    public Client(){}

    public Client(Parcel in){
       clientEmail=in.readString();
        clientName=in.readString();
        clientPass=in.readString();
        clientAddress=in.readString();
        clientSurname=in.readString();
        for (int i = 0; i < in.readInt(); i++) {
            clientPhotos[i]=in.readParcelable(Photo.class.getClassLoader());
        }
        for (int i = 0; i < in.readInt(); i++) {
            clientPlaces[i]=in.readParcelable(Place.class.getClassLoader());
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientEmail);
        dest.writeString(clientName);
        dest.writeString(clientPass);
        dest.writeString(clientAddress);
        dest.writeString(clientSurname);
        dest.writeInt(clientPhotos.length);
        for(Photo photo:clientPhotos){
            dest.writeParcelable(photo, flags);
        }
        dest.writeInt(clientPlaces.length);
        for(Place p:clientPlaces){
            dest.writeParcelable(p,flags);
        }
    }
}