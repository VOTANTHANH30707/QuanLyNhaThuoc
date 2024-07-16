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
public class NhanVien {
    String MaNhanVien;
    String MaKhau;
    String Ho;
    String Ten;
    String NgaySinh;
    String GioiTinh;
    String Email;
    String DiaChi;
    String CCCD;
    String ChucVu;
    String Hinh;
    String TrangThai;

    public NhanVien(String MaNhanVien, String MaKhau, String Ho, String Ten, String NgaySinh, String GioiTinh, String Email, String DiaChi, String CCCD, String ChucVu, String Hinh, String TrangThai) {
        this.MaNhanVien = MaNhanVien;
        this.MaKhau = MaKhau;
        this.Ho = Ho;
        this.Ten = Ten;
        this.NgaySinh = NgaySinh;
        this.GioiTinh = GioiTinh;
        this.Email = Email;
        this.DiaChi = DiaChi;
        this.CCCD = CCCD;
        this.ChucVu = ChucVu;
        this.Hinh = Hinh;
        this.TrangThai = TrangThai;
    }

    public NhanVien() {
    }

    public String getMaNhanVien() {
        return MaNhanVien;
    }

    public String getMaKhau() {
        return MaKhau;
    }

    public String getHo() {
        return Ho;
    }

    public String getTen() {
        return Ten;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String NgaySinh) {
        this.NgaySinh = NgaySinh;
    }

  

    public String getGioiTinh() {
        return GioiTinh;
    }

    public String getEmail() {
        return Email;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public String getCCCD() {
        return CCCD;
    }

    public String getChucVu() {
        return ChucVu;
    }

    public String getHinh() {
        return Hinh;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setMaNhanVien(String MaNhanVien) {
        this.MaNhanVien = MaNhanVien;
    }

    public void setMaKhau(String MaKhau) {
        this.MaKhau = MaKhau;
    }

    public void setHo(String Ho) {
        this.Ho = Ho;
    }

    public void setTen(String Ten) {
        this.Ten = Ten;
    }

  

    public void setGioiTinh(String GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public void setChucVu(String ChucVu) {
        this.ChucVu = ChucVu;
    }

    public void setHinh(String Hinh) {
        this.Hinh = Hinh;
    }

    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }
    
    
}
