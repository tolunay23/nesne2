import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class HotelAppGUI extends JFrame {

    private final HotelService hotelService;

    // Odalar tablosu
    private DefaultTableModel roomsTableModel;
    private JTable roomsTable;

    // Rezervasyon tablosu
    private DefaultTableModel reservationsTableModel;
    private JTable reservationsTable;

    // Yeni rezervasyon form bileşenleri
    private JTextField tfName;
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JTextField tfCheckIn;
    private JTextField tfCheckOut;
    private JTextField tfRoomId;

    // Renk paleti
    private final Color BG_LIGHT = Color.decode("#EDE7C7");
    private final Color RED_MAIN = Color.decode("#8B0000");
    private final Color RED_DARK = Color.decode("#5B0202");
    private final Color TEXT_DARK = Color.decode("#200E01");

    public HotelAppGUI() {
        this.hotelService = DemoDataFactory.createDemoHotelService();
        initUI();
    }

    private void initUI() {
        setTitle("Otel Rezervasyon Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.setBackground(BG_LIGHT);

        JLabel title = new JLabel("Otel Rezervasyon Sistemi", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(RED_MAIN);
        title.setBorder(new EmptyBorder(10, 0, 10, 0));
        root.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.setBackground(BG_LIGHT);
        tabs.setForeground(TEXT_DARK);

        tabs.addTab("Odalar", createRoomsPanel());
        tabs.addTab("Rezervasyonlar", createReservationsPanel());
        tabs.addTab("Yeni Rezervasyon", createNewReservationPanel());

        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);
    }

    /* ================= ODALAR SEKME ================= */

    private JPanel createRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JLabel info = new JLabel(" Oteldeki mevcut odaları ve durumlarını görüntüleyin.");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setOpaque(true);
        info.setBackground(BG_LIGHT);
        info.setForeground(RED_DARK);
        info.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.add(info, BorderLayout.NORTH);

        roomsTableModel = new DefaultTableModel(
                new Object[]{"ID", "Oda No", "Tip", "Kapasite", "Fiyat (Gece)"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomsTable = new JTable(roomsTableModel);
        roomsTable.setRowHeight(24);
        roomsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = roomsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(BG_LIGHT);
        header.setForeground(RED_DARK);

        roomsTable.setBackground(new Color(0xF3EED5));
        roomsTable.setGridColor(RED_DARK);
        roomsTable.setSelectionBackground(RED_MAIN);
        roomsTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(roomsTable);
        scrollPane.getViewport().setBackground(new Color(0xF3EED5));
        scrollPane.setBorder(BorderFactory.createLineBorder(RED_DARK, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);

        JButton btnAll = new JButton("Tüm Odaları Listele");
        JButton btnAvailable = new JButton("Belirli Tarihte Boş Odalar");

        styleButton(btnAll);
        styleButton(btnAvailable);

        btnAll.addActionListener(e -> loadAllRooms());
        btnAvailable.addActionListener(e -> showAvailableRoomsDialog());

        bottom.add(btnAll);
        bottom.add(btnAvailable);

        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void loadAllRooms() {
        roomsTableModel.setRowCount(0);
        List<Room> rooms = hotelService.getAllRooms();
        for (Room r : rooms) {
            roomsTableModel.addRow(new Object[]{
                    r.getId(),
                    r.getNumber(),
                    r.getType(),
                    r.getCapacity(),
                    r.getPricePerNight()
            });
        }
    }

    private void showAvailableRoomsDialog() {
        JTextField tfIn = new JTextField("2025-01-01");
        JTextField tfOut = new JTextField("2025-01-02");

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBackground(BG_LIGHT);
        JLabel l1 = new JLabel("Giriş (YYYY-MM-DD):");
        JLabel l2 = new JLabel("Çıkış (YYYY-MM-DD):");
        l1.setForeground(TEXT_DARK);
        l2.setForeground(TEXT_DARK);
        panel.add(l1);
        panel.add(tfIn);
        panel.add(l2);
        panel.add(tfOut);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Tarih Aralığı Seç",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate in = LocalDate.parse(tfIn.getText().trim());
                LocalDate out = LocalDate.parse(tfOut.getText().trim());

                List<Room> available = hotelService.getAvailableRooms(in, out);
                if (available.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Bu tarihlerde boş oda yok.",
                            "Bilgi",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    roomsTableModel.setRowCount(0);
                    for (Room r : available) {
                        roomsTableModel.addRow(new Object[]{
                                r.getId(),
                                r.getNumber(),
                                r.getType(),
                                r.getCapacity(),
                                r.getPricePerNight()
                        });
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Tarih formatını doğru girin. Örnek: 2025-01-15",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /* ================= REZERVASYONLAR SEKME ================= */

    private JPanel createReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JLabel info = new JLabel(" Mevcut rezervasyonları görüntüleyin ve iptal edin.");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setOpaque(true);
        info.setBackground(BG_LIGHT);
        info.setForeground(RED_DARK);
        info.setBorder(new EmptyBorder(8, 8, 8, 8));
        panel.add(info, BorderLayout.NORTH);

        reservationsTableModel = new DefaultTableModel(
                new Object[]{"ID", "Oda No", "Tip", "Misafir", "Giriş", "Çıkış"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationsTable = new JTable(reservationsTableModel);
        reservationsTable.setRowHeight(24);
        reservationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = reservationsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(BG_LIGHT);
        header.setForeground(RED_DARK);

        reservationsTable.setBackground(new Color(0xF3EED5));
        reservationsTable.setGridColor(RED_DARK);
        reservationsTable.setSelectionBackground(RED_MAIN);
        reservationsTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        scrollPane.getViewport().setBackground(new Color(0xF3EED5));
        scrollPane.setBorder(BorderFactory.createLineBorder(RED_DARK, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(5, 0, 0, 0));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);

        JButton btnLoad = new JButton("Rezervasyonları Listele");
        JButton btnCancelSelected = new JButton("Seçili Rezervasyonu İptal Et");

        styleButton(btnLoad);
        styleButton(btnCancelSelected);

        btnLoad.addActionListener(e -> loadReservations());
        btnCancelSelected.addActionListener(e -> cancelSelectedReservation());

        buttons.add(btnLoad);
        buttons.add(btnCancelSelected);

        bottom.add(buttons, BorderLayout.EAST);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void loadReservations() {
        reservationsTableModel.setRowCount(0);
        List<Reservation> reservations = hotelService.getAllReservations();
        for (Reservation r : reservations) {
            reservationsTableModel.addRow(new Object[]{
                    r.getId(),
                    r.getRoom().getNumber(),
                    r.getRoom().getType(),
                    r.getGuest().toString(),
                    r.getCheckIn(),
                    r.getCheckOut()
            });
        }
    }

    private void cancelSelectedReservation() {
        int row = reservationsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen tablodan bir rezervasyon seçin.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) reservationsTableModel.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "ID " + id + " numaralı rezervasyon iptal edilsin mi?",
                "Onay",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = hotelService.cancelReservation(id);
            if (result) {
                JOptionPane.showMessageDialog(this,
                        "Rezervasyon iptal edildi.",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE);
                loadReservations();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Rezervasyon bulunamadı.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /* ================= YENİ REZERVASYON SEKME ================= */

    private JPanel createNewReservationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(true);
        formPanel.setBackground(BG_LIGHT);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(RED_MAIN, 2),
                " Rezervasyon Bilgileri ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                RED_MAIN
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0;
        gc.gridy = 0;

        tfName = new JTextField();
        tfPhone = new JTextField();
        tfEmail = new JTextField();
        tfCheckIn = new JTextField("2025-01-01");
        tfCheckOut = new JTextField("2025-01-02");
        tfRoomId = new JTextField();

        addFormRow(formPanel, gc, "Misafir Adı:", tfName);
        addFormRow(formPanel, gc, "Telefon:", tfPhone);
        addFormRow(formPanel, gc, "E-posta:", tfEmail);
        addFormRow(formPanel, gc, "Giriş Tarihi (YYYY-MM-DD):", tfCheckIn);
        addFormRow(formPanel, gc, "Çıkış Tarihi (YYYY-MM-DD):", tfCheckOut);
        addFormRow(formPanel, gc, "Oda ID:", tfRoomId);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton btnShowAvailable = new JButton("Bu Tarihlerde Boş Odaları Göster");
        JButton btnCreate = new JButton("Rezervasyon Oluştur");

        styleButton(btnShowAvailable);
        styleButton(btnCreate);

        btnShowAvailable.addActionListener(e -> showAvailableRoomsForFormDates());
        btnCreate.addActionListener(e -> handleCreateReservation());

        buttonPanel.add(btnShowAvailable);
        buttonPanel.add(btnCreate);

        gc.gridx = 0;
        gc.gridy++;
        gc.gridwidth = 2;
        formPanel.add(buttonPanel, gc);

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setWrapStyleWord(true);
        info.setLineWrap(true);
        info.setText("""
                Bu bölümden yeni bir rezervasyon oluşturabilirsiniz.
                
                1) Giriş ve çıkış tarihlerini girin.
                2) 'Bu Tarihlerde Boş Odaları Göster' ile uygun odaları görün.
                3) Seçtiğiniz oda ID'sini girip 'Rezervasyon Oluştur' butonuna basın.
                """);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setBorder(new EmptyBorder(10, 10, 10, 10));
        info.setBackground(BG_LIGHT);
        info.setForeground(TEXT_DARK);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(info, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                formPanel,
                rightPanel
        );
        splitPane.setResizeWeight(0.6);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private void addFormRow(JPanel formPanel, GridBagConstraints gc,
                            String labelText, JComponent field) {
        gc.gridx = 0;
        gc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_DARK);
        formPanel.add(label, gc);

        gc.gridx = 1;
        gc.weightx = 0.7;
        formPanel.add(field, gc);

        gc.gridy++;
    }

    private void showAvailableRoomsForFormDates() {
        try {
            LocalDate checkIn = LocalDate.parse(tfCheckIn.getText().trim());
            LocalDate checkOut = LocalDate.parse(tfCheckOut.getText().trim());

            List<Room> available = hotelService.getAvailableRooms(checkIn, checkOut);
            if (available.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Bu tarihlerde boş oda yok.",
                        "Bilgi",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Boş odalar:\n\n");
                for (Room r : available) {
                    sb.append("ID: ").append(r.getId())
                            .append(" | Oda: ").append(r.getNumber())
                            .append(" | Tip: ").append(r.getType())
                            .append(" | Kapasite: ").append(r.getCapacity())
                            .append(" | Fiyat: ").append(r.getPricePerNight())
                            .append("\n");
                }
                JOptionPane.showMessageDialog(this,
                        sb.toString(),
                        "Boş Odalar",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Tarih formatını doğru girin. Örnek: 2025-01-15",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreateReservation() {
        String name = tfName.getText().trim();
        String phone = tfPhone.getText().trim();
        String email = tfEmail.getText().trim();
        String roomIdStr = tfRoomId.getText().trim();
        String inStr = tfCheckIn.getText().trim();
        String outStr = tfCheckOut.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()
                || roomIdStr.isEmpty() || inStr.isEmpty() || outStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Lütfen tüm alanları doldurun.",
                    "Uyarı",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int roomId = Integer.parseInt(roomIdStr);
            LocalDate checkIn = LocalDate.parse(inStr);
            LocalDate checkOut = LocalDate.parse(outStr);

            Reservation res = hotelService.createReservation(
                    name, phone, email, roomId, checkIn, checkOut
            );

            JOptionPane.showMessageDialog(this,
                    "Rezervasyon oluşturuldu.\n\n" + res,
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);

            tfName.setText("");
            tfPhone.setText("");
            tfEmail.setText("");
            tfRoomId.setText("");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "Oda ID sayısal olmalı.",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Hata: " + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBackground(BG_LIGHT);
        button.setForeground(RED_MAIN);
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info :
                    UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            HotelAppGUI gui = new HotelAppGUI();
            gui.setVisible(true);
        });
    }
}
