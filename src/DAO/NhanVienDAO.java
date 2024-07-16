/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.NhanVien;

import Utils.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DIEUMY
 */
public class NhanVienDAO {

    public void insert(NhanVien model) {
        String sql = "INSERT INTO NhanVien (MaNhanVien, MatKhau, Ho, Ten, NgaySinh, GioiTinh, Email, DiaChi, CCCD, ChucVu, Hinh, TrangThai)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaNhanVien(),
                model.getMaKhau(),
                model.getHo(),
                model.getTen(),
                model.getNgaySinh(),
                model.getGioiTinh(),
                model.getEmail(),
                model.getDiaChi(),
                model.getCCCD(),
                model.getChucVu(),
                model.getHinh(),
                model.getTrangThai()
        );
    }

    public void update(NhanVien model) {
        String sql = "UPDATE NhanVien SET MatKhau=?, Ho=?, Ten =?, NgaySinh=?, GioiTinh = ?, Email = ?, "
                + "DiaChi = ?, CCCD = ?, ChucVu = ?, Hinh = ?, TrangThai = ? WHERE MaNhanVien = ?";
        JdbcHelper.executeUpdate(sql,
                model.getMaKhau(),
                model.getHo(),
                model.getTen(),
                model.getNgaySinh(),
                model.getGioiTinh(),
                model.getEmail(),
                model.getDiaChi(),
                model.getCCCD(),
                model.getChucVu(),
                model.getHinh(),
                model.getTrangThai(),
                model.getMaNhanVien()
        );

    }

    public void reset(String MaNV) {
        String sql = "UPDATE NhanVien SET MatKhau= '123456' WHERE MaNhanVien = ?";
        JdbcHelper.executeUpdate(sql, MaNV);
    }

    public void delete(String MaNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNhanVien =?";
        JdbcHelper.executeUpdate(sql, MaNV);
    }

    public List<NhanVien> select() {
        String sql = "SELECT * FROM NhanVien";
        return select(sql);
    }

    public NhanVien findById(String manv) {
        String sql = "SELECT * FROM NhanVien WHERE MaNhanVien=?";
        List<NhanVien> list = select(sql, manv);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<NhanVien> select(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    NhanVien model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    public List<NhanVien> select1(String... columns) {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("At least one column must be specified.");
        }

        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length - 1; i++) {
            sqlBuilder.append(columns[i]).append(", ");
        }
        sqlBuilder.append(columns[columns.length - 1]).append(" FROM NhanVien");

        String sql = sqlBuilder.toString();
        return select1(sql, columns);
    }

    private List<NhanVien> select1(String sql, String... columns) {
        List<NhanVien> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql)) {
            while (rs.next()) {
                NhanVien model = readFromResultSetSelect1(rs, columns);
                list.add(model);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private NhanVien readFromResultSetSelect1(ResultSet rs, String... columns) throws SQLException {
        NhanVien model = new NhanVien();
        for (String column : columns) {
            switch (column) {
                case "MaNhanVien":
                    model.setMaNhanVien(rs.getString("MaNhanVien"));
                    break;
                case "Ho":
                    model.setHo(rs.getString("Ho"));
                    break;
                case "Ten":
                    model.setTen(rs.getString("Ten"));
                    break;
                case "NgaySinh":
                    model.setNgaySinh(rs.getString("NgaySinh"));
                    break;
                case "GioiTinh":
                    model.setGioiTinh(rs.getString("GioiTinh"));
                    break;
                case "DiaChi":
                    model.setDiaChi(rs.getString("DiaChi"));
                    break;
                // Add more cases for additional columns if needed
                // case "ColumnName":
                //    model.setColumnName(rs.getString("ColumnName"));
                //    break;
                // ...
            }
        }
        return model;
    }

    private NhanVien readFromResultSet(ResultSet rs) throws SQLException {
        NhanVien model = new NhanVien();
        model.setMaNhanVien(rs.getString("MaNhanVien"));
        model.setMaKhau(rs.getString("MatKhau"));
        model.setHo(rs.getString("Ho"));
        model.setTen(rs.getString("Ten"));
        model.setNgaySinh(rs.getString("NgaySinh"));
        model.setGioiTinh(rs.getString("GioiTinh"));
        model.setEmail(rs.getString("Email"));
        model.setDiaChi(rs.getString("DiaChi"));
        model.setCCCD(rs.getString("CCCD"));
        model.setChucVu(rs.getString("ChucVu"));
        model.setHinh(rs.getString("Hinh"));
        model.setTrangThai(rs.getString("TrangThai"));
        return model;
    }

    public List<String> getAllMaNhanVien(String prefix) throws SQLException {
        List<String> maNhanVienList = new ArrayList<>();
        String sql = "SELECT MaNhanVien FROM NhanVien WHERE MaNhanVien LIKE ?";

        try (
                 var ps = JdbcHelper.prepareStatement(sql, prefix + "%"); ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                maNhanVienList.add(rs.getString("MaNhanVien"));
            }
        }

        return maNhanVienList;
    }

  
    
    public void resetMatKhau(NhanVien modelNV, String mk) {
        String sql = "UPDATE NhanVien SET MatKhau=? WHERE MaNhanVien = ?";
        JdbcHelper.executeUpdate(sql, mk, modelNV.getMaNhanVien());
    }
}
