/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package qlthuoc;

import DAO.DonViDAO;
import DAO.ThuocDAO;
import Entity.DonVi;
import Entity.Thuoc;
import Utils.DialogHelper;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nguye
 */
public class BanThuoc extends javax.swing.JFrame {

    private DefaultTableModel model = new DefaultTableModel();
    private Locale vnLocale = new Locale("vi", "VN");
    private NumberFormat vnCurrencyFormat = NumberFormat.getCurrencyInstance(vnLocale);

    /**
     * Creates new form BanThuoc
     */
    public BanThuoc() {
        initComponents();
        init();
    }

    public void init() {
        setTitle("Quản lý nhà thuốc Long Phụng Sum Vầy");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        loadThuoc();
        Image us = Toolkit.getDefaultToolkit().createImage("src\\Icons\\logocuabeHung.png");
        this.setIconImage(us);
    }

    ThuocDAO daoThuoc = new ThuocDAO();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate currentDate = LocalDate.now();

    public void loadThuoc() {
        model = (DefaultTableModel) tblThuoc.getModel();
        model.setRowCount(0);
        tblThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String timKiem = txtTimkiem.getText();
        List<Thuoc> list = daoThuoc.findThuoc(timKiem);
        for (int i = 0; i < list.size(); i++) {
            LocalDate ngayCanKiemTra = LocalDate.parse(String.valueOf(list.get(i).getHanSuDung()));
            if (!(list.get(i).getSoLuong().equals(0) || ngayCanKiemTra.isBefore(currentDate))) {
                model.addRow(new Object[]{i + 1,
                    list.get(i).getMaThuoc(), list.get(i).getTenthuoc(), list.get(i).getSoLuong(),
                    vnCurrencyFormat.format(list.get(i).getGia()), list.get(i).getHanSuDung()});
            }
        }
    }

    DonViDAO daoDonVi = new DonViDAO();

    public void loadDonVi() {
        int index = tblThuoc.getSelectedRow();
        String maThuoc = (String) tblThuoc.getValueAt(index, 1);
        String tenThuoc = (String) tblThuoc.getValueAt(index, 2);
        Thuoc t = daoThuoc.findById(maThuoc);
        Double donGia = t.getGia();
        Date hanSuDung = (Date) tblThuoc.getValueAt(index, 5);
        List<DonVi> listDV = daoDonVi.select();
        cboDonVi.removeAllItems();
        for (DonVi dv : listDV) {
            if (dv.getMaThuoc().equals(maThuoc)) {
                cboDonVi.addItem(dv.getDonViTinh());
            }
        }
        txtMathuoc.setText(maThuoc);
        txtTenThuoc.setText(tenThuoc);
        double gia = 0;
        for (DonVi dv : listDV) {
            if (dv.getDonViTinh().equals(cboDonVi.getSelectedItem())) {
                gia = dv.getSoLuong() * donGia;
            }
        }
        String formattedVnCurrency = vnCurrencyFormat.format(gia);
        txtDonGia.setText(String.valueOf(formattedVnCurrency));
        txtHanSuDung.setText(String.valueOf(hanSuDung));
    }

    private Double tongSoluong;

    public void setTien() {
        int index = tblThuoc.getSelectedRow();
        String maThuoc = (String) tblThuoc.getValueAt(index, 1);
        String donVi = (String) cboDonVi.getSelectedItem();
        List<DonVi> listDV = daoDonVi.select();
        Thuoc t = daoThuoc.findById(maThuoc);
        txtThanhTien.setText("");
        for (DonVi dv : listDV) {
            if (dv.getDonViTinh().equals(donVi)) {
                Double tong1giatri = (Double) (dv.getSoLuong() * t.getGia());
                int soLuong = 0;
                try {
                    soLuong = Integer.parseInt(txtSoLuong.getText());
                } catch (NumberFormatException e) {
                    DialogHelper.alert(this, "Vui lòng nhập số!");
                    txtSoLuong.setText("");
                    return;
                }
                tongSoluong = tong1giatri * soLuong;
                String formattedVnCurrency = vnCurrencyFormat.format(tongSoluong);
                txtThanhTien.setText(formattedVnCurrency);
            }
        }
    }

    public boolean checkNull() {
        if (txtSoLuong.getText().equals("")) {
            DialogHelper.alert(this, "Vui lòng nhập số lượng!");
            return true;
        }
        return false;
    }

    List<Entity.BanThuoc> listHD = new ArrayList<>();
    int indexDonGia = 0;

    public void themVaoHoaDon() {
        if (checkNull()) {
            return;
        }
        String maThuoc = txtMathuoc.getText();
        String tenThuoc = txtTenThuoc.getText();
        String donVi = String.valueOf(cboDonVi.getSelectedItem());
        int soLuong = Integer.parseInt(txtSoLuong.getText());
        String hanSuDung = txtHanSuDung.getText();
        int index = tblThuoc.getSelectedRow();
        Thuoc t = daoThuoc.findById((String) tblThuoc.getValueAt(index, 1));
        Double donGia = t.getGia();
        double gia = 0;
        List<DonVi> listDV = daoDonVi.select();
        for (DonVi dv : listDV) {
            if (dv.getDonViTinh().equals(cboDonVi.getSelectedItem())) {
                gia = dv.getSoLuong() * donGia;
            }
        }
        Entity.BanThuoc bt = new Entity.BanThuoc(maThuoc, tenThuoc,
                donVi, soLuong, tongSoluong, gia, hanSuDung);
        listHD.add(bt);
        DialogHelper.alert(this, "Thêm sản phẩm " + listHD.get(listHD.size() - 1).getTenThuoc()
                + " vào hóa đơn thành công!");
        double tongtien = 0;
        for (Entity.BanThuoc banThuoc : listHD) {
            tongtien += banThuoc.getThanhTien();
        }
        String formattedVnCurrency = vnCurrencyFormat.format(tongtien);
        txtTongThanhTien.setText(formattedVnCurrency);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtTimkiem = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThuoc = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMathuoc = new javax.swing.JTextField();
        txtTenThuoc = new javax.swing.JTextField();
        txtSoLuong = new javax.swing.JTextField();
        txtThanhTien = new javax.swing.JTextField();
        txtHanSuDung = new javax.swing.JTextField();
        cboDonVi = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTongThanhTien = new javax.swing.JTextField();
        btnHoaDon = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(14, 144, 238))); // NOI18N

        txtTimkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimkiemActionPerformed(evt);
            }
        });
        txtTimkiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimkiemKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimkiemKeyReleased(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Search.png"))); // NOI18N
        jButton1.setText("Tìm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtTimkiem, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách thuốc", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(14, 144, 238))); // NOI18N

        tblThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Hạn sử dụng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThuoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThuocMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblThuoc);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(14, 144, 238));
        jLabel1.setText("Bán thuốc");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bán thuốc", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(14, 144, 238))); // NOI18N

        jLabel2.setText("Mã thuốc:");

        jLabel3.setText("Tên thuốc");

        jLabel4.setText("Đơn vị:");

        jLabel5.setText("Số lượng:");

        jLabel6.setText("Thành tiền");

        jLabel7.setText("Hạn sử dụng");

        txtMathuoc.setEditable(false);

        txtTenThuoc.setEditable(false);

        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });
        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoLuongKeyReleased(evt);
            }
        });

        txtThanhTien.setEditable(false);

        txtHanSuDung.setEditable(false);

        cboDonVi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDonViActionPerformed(evt);
            }
        });

        jLabel8.setText("Đơn giá:");

        txtDonGia.setEditable(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtThanhTien)
                    .addComponent(txtSoLuong)
                    .addComponent(cboDonVi, 0, 204, Short.MAX_VALUE)
                    .addComponent(txtTenThuoc)
                    .addComponent(txtMathuoc, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtHanSuDung, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtDonGia))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMathuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cboDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtHanSuDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel10.setText("Tổng thành tiền");

        txtTongThanhTien.setEditable(false);

        btnHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Add to basket.png"))); // NOI18N
        btnHoaDon.setText("Hóa đơn");
        btnHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoaDonActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Create.png"))); // NOI18N
        jButton5.setText("Thêm");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Delete.png"))); // NOI18N
        jButton6.setText("Hủy");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(337, 337, 337)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHoaDon)
                .addGap(121, 121, 121)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(txtTongThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTongThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(btnHoaDon)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongActionPerformed

    }//GEN-LAST:event_txtSoLuongActionPerformed

    private void tblThuocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThuocMouseClicked
        if (evt.getClickCount() == 2) {
            loadDonVi();
            indexDonGia = tblThuoc.getSelectedRow();
        }
    }//GEN-LAST:event_tblThuocMouseClicked

    private void cboDonViActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDonViActionPerformed
        txtThanhTien.setText("");
        txtSoLuong.setText("");
        int index = tblThuoc.getSelectedRow();
        Thuoc t = daoThuoc.findById((String) tblThuoc.getValueAt(index, 1));
        Double donGia = t.getGia();
        List<DonVi> listDV = daoDonVi.select();
        double gia = 0;
        for (DonVi dv : listDV) {
            if (dv.getDonViTinh().equals(cboDonVi.getSelectedItem())) {
                gia = dv.getSoLuong() * donGia;
            }
        }
        String formattedVnCurrency = vnCurrencyFormat.format(gia);
        txtDonGia.setText(String.valueOf(formattedVnCurrency));
    }//GEN-LAST:event_cboDonViActionPerformed

    private void txtSoLuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongKeyReleased
        if (!txtSoLuong.getText().equals("")) {
            setTien();
        } else {
            txtThanhTien.setText("");
        }
    }//GEN-LAST:event_txtSoLuongKeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        themVaoHoaDon();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonActionPerformed
        new HoaDonJDialog(this, true, listHD).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnHoaDonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtTimkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimkiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimkiemActionPerformed

    private void txtTimkiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimkiemKeyPressed
        loadThuoc();
    }//GEN-LAST:event_txtTimkiemKeyPressed

    private void txtTimkiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimkiemKeyReleased
        loadThuoc();

    }//GEN-LAST:event_txtTimkiemKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BanThuoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BanThuoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BanThuoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BanThuoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BanThuoc().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHoaDon;
    private javax.swing.JComboBox<String> cboDonVi;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblThuoc;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtHanSuDung;
    private javax.swing.JTextField txtMathuoc;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenThuoc;
    private javax.swing.JTextField txtThanhTien;
    private javax.swing.JTextField txtTimkiem;
    private javax.swing.JTextField txtTongThanhTien;
    // End of variables declaration//GEN-END:variables
}
