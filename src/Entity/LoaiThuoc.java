/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author nguye
 */
public class LoaiThuoc {
    String MaLoaiThuoc;
    String TenLoai;
    String Mota;

    public LoaiThuoc() {
    }

    public LoaiThuoc(String MaLoaiThuoc, String TenLoai, String Mota) {
        this.MaLoaiThuoc = MaLoaiThuoc;
        this.TenLoai = TenLoai;
        this.Mota = Mota;
    }

    public String getMaLoaiThuoc() {
        return MaLoaiThuoc;
    }

    public String getTenLoai() {
        return TenLoai;
    }

    public String getMota() {
        return Mota;
    }

    public void setMaLoaiThuoc(String MaLoaiThuoc) {
        this.MaLoaiThuoc = MaLoaiThuoc;
    }

    public void setTenLoai(String TenLoai) {
        this.TenLoai = TenLoai;
    }

    public void setMota(String Mota) {
        this.Mota = Mota;
    }
    
    
}
