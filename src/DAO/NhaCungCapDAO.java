/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.NhaCungCap;
import Utils.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DIEUMY
 */
public class NhaCungCapDAO {
    
    public void insert(NhaCungCap modelNCC) {
        String sql = "INSERT INTO NhaCungCap (MaNhaCungCap, TenNhaCungCap, LoaiNhaCungCap, MoTa)"
                + "VALUES (?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                modelNCC.getMaNhaCungCap(),
                modelNCC.getTenNhaCungCap(),
                modelNCC.getLoaiNhaCungCap(),
                modelNCC.getMoTa()
        );
    }
    
    public void update(NhaCungCap modelNCC) {
        String sql = "UPDATE NhaCungCap SET TenNhaCungCap = ?, LoaiNhaCungCap = ?, MoTa =? WHERE MaNhaCungCap = ?";
        JdbcHelper.executeUpdate(sql,
                modelNCC.getTenNhaCungCap(),
                modelNCC.getLoaiNhaCungCap(),
                modelNCC.getMoTa(),
                modelNCC.getMaNhaCungCap()
        );

    }

    public void delete(String MaNV) {
        String sql = "DELETE FROM NhaCungCap WHERE MaNhaCungCap =?";
        JdbcHelper.executeUpdate(sql, MaNV);
    }

    public List<NhaCungCap> select() {
        String sql = "SELECT * FROM NhaCungCap";
        return select(sql);
    }

    public NhaCungCap findById(String manv) {
        String sql = "SELECT * FROM NhaCungCap WHERE MaNhaCungCap = ?";
        List<NhaCungCap> list = select(sql, manv);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<NhaCungCap> select(String sql, Object... args) {
        List<NhaCungCap> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    NhaCungCap modelNCC = readFromResultSet(rs);
                    list.add(modelNCC);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private NhaCungCap readFromResultSet(ResultSet rs) throws SQLException {
        NhaCungCap modelNCC = new NhaCungCap();
        modelNCC.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
        modelNCC.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
        modelNCC.setLoaiNhaCungCap(rs.getString("LoaiNhaCungCap"));
        modelNCC.setMoTa(rs.getString("MoTa"));
        return modelNCC;
    }
}
