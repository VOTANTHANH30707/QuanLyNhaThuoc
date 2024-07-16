/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entity.DoanhThu;
import Entity.HoaDon;
import Utils.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DIEUMY
 */
public class DoanhThuDAO {

    private List<DoanhThu> select(String sql, Object... args) {
        List<DoanhThu> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    DoanhThu modelHD = readFromResultSet(rs);
                    list.add(modelHD);
                }
            } finally {
//                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private DoanhThu readFromResultSet(ResultSet rs) throws SQLException {
        DoanhThu modelHD = new DoanhThu();
        modelHD.setNam(rs.getInt("nam"));
        modelHD.setThang(rs.getInt("thang"));
        modelHD.setSoLuongHD(rs.getInt("SoLuongHD"));
        modelHD.setTongTien(rs.getDouble("TongTien"));
        return modelHD;
    }

    public List<DoanhThu> selectDataChart(int nam) {
        String sql = "exec sp_dataChart ?";
        return this.select(sql, nam);
    }

}
