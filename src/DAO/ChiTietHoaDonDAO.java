/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.ChiTietHoaDon;
import Utils.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DIEUMY
 */
public class ChiTietHoaDonDAO {

    public void insert(ChiTietHoaDon modelCTHD) {
        String sql = "INSERT INTO ChiTietHoaDon (STT, MaHoaDon, MaThuoc, SoLuong, Gia, DonVi)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                modelCTHD.getSTT(),
                modelCTHD.getMaHoaDon(),
                modelCTHD.getMaThuoc(),
                modelCTHD.getSoLuong(),
                modelCTHD.getGia(),
                modelCTHD.getDonVi()
        );
    }

    public void update(ChiTietHoaDon modelCTHD) {
        String sql = "UPDATE ChiTietHoaDon SET MaHoaDon = ? MaThuoc = ?, SoLuong = ?, Gia =?, DonVi = ? WHERE STT = ?";
        JdbcHelper.executeUpdate(sql,
                modelCTHD.getMaHoaDon(),
                modelCTHD.getMaThuoc(),
                modelCTHD.getSoLuong(),
                modelCTHD.getGia(),
                modelCTHD.getDonVi(),
                modelCTHD.getSTT()
        );

    }

    public List<Object[]> getChiTiet(String mahd) {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "exec sp_chiTietHoaDon ?";
                rs = JdbcHelper.executeQuery(sql, mahd);
                int i = 1;
                while (rs.next()) {
                    Object[] model = {
                        i++,
                        rs.getString("MaThuoc"),
                        rs.getString("TenThuoc"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("Gia"),
                        rs.getString("DonVi"),
                        rs.getDouble("ThanhTien")
                    };
                    list.add(model);

                }
            } finally {
//                rs.getStatement().getConnection().close();

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);

        }
        return list;

    }

    public List<ChiTietHoaDon> selectByKeyword(String keyword) {
        String sql = "select * from ChiTietHoaDon where MaHoaDon LIKE ? ";
        return this.select(sql, "%" + keyword + "%");
    }

    public void delete(String MaNV) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE STT =?";
        JdbcHelper.executeUpdate(sql, MaNV);
    }

    public List<ChiTietHoaDon> select() {
        String sql = "SELECT * FROM ChiTietHoaDon";
        return select(sql);
    }

    public ChiTietHoaDon findById(String manv) {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE STT=?";
        List<ChiTietHoaDon> list = select(sql, manv);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<ChiTietHoaDon> select(String sql, Object... args) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    ChiTietHoaDon modelCTHD = readFromResultSet(rs);
                    list.add(modelCTHD);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private ChiTietHoaDon readFromResultSet(ResultSet rs) throws SQLException {
        ChiTietHoaDon modelCTHD = new ChiTietHoaDon();
        modelCTHD.setSTT(rs.getString("STT"));
        modelCTHD.setMaHoaDon(rs.getString("MaHoaDon"));
        modelCTHD.setMaThuoc(rs.getString("MaThuoc"));
        modelCTHD.setSoLuong(rs.getInt("SoLuong"));
        modelCTHD.setGia(rs.getDouble("Gia"));
        modelCTHD.setDonVi(rs.getString("DonVi"));
        return modelCTHD;
    }
}
