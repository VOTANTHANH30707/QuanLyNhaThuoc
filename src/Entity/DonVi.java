/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author nguye
 */
public class DonVi {

    private String STT, maThuoc, donViTinh;
    private int soLuong;

    public DonVi() {
    }

    public String getSTT() {
        return STT;
    }

    public DonVi(String STT, String maThuoc, String donViTinh, int soLuong) {
        this.STT = STT;
        this.maThuoc = maThuoc;
        this.donViTinh = donViTinh;
        this.soLuong = soLuong;
    }

    public void setSTT(String STT) {
        this.STT = STT;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
