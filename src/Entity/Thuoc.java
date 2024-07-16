/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.Date;

/**
 *
 * @author nguye
 */
public class Thuoc {
    String MaThuoc;
    String MaLoaiThuoc;
    String MaNhaCungCap;
    String Tenthuoc;
    Integer SoLuong;
    Double Gia;
    String Mota;
    Date hanSuDung;

    public Thuoc() {
    }

    public Thuoc(String MaThuoc, String MaLoaiThuoc, String MaNhaCungCap, String Tenthuoc, Integer SoLuong, Double Gia, String Mota, Date hanSuDung) {
        this.MaThuoc = MaThuoc;
        this.MaLoaiThuoc = MaLoaiThuoc;
        this.MaNhaCungCap = MaNhaCungCap;
        this.Tenthuoc = Tenthuoc;
        this.SoLuong = SoLuong;
        this.Gia = Gia;
        this.Mota = Mota;
        this.hanSuDung = hanSuDung;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public Double getGia() {
        return Gia;
    }

    public void setGia(Double Gia) {
        this.Gia = Gia;
    }

    

    public String getMaThuoc() {
        return MaThuoc;
    }

    public String getMaLoaiThuoc() {
        return MaLoaiThuoc;
    }

    public String getMaNhaCungCap() {
        return MaNhaCungCap;
    }

    public String getTenthuoc() {
        return Tenthuoc;
    }

    public Integer getSoLuong() {
        return SoLuong;
    }

    public String getMota() {
        return Mota;
    }

    public void setMaThuoc(String MaThuoc) {
        this.MaThuoc = MaThuoc;
    }

    public void setMaLoaiThuoc(String MaLoaiThuoc) {
        this.MaLoaiThuoc = MaLoaiThuoc;
    }

    public void setMaNhaCungCap(String MaNhaCungCap) {
        this.MaNhaCungCap = MaNhaCungCap;
    }

    public void setTenthuoc(String Tenthuoc) {
        this.Tenthuoc = Tenthuoc;
    }

    public void setSoLuong(Integer SoLuong) {
        this.SoLuong = SoLuong;
    }

    public void setMota(String Mota) {
        this.Mota = Mota;
    }
    
}
