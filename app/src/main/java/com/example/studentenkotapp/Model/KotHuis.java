package com.example.studentenkotapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class KotHuis  implements Parcelable {

    private String Adres;
    private int huisnr;
    private String gemeente;
    private int aantalKamers;
    private int aantalKamersBezet;
    private int Rating;
    private int id;


    public KotHuis(String adres, int huisnr, String gemeente, int aantalKamers, int aantalKamersBezet, int rating, int id) {
        Adres = adres;
        this.huisnr = huisnr;
        this.gemeente = gemeente;
        this.aantalKamers = aantalKamers;
        this.aantalKamersBezet = aantalKamersBezet;
        this.Rating = rating;
        this.id = id;
    }

    public KotHuis(String adres, int huisnr, String gemeente) {
        Adres = adres;
        this.huisnr = huisnr;
        this.gemeente = gemeente;
    }

    protected KotHuis(Parcel in) {
        Adres = in.readString();
        huisnr = in.readInt();
        gemeente = in.readString();
        aantalKamers = in.readInt();
        aantalKamersBezet = in.readInt();
        Rating = in.readInt();
        id = in.readInt();

    }

    public static final Creator<KotHuis> CREATOR = new Creator<KotHuis>() {
        @Override
        public KotHuis createFromParcel(Parcel in) {
            return new KotHuis(in);
        }

        @Override
        public KotHuis[] newArray(int size) {
            return new KotHuis[size];
        }
    };

    public String getAdres() {
        return Adres;
    }

    public void setAdres(String adres) {
        Adres = adres;
    }

    public int getHuisnr() {
        return huisnr;
    }

    public void setHuisnr(int huisnr) {
        this.huisnr = huisnr;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public int getAantalKamers() {
        return aantalKamers;
    }

    public void setAantalKamers(int aantalKamers) {
        this.aantalKamers = aantalKamers;
    }

    public int getAantalKamersBezet() {
        return aantalKamersBezet;
    }

    public void setAantalKamersBezet(int aantalKamersBezet) {
        this.aantalKamersBezet = aantalKamersBezet;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        this.Rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Adres);
        dest.writeInt(huisnr);
        dest.writeString(gemeente);
        dest.writeInt(aantalKamers);
        dest.writeInt(aantalKamersBezet);
        dest.writeInt(Rating);
        dest.writeInt(id);
    }
}



