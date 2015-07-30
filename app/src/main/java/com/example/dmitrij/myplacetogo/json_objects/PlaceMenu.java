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
public class PlaceMenu implements Parcelable{
    public String menuName;
    public String menuDescription;
    public float price;
    public Place place;
    public PlaceMenu(){}

    public PlaceMenu(Parcel dest){
        menuName=dest.readString();
        menuDescription=dest.readString();
        price=dest.readFloat();
        place=dest.readParcelable(Place.class.getClassLoader());
    }

    private static final Parcelable.Creator<PlaceMenu>CREATOR=new Parcelable.Creator<PlaceMenu>(){
        @Override
        public PlaceMenu createFromParcel(Parcel source) {
            return new PlaceMenu(source);
        }

        @Override
        public PlaceMenu[] newArray(int size) {
            return new PlaceMenu[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(menuName);
        dest.writeString(menuDescription);
        dest.writeFloat(price);
        dest.writeParcelable(place,flags);
    }
}
