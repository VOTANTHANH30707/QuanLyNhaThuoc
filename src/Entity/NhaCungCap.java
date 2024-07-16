/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author DIEUMY
 */
public class NhaCungCap {
    String MaNhaCungCap;
    String TenNhaCungCap;
    String LoaiNhaCungCap;
    String MoTa;

    public NhaCungCap() {
    }

    public NhaCungCap(String MaNhaCungCap, String TenNhaCungCap, String LoaiNhaCungCap, String MoTa) {
        this.MaNhaCungCap = MaNhaCungCap;
        this.TenNhaCungCap = TenNhaCungCap;
        this.LoaiNhaCungCap = LoaiNhaCungCap;
        this.MoTa = MoTa;
    }

    public String getMaNhaCungCap() {
        return MaNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return TenNhaCungCap;
    }

    public String getLoaiNhaCungCap() {
        return LoaiNhaCungCap;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMaNhaCungCap(String MaNhaCungCap) {
        this.MaNhaCungCap = MaNhaCungCap;
    }

    public void setTenNhaCungCap(String TenNhaCungCap) {
        this.TenNhaCungCap = TenNhaCungCap;
    }

    public void setLoaiNhaCungCap(String LoaiNhaCungCap) {
        this.LoaiNhaCungCap = LoaiNhaCungCap;
    }

    public void setMoTa(String MoTa) {
        this.MoTa = MoTa;
    }
    
}
