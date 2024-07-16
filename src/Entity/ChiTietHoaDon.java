/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author DIEUMY
 */
public class ChiTietHoaDon {

    String STT;
    String MaHoaDon;
    String MaThuoc;
    Integer SoLuong;
    Double Gia;
    String DonVi;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String STT, String MaHoaDon, String MaThuoc, Integer SoLuong, Double Gia, String DonVi) {
        this.STT = STT;
        this.MaHoaDon = MaHoaDon;
        this.MaThuoc = MaThuoc;
        this.SoLuong = SoLuong;
        this.Gia = Gia;
        this.DonVi = DonVi;
    }

    public String getSTT() {
        return STT;
    }

    public void setSTT(String STT) {
        this.STT = STT;
    }

    public String getMaHoaDon() {
        return MaHoaDon;
    }

    public String getMaThuoc() {
        return MaThuoc;
    }

    public Integer getSoLuong() {
        return SoLuong;
    }

    public Double getGia() {
        return Gia;
    }

    public String getDonVi() {
        return DonVi;
    }

    public void setMaHoaDon(String MaHoaDon) {
        this.MaHoaDon = MaHoaDon;
    }

    public void setMaThuoc(String MaThuoc) {
        this.MaThuoc = MaThuoc;
    }

    public void setSoLuong(Integer SoLuong) {
        this.SoLuong = SoLuong;
    }

    public void setGia(Double Gia) {
        this.Gia = Gia;
    }

    public void setDonVi(String DonVi) {
        this.DonVi = DonVi;
    }

}
