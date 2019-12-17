package com.example.studentenkotapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    private int iD;
    private String voorNaam;
    private String achterNaam;
    private String eMail;
    private String passWord;
    private String telefoon;
    private String StudieRichting;
    private Integer kotID;


    public Student (String voorNaam, String achterNaam, String eMail, String passWord, String telefoon, int iD,Integer kotID) {
        this.voorNaam = voorNaam;
        this.achterNaam = achterNaam;
        this.eMail = eMail;
        this.passWord = passWord;
        this.telefoon = telefoon;
        this.iD = iD;
        this.kotID = kotID;
    }

    public Student(String voorNaam, String achterNaam, String studieRichting) {
        this.voorNaam = voorNaam;
        this.achterNaam = achterNaam;
        StudieRichting = studieRichting;
    }

    protected Student(Parcel in) {
        voorNaam = in.readString();
        achterNaam = in.readString();
        eMail = in.readString();
        passWord = in.readString();
        telefoon = in.readString();
        StudieRichting = in.readString();
        iD = in.readInt();
        if (in.readByte() == 0) {
            kotID = null;
        } else {
            kotID = in.readInt();
        }
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getVoorNaam() {
        return voorNaam;
    }

    public void setVoorNaam(String voorNaam) {
        this.voorNaam = voorNaam;
    }

    public String getAchterNaam() {
        return achterNaam;
    }

    public void setAchterNaam(String achterNaam) {
        this.achterNaam = achterNaam;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getTelefoon() {
        return telefoon;
    }

    public void setTelefoon(String telefoon) {
        this.telefoon = telefoon;
    }

    public String getStudieRichting() {
        return StudieRichting;
    }

    public void setStudieRichting(String studieRichting) {
        StudieRichting = studieRichting;
    }

    public Integer getKotID() {
        return kotID;
    }

    public void setKotID(Integer kotID) {
        this.kotID = kotID;
    }

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(voorNaam);
        dest.writeString(achterNaam);
        dest.writeString(eMail);
        dest.writeString(passWord);
        dest.writeString(telefoon);
        dest.writeString(StudieRichting);
        dest.writeInt(iD);
        if (kotID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(kotID);
        }
    }
}
