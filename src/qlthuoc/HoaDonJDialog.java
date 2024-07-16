/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package qlthuoc;

import DAO.ChiTietHoaDonDAO;
import DAO.DonViDAO;
import DAO.HoaDonDAO;
import DAO.ThuocDAO;
import Entity.BanThuoc;
import Entity.ChiTietHoaDon;
import Entity.DonVi;
import Entity.HoaDon;
import Entity.Thuoc;
import Utils.DateHelper;
import Utils.DialogHelper;
import Utils.ShareHelper;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 *
 * @author nguye
 */
public class HoaDonJDialog extends javax.swing.JDialog {

    private Date currentDate = new Date();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Locale vnLocale = new Locale("vi", "VN");
    private SimpleDateFormat date1 = new SimpleDateFormat("HH:mm:ss    dd-MM-yyyy", vnLocale);
    private double tongTien;
    private NumberFormat vnCurrencyFormat = NumberFormat.getCurrencyInstance(vnLocale);
    private List<Entity.BanThuoc> listBT = new ArrayList<>();
    private DefaultTableModel model = new DefaultTableModel();

    /**
     * Creates new form HoaDonJDialog
     */
    public HoaDonJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Quản lý nhà thuốc Long Phụng Sum Vầy");
        lblNgay.setText(date1.format(currentDate));
        Image us = Toolkit.getDefaultToolkit().createImage("src\\Icons\\logocuabeHung.png");
        this.setIconImage(us);
    }

    public HoaDonJDialog(java.awt.Frame parent, boolean modal, List<Entity.BanThuoc> listBT) {
        super(parent, modal);
        initComponents();
        this.listBT = listBT;
        setLocationRelativeTo(null);
        loadHoaDon();
        lblNgay.setText(date1.format(currentDate));
    }
    String tongTienFormat = "";

    public void loadHoaDon() {
        model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        tblHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (BanThuoc bt : listBT) {
            String thanhTien = vnCurrencyFormat.format(bt.getThanhTien());
            String donGia = vnCurrencyFormat.format(bt.getDonGia());
            model.addRow(new Object[]{
                bt.getTenThuoc(), bt.getSoLuong(), bt.getDonVi(), donGia, thanhTien
            });
            tongTien += bt.getThanhTien();
        }
        tongTienFormat = vnCurrencyFormat.format(tongTien);
        lblTongTien.setText("Tổng tiền: " + tongTienFormat);
    }

//    public void loadThongTin() {
//        String danhsach = "";
//        for (BanThuoc bt : listBT) {
//            danhsach += bt.getTenThuoc() + "     " + bt.getSoLuong() + "     " + bt.getThanhTien() + "\n";
//        }
//        JOptionPane.showMessageDialog(this, danhsach);
//    }
    HoaDonDAO daoHD = new HoaDonDAO();

    public HoaDon getFromHD() {
        List<HoaDon> listHoaDon = daoHD.select();
        String pm = listHoaDon.get(listHoaDon.size() - 1).getMaHoaDon();
        String substring = pm.substring(2);
        int soHD = Integer.parseInt(substring) + 1;

        HoaDon hoaDon = new HoaDon();

        if (soHD < 100) {
            hoaDon.setMaHoaDon("HD0" + soHD);
        } else {
            hoaDon.setMaHoaDon("HD" + soHD);
        }
        hoaDon.setMaNhanVien(ShareHelper.USER.getMaNhanVien());
        hoaDon.setNgayBan(currentDate);
        hoaDon.setTongTien(tongTien);
        hoaDon.setTenKH(txtTenKhachHang.getText());
        hoaDon.setSDT(txtSoDienThoai.getText());
        return hoaDon;
    }

    ChiTietHoaDonDAO daoCTHD = new ChiTietHoaDonDAO();
    ThuocDAO daoThuoc = new ThuocDAO();
    DonViDAO daoDonVi = new DonViDAO();

    public void thanhToan() {
        if (!listBT.isEmpty()) {
            if (!txtSoDienThoai.getText().equals("")) {
                String phoneFormat = "0\\d{9,10}";
                if (!txtSoDienThoai.getText().matches(phoneFormat)) {
                    DialogHelper.alert(this, "Số điện thoại bạn nhập không đúng định dạng!");
                    return;
                }
            }
            List<HoaDon> listHoaDon = daoHD.select();
            String maHD = getFromHD().getMaHoaDon();
            daoHD.insert(getFromHD());
            for (BanThuoc bt : listBT) {
                List<ChiTietHoaDon> list = daoCTHD.select();
                int STT = list.size() + 1;
                ChiTietHoaDon modelCTHD = new ChiTietHoaDon();
                modelCTHD.setSTT(String.valueOf(STT));
                modelCTHD.setMaHoaDon(maHD);
                modelCTHD.setMaThuoc(bt.getMaThuoc());
                modelCTHD.setSoLuong(bt.getSoLuong());
                modelCTHD.setGia(bt.getDonGia());
                modelCTHD.setDonVi(bt.getDonVi());
                daoCTHD.insert(modelCTHD);
            }
            DialogHelper.alert(this, "Thêm thành công.");
            
            for (BanThuoc bt : listBT) {
                Thuoc t = daoThuoc.findById(bt.getMaThuoc());
                List<DonVi> listDV = daoDonVi.select();
                int soLuong = 0;
                for (DonVi donVi : listDV) {
                    if (donVi.getMaThuoc().equals(bt.getMaThuoc())) {
                        if (bt.getDonVi().equals(donVi.getDonViTinh())) {
                            soLuong = bt.getSoLuong() * donVi.getSoLuong();
                        }
                    }
                }
                daoThuoc.update(new Thuoc(t.getMaThuoc(), t.getMaLoaiThuoc(), t.getMaNhaCungCap(),
                        t.getTenthuoc(), t.getSoLuong() - soLuong, t.getGia(),
                        t.getMota(), t.getHanSuDung()));
            }
            // Tạo một tài liệu Word mới
            XWPFDocument document = new XWPFDocument();
            // Thêm một đoạn văn bản mới vào tài liệu
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Nhà Thuốc Long Phụng Sum Vầy\n");
            run.setText("Tên khách hàng: " + txtTenKhachHang.getText() + "\t" + "Số điện thoai: " + txtSoDienThoai.getText() + "\n");
            run.setText(String.valueOf(dateFormat.format(currentDate)));
            for (BanThuoc bt : listBT) {
                String thanhTien = vnCurrencyFormat.format(bt.getThanhTien());
                run.setText(bt.getTenThuoc() + "\t" + bt.getSoLuong() + "\t" + bt.getDonVi() + "\t" + thanhTien + "\n");
                tongTien += bt.getThanhTien();
            }
            run.setText("Tổng thành tiền " + lblTongTien.getText());

            try (FileOutputStream out = new FileOutputStream("D:\\hoaDon" + listHoaDon.get(listHoaDon.size() - 1).getMaHoaDon() + ".docx"
            )) {
                document.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            model.setRowCount(0);
            txtSoDienThoai.setText("");
            txtTenKhachHang.setText("");
            lblTongTien.setText("Tổng tiền: ");
            tongTienFormat = "";
            listBT.clear();
            this.dispose();
        } else {
            DialogHelper.alert(this, "Bạn chưa có thuốc!");
        }
    }

    public void xoaThuoc() {
        tongTien = 0;
        tongTienFormat = "";
        listBT.remove(tblHoaDon.getSelectedRow());
        loadHoaDon();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTenKhachHang = new javax.swing.JTextField();
        txtSoDienThoai = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        lblNgay = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(14, 144, 238));
        jLabel1.setText("Nhà thuốc Long Phụng Sum Vầy");

        jLabel2.setText("Tên khách hàng");

        jLabel3.setText("Số điện thoại");

        txtSoDienThoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoDienThoaiActionPerformed(evt);
            }
        });

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tên thuốc", "Số lượng", "Đơn vị", "Đơn giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblHoaDonMousePressed(evt);
            }
        });
        tblHoaDon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblHoaDonKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        lblNgay.setText("20/20/2020");

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(255, 51, 51));
        lblTongTien.setText("Tổng tiền:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Delete.png"))); // NOI18N
        jButton1.setText("Đóng");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Tick.png"))); // NOI18N
        jButton2.setText("Thanh toán");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(65, 65, 65))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jButton1)
                        .addGap(151, 151, 151)
                        .addComponent(jButton2)))
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(103, 103, 103))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNgay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTongTien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoDienThoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoDienThoaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoDienThoaiActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        qlthuoc.BanThuoc bt = new qlthuoc.BanThuoc();
        bt.loadThuoc();
        bt.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        thanhToan();
        new qlthuoc.BanThuoc().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblHoaDonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblHoaDonKeyReleased
        if (!listBT.isEmpty()) {
            if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                int a = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa không?",
                        "Hệ thống quản lý Nhà thuốc LONG PHỤNG SUM VẦY", JOptionPane.YES_NO_OPTION);
                if (a == JOptionPane.YES_OPTION) {
                    xoaThuoc();
                }
            }
        }
    }//GEN-LAST:event_tblHoaDonKeyReleased

    private void tblHoaDonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMousePressed

    }//GEN-LAST:event_tblHoaDonMousePressed

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
            java.util.logging.Logger.getLogger(HoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HoaDonJDialog dialog = new HoaDonJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNgay;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTenKhachHang;
    // End of variables declaration//GEN-END:variables
}
