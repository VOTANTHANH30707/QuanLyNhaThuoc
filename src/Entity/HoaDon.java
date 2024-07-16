/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.Date;

/**
 *
 * @author DIEUMY
 */
public class HoaDon {
    String MaHoaDon;
    String MaNhanVien;
    Date NgayBan;
    double TongTien;
    String SDT;
    String TenKH;

    public HoaDon() {
    }

    public HoaDon(String MaHoaDon, String MaNhanVien, Date NgayBan, double TongTien, String SDT, String TenKH) {
        this.MaHoaDon = MaHoaDon;
        this.MaNhanVien = MaNhanVien;
        this.NgayBan = NgayBan;
        this.TongTien = TongTien;
        this.SDT = SDT;
        this.TenKH = TenKH;
    }

    public String getMaHoaDon() {
        return MaHoaDon;
    }

    public String getMaNhanVien() {
        return MaNhanVien;
    }

    public Date getNgayBan() {
        return NgayBan;
    }

    public double getTongTien() {
        return TongTien;
    }

    public String getSDT() {
        return SDT;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setMaHoaDon(String MaHoaDon) {
        this.MaHoaDon = MaHoaDon;
    }

    public void setMaNhanVien(String MaNhanVien) {
        this.MaNhanVien = MaNhanVien;
    }

    public void setNgayBan(Date NgayBan) {
        this.NgayBan = NgayBan;
    }

    public void setTongTien(double TongTien) {
        this.TongTien = TongTien;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public void setTenKH(String TenKH) {
        this.TenKH = TenKH;
    }
    
}
