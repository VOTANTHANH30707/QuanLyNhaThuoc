/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package qlthuoc;

import DAO.DonViDAO;
import DAO.LoaiThuocDAO;
import DAO.NhaCungCapDAO;
import DAO.ThuocDAO;
import Entity.DonVi;
import Entity.LoaiThuoc;
import Entity.NhaCungCap;
import Entity.Thuoc;
import Utils.DateHelper;
import Utils.DialogHelper;
import Utils.ShareHelper;
import Utils.QRCode;
import com.google.zxing.WriterException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nphun
 */
public class ThuocJDiagram extends javax.swing.JDialog {

    ThuocDAO daoMaThuoc = new ThuocDAO();
    Thuoc t = new Thuoc();
    List<Thuoc> list = daoMaThuoc.select();
    String pm = list.get(list.size() - 1).getMaThuoc();
    public static String qrCodeImage = "";
    java.awt.Frame parent = null;

    /**
     * Creates new form ThuocJDiagram
     */
    public ThuocJDiagram(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
        txtMaThuoc.setEditable(false);
        Image us = Toolkit.getDefaultToolkit().createImage("src\\Icons\\logocuabeHung.png");
        this.setIconImage(us);
    }

    public void init() {
        setLocationRelativeTo(null);
        if (!ShareHelper.isManager()) {
            tabs.setEnabledAt(1, false);
        } else {
            tabs.setEnabledAt(1, true);
        }
    }

    int index = 0;
    ThuocDAO dao = new ThuocDAO();
    Locale vn = new Locale("vi", "VN");
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(vn);

    void load() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            List<Thuoc> list = dao.select1("MaThuoc", "MaLoaiThuoc", "MaNhaCungCap", "TenThuoc", "SoLuong", "Gia", "Mota", "HanSuDung");
            int i = 1;
            for (Thuoc t : list) {
                Object[] row = {i++,
                    t.getMaThuoc(),
                    t.getMaLoaiThuoc(),
                    t.getMaNhaCungCap(),
                    t.getTenthuoc(),
                    numberFormat.format(t.getGia()),
                    t.getMota(),
                    t.getHanSuDung()};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    LoaiThuocDAO ltdao = new LoaiThuocDAO();
    NhaCungCapDAO nccdao = new NhaCungCapDAO();

    void fillCB() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboLoaiThuoc.getModel();
        model.removeAllElements();
        List<LoaiThuoc> list = ltdao.select();
        for (LoaiThuoc lt : list) {
            model.addElement(lt.getMaLoaiThuoc());
        }
    }

    void fillCB1() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboNCC.getModel();
        model.removeAllElements();
        List<NhaCungCap> list = nccdao.select();
        for (NhaCungCap ncc : list) {
            model.addElement(ncc.getMaNhaCungCap());
        }
    }

    void setForm(Thuoc model) {
        txtMaThuoc.setText(model.getMaThuoc());
        cboLoaiThuoc.setSelectedItem(model.getMaLoaiThuoc());
        cboNCC.setSelectedItem(model.getMaNhaCungCap());
        txtThuoc.setText(model.getTenthuoc());
        txtGia.setText(String.valueOf(model.getGia()));
        txtMoTa.setText(model.getMota());
        txtHSD.setText(String.valueOf(model.getHanSuDung()));
    }

    Thuoc getForm() {
        t.setMaThuoc(txtMaThuoc.getText());
        t.setMaLoaiThuoc((String) cboLoaiThuoc.getSelectedItem());
        t.setMaNhaCungCap((String) cboNCC.getSelectedItem());
        t.setTenthuoc(txtThuoc.getText());
        t.setSoLuong(0);
        t.setGia(Double.parseDouble(txtGia.getText()));
        t.setMota(txtMoTa.getText());
        Date date = null;
        try {
            date = sdf.parse(txtHSD.getText());
        } catch (Exception e) {
        }
        t.setHanSuDung(date);
        return t;
    }

    void edit() {
        try {
            String MaThuoc = (String) tblGridView.getValueAt(this.index, 1);
            Thuoc model = dao.findById(MaThuoc);
            if (model != null) {
                this.setForm(model);
                tabs.setSelectedIndex(1);
                btnInsert.setEnabled(false);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void update() {
        if (validator()) {
            Thuoc model = getForm();
            try {
                dao.update(model);
                this.load();
                this.clear();
                DialogHelper.alert(this, "Cập nhật thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "Cập nhật thất bại!");
            }
        }
    }

    void insert() {
        if (validator()) {
            Thuoc model = getForm();
            if (!listDonVi.isEmpty()) {
                try {
                    dao.insert(model);
                    this.load();
                    this.clear();
                    for (DonVi dv : listDonVi) {
                        daoDV.insert(new DonVi(dv.getSTT(),
                                dv.getMaThuoc(), dv.getDonViTinh(), dv.getSoLuong()));
                    }
                    listDonVi.clear();
                    DialogHelper.alert(this, "Thêm mới thành công!");
                } catch (Exception e) {
                    DialogHelper.alert(this, "Thêm mới thất bại!");
                    System.out.println(e);
                }
            } else {
                DialogHelper.alert(this, "Bạn chưa có đơn vị cho thuốc!");
            }
        }
    }

    void delete() {
        if (txtMaThuoc.getText().equals("")) {
            DialogHelper.alert(this, "Mã thuốc đang để trống!");
        } else if (!txtMaThuoc.getText().equals(pm)) {
            DialogHelper.alert(this, "Thuốc chưa có trong danh sách!");
        } else {
            if (DialogHelper.confirm(this, "Bạn có muốn xóa hay không?")) {
                String maThuoc = txtMaThuoc.getText();
                try {
                    dao.delete(maThuoc);
                    this.load();
                    this.clear();
                    DialogHelper.alert(this, "Xóa thành công!");
                } catch (Exception e) {
                    DialogHelper.alert(this, "Xóa thất bại!");
                }
            }
        }
    }

    void clear() {
        this.setForm(new Thuoc());
        String subString = pm.substring(2);
        int soMaThuoc = Integer.parseInt(subString) + 1;
        if (soMaThuoc < 100) {
            txtMaThuoc.setText("T0" + soMaThuoc);
        } else {
            txtMaThuoc.setText("T" + soMaThuoc);
        }
        cboLoaiThuoc.setSelectedIndex(0);
        cboNCC.setSelectedIndex(0);
        txtThuoc.setText("");
        txtHSD.setText("");
        txtGia.setText("");
        txtMoTa.setText("");
        txtSoLuong.setText("");
        cboDonVi.setSelectedIndex(0);
        btnInsert.setEnabled(true);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    boolean validator() {
        StringBuilder thongBaoLoi = new StringBuilder();
        boolean coLoi = false;
        if (txtThuoc.getText().equals("")) {
            thongBaoLoi.append("Không để trống tên thuốc!\n");
            coLoi = true;
        } else if (!txtThuoc.getText().matches("^[a-zA-ZÀ-ỹ\s]+$")) {
            thongBaoLoi.append("Tên thuốc không phải là số!\n");
            coLoi = true;
        }
        if (txtGia.getText().equals("")) {
            thongBaoLoi.append("Chưa nhập giá thuốc!\n");
            coLoi = true;
        } else if (!txtGia.getText().matches("-?\\d+(\\.\\d+)?")) {
            thongBaoLoi.append("Giá thuốc phải là số!\n");
            coLoi = true;
        }
        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        try {
            Date date = sdf.parse(txtHSD.getText());
            if (!date.after(today)) {
                thongBaoLoi.append("Sản phẩm đã quá hạn!\n");
                coLoi = true;
            }
        } catch (Exception e) {
            thongBaoLoi.append("Định dạng HanSuDung không hợp lệ! (VD: 2023-11-23)\n");
            coLoi = true;
        }
        if (coLoi) {
            DialogHelper.alert(this, thongBaoLoi.toString());
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMaThuoc = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtThuoc = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtHSD = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JEditorPane();
        btnDelete = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        btnQR = new javax.swing.JButton();
        cboLoaiThuoc = new javax.swing.JComboBox<>();
        cboNCC = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        cboDonVi = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        btnThemDonVi = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Hệ thống quản lý nhà thuốc");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã thuốc", "Mã loại thuốc", "Mã nhà cung cấp", "Tên thuốc", "Số lượng", "Giá", "Mô tả", "HSD"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
        if (tblGridView.getColumnModel().getColumnCount() > 0) {
            tblGridView.getColumnModel().getColumn(4).setResizable(false);
            tblGridView.getColumnModel().getColumn(4).setHeaderValue("Số lượng");
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("Danh sách", jPanel1);

        jPanel2.setEnabled(false);

        jLabel5.setText("Mã thuốc:");

        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");

        jLabel1.setText("Mã loại thuốc:");

        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("*");

        jLabel3.setText("Mã nhà cung cấp:");

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("*");

        jLabel6.setText("Tên thuốc:");

        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("*");

        jLabel8.setText("Giá:");

        jLabel9.setForeground(new java.awt.Color(255, 0, 51));
        jLabel9.setText("*");

        jLabel11.setText("Hạn sử dụng:");

        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("*");

        jLabel13.setText("Mô tả:");

        jScrollPane2.setViewportView(txtMoTa);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Delete.png"))); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Refresh.png"))); // NOI18N
        btnNew.setText("Mới");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Edit.png"))); // NOI18N
        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Create.png"))); // NOI18N
        btnInsert.setText("Thêm");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnQR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Refresh.png"))); // NOI18N
        btnQR.setText("QRCODE");
        btnQR.setEnabled(false);
        btnQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQRActionPerformed(evt);
            }
        });

        cboLoaiThuoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboNCC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel14.setText("Đơn vị:");

        cboDonVi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hộp", "Gói", "Vỉ", "Viên", "Chai", "Tuýp" }));

        jLabel15.setForeground(new java.awt.Color(255, 0, 51));
        jLabel15.setText("*");

        jLabel16.setText("Số lượng/đơn vị:");

        jLabel17.setForeground(new java.awt.Color(255, 0, 0));
        jLabel17.setText("*");

        btnThemDonVi.setText("Thêm");
        btnThemDonVi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDonViActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cboLoaiThuoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtHSD, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtSoLuong)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnThemDonVi))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 15, Short.MAX_VALUE)
                                .addComponent(btnNew)
                                .addGap(18, 18, 18)
                                .addComponent(btnQR)
                                .addGap(240, 240, 240))
                            .addComponent(jScrollPane2))))
                .addGap(65, 65, 65))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnInsert)
                .addGap(18, 18, 18)
                .addComponent(btnUpdate)
                .addGap(18, 18, 18)
                .addComponent(btnDelete)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel10)
                    .addComponent(txtMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(cboLoaiThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(txtThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(txtHSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cboDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemDonVi))
                .addGap(18, 25, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew)
                    .addComponent(btnInsert)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnQR))
                .addContainerGap())
        );

        tabs.addTab("Thêm thuốc", jPanel2);

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
            .addComponent(tabs)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        this.clear();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        btnQR.setEnabled(false);
        this.delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        this.insert();
    }//GEN-LAST:event_btnInsertActionPerformed

    private void tblGridViewMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMousePressed
        // TODO add your handling code here:
        if (ShareHelper.isManager()) {
            if (evt.getClickCount() == 2) {
                this.index = tblGridView.rowAtPoint(evt.getPoint());
                this.index = tblGridView.rowAtPoint(evt.getPoint());
                this.edit();
                btnQR.setEnabled(true);
            }
        }

    }//GEN-LAST:event_tblGridViewMousePressed

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.load();
        this.fillCB();
        this.fillCB1();
    }//GEN-LAST:event_formWindowOpened

    private void btnQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQRActionPerformed
        // TODO add your handling code here:
        try {
            String maThuoc = (String) tblGridView.getValueAt(this.index, 1);
            Thuoc t1 = dao.findById(maThuoc);
            String qrCodeText = "Ma thuoc: " + t1.getMaThuoc() + " Ten thuoc: " + t1.getTenthuoc() + " Gia: " + t1.getGia();
            String filePath = t1.getMaThuoc() + t1.getTenthuoc() + ".jpg";
            File destination = new File("storeFiles", filePath);

            filePath = Paths.get(destination.getAbsolutePath()).toString();
            int size = 200;
            String fileType = "jpg";
            File qrFile = new File(filePath);
            QRCode.createQRImage(qrFile, qrCodeText, size, fileType);
            this.qrCodeImage = filePath;
            new QRCodeJDialog(this.parent, true).setVisible(true);
        } catch (WriterException ex) {
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_btnQRActionPerformed

    DonViDAO daoDV = new DonViDAO();
    List<DonVi> listDonVi = new ArrayList<>();
    int stt = 0;

    public void themDonVi() {
        if (txtSoLuong.equals("")) {
            DialogHelper.alert(this, "Số lượng không được để trống!");
            return;
        }
        try {
            int a = Integer.parseInt(txtSoLuong.getText());
        } catch (Exception e) {
            DialogHelper.alert(this, "Số lượng phải là số!");
            return;
        }
        if (Integer.parseInt(txtSoLuong.getText()) <= 0) {
            DialogHelper.alert(this, "Số lượng không được bé hơn hoặc bằng 0!");
            return;
        }
        List<DonVi> listGetSL = daoDV.select();
        if (stt == 0) {
            stt = listGetSL.size() + 1;
        } else {
            stt += 1;
        }
//        DialogHelper.alert(parent, String.valueOf(stt));
        String maThuoc = txtMaThuoc.getText();
        String donVi = String.valueOf(cboDonVi.getSelectedItem());
        int soLuong = Integer.valueOf(txtSoLuong.getText());
        DonVi dv = new DonVi(String.valueOf(stt), maThuoc, donVi, soLuong);
        listDonVi.add(dv);
        DialogHelper.alert(this, "Thêm đơn vị thành công!");
    }

    private void btnThemDonViActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDonViActionPerformed
        themDonVi();
        cboDonVi.setSelectedIndex(0);
        txtSoLuong.setText("");
    }//GEN-LAST:event_btnThemDonViActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThuocJDiagram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThuocJDiagram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThuocJDiagram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThuocJDiagram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThuocJDiagram dialog = new ThuocJDiagram(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnQR;
    private javax.swing.JButton btnThemDonVi;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboDonVi;
    private javax.swing.JComboBox<String> cboLoaiThuoc;
    private javax.swing.JComboBox<String> cboNCC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtHSD;
    private javax.swing.JTextField txtMaThuoc;
    private javax.swing.JEditorPane txtMoTa;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtThuoc;
    // End of variables declaration//GEN-END:variables
}
