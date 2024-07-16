/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.LoaiThuoc;
import Utils.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DIEUMY
 */
public class LoaiThuocDAO {
    
    public void insert(LoaiThuoc modelLT) {
        String sql = "INSERT INTO LoaiThuoc (MaLoaiThuoc, TenLoai, MoTa) VALUES (?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                modelLT.getMaLoaiThuoc(),
                modelLT.getTenLoai(),
                modelLT.getMota()
        );
    }
    
    public void update(LoaiThuoc modelLT) {
        String sql = "UPDATE LoaiThuoc SET TenLoai = ?, MoTa = ? WHERE MaLoaiThuoc = ?";
        JdbcHelper.executeUpdate(sql,
                modelLT.getTenLoai(),
                modelLT.getMota(),
                modelLT.getMaLoaiThuoc()
        );

    }

    public void delete(String MaNV) {
        String sql = "DELETE FROM LoaiThuoc WHERE MaLoaiThuoc =?";
        JdbcHelper.executeUpdate(sql, MaNV);
    }

    public List<LoaiThuoc> select() {
        String sql = "SELECT * FROM LoaiThuoc";
        return select(sql);
    }

    public LoaiThuoc findById(String manv) {
        String sql = "SELECT * FROM LoaiThuoc WHERE MaLoaiThuoc=?";
        List<LoaiThuoc> list = select(sql, manv);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<LoaiThuoc> select(String sql, Object... args) {
        List<LoaiThuoc> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    LoaiThuoc modelLT = readFromResultSet(rs);
                    list.add(modelLT);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private LoaiThuoc readFromResultSet(ResultSet rs) throws SQLException {
        LoaiThuoc modelLT = new LoaiThuoc();
        modelLT.setMaLoaiThuoc(rs.getString("MaLoaiThuoc"));
        modelLT.setTenLoai(rs.getString("TenLoai"));
        modelLT.setMota(rs.getString("MoTa"));
        return modelLT;
    }
}
