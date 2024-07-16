/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.Thuoc;
import Utils.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DIEUMY
 */
public class ThuocDAO {

    public void insert(Thuoc modelT) {
        String sql = "INSERT INTO Thuoc (MaThuoc, MaLoaiThuoc, MaNhaCungCap, TenThuoc, SoLuong, Gia, MoTa, HanSuDung)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                modelT.getMaThuoc(),
                modelT.getMaLoaiThuoc(),
                modelT.getMaNhaCungCap(),
                modelT.getTenthuoc(),
                modelT.getSoLuong(),
                modelT.getGia(),
                modelT.getMota(),
                modelT.getHanSuDung()
        );
    }

    public void update(Thuoc modelT) {
        String sql = "UPDATE Thuoc SET MaLoaiThuoc = ?, MaNhaCungCap = ?, TenThuoc = ?, SoLuong = ?,"
                + " Gia = ?, MoTa = ?, HanSuDung = ? WHERE MaThuoc = ?";
        JdbcHelper.executeUpdate(sql,
                modelT.getMaLoaiThuoc(),
                modelT.getMaNhaCungCap(),
                modelT.getTenthuoc(),
                modelT.getSoLuong(),
                modelT.getGia(),
                modelT.getMota(),
                modelT.getHanSuDung(),
                modelT.getMaThuoc()
        );

    }

    public void delete(String MaNV) {
        String sql = "DELETE FROM DonVi WHERE MaThuoc = ?";
        String sql1 = "DELETE FROM Thuoc WHERE MaThuoc = ?";
        String sql2 = "DELETE FROM ChiTietHoaDon WHERE MaThuoc = ?";
        JdbcHelper.executeUpdate(sql, MaNV);
        JdbcHelper.executeUpdate(sql2, MaNV);
        JdbcHelper.executeUpdate(sql1, MaNV);
    }

    public List<Thuoc> select() {
        String sql = "SELECT * FROM Thuoc";
        return select(sql);
    }

    public List<Thuoc> findThuoc(String key) {
        String sql = "Select MaThuoc, MaLoaiThuoc, MaNhaCungCap, TenThuoc, SoLuong, Gia, MoTa, HanSuDung from Thuoc"
                + " where MaThuoc like ? or TenThuoc like ?";
        return this.select(sql, "%" + key + "%", "%" + key + "%");
    }

    public List<Thuoc> select1(String... columns) {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("At least one column must be specified.");
        }
        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.length - 1; i++) {
            sqlBuilder.append(columns[i]).append(", ");
        }
        sqlBuilder.append(columns[columns.length - 1]).append(" FROM Thuoc");

        String sql = sqlBuilder.toString();
        return select1(sql, columns);
    }

    private List<Thuoc> select1(String sql, String... columns) {
        List<Thuoc> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql)) {
            while (rs.next()) {
                Thuoc modelT = readFromResultSetSelect1(rs, columns);
                list.add(modelT);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private Thuoc readFromResultSetSelect1(ResultSet rs, String... columns) throws SQLException {
        Thuoc modelT = new Thuoc();
        for (String column : columns) {
            switch (column) {
                case "MaThuoc":
                    modelT.setMaThuoc(rs.getString("MaThuoc"));
                    break;
                case "MaLoaiThuoc":
                    modelT.setMaLoaiThuoc(rs.getString("MaLoaiThuoc"));
                    break;
                case "MaNhaCungCap":
                    modelT.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
                    break;
                case "TenThuoc":
                    modelT.setTenthuoc(rs.getString("TenThuoc"));
                    break;
                case "SoLuong":
                    modelT.setSoLuong(rs.getInt("SoLuong"));
                    break;
                case "Gia":
                    modelT.setGia(rs.getDouble("Gia"));
                    break;
                case "Mota":
                    modelT.setMota(rs.getString("Mota"));
                    break;
                case "HanSuDung":
                    modelT.setHanSuDung(rs.getDate("HanSuDung"));
                    break;
                // Add more cases for additional columns if needed
                // case "ColumnName":
                //    modelT.setColumnName(rs.getString("ColumnName"));
                //    break;
                // ...
            }
        }
        return modelT;
    }

    public Thuoc findById(String manv) {
        String sql = "SELECT * FROM Thuoc WHERE MaThuoc=?";
        List<Thuoc> list = select(sql, manv);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<Thuoc> select(String sql, Object... args) {
        List<Thuoc> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    Thuoc modelT = readFromResultSet(rs);
                    list.add(modelT);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    public List<Thuoc> selectThuocHetHan() {
        String sql = "SELECT * FROM Thuoc WHERE DATEDIFF(DAY, HanSuDung, GETDATE()) > 0;";
        return select(sql);
    }

    private Thuoc readFromResultSet(ResultSet rs) throws SQLException {
        Thuoc modelT = new Thuoc();
        modelT.setMaThuoc(rs.getString("MaThuoc"));
        modelT.setMaLoaiThuoc(rs.getString("MaLoaiThuoc"));
        modelT.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
        modelT.setTenthuoc(rs.getString("TenThuoc"));
        modelT.setSoLuong(rs.getInt("SoLuong"));
        modelT.setGia(rs.getDouble("Gia"));
        modelT.setMota(rs.getString("MoTa"));
        modelT.setHanSuDung(rs.getDate("HanSuDung"));
        return modelT;
    }

}
