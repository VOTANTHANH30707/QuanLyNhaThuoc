/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.DonVi;
import Utils.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DIEUMY
 */
public class DonViDAO {

    public void insert(DonVi modelDV) {
        String sql = "SET IDENTITY_INSERT DonVi ON "
                + "INSERT INTO DonVi (STT, MaThuoc, DonViTinh, SoLuong)"
                + "VALUES (?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                modelDV.getSTT(),
                modelDV.getMaThuoc(),
                modelDV.getDonViTinh(),
                modelDV.getSoLuong()
        );
    }

    public void update(DonVi modelDV) {
        String sql = "UPDATE DonVi SET MaThuoc = ?, DonViTinh = ?, SoLuong =? WHERE STT = ?";
        JdbcHelper.executeUpdate(sql,
                modelDV.getMaThuoc(),
                modelDV.getDonViTinh(),
                modelDV.getSoLuong(),
                modelDV.getSTT()
        );

    }

    public void delete(String STT) {
        String sql = "DELETE FROM DonVi WHERE STT =?";
        JdbcHelper.executeUpdate(sql, STT);
    }

    public List<DonVi> select() {
        String sql = "SELECT * FROM DonVi";
        return select(sql);
    }

    public DonVi findById(String manv) {
        String sql = "SELECT * FROM DonVi WHERE MaThuoc=?";
        List<DonVi> list = select(sql, manv);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<DonVi> select(String sql, Object... args) {
        List<DonVi> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    DonVi modelDV = readFromResultSet(rs);
                    list.add(modelDV);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private DonVi readFromResultSet(ResultSet rs) throws SQLException {
        DonVi modelDV = new DonVi();
        modelDV.setSTT(rs.getString("STT"));
        modelDV.setMaThuoc(rs.getString("MaThuoc"));
        modelDV.setDonViTinh(rs.getString("DonViTinh"));
        modelDV.setSoLuong(rs.getInt("SoLuong"));
        return modelDV;
    }
}
