package com.example.vschatapp.Models;

public class Kullanicilar {

    private String dogumtarihi, egitim,hakkimda,isim,resim;
    private Object state;

    public Kullanicilar() {
    }

    public Kullanicilar(String dogumtarihi, String egitim, String hakkimda, String isim, String resim, Object state) {
        this.dogumtarihi = dogumtarihi;
        this.egitim = egitim;
        this.hakkimda = hakkimda;
        this.isim = isim;
        this.resim = resim;
        this.state=state;
    }

    public String getDogumtarihi() {
        return dogumtarihi;
    }

    public void setDogumtarihi(String dogumtarihi) {
        this.dogumtarihi = dogumtarihi;
    }

    public String getEgitim() {
        return egitim;
    }

    public void setEgitim(String egitim) {
        this.egitim = egitim;
    }

    public String getHakkimda() {
        return hakkimda;
    }

    public void setHakkimda(String hakkimda) {
        this.hakkimda = hakkimda;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    @Override
    public String toString() {
        return "Kullanicilar{" +
                "dogumtarihi='" + dogumtarihi + '\'' +
                ", egitim='" + egitim + '\'' +
                ", hakkimda='" + hakkimda + '\'' +
                ", isim='" + isim + '\'' +
                ", resim='" + resim + '\'' +
                '}';
    }
}
