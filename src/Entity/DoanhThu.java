/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author DIEUMY
 */
public class DoanhThu {
    int nam;
    int thang;
    int soLuongHD;
    double tongTien;

    public DoanhThu() {
    }

    public DoanhThu(int nam, int thang, int soLuongHD, double tongTien) {
        this.nam = nam;
        this.thang = thang;
        this.soLuongHD = soLuongHD;
        this.tongTien = tongTien;
    }

    public int getNam() {
        return nam;
    }

    public int getThang() {
        return thang;
    }

    public int getSoLuongHD() {
        return soLuongHD;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public void setSoLuongHD(int soLuongHD) {
        this.soLuongHD = soLuongHD;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
    
    
}
