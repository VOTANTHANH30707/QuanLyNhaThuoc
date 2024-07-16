/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.DoanhThu;
import Entity.HoaDon;
import Utils.JdbcHelper;
import Utils.DateHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DIEUMY
 */
public class HoaDonDAO {

    public void insert(HoaDon modelHD) {
        String sql = "INSERT INTO HoaDon (MaHoaDon, MaNhanVien, NgayBan, TongTien, KhachHangSDT, TenKH)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                modelHD.getMaHoaDon(),
                modelHD.getMaNhanVien(),
                modelHD.getNgayBan(),
                modelHD.getTongTien(),
                modelHD.getSDT(),
                modelHD.getTenKH()
        );
    }

    public void update(HoaDon modelHD) {
        String sql = "UPDATE HoaDon SET MaNhanVien = ?, NgayBan = ?, TongTien = ?, KhachHangSDT =?, TenKH = ? WHERE MaHoaDon = ?";
        JdbcHelper.executeUpdate(sql,
                modelHD.getMaNhanVien(),
                modelHD.getNgayBan(),
                modelHD.getTongTien(),
                modelHD.getSDT(),
                modelHD.getTenKH(),
                modelHD.getMaHoaDon()
        );

    }

    public void delete(String MaNV) {
        String sql = "DELETE FROM HoaDon WHERE MaHoaDon =?";
        JdbcHelper.executeUpdate(sql, MaNV);
    }

    public List<HoaDon> select() {
        String sql = "SELECT * FROM HoaDon";
        return select(sql);
    }

    public List<HoaDon> findByMaNV(String manv) {
        String sql = "SELECT MaHoaDon, MaNhanVien, NgayBan, TongTien, KhachHangSDT, TenKH FROM HoaDon WHERE MaNhanVien like ? ";
        return this.select(sql, "%" + manv + "%");
    }

    public List<HoaDon> findByMaNVDate(String manv, String dateForm, String toDay) {
        String sql = "SELECT MaHoaDon, MaNhanVien, NgayBan, TongTien, KhachHangSDT, TenKH FROM HoaDon "
                + "WHERE MaNhanVien like '" + manv + "' and NgayBan BETWEEN '" + dateForm + "' AND '" + toDay + "'";
        return this.select(sql);
    }

    public List<HoaDon> findByMaNV_Today(String manv, Date day) {
        String sql = "SELECT MaHoaDon, MaNhanVien, NgayBan, TongTien, KhachHangSDT, TenKH FROM HoaDon WHERE MaNhanVien like ? and NgayBan = ? ";
        return this.select(sql, "%" + manv + "%", day);
    }

    public HoaDon findById(String manv) {
        String sql = "SELECT * FROM HoaDon WHERE MaHoaDon=?";
        List<HoaDon> list = select(sql, manv);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<HoaDon> Select_invoice_by_date(String date, String date2) {
        String sql = "SELECT *FROM HoaDon WHERE NgayBan  BETWEEN '" + date + "' AND '" + date2 + "'";
        return select(sql);
    }

    public List<HoaDon> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM HoaDon WHERE MaHoaDon LIKE ? or KhachHangSDT LIKE ? or NgayBan LIKE ? or MaNhanVien LIKE ? or TenKH LIKE ?";
        return this.select(sql, "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%");
    }

    private List<HoaDon> select(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    HoaDon modelHD = readFromResultSet(rs);
                    list.add(modelHD);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private HoaDon readFromResultSet(ResultSet rs) throws SQLException {
        HoaDon modelHD = new HoaDon();
        modelHD.setMaHoaDon(rs.getString("MaHoaDon"));
        modelHD.setMaNhanVien(rs.getString("MaNhanVien"));
        modelHD.setNgayBan(rs.getDate("NgayBan"));
        modelHD.setTongTien(rs.getDouble("TongTien"));
        modelHD.setSDT(rs.getString("KhachHangSDT"));
        modelHD.setTenKH(rs.getString("TenKH"));
        return modelHD;
    }
}
