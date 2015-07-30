/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.dmitrij.myplacetogo.json_objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 *
 * @author Dmitrij
 */
public class Place implements Parcelable {
    @Expose
    public String placeName;
    @Expose
    public String placeDescrption;

    public PlaceMenu[]placeMenus;
    @Expose
    public long placeId;
    @Expose
    public int placeRating;
    @Expose
    public String placeAdrress;
    @Expose
    public String placeSpeciality;

    public Photo []placePhotos;
    public Place(){}

    @Override
    public int describeContents() {
        return 0;
    }

    public void setPlaceMenus(PlaceMenu[] placeMenus) {
        this.placeMenus = placeMenus;
    }

    public static final Parcelable.Creator<Place> CREATOR
            = new Parcelable.Creator<Place>() {
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }
        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    private Place(Parcel dest){
      placeName= dest.readString();
        placeDescrption=dest.readString();
        placeAdrress=dest.readString();
        placeSpeciality=dest.readString();
        placeRating=dest.readInt();
        placeId=dest.readLong();
        int lenth = dest.readInt();
        PlaceMenu[]menu=new PlaceMenu[lenth];
        for (int i = 0; i <lenth; i++) {
            menu[i]=(PlaceMenu)dest.readParcelable(PlaceMenu.class.getClassLoader());
        }
        placeMenus=menu;

}


        @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeName);
        dest.writeString(placeDescrption);
        dest.writeString(placeAdrress);
        dest.writeString(placeSpeciality);
        dest.writeInt(placeRating);
        dest.writeLong(placeId);
        dest.writeInt(placeMenus.length);
        PlaceMenu[]menu=placeMenus;
            for (int i = 0; i < menu.length; i++) {
                dest.writeParcelable(menu[i],flags);
            }
            dest.writeInt(placePhotos.length);
            for (int i = 0; i < placePhotos.length; i++) {
                dest.writeParcelable(placePhotos[i],flags);
            }
        }

    }

