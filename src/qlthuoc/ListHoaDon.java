/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package qlthuoc;

import DAO.ChiTietHoaDonDAO;
import DAO.HoaDonDAO;
import Entity.HoaDon;
import Utils.DateHelper;
import Utils.DialogHelper;
import Utils.ShareHelper;
import com.raven.datechooser.DateBetween;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.listener.DateChooserAction;
import com.raven.datechooser.listener.DateChooserAdapter;
import java.awt.Image;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DIEUMY
 */
public class ListHoaDon extends javax.swing.JDialog {

    Locale vietnameseLocale = new Locale("vi", "VN");
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(vietnameseLocale);

    /**
     * Creates new form ListHoaDon
     */
    public ListHoaDon(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        loadDataTotable();
        Image us = Toolkit.getDefaultToolkit().createImage("src\\Icons\\logocuabeHung.png");
        this.setIconImage(us);
    }
    private DateChooser chDate = new DateChooser();

    void init() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Image us = Toolkit.getDefaultToolkit().createImage("src\\Icons\\logocuabeHung.png");
        this.setIconImage(us);
        chDate.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        chDate.setTextField(txtTimHD1);
        chDate.setDateSelectionMode(DateChooser.DateSelectionMode.BETWEEN_DATE_SELECTED);
//        chDate.setLabelCurrentDayVisible(false);
        chDate.addActionDateChooserListener(new DateChooserAdapter() {
            @Override
            public void dateBetweenChanged(DateBetween date, DateChooserAction action) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateForm = df.format(date.getFromDate());
                String today = df.format(date.getToDate());
                loadDataTotableDate(dateForm, today);
            }

        });
        setTitle("Danh sách hóa đơn");
        lblNhanVien.setText(ShareHelper.USER.getHo() + " " + ShareHelper.USER.getTen());
    }

    HoaDonDAO hoadondao = new HoaDonDAO();
    List<HoaDon> list = new ArrayList<>();
    private DefaultTableModel modelTable = new DefaultTableModel();
    int index = -1;

    void loadDataTotableDate(String dateForm, String toDay) {
        ///////////////////////////////////////////////////////////////////////////////////
        list = hoadondao.findByMaNVDate(ShareHelper.USER.getMaNhanVien(), dateForm, toDay);
        //       list = hoadondao.findByMaNV("NV001");
        modelTable = (DefaultTableModel) tblGridView.getModel();
        modelTable.setRowCount(0);
        tblGridView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Double doanhThu = 0.0;
        for (int i = 0; i < list.size(); i++) {
            modelTable.addRow(new Object[]{i + 1,
                list.get(i).getMaHoaDon(), list.get(i).getNgayBan(),
                numberFormat.format(list.get(i).getTongTien()),
                list.get(i).getTenKH(), list.get(i).getSDT()});
            doanhThu += list.get(i).getTongTien();
        }
        lblDoanhThu.setText(numberFormat.format(doanhThu));
    }

    void loadDataTotable() {
        ///////////////////////////////////////////////////////////////////////////////////
        list = hoadondao.findByMaNV(ShareHelper.USER.getMaNhanVien());
        //       list = hoadondao.findByMaNV("NV001");
        modelTable = (DefaultTableModel) tblGridView.getModel();
        modelTable.setRowCount(0);
        tblGridView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Double doanhThu = 0.0;
        for (int i = 0; i < list.size(); i++) {
            modelTable.addRow(new Object[]{i + 1,
                list.get(i).getMaHoaDon(), list.get(i).getNgayBan(),
                numberFormat.format(list.get(i).getTongTien()),
                list.get(i).getTenKH(), list.get(i).getSDT()});
            doanhThu += list.get(i).getTongTien();
        }
        lblDoanhThu.setText(numberFormat.format(doanhThu));
    }
    ChiTietHoaDonDAO chitietdao = new ChiTietHoaDonDAO();

    void xem() {
        String mahd = (String) tblGridView.getValueAt(index, 1);
        List<Object[]> listhd = chitietdao.getChiTiet(mahd);

        tabs.setSelectedIndex(1);
        modelTable = (DefaultTableModel) tblChiTiet.getModel();
        modelTable.setRowCount(0);
        tblGridView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Object[] row : listhd) {
            modelTable.addRow(new Object[]{
                row[0], row[1], row[2], row[5], row[3],
                numberFormat.format(row[4]), numberFormat.format(row[6])
            });
        }
        lblMaHD.setText(mahd);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaHoaDon().equals(mahd)) {
                lblNgayBan.setText(DateHelper.toString(list.get(i).getNgayBan()));
                lblTenKH.setText(list.get(i).getTenKH());
                lblSDT.setText(list.get(i).getSDT());
                lblTongTien.setText(numberFormat.format(list.get(i).getTongTien()));
            }
        }
    }

    public class ngay {

        static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd");

        public static Date toDate(String date, String... pattern) {
            try {
                if (pattern.length > 0) {
                    DATE_FORMATER.applyPattern(pattern[0]);
                }
                if (date == null) {
                    return DateHelper.now();
                }
                return DATE_FORMATER.parse(date);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    void loadDataToTableToday() throws ParseException {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(currentDate);
        list = hoadondao.findByMaNV_Today(ShareHelper.USER.getMaNhanVien(), ngay.toDate(date));
//        list = hoadondao.findByMaNV_Today("NV001", DateHelper.now());

        modelTable = (DefaultTableModel) tblGridView.getModel();
        modelTable.setRowCount(0);
        tblGridView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Double doanhThu = 0.0;
        for (int i = 0; i < list.size(); i++) {
            modelTable.addRow(new Object[]{i + 1,
                list.get(i).getMaHoaDon(), list.get(i).getNgayBan(),
                numberFormat.format(list.get(i).getTongTien()),
                list.get(i).getTenKH(), list.get(i).getSDT()});
            doanhThu += list.get(i).getTongTien();
        }
        lblDoanhThu.setText(numberFormat.format(doanhThu));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        btnToday = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        lblDoanhThu = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTimHD1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblSDT = new javax.swing.JLabel();
        lblNgayBan = new javax.swing.JLabel();
        lblTenKH = new javax.swing.JLabel();
        lblMaHD = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblChiTiet = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã hóa đơn", "Ngày bán", "Tổng tiền", "Tên khách hàng", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGridView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridViewMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblGridViewMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblGridView);

        btnToday.setText("Hôm nay");
        btnToday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTodayActionPerformed(evt);
            }
        });

        jLabel7.setText("Doanh thu");

        lblDoanhThu.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblDoanhThu.setForeground(new java.awt.Color(255, 0, 0));
        lblDoanhThu.setText("jLabel8");

        jLabel8.setText("Chọn ngày:");

        txtTimHD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimHD1ActionPerformed(evt);
            }
        });
        txtTimHD1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimHD1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimHD1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnToday)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimHD1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(lblDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimHD1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(btnToday))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblDoanhThu))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("Danh sách", jPanel1);

        jPanel2.setEnabled(false);

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 255));
        jLabel16.setText("Chi Tiết Hóa Đơn");

        jLabel1.setText("Mã hóa đơn");

        jLabel2.setText("Ngày bán");

        jLabel3.setText("Tên khách hàng");

        jLabel4.setText("Số điện thoại");

        lblSDT.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblSDT.setText("jLabel6");

        lblNgayBan.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblNgayBan.setText("jLabel6");

        lblTenKH.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblTenKH.setText("jLabel6");

        lblMaHD.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblMaHD.setText("jLabel6");

        tblChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã thuốc", "Tên thuốc", "Đơn vị tính", "Số lượng ", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblChiTiet);
        if (tblChiTiet.getColumnModel().getColumnCount() > 0) {
            tblChiTiet.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel5.setText("Tổng tiền");

        lblTongTien.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(204, 0, 51));
        lblTongTien.setText("jLabel6");

        jLabel6.setText("Nhân viên");

        lblNhanVien.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblNhanVien.setForeground(new java.awt.Color(51, 51, 255));
        lblNhanVien.setText("jLabel7");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNgayBan, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(32, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(lblNgayBan)
                    .addComponent(lblMaHD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(lblSDT)
                    .addComponent(lblTenKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblTongTien)
                    .addComponent(jLabel6)
                    .addComponent(lblNhanVien))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        tabs.addTab("Chi tiết hóa đơn", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void tblGridViewMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMousePressed

        if (evt.getClickCount() == 2) {
            this.index = tblGridView.rowAtPoint(evt.getPoint());
            this.xem();
        }
    }//GEN-LAST:event_tblGridViewMousePressed

    private void btnTodayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTodayActionPerformed
        try {
            loadDataToTableToday();
        } catch (ParseException ex) {
            Logger.getLogger(ListHoaDon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTodayActionPerformed

    private void txtTimHD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimHD1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimHD1ActionPerformed

    private void txtTimHD1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimHD1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimHD1KeyPressed

    private void txtTimHD1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimHD1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimHD1KeyReleased

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
            java.util.logging.Logger.getLogger(ListHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ListHoaDon dialog = new ListHoaDon(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnToday;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDoanhThu;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblNgayBan;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JLabel lblTenKH;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblChiTiet;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtTimHD1;
    // End of variables declaration//GEN-END:variables
}
