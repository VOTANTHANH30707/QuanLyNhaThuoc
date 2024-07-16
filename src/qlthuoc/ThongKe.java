/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package qlthuoc;

import DAO.ChiTietHoaDonDAO;
import DAO.DoanhThuDAO;
import DAO.HoaDonDAO;
import DAO.ThuocDAO;
import Entity.ChiTietHoaDon;
import Entity.DoanhThu;
import Entity.HoaDon;
import Entity.Thuoc;
import Utils.DateHelper;
import Utils.DialogHelper;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.nio.file.Files.list;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import com.raven.datechooser.DateBetween;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.listener.DateChooserAction;
import com.raven.datechooser.listener.DateChooserAdapter;
import java.text.NumberFormat;
import java.util.Locale;

public class ThongKe extends javax.swing.JDialog {

    /**
     * Creates new form ThongKe
     */
    public ThongKe(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }
    private DateChooser chDate = new DateChooser();
    private HoaDonDAO daoHoaDon = new HoaDonDAO();
    private List<HoaDon> listHD = new ArrayList<>();
    private ChiTietHoaDon chiTietHD = new ChiTietHoaDon();
    private Connection conn = null;
    DefaultTableModel model;

    void init() {
//        conn = CONNECT_SQL.getConnection("sa", "songlong", "PRO1041_QLNhaThuoc");
        setLocationRelativeTo(null);
        loadTonKho();
        loadHoaDon();
        Image us = Toolkit.getDefaultToolkit().createImage("src\\Icons\\logocuabeHung.png");
        this.setIconImage(us);
        model = (DefaultTableModel) tblHoaDon.getModel();
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
                loadHD_Theo_Ngay(dateForm, today);
            }

        });
        ThongKe.setDataToChart1(pnlDoanhThu, 2023);
        cboNam.removeAllItems();
        for (int i = 2018; i < 2060; i++) {
            String nam = String.valueOf(i);
            cboNam.addItem(nam);
        }
        cboNam.setSelectedItem("2023");
    }
    Locale vietnameseLocale = new Locale("vi", "VN");
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(vietnameseLocale);

    void loadHD_Theo_Ngay(String date, String date2) {
        model.setRowCount(0);
        int i = 1;
        for (HoaDon listHD1 : listHD) {
            i++;
        }
        try {
            List<HoaDon> list = daoHoaDon.Select_invoice_by_date(date, date2);
            for (HoaDon hd : list) {
                Object[] row = {
                    i++,
                    hd.getMaHoaDon(),
                    hd.getMaNhanVien(),
                    hd.getNgayBan(),
                    numberFormat.format(hd.getTongTien()),
                    hd.getSDT(),
                    hd.getTenKH()
                };
                model.addRow(row);
            }
            double tongTien = 0;
            for (HoaDon hd1 : list) {
                tongTien += hd1.getTongTien();
            }
            lblTongTien.setText(numberFormat.format(tongTien));

        } catch (Exception e) {
        }

    }

    void loadHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0);
        int i = 1;
        for (int j = 0; j < listHD.size(); j++) {
            i++;
        }
        try {
            String keyWord = txtTimHD.getText();
            List<HoaDon> list = daoHoaDon.selectByKeyword(keyWord);
            for (HoaDon hd : list) {
                Object[] row = {
                    i++,
                    hd.getMaHoaDon(),
                    hd.getMaNhanVien(),
                    hd.getNgayBan(),
                    numberFormat.format(hd.getTongTien()),
                    hd.getSDT(),
                    hd.getTenKH()
                };
                model.addRow(row);
            }
            double tongTien = 0;
            for (HoaDon hd1 : list) {
                tongTien += hd1.getTongTien();
            }
            lblTongTien.setText(numberFormat.format(tongTien));
        } catch (Exception e) {
        }
    }

    ThuocDAO daoThuoc = new ThuocDAO();
    List<Thuoc> listThuoc = new ArrayList<>();

    void loadTonKho() {
        DefaultTableModel model = (DefaultTableModel) tblTonKho.getModel();
        model.setRowCount(0);
        int i = 1;
        for (int j = 0; j < listThuoc.size(); j++) {
            i++;
        }
        try {
            List<Thuoc> list = daoThuoc.select();
            for (Thuoc thuoc : list) {
                Object[] row = {
                    i++,
                    thuoc.getMaThuoc(),
                    thuoc.getMaLoaiThuoc(),
                    thuoc.getMaNhaCungCap(),
                    thuoc.getTenthuoc(),
                    thuoc.getSoLuong(),
                    thuoc.getGia(),
                    thuoc.getHanSuDung(),
                    thuoc.getMota()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
        }
    }

    void thuocHetHan() {
        DefaultTableModel model = (DefaultTableModel) tblTonKho.getModel();
        model.setRowCount(0);
        int i = 1;
        for (int j = 0; j < listThuoc.size(); j++) {
            i++;
        }
        try {
            List<Thuoc> list = daoThuoc.selectThuocHetHan();
            for (Thuoc thuoc : list) {
                Object[] row = {
                    i++,
                    thuoc.getMaThuoc(),
                    thuoc.getMaLoaiThuoc(),
                    thuoc.getMaNhaCungCap(),
                    thuoc.getTenthuoc(),
                    thuoc.getSoLuong(),
                    thuoc.getGia(),
                    thuoc.getHanSuDung(),
                    thuoc.getMota()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
        }
    }

    void xuatHD() throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Danh_Sach_Hoa_Don");
        XSSFRow row = null;
        Cell cell = null;
        row = sheet.createRow(0);
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Danh Sách Hóa Đơn");
        row = sheet.createRow(2);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("STT");

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Mã Hóa Đơn");

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Mã Nhân Viên");

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Ngày Bán");

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Tổng Tiền");

        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("SĐT Khách Hàng");

        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Tên KH");

        List<HoaDon> list = daoHoaDon.select();
        for (int i = 0; i < list.size(); i++) {

            row = sheet.createRow(3 + i);

            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue(i + 1);//STT

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(list.get(i).getMaHoaDon());

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(list.get(i).getMaNhanVien());

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(list.get(i).getNgayBan());

            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(list.get(i).getTongTien());

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellValue(list.get(i).getSDT());

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue(list.get(i).getTenKH());
        }

        // Use a file chooser to let the user choose the location to save the file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Write the workbook content to the selected file
            try (FileOutputStream fos = new FileOutputStream(filePath + ".xlsx")) {
                workbook.write(fos);
                fos.close();
            }
            DialogHelper.alert(this, "Xuất thành công!");
            System.out.println("Excel file created successfully at: " + filePath + ".xlsx");
        } else {
            System.out.println("Save operation canceled by the user.");
        }
    }

    void XuatTonKho() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Danh_Sach_Thuoc_Ton_Kho");
        Header header = sheet.getFirstHeader();
        header.setCenter("Danh Sách Thuốc Tồn Kho");
        XSSFRow row = null;
        Cell cell = null;
        row = sheet.createRow(0);
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Danh Sách Thuốc Tồn Kho");
        row = sheet.createRow(2);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("STT");

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Mã Thuốc");

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Mã Loại Thuốc");

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Mã Nhà Cung Cấp");

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Tên Thuốc");

        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Số Lượng");

        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Giá");

        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Hạn Sử Dụng");

        cell = row.createCell(8, CellType.STRING);
        cell.setCellValue("Mô Tả");

        List<Thuoc> list = daoThuoc.select();
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(3 + i);
            cell = row.createCell(0, CellType.NUMERIC);
            cell.setCellValue(i + 1);//STT

            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(list.get(i).getMaThuoc());

            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(list.get(i).getMaLoaiThuoc());

            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(list.get(i).getMaNhaCungCap());

            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(list.get(i).getTenthuoc());

            cell = row.createCell(5, CellType.NUMERIC);
            cell.setCellValue(list.get(i).getSoLuong());

            cell = row.createCell(6, CellType.STRING);
            cell.setCellValue(list.get(i).getGia());

            cell = row.createCell(7, CellType.STRING);
            cell.setCellValue(DateHelper.toString(list.get(i).getHanSuDung()));

            cell = row.createCell(8, CellType.STRING);
            cell.setCellValue(list.get(i).getMota());
        }

        // Use a file chooser to let the user choose the location to save the file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Write the workbook content to the selected file
            try (FileOutputStream fos = new FileOutputStream(filePath + ".xlsx")) {
                workbook.write(fos);
                fos.close();
            }

            System.out.println("Excel file created successfully at: " + filePath + ".xlsx");
        } else {
            System.out.println("Save operation canceled by the user.");
        }

    }

    void ReadFileHD() throws IOException {

        //Tạo phiên bản Workbook chứa tham chiếu đến tệp .xlsx
        JFileChooser fileChooser = new JFileChooser();

        // Hiển thị hộp thoại lưu file
        int result = fileChooser.showSaveDialog(this);
        // Nếu người dùng nhấn nút Save
        if (result == JFileChooser.APPROVE_OPTION) {
            // Lấy đường dẫn file
            File file = fileChooser.getSelectedFile();
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                FileInputStream fis = new FileInputStream(file);

                // Lấy trang tính đầu tiên/mong muốn từ sổ làm việc
                XSSFSheet sheet = workbook.getSheetAt(0);

                //Lặp qua từng hàng một
                Iterator<Row> rowIterator = sheet.iterator();
                HoaDon hd = new HoaDon();
                rowIterator.next();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    //Đối với mỗi hàng, lặp qua tất cả các cột
                    Iterator<Cell> cellIterator = row.cellIterator();
                    row = rowIterator.next();
                    hd.setMaHoaDon(row.getCell(1).getStringCellValue());
                    hd.setMaNhanVien(row.getCell(2).getStringCellValue());
                    hd.setNgayBan(row.getCell(3).getDateCellValue());
                    hd.setTongTien(Double.parseDouble(row.getCell(4).getStringCellValue()));
                    hd.setSDT(row.getCell(5).getStringCellValue());
                    hd.setTenKH(row.getCell(6).getStringCellValue());
                    daoHoaDon.insert(hd);
                }
                loadHoaDon();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void ReadFileTonKho() throws IOException {

        //Tạo phiên bản Workbook chứa tham chiếu đến tệp .xlsx
        JFileChooser fileChooser = new JFileChooser();

        // Hiển thị hộp thoại lưu file
        int result = fileChooser.showSaveDialog(this);
        // Nếu người dùng nhấn nút Save
        if (result == JFileChooser.APPROVE_OPTION) {
            // Lấy đường dẫn file
            File file = fileChooser.getSelectedFile();
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                FileInputStream fis = new FileInputStream(file);

                // Lấy trang tính đầu tiên/mong muốn từ sổ làm việc
                XSSFSheet sheet = workbook.getSheetAt(0);

                //Lặp qua từng hàng một
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    //Đối với mỗi hàng, lặp qua tất cả các cột
                    Iterator<Cell> cellIterator = row.cellIterator();

                }
                loadTonKho();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    ChiTietHoaDonDAO daoCT = new ChiTietHoaDonDAO();

    public void HoaDonChiTiet() {
        String maHD = (String) tblHoaDon.getValueAt(row, 1);
        DefaultTableModel model = (DefaultTableModel) tblChiTietHD.getModel();
        model.setRowCount(0);
        try {
            List<ChiTietHoaDon> list = daoCT.selectByKeyword(maHD);
            for (ChiTietHoaDon chiTietHD : list) {
                Object[] row = {
                    chiTietHD.getSTT(),
                    chiTietHD.getMaHoaDon(),
                    chiTietHD.getMaThuoc(),
                    chiTietHD.getSoLuong(),
                    numberFormat.format(chiTietHD.getGia()),
                    chiTietHD.getDonVi()
                };
                model.addRow(row);
            }

        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        pnHoaDon = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        txtTimHD = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtTimHD1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        pnChiTietHD = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblChiTietHD = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTonKho = new javax.swing.JTable();
        btnHetHan = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        pnl = new javax.swing.JPanel();
        pnlDoanhThu = new javax.swing.JPanel();
        cboNam = new javax.swing.JComboBox<>();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 573, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Hóa Đơn", "Mã Nhân Viên", "Ngày Bán", "Tổng Tiền", "SDT Khách Hàng", "Tên KH"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblHoaDonMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        txtTimHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimHDActionPerformed(evt);
            }
        });
        txtTimHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimHDKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimHDKeyReleased(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Search.png"))); // NOI18N
        jButton1.setText("Tìm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Xuất Hóa Đơn");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

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

        jLabel1.setText("Chọn ngày:");

        jLabel2.setText("Doanh thu: ");

        lblTongTien.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(255, 51, 51));
        lblTongTien.setText("jLabel3");

        javax.swing.GroupLayout pnHoaDonLayout = new javax.swing.GroupLayout(pnHoaDon);
        pnHoaDon.setLayout(pnHoaDonLayout);
        pnHoaDonLayout.setHorizontalGroup(
            pnHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHoaDonLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(txtTimHD, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimHD1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnHoaDonLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3))
        );
        pnHoaDonLayout.setVerticalGroup(
            pnHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHoaDonLayout.createSequentialGroup()
                .addGap(0, 36, Short.MAX_VALUE)
                .addGroup(pnHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimHD, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(txtTimHD1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jLabel2)
                    .addComponent(lblTongTien))
                .addGap(7, 7, 7))
        );

        tblChiTietHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Hóa Đơn", "Mã Thuốc", "Số Lượng", "Giá", "Đơn Vị"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblChiTietHD);

        jButton8.setText("Thoát");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnChiTietHDLayout = new javax.swing.GroupLayout(pnChiTietHD);
        pnChiTietHD.setLayout(pnChiTietHDLayout);
        pnChiTietHDLayout.setHorizontalGroup(
            pnChiTietHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnChiTietHDLayout.createSequentialGroup()
                .addComponent(jButton8)
                .addGap(33, 33, 33))
        );
        pnChiTietHDLayout.setVerticalGroup(
            pnChiTietHDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnChiTietHDLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addContainerGap())
        );

        jLayeredPane1.setLayer(pnHoaDon, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnChiTietHD, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(pnChiTietHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(pnChiTietHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Thống Kê Bán Hàng", jLayeredPane1);

        tblTonKho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Thuốc", "Mã Loại Thuốc", "Mã Nhà Cung Cấp", "Tên Thuốc ", "Số Lượng", "Giá ", "Hạn Sử Dụng", "Mô Tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblTonKho);

        btnHetHan.setText("Thuốc Hết Hạn");
        btnHetHan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHetHanActionPerformed(evt);
            }
        });

        jButton2.setText("Thuốc Còn Hạn");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("Xuất Danh Sách");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("Chọn File");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(btnHetHan))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHetHan)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5)
                        .addComponent(jButton7))
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thống Kê Thuốc Tồn Kho", jPanel2);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Search.png"))); // NOI18N
        jButton4.setText("Tìm");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Nhập Thuốc", "Mã Loại Thuốc", "Mã NCC", "Giá Nhập", "Số Lượng", "Ngày Nhập", "HSD", "Mô Tả", "Đơn Vị", "Quy Đổi"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(495, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Thống Kê Nhập Thuốc", jPanel3);

        javax.swing.GroupLayout pnlDoanhThuLayout = new javax.swing.GroupLayout(pnlDoanhThu);
        pnlDoanhThu.setLayout(pnlDoanhThuLayout);
        pnlDoanhThuLayout.setHorizontalGroup(
            pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlDoanhThuLayout.setVerticalGroup(
            pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 325, Short.MAX_VALUE)
        );

        cboNam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnl);
        pnl.setLayout(pnlLayout);
        pnlLayout.setHorizontalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlLayout.createSequentialGroup()
                        .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 724, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlLayout.setVerticalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Thống kê doanh thu", pnl);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHetHanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHetHanActionPerformed
        // TODO add your handling code here:
        thuocHetHan();
    }//GEN-LAST:event_btnHetHanActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        loadTonKho();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            // TODO add your handling code here:
            XuatTonKho();

        } catch (IOException ex) {
            Logger.getLogger(ThongKe.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            // TODO add your haandling code here:
            ReadFileTonKho();

        } catch (IOException ex) {
            Logger.getLogger(ThongKe.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked

    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void tblHoaDonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMousePressed
        if (evt.getClickCount() == 2) {
            row = tblHoaDon.getSelectedRow();
            HoaDonChiTiet();
            pnChiTietHD.setVisible(true);
            pnHoaDon.setVisible(false);
//            String maHD = (String) tblHoaDon.getValueAt(row, 1);
//            new ChiTietHoaDonJFrame(maHD).setVisible(true);
        }
    }//GEN-LAST:event_tblHoaDonMousePressed

    private void txtTimHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimHDActionPerformed

    private void txtTimHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimHDKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimHDKeyPressed

    private void txtTimHDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimHDKeyReleased
        // TODO add your handling code here:
        loadHoaDon();

    }//GEN-LAST:event_txtTimHDKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        loadHoaDon();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:
            xuatHD();

        } catch (IOException ex) {
            Logger.getLogger(ThongKe.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        pnChiTietHD.setVisible(false);
        pnHoaDon.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void cboNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNamActionPerformed
        try {
            String nam1 = (String) cboNam.getSelectedItem();
            int nam = Integer.parseInt(nam1);

            ThongKe.setDataToChart1(pnlDoanhThu, nam);
        } catch (Exception e) {
        }

    }//GEN-LAST:event_cboNamActionPerformed

    private void txtTimHD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimHD1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimHD1ActionPerformed

    private void txtTimHD1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimHD1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimHD1KeyPressed

    private void txtTimHD1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimHD1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimHD1KeyReleased
    int row = -1;

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
            java.util.logging.Logger.getLogger(ThongKe.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongKe.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongKe.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongKe.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThongKe dialog = new ThongKe(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnHetHan;
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel pnChiTietHD;
    private javax.swing.JPanel pnHoaDon;
    private javax.swing.JPanel pnl;
    private javax.swing.JPanel pnlDoanhThu;
    private javax.swing.JTable tblChiTietHD;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblTonKho;
    private javax.swing.JTextField txtTimHD;
    private javax.swing.JTextField txtTimHD1;
    // End of variables declaration//GEN-END:variables

    public static void setDataToChart1(JPanel jpnItem, int nam) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DoanhThuDAO daodoanhthu = new DoanhThuDAO();
        List<DoanhThu> list = daodoanhthu.selectDataChart(nam);

        for (int i = 1; i <= 12; i++) {
            boolean monthFound = false;

            for (DoanhThu doanhThu : list) {
                if (doanhThu.getThang() == i) {
                    dataset.addValue(doanhThu.getTongTien(), "Revenue", "" + doanhThu.getThang());
                    monthFound = true;
                    break;
                }
            }

            if (!monthFound) {
                dataset.addValue(0, "Revenue", "" + i);
            }
        }
        String title = "Biểu đồ thống kê doanh thu " + nam;
        JFreeChart barChart = ChartFactory.createBarChart(
                title.toUpperCase(),
                "Tháng", "Doanh thu (VND)",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(jpnItem.getWidth(), 321));

        jpnItem.removeAll();
        jpnItem.setLayout(new CardLayout());
        jpnItem.add(chartPanel);
        jpnItem.validate();
        jpnItem.repaint();
    }

}
