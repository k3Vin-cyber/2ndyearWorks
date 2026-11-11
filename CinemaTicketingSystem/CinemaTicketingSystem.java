import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// ============================================================================
// OOP: ABSTRACTION VIA INTERFACE
// ============================================================================
interface Bookable {
    double calculateFinalPrice();
    String getSeatListString();
}

// ============================================================================
// OOP: INHERITANCE & ENCAPSULATION - PERSON & CUSTOMER CLASSES
// ============================================================================
abstract class Person {
    private final String id;
    private final String fullName;
    private final String contactNumber;

    public Person(String id, String fullName, String contactNumber) {
        this.id = id;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getContactNumber() { return contactNumber; }

    public abstract double getDiscountRate();
    public abstract String getCustomerCategory();
}

class Customer extends Person {
    private final String customerType;

    public Customer(String id, String fullName, String contactNumber, String customerType) {
        super(id, fullName, contactNumber);
        this.customerType = customerType;
    }

    @Override
    public double getDiscountRate() {
        // Structured Programming: Conditional discount logic
        if (customerType.startsWith("Student")) {
            return 0.15;
        } else if (customerType.startsWith("Senior")) {
            return 0.20;
        } else if (customerType.startsWith("VIP")) {
            return 0.10;
        }
        return 0.0;
    }

    @Override
    public String getCustomerCategory() {
        return customerType;
    }
}

// ============================================================================
// OOP + DSA (2D ARRAYS): MOVIE CLASS WITH 2D SEAT GRID [6 ROWS][10 COLS]
// ============================================================================
class Movie {
    public static final int ROWS = 6;
    public static final int COLS = 10;
    public static final int SEAT_AVAILABLE = 0;
    public static final int SEAT_SELECTED = 1;
    public static final int SEAT_RESERVED = 2;

    private final String movieId;
    private final String title;
    private final String studio;
    private final String cinemaHall;
    private final String showtime;
    private final String formatTag;
    private final double basePrice;
    private final String synopsis;
    private final Color accentColor;
    private final int[][] seatGrid;
    private final String[][] reservedByTicket;

    public Movie(String movieId, String title, String studio, String cinemaHall,
                 String showtime, String formatTag, double basePrice,
                 String synopsis, Color accentColor) {
        this.movieId = movieId;
        this.title = title;
        this.studio = studio;
        this.cinemaHall = cinemaHall;
        this.showtime = showtime;
        this.formatTag = formatTag;
        this.basePrice = basePrice;
        this.synopsis = synopsis;
        this.accentColor = accentColor;
        this.seatGrid = new int[ROWS][COLS];
        this.reservedByTicket = new String[ROWS][COLS];
    }

    public String getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getStudio() { return studio; }
    public String getCinemaHall() { return cinemaHall; }
    public String getShowtime() { return showtime; }
    public String getFormatTag() { return formatTag; }
    public double getBasePrice() { return basePrice; }
    public String getSynopsis() { return synopsis; }
    public Color getAccentColor() { return accentColor; }
    public int[][] getSeatGrid() { return seatGrid; }

    public static String getSeatCode(int row, int col) {
        return ((char) ('A' + row)) + String.valueOf(col + 1);
    }

    public static boolean isVipRow(int row) {
        return row >= 4; // Rows E & F are VIP Recliners (+PHP 60)
    }

    // Structured Programming: Nested loops over 2D array
    public int getAvailableSeatCount() {
        int count = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (seatGrid[r][c] == SEAT_AVAILABLE) {
                    count++;
                }
            }
        }
        return count;
    }

    public void clearTemporarySelections() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (seatGrid[r][c] == SEAT_SELECTED) {
                    seatGrid[r][c] = SEAT_AVAILABLE;
                }
            }
        }
    }

    public List<String> getSelectedSeatCodes() {
        List<String> list = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (seatGrid[r][c] == SEAT_SELECTED) {
                    list.add(getSeatCode(r, c));
                }
            }
        }
        return list;
    }

    public int countSelectedVipSeats() {
        int vip = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (seatGrid[r][c] == SEAT_SELECTED && isVipRow(r)) {
                    vip++;
                }
            }
        }
        return vip;
    }

    public void lockSelectedSeats(String ticketId) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (seatGrid[r][c] == SEAT_SELECTED) {
                    seatGrid[r][c] = SEAT_RESERVED;
                    reservedByTicket[r][c] = ticketId;
                }
            }
        }
    }

    public void markSeatReservedByCode(String code, String ticketId) {
        if (code == null || code.length() < 2) return;
        int r = code.charAt(0) - 'A';
        try {
            int c = Integer.parseInt(code.substring(1)) - 1;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
                seatGrid[r][c] = SEAT_RESERVED;
                reservedByTicket[r][c] = ticketId;
            }
        } catch (NumberFormatException ignored) {}
    }

    public void releaseSeatsByTicketId(String ticketId) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (ticketId.equalsIgnoreCase(reservedByTicket[r][c])) {
                    seatGrid[r][c] = SEAT_AVAILABLE;
                    reservedByTicket[r][c] = null;
                }
            }
        }
    }
}

// ============================================================================
// OOP: TICKET CLASS (Polymorphic Method Overloading & 1D Seat Array)
// ============================================================================
class Ticket implements Bookable {
    private final String ticketId;
    private final Customer customer;
    private final Movie movie;
    private final String[] seatNumbers;
    private final int vipSeatCount;
    private final double snackCost;
    private final String snackLabel;
    private final double totalPrice;
    private String status;
    private final String timestamp;

    public Ticket(String ticketId, Customer customer, Movie movie, String[] seatNumbers,
                  int vipSeatCount, double snackCost, String snackLabel, String status) {
        this.ticketId = ticketId;
        this.customer = customer;
        this.movie = movie;
        this.seatNumbers = seatNumbers;
        this.vipSeatCount = vipSeatCount;
        this.snackCost = snackCost;
        this.snackLabel = snackLabel;
        this.status = status;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
        this.totalPrice = calculateFinalPrice();
    }

    // Method Overloading 1
    public double computePrice(int count, double rate) {
        return count * rate;
    }

    // Method Overloading 2
    public double computePrice(int count, double rate, int vipCount, double discount, double snacks) {
        double seatSub = computePrice(count, rate) + (vipCount * 60.0);
        double discounted = seatSub - (seatSub * discount);
        return Math.max(0.0, discounted + snacks);
    }

    @Override
    public final double calculateFinalPrice() {
        return computePrice(seatNumbers.length, movie.getBasePrice(),
                vipSeatCount, customer.getDiscountRate(), snackCost);
    }

    @Override
    public String getSeatListString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seatNumbers.length; i++) {
            sb.append(seatNumbers[i]);
            if (i < seatNumbers.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public String getTicketId() { return ticketId; }
    public Customer getCustomer() { return customer; }
    public Movie getMovie() { return movie; }
    public String getSnackLabel() { return snackLabel; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTimestamp() { return timestamp; }
}

// ============================================================================
// DSA 2: FIFO QUEUE DATA STRUCTURE (First-In, First-Out Box Office Line)
// ============================================================================
class QueueNode {
    Ticket ticket;
    QueueNode next;

    public QueueNode(Ticket ticket) {
        this.ticket = ticket;
    }
}

class ReservationQueue {
    private QueueNode front;
    private QueueNode rear;
    private int size;

    public void enqueue(Ticket ticket) {
        QueueNode node = new QueueNode(ticket);
        if (rear == null) {
            front = node;
            rear = node;
        } else {
            rear.next = node;
            rear = node;
        }
        size++;
    }

    public Ticket dequeue() {
        if (isEmpty()) return null;
        Ticket removed = front.ticket;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return removed;
    }

    public boolean isEmpty() { return front == null; }
    public int getSize() { return size; }

    public Ticket[] toArray() {
        Ticket[] arr = new Ticket[size];
        QueueNode curr = front;
        int i = 0;
        while (curr != null) {
            arr[i++] = curr.ticket;
            curr = curr.next;
        }
        return arr;
    }
}

// ============================================================================
// DSA 3: QUICK SORT ALGORITHM FOR BOX OFFICE RECORDS
// ============================================================================
class TicketQuickSorter {
    public static void sortByPriceDesc(Ticket[] arr, int low, int high) {
        if (low < high) {
            int p = partitionPrice(arr, low, high);
            sortByPriceDesc(arr, low, p - 1);
            sortByPriceDesc(arr, p + 1, high);
        }
    }

    private static int partitionPrice(Ticket[] arr, int low, int high) {
        Ticket pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j].getTotalPrice() >= pivot.getTotalPrice()) {
                i++;
                Ticket tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }
        Ticket tmp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = tmp;
        return i + 1;
    }

    public static void sortByNameAsc(Ticket[] arr, int low, int high) {
        if (low < high) {
            int p = partitionName(arr, low, high);
            sortByNameAsc(arr, low, p - 1);
            sortByNameAsc(arr, p + 1, high);
        }
    }

    private static int partitionName(Ticket[] arr, int low, int high) {
        Ticket pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j].getCustomer().getFullName().compareToIgnoreCase(pivot.getCustomer().getFullName()) <= 0) {
                i++;
                Ticket tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
        }
        Ticket tmp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = tmp;
        return i + 1;
    }
}

// ============================================================================
// XAMPP MYSQL / MARIADB MANAGER + AUTO WEB PORTAL GENERATOR
// ============================================================================
class CinemaDatabaseManager {
    private static final String USER = "root";
    private static final String PASS = "";
    private static final String HTDOCS_PATH = "C:\\xampp\\htdocs\\cinema_ticketing";
    private static boolean connected = false;
    private static String statusMessage = "Offline Mode (Start XAMPP)";

    public static boolean isConnected() { return connected; }
    public static String getStatusMessage() { return statusMessage; }

    private static Connection getJdbcConnection(String db) throws SQLException {
        String suffix = (db == null || db.isEmpty()) ? "" : "/" + db;
        try {
            return DriverManager.getConnection("jdbc:mariadb://localhost:3306" + suffix, USER, PASS);
        } catch (SQLException e) {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306" + suffix, USER, PASS);
        }
    }

    public static void initializeAndSync(Movie[] movies, List<Ticket> tickets) {
        connected = false;
        // 1. Direct JDBC Attempt
        try (Connection conn = getJdbcConnection("");
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS cinema_ticketing_db");
            stmt.executeUpdate("USE cinema_ticketing_db");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS studio_movies ("
                    + "movie_id VARCHAR(15) PRIMARY KEY, "
                    + "studio VARCHAR(50) NOT NULL, "
                    + "title VARCHAR(100) NOT NULL, "
                    + "hall_name VARCHAR(40) NOT NULL, "
                    + "showtime VARCHAR(30) NOT NULL, "
                    + "base_price DOUBLE NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticket_reservations ("
                    + "ticket_id VARCHAR(20) PRIMARY KEY, "
                    + "customer_name VARCHAR(80) NOT NULL, "
                    + "customer_phone VARCHAR(30) NOT NULL, "
                    + "customer_type VARCHAR(40) NOT NULL, "
                    + "studio VARCHAR(50) NOT NULL, "
                    + "movie_title VARCHAR(100) NOT NULL, "
                    + "seats VARCHAR(120) NOT NULL, "
                    + "snack_addon VARCHAR(60) NOT NULL, "
                    + "total_price DOUBLE NOT NULL, "
                    + "status VARCHAR(25) NOT NULL, "
                    + "booked_at VARCHAR(40) NOT NULL)");

            String insertMovie = "INSERT IGNORE INTO studio_movies VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertMovie)) {
                for (Movie m : movies) {
                    ps.setString(1, m.getMovieId());
                    ps.setString(2, m.getStudio());
                    ps.setString(3, m.getTitle());
                    ps.setString(4, m.getCinemaHall());
                    ps.setString(5, m.getShowtime());
                    ps.setDouble(6, m.getBasePrice());
                    ps.executeUpdate();
                }
            }

            for (Ticket t : tickets) {
                saveTicketJdbc(t);
            }

            connected = true;
            statusMessage = "XAMPP Connected (JDBC :3306)";
            System.out.println("Database connected successfully via JDBC!");
        } catch (SQLException ignored) {}

        // 2. XAMPP Apache + PHP Bridge (Works automatically without extra JAR config)
        try {
            Path folder = Paths.get(HTDOCS_PATH);
            Files.createDirectories(folder);
            String phpPortal = buildPortalPhp(movies, tickets);
            Files.write(Paths.get(HTDOCS_PATH, "index.php"), phpPortal.getBytes(StandardCharsets.UTF_8));

            HttpURLConnection http = (HttpURLConnection) URI.create("http://localhost/cinema_ticketing/index.php?sync=1").toURL().openConnection();
            http.setConnectTimeout(1500);
            http.setReadTimeout(1500);
            if (http.getResponseCode() == 200) {
                connected = true;
                if (!statusMessage.contains("JDBC")) {
                    statusMessage = "XAMPP MySQL Active (localhost)";
                }
                System.out.println("Synced with XAMPP MySQL at http://localhost/cinema_ticketing/");
            }
        } catch (Exception e) {
            if (!connected) {
                statusMessage = "Local Memory Mode (Start XAMPP)";
            }
        }
    }

    private static boolean saveTicketJdbc(Ticket t) {
        String sql = "INSERT INTO ticket_reservations VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE status=VALUES(status), total_price=VALUES(total_price)";
        try (Connection conn = getJdbcConnection("cinema_ticketing_db");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTicketId());
            ps.setString(2, t.getCustomer().getFullName());
            ps.setString(3, t.getCustomer().getContactNumber());
            ps.setString(4, t.getCustomer().getCustomerCategory());
            ps.setString(5, t.getMovie().getStudio());
            ps.setString(6, t.getMovie().getTitle());
            ps.setString(7, t.getSeatListString());
            ps.setString(8, t.getSnackLabel());
            ps.setDouble(9, t.getTotalPrice());
            ps.setString(10, t.getStatus());
            ps.setString(11, t.getTimestamp());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void saveTicket(Ticket t, Movie[] movies, List<Ticket> allTickets) {
        if (saveTicketJdbc(t)) {
            connected = true;
        }
        try {
            Path folder = Paths.get(HTDOCS_PATH);
            Files.createDirectories(folder);
            Files.write(Paths.get(HTDOCS_PATH, "index.php"),
                    buildPortalPhp(movies, allTickets).getBytes(StandardCharsets.UTF_8));

            HttpURLConnection http = (HttpURLConnection) URI.create("http://localhost/cinema_ticketing/index.php?sync=1").toURL().openConnection();
            http.setConnectTimeout(1200);
            if (http.getResponseCode() == 200) {
                connected = true;
                statusMessage = "XAMPP MySQL Active (localhost)";
            }
        } catch (Exception ignored) {}
    }

    public static void deleteTicket(String ticketId, Movie[] movies, List<Ticket> allTickets) {
        try (Connection conn = getJdbcConnection("cinema_ticketing_db");
             PreparedStatement ps = conn.prepareStatement("DELETE FROM ticket_reservations WHERE ticket_id=?")) {
            ps.setString(1, ticketId);
            ps.executeUpdate();
        } catch (SQLException ignored) {}

        try {
            Files.write(Paths.get(HTDOCS_PATH, "index.php"),
                    buildPortalPhp(movies, allTickets).getBytes(StandardCharsets.UTF_8));
            HttpURLConnection http = (HttpURLConnection) URI.create(
                    "http://localhost/cinema_ticketing/index.php?delete=" + URLEncoder.encode(ticketId, StandardCharsets.UTF_8)
            ).toURL().openConnection();
            http.setConnectTimeout(1200);
            try (OutputStream os = http.getOutputStream()) {
                os.flush();
            } catch (Exception ignored) {}
            http.getResponseCode();
        } catch (Exception ignored) {}
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("'", "\\'");
    }

    private static String buildPortalPhp(Movie[] movies, List<Ticket> tickets) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?php\n");
        sb.append("$conn = @new mysqli('localhost', 'root', '');\n");
        sb.append("if ($conn->connect_error) { die('<h2 style=\"font-family:sans-serif;color:#ef4444;padding:30px;\">Please start MySQL in XAMPP Control Panel!</h2>'); }\n");
        sb.append("$conn->query(\"CREATE DATABASE IF NOT EXISTS cinema_ticketing_db CHARACTER SET utf8mb4\");\n");
        sb.append("$conn->select_db('cinema_ticketing_db');\n");
        sb.append("$conn->query(\"CREATE TABLE IF NOT EXISTS studio_movies (movie_id VARCHAR(15) PRIMARY KEY, studio VARCHAR(50), title VARCHAR(100), hall_name VARCHAR(40), showtime VARCHAR(30), base_price DOUBLE)\");\n");
        sb.append("$conn->query(\"CREATE TABLE IF NOT EXISTS ticket_reservations (ticket_id VARCHAR(20) PRIMARY KEY, customer_name VARCHAR(80), customer_phone VARCHAR(30), customer_type VARCHAR(40), studio VARCHAR(50), movie_title VARCHAR(100), seats VARCHAR(120), snack_addon VARCHAR(60), total_price DOUBLE, status VARCHAR(25), booked_at VARCHAR(40))\");\n");

        for (Movie m : movies) {
            sb.append(String.format(
                "$conn->query(\"INSERT IGNORE INTO studio_movies VALUES ('%s','%s','%s','%s','%s',%.2f)\");\n",
                esc(m.getMovieId()), esc(m.getStudio()), esc(m.getTitle()),
                esc(m.getCinemaHall()), esc(m.getShowtime()), m.getBasePrice()
            ));
        }

        for (Ticket t : tickets) {
            sb.append(String.format(
                "$conn->query(\"INSERT INTO ticket_reservations VALUES ('%s','%s','%s','%s','%s','%s','%s','%s',%.2f,'%s','%s') ON DUPLICATE KEY UPDATE status='%s', total_price=%.2f\");\n",
                esc(t.getTicketId()), esc(t.getCustomer().getFullName()), esc(t.getCustomer().getContactNumber()),
                esc(t.getCustomer().getCustomerCategory()), esc(t.getMovie().getStudio()), esc(t.getMovie().getTitle()),
                esc(t.getSeatListString()), esc(t.getSnackLabel()), t.getTotalPrice(),
                esc(t.getStatus()), esc(t.getTimestamp()), esc(t.getStatus()), t.getTotalPrice()
            ));
        }

        sb.append("if (isset($_GET['delete'])) { $d = $conn->real_escape_string($_GET['delete']); $conn->query(\"DELETE FROM ticket_reservations WHERE ticket_id='$d'\"); }\n");
        sb.append("$res = $conn->query(\"SELECT * FROM ticket_reservations ORDER BY ticket_id DESC\");\n");
        sb.append("?>\n");
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>CineVerse IMAX - XAMPP MySQL Portal</title>\n");
        sb.append("<style>body{font-family:'Segoe UI',sans-serif;background:#0b0f19;color:#f8fafc;margin:0;}\n");
        sb.append("header{background:#111827;padding:22px 40px;border-bottom:3px solid #6366f1;display:flex;justify-content:space-between;align-items:center;}\n");
        sb.append(".wrap{max-width:1200px;margin:30px auto;padding:0 24px;}\n");
        sb.append("table{width:100%;border-collapse:collapse;background:#1e293b;border-radius:10px;overflow:hidden;}\n");
        sb.append("th{background:#0f172a;color:#818cf8;padding:14px;text-align:left;font-size:12px;text-transform:uppercase;}\n");
        sb.append("td{padding:12px 14px;border-bottom:1px solid #334155;font-size:14px;}\n");
        sb.append(".badge{background:#10b981;color:#052e16;padding:4px 10px;border-radius:99px;font-weight:bold;font-size:11px;}</style></head><body>\n");
        sb.append("<header><div><h1 style='margin:0;color:#fff;font-size:22px;'>CINEVERSE IMAX • ANIMATION STUDIO BOX OFFICE</h1>");
        sb.append("<div style='color:#94a3b8;font-size:13px;margin-top:4px;'>XAMPP MySQL Database: <b>cinema_ticketing_db</b> • Live Synced with Java Swing</div></div></header>\n");
        sb.append("<div class='wrap'><h2>Confirmed Ticket Reservations</h2><table><tr><th>Ticket ID</th><th>Customer</th><th>Contact</th><th>Tier</th><th>Animation Studio</th><th>Movie Title</th><th>Seats</th><th>Total Paid</th><th>Status</th></tr>\n");
        sb.append("<?php while($r = $res->fetch_assoc()): ?>\n");
        sb.append("<tr><td><b><?= htmlspecialchars($r['ticket_id']) ?></b></td><td><?= htmlspecialchars($r['customer_name']) ?></td><td><?= htmlspecialchars($r['customer_phone']) ?></td><td><?= htmlspecialchars($r['customer_type']) ?></td><td style='color:#38bdf8;font-weight:bold;'><?= htmlspecialchars($r['studio']) ?></td><td><?= htmlspecialchars($r['movie_title']) ?></td><td style='color:#fbbf24;font-weight:bold;'><?= htmlspecialchars($r['seats']) ?></td><td>PHP <?= number_format($r['total_price'], 2) ?></td><td><span class='badge'><?= htmlspecialchars($r['status']) ?></span></td></tr>\n");
        sb.append("<?php endwhile; ?></table></div></body></html>");
        return sb.toString();
    }
}

// ============================================================================
// 60 FPS PARTICLE ANIMATION ENGINE
// ============================================================================
class CinemaParticle {
    double x, y, vx, vy, size, alpha;
    Color color;
    boolean burst;

    public CinemaParticle(double x, double y, double vx, double vy, double size, Color color, boolean burst) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.color = color;
        this.alpha = 1.0;
        this.burst = burst;
    }

    public boolean update(int maxW, int maxH) {
        x += vx;
        y += vy;
        if (burst) {
            vy += 0.16;
            alpha -= 0.024;
            return alpha > 0;
        } else {
            if (y < 0) {
                y = Math.max(100, maxH);
                x = Math.random() * Math.max(100, maxW);
            }
            return true;
        }
    }
}

// ============================================================================
// MAIN MODERN DESKTOP APPLICATION (Sidebar Layout + 60 FPS Canvas + Clean UI)
// ============================================================================
@SuppressWarnings("serial")
public class CinemaTicketingSystem extends JFrame {

    private static final String[] STUDIO_FILTERS = {
        "All Studios", "Disney", "DreamWorks Animation", "Pixar",
        "Illumination", "Sony Pictures Animation", "Warner Bros. Animation",
        "Studio Ghibli", "Paramount Animation"
    };

    // DSA 1: 1D Array of 8 Animation Studio Movies
    private final Movie[] movieCatalog = new Movie[8];

    // DSA 2: FIFO Queue for Box Office Customer Line
    private final ReservationQueue boxOfficeQueue = new ReservationQueue();

    private final List<Ticket> confirmedTickets = new ArrayList<>();

    private Movie selectedMovie;
    private int ticketCounter = 1005;
    private String activeStudioFilter = "All Studios";

    // Layout & Navigation
    private final CardLayout contentCardLayout = new CardLayout();
    private final JPanel mainStagePanel = new JPanel(contentCardLayout);
    private JButton navCatalogBtn, navSeatMapBtn, navQueueBtn, navRecordsBtn;

    // Top KPI Metric Labels
    private JLabel kpiSelectedMovieLabel;
    private JLabel kpiAvailableSeatsLabel;
    private JLabel kpiQueueCountLabel;
    private JLabel kpiTotalRevenueLabel;
    private JButton xamppStatusPillBtn;

    // View 1: Studio Catalog Canvas
    private StudioShowcaseCanvas showcaseCanvas;

    // View 2: Interactive 2D Seat Map & Checkout Sidebar
    private AuditoriumSeatCanvas seatCanvas;
    private JComboBox<String> auditoriumCombo;
    private JTextField custNameField;
    private JTextField custPhoneField;
    private JComboBox<String> custDiscountCombo;
    private JComboBox<String> snackCombo;
    private JLabel liveReceiptLabel;
    private BoardingPassPreviewPanel boardingPassPanel;

    // View 3: FIFO Queue Visualizer & Table
    private QueuePipelineCanvas queueCanvas;
    private DefaultTableModel queueTableModel;

    // View 4: Box Office Records Table (Clean Light Dashboard Style)
    private DefaultTableModel recordsTableModel;
    private JTable recordsTable;
    private JTextField searchField;

    // Modern UI Color Palette
    private static final Color SIDEBAR_BG = new Color(13, 18, 30);
    private static final Color STAGE_DARK = new Color(17, 24, 39);
    private static final Color CARD_SURFACE = new Color(30, 41, 59);
    private static final Color BORDER_SUBTLE = new Color(51, 65, 85);
    private static final Color ACCENT_INDIGO = new Color(99, 102, 241);
    private static final Color ACCENT_GOLD = new Color(245, 158, 11);
    private static final Color ACCENT_EMERALD = new Color(16, 185, 129);
    private static final Color TEXT_MUTED = new Color(148, 163, 184);

    public CinemaTicketingSystem() {
        seedStudioMovies();
        selectedMovie = movieCatalog[0];
        seedInitialData();
        CinemaDatabaseManager.initializeAndSync(movieCatalog, confirmedTickets);

        initModernDesktopUI();
        refreshAllMetricsAndTables();
        start60FpsLoop();
    }

    private void seedStudioMovies() {
        movieCatalog[0] = new Movie(
                "MOV-DIS01", "Zootopia 2", "Disney",
                "Hall 1 • IMAX Laser", "01:30 PM", "IMAX 3D", 340.0,
                "Detectives Judy Hopps and Nick Wilde go undercover in Marsh Market to solve a reptile mystery.",
                new Color(56, 189, 248)
        );
        movieCatalog[1] = new Movie(
                "MOV-DRW02", "The Wild Robot", "DreamWorks Animation",
                "Hall 2 • Dolby Atmos", "03:00 PM", "Dolby 7.1", 350.0,
                "Shipwrecked robot ROZZUM 7134 bonds with an orphaned gosling and protects a wild island.",
                new Color(52, 211, 153)
        );
        movieCatalog[2] = new Movie(
                "MOV-PIX03", "Inside Out: Core Memories", "Pixar",
                "Hall 3 • Director's Club", "04:15 PM", "VIP 2D", 330.0,
                "Joy, Sadness, and Anxiety navigate Headquarters as Riley enters a high-stakes championship.",
                new Color(251, 191, 36)
        );
        movieCatalog[3] = new Movie(
                "MOV-ILL04", "Super Mario Galaxy Movie", "Illumination",
                "Hall 4 • 4DX Motion", "05:45 PM", "4DX 3D", 380.0,
                "Mario, Luigi, and Peach launch across cosmic observatories to defend the Power Stars.",
                new Color(250, 204, 21)
        );
        movieCatalog[4] = new Movie(
                "MOV-SNY05", "Beyond the Spider-Verse", "Sony Pictures Animation",
                "Hall 5 • IMAX Grand", "07:00 PM", "IMAX 3D", 400.0,
                "Miles Morales and Gwen Stacy race across colliding multiverse dimensions to save Brooklyn.",
                new Color(244, 63, 94)
        );
        movieCatalog[5] = new Movie(
                "MOV-WBA06", "Batman: Gotham by Gaslight", "Warner Bros. Animation",
                "Hall 6 • Noir Screen", "08:15 PM", "2D Digital", 320.0,
                "A steampunk Dark Knight stalks a phantom syndicate across fog-covered Victorian Gotham.",
                new Color(168, 85, 247)
        );
        movieCatalog[6] = new Movie(
                "MOV-GHB07", "Spirited Away (4K Remaster)", "Studio Ghibli",
                "Hall 7 • Heritage Theater", "06:30 PM", "4K IMAX", 310.0,
                "Chihiro journeys through a luminous spirit bathhouse in Hayao Miyazaki's timeless classic.",
                new Color(45, 212, 191)
        );
        movieCatalog[7] = new Movie(
                "MOV-PAR08", "Transformers One", "Paramount Animation",
                "Hall 8 • IMAX Laser", "09:00 PM", "IMAX 3D", 370.0,
                "The untold origin story of Orion Pax and D-16 before they became Optimus Prime and Megatron.",
                new Color(96, 165, 250)
        );
    }

    private void seedInitialData() {
        Customer c1 = new Customer("CUST-101", "Kevin Bryan Galgo", "0917-555-0192", "Student (15% Off)");
        movieCatalog[4].markSeatReservedByCode("E5", "TKT-1001");
        movieCatalog[4].markSeatReservedByCode("E6", "TKT-1001");
        confirmedTickets.add(new Ticket("TKT-1001", c1, movieCatalog[4], new String[]{"E5", "E6"},
                2, 150.0, "Caramel Popcorn + 2 Sodas", "CONFIRMED"));

        Customer c2 = new Customer("CUST-102", "Coline Daanton", "0918-555-0241", "Student (15% Off)");
        movieCatalog[6].markSeatReservedByCode("C4", "TKT-1002");
        movieCatalog[6].markSeatReservedByCode("C5", "TKT-1002");
        confirmedTickets.add(new Ticket("TKT-1002", c2, movieCatalog[6], new String[]{"C4", "C5"},
                0, 95.0, "Butter Popcorn + Iced Tea", "CONFIRMED"));

        Customer c3 = new Customer("CUST-103", "Star Defino", "0998-555-0811", "VIP Club (10% Off)");
        movieCatalog[0].markSeatReservedByCode("F4", "TKT-1003");
        movieCatalog[0].markSeatReservedByCode("F5", "TKT-1003");
        confirmedTickets.add(new Ticket("TKT-1003", c3, movieCatalog[0], new String[]{"F4", "F5"},
                2, 220.0, "Director's Nacho Combo", "CONFIRMED"));

        Customer q1 = new Customer("CUST-104", "Reinier Mayor", "0922-555-0933", "Regular (0% Off)");
        movieCatalog[3].markSeatReservedByCode("D5", "TKT-1004");
        movieCatalog[3].markSeatReservedByCode("D6", "TKT-1004");
        boxOfficeQueue.enqueue(new Ticket("TKT-1004", q1, movieCatalog[3], new String[]{"D5", "D6"},
                0, 95.0, "Butter Popcorn + Iced Tea", "IN QUEUE"));
    }

    // ========================================================================
    // MODERN SIDEBAR + TOP KPI HEADER ARCHITECTURE (Zero Overlapping!)
    // ========================================================================
    private void initModernDesktopUI() {
        setTitle("CineVerse IMAX — Animation Studio Ticketing System");
        setSize(1260, 800);
        setMinimumSize(new Dimension(1100, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. SLEEK LEFT NAVIGATION SIDEBAR (235px wide)
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(235, 800));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_SUBTLE));

        // Brand Logo Box at Top of Sidebar
        JPanel brandBox = new JPanel();
        brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));
        brandBox.setOpaque(false);
        brandBox.setBorder(new EmptyBorder(24, 20, 24, 20));

        JLabel brandTitle = new JLabel("CINEVERSE IMAX");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandTitle.setForeground(Color.WHITE);

        JLabel brandSub = new JLabel("8 Animation Studios CMS");
        brandSub.setFont(new Font("Segoe UI", Font.BOLD, 12));
        brandSub.setForeground(ACCENT_GOLD);

        JLabel rubricTag = new JLabel("Arrays • FIFO Queue • OOP");
        rubricTag.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rubricTag.setForeground(TEXT_MUTED);

        brandBox.add(brandTitle);
        brandBox.add(Box.createVerticalStrut(4));
        brandBox.add(brandSub);
        brandBox.add(Box.createVerticalStrut(2));
        brandBox.add(rubricTag);

        // Sidebar Navigation Buttons
        JPanel navList = new JPanel(new GridLayout(4, 1, 0, 10));
        navList.setOpaque(false);
        navList.setBorder(new EmptyBorder(10, 14, 10, 14));

        navCatalogBtn = createSidebarButton("01   Studio Showcase", "CATALOG", true);
        navSeatMapBtn = createSidebarButton("02   Auditorium & Seats", "SEATS", false);
        navQueueBtn = createSidebarButton("03   FIFO Ticket Queue", "QUEUE", false);
        navRecordsBtn = createSidebarButton("04   Box Office & XAMPP", "RECORDS", false);

        navList.add(navCatalogBtn);
        navList.add(navSeatMapBtn);
        navList.add(navQueueBtn);
        navList.add(navRecordsBtn);

        JPanel navTopWrapper = new JPanel(new BorderLayout());
        navTopWrapper.setOpaque(false);
        navTopWrapper.add(brandBox, BorderLayout.NORTH);
        navTopWrapper.add(navList, BorderLayout.CENTER);

        // Sidebar Bottom Quick-XAMPP Box
        JPanel sidebarFooter = new JPanel(new GridLayout(2, 1, 0, 8));
        sidebarFooter.setOpaque(false);
        sidebarFooter.setBorder(new EmptyBorder(16, 14, 20, 14));

        JButton quickSyncBtn = new JButton("Sync XAMPP MySQL");
        quickSyncBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        quickSyncBtn.setBackground(new Color(30, 41, 59));
        quickSyncBtn.setForeground(Color.WHITE);
        quickSyncBtn.setFocusPainted(false);
        quickSyncBtn.setBorder(new EmptyBorder(9, 12, 9, 12));
        quickSyncBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quickSyncBtn.addActionListener(e -> {
            CinemaDatabaseManager.initializeAndSync(movieCatalog, confirmedTickets);
            refreshAllMetricsAndTables();
            JOptionPane.showMessageDialog(this,
                    "XAMPP Sync Complete!\nStatus: " + CinemaDatabaseManager.getStatusMessage()
                    + "\nDatabase: cinema_ticketing_db",
                    "XAMPP MySQL Status", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton openWebBtn = new JButton("Open Web Box Office");
        openWebBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        openWebBtn.setBackground(ACCENT_INDIGO);
        openWebBtn.setForeground(Color.WHITE);
        openWebBtn.setFocusPainted(false);
        openWebBtn.setBorder(new EmptyBorder(9, 12, 9, 12));
        openWebBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openWebBtn.addActionListener(e -> openLocalhostPortal());

        sidebarFooter.add(quickSyncBtn);
        sidebarFooter.add(openWebBtn);

        sidebar.add(navTopWrapper, BorderLayout.NORTH);
        sidebar.add(sidebarFooter, BorderLayout.SOUTH);
        add(sidebar, BorderLayout.WEST);

        // 2. RIGHT MAIN AREA (Top Metrics Bar + 4 Card Views)
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setBackground(STAGE_DARK);

        rightContainer.add(buildTopMetricsHeader(), BorderLayout.NORTH);

        mainStagePanel.setBackground(STAGE_DARK);
        mainStagePanel.add(buildStudioCatalogView(), "CATALOG");
        mainStagePanel.add(buildSeatReservationView(), "SEATS");
        mainStagePanel.add(buildFifoQueueView(), "QUEUE");
        mainStagePanel.add(buildRecordsDatabaseView(), "RECORDS");

        rightContainer.add(mainStagePanel, BorderLayout.CENTER);
        add(rightContainer, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text, String cardKey, boolean active) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 16, 12, 16));
        styleSidebarButton(btn, active);
        btn.addActionListener(e -> switchView(cardKey));
        return btn;
    }

    private void switchView(String cardKey) {
        contentCardLayout.show(mainStagePanel, cardKey);
        styleSidebarButton(navCatalogBtn, cardKey.equals("CATALOG"));
        styleSidebarButton(navSeatMapBtn, cardKey.equals("SEATS"));
        styleSidebarButton(navQueueBtn, cardKey.equals("QUEUE"));
        styleSidebarButton(navRecordsBtn, cardKey.equals("RECORDS"));
        refreshAllMetricsAndTables();
    }

    private void styleSidebarButton(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(ACCENT_INDIGO);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(22, 30, 46));
            btn.setForeground(new Color(203, 213, 225));
        }
    }

    // ========================================================================
    // TOP KPI METRICS BAR (Clean, Spacious, Never Overlaps!)
    // ========================================================================
    private JPanel buildTopMetricsHeader() {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setBackground(new Color(15, 23, 42));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_SUBTLE),
                new EmptyBorder(12, 24, 12, 24)
        ));

        JPanel metricsGrid = new JPanel(new GridLayout(1, 4, 14, 0));
        metricsGrid.setOpaque(false);

        kpiSelectedMovieLabel = createMetricPill("ACTIVE AUDITORIUM", "Zootopia 2 (Disney)", new Color(56, 189, 248));
        kpiAvailableSeatsLabel = createMetricPill("2D ARRAY SEATS", "58 / 60 Available", ACCENT_EMERALD);
        kpiQueueCountLabel = createMetricPill("FIFO QUEUE LINE", "1 Customer Waiting", ACCENT_GOLD);
        kpiTotalRevenueLabel = createMetricPill("BOX OFFICE REVENUE", "PHP 2,340.00", new Color(168, 85, 247));

        metricsGrid.add(kpiSelectedMovieLabel);
        metricsGrid.add(kpiAvailableSeatsLabel);
        metricsGrid.add(kpiQueueCountLabel);
        metricsGrid.add(kpiTotalRevenueLabel);

        xamppStatusPillBtn = new JButton(CinemaDatabaseManager.getStatusMessage());
        xamppStatusPillBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        xamppStatusPillBtn.setFocusPainted(false);
        xamppStatusPillBtn.setBorder(new EmptyBorder(8, 14, 8, 14));
        xamppStatusPillBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        xamppStatusPillBtn.addActionListener(e -> {
            CinemaDatabaseManager.initializeAndSync(movieCatalog, confirmedTickets);
            refreshAllMetricsAndTables();
        });

        bar.add(metricsGrid, BorderLayout.CENTER);
        bar.add(xamppStatusPillBtn, BorderLayout.EAST);
        return bar;
    }

    private JLabel createMetricPill(String title, String val, Color accent) {
        JLabel lbl = new JLabel();
        lbl.setOpaque(true);
        lbl.setBackground(new Color(24, 33, 52));
        lbl.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(6, 12, 6, 12)
        ));
        updateMetricPillText(lbl, title, val, accent);
        return lbl;
    }

    private void updateMetricPillText(JLabel lbl, String title, String val, Color accent) {
        String hex = String.format("#%02x%02x%02x", accent.getRed(), accent.getGreen(), accent.getBlue());
        lbl.setText("<html><font size='2' color='#94A3B8'><b>" + title + "</b></font><br>"
                + "<font size='3' color='" + hex + "'><b>" + val + "</b></font></html>");
    }

    // ========================================================================
    // VIEW 1: 8 ANIMATION STUDIOS CATALOG + 60 FPS FEATURE HERO BANNER
    // ========================================================================
    private JPanel buildStudioCatalogView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(STAGE_DARK);

        // Clean Studio Filter Bar
        JPanel filterStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        filterStrip.setBackground(new Color(20, 28, 44));
        filterStrip.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_SUBTLE));

        JLabel filterLbl = new JLabel("  STUDIO CATEGORY:");
        filterLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        filterLbl.setForeground(TEXT_MUTED);
        filterStrip.add(filterLbl);

        for (String studio : STUDIO_FILTERS) {
            JButton chip = new JButton(studio);
            chip.setFont(new Font("Segoe UI", Font.BOLD, 11));
            chip.setFocusPainted(false);
            chip.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chip.setBorder(new EmptyBorder(5, 11, 5, 11));
            boolean isAll = studio.equals("All Studios");
            chip.setBackground(isAll ? ACCENT_GOLD : new Color(30, 41, 59));
            chip.setForeground(isAll ? Color.BLACK : new Color(226, 232, 240));

            chip.addActionListener(e -> {
                activeStudioFilter = studio;
                for (Component c : filterStrip.getComponents()) {
                    if (c instanceof JButton) {
                        c.setBackground(new Color(30, 41, 59));
                        c.setForeground(new Color(226, 232, 240));
                    }
                }
                chip.setBackground(ACCENT_GOLD);
                chip.setForeground(Color.BLACK);
                showcaseCanvas.repaint();
            });
            filterStrip.add(chip);
        }

        panel.add(filterStrip, BorderLayout.NORTH);

        showcaseCanvas = new StudioShowcaseCanvas();
        panel.add(showcaseCanvas, BorderLayout.CENTER);
        return panel;
    }

    // ========================================================================
    // VIEW 2: SLEEK 2D AUDITORIUM SEAT MAP + MODERN CHECKOUT SIDEBAR
    // ========================================================================
    private JPanel buildSeatReservationView() {
        JPanel root = new JPanel(new BorderLayout(16, 0));
        root.setBackground(STAGE_DARK);
        root.setBorder(new EmptyBorder(14, 18, 16, 18));

        // Left Center: Top Auditorium Switcher + 2D Seat Canvas
        JPanel leftArena = new JPanel(new BorderLayout(0, 10));
        leftArena.setOpaque(false);

        JPanel switcherBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        switcherBar.setBackground(CARD_SURFACE);
        switcherBar.setBorder(new LineBorder(BORDER_SUBTLE, 1));

        JLabel swLbl = new JLabel("Select Studio Film:");
        swLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        swLbl.setForeground(Color.WHITE);

        String[] items = new String[movieCatalog.length];
        for (int i = 0; i < movieCatalog.length; i++) {
            items[i] = (i + 1) + ". " + movieCatalog[i].getTitle() + "  [" + movieCatalog[i].getStudio()
                    + " • PHP " + (int) movieCatalog[i].getBasePrice() + "]";
        }

        auditoriumCombo = new JComboBox<>(items);
        auditoriumCombo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        auditoriumCombo.addActionListener(e -> {
            int idx = auditoriumCombo.getSelectedIndex();
            if (idx >= 0 && idx < movieCatalog.length) {
                selectedMovie = movieCatalog[idx];
                updateLiveReceipt();
                refreshAllMetricsAndTables();
                seatCanvas.repaint();
            }
        });

        JButton clearSeatsBtn = new JButton("Reset Selected Seats");
        clearSeatsBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        clearSeatsBtn.setBackground(new Color(51, 65, 85));
        clearSeatsBtn.setForeground(Color.WHITE);
        clearSeatsBtn.setFocusPainted(false);
        clearSeatsBtn.setBorder(new EmptyBorder(6, 12, 6, 12));
        clearSeatsBtn.addActionListener(e -> {
            selectedMovie.clearTemporarySelections();
            updateLiveReceipt();
            seatCanvas.repaint();
        });

        switcherBar.add(swLbl);
        switcherBar.add(auditoriumCombo);
        switcherBar.add(clearSeatsBtn);

        seatCanvas = new AuditoriumSeatCanvas();

        leftArena.add(switcherBar, BorderLayout.NORTH);
        leftArena.add(seatCanvas, BorderLayout.CENTER);

        // Right Checkout Card (355px wide, Clean Dribbble Card Layout)
        JPanel rightCard = new JPanel(new BorderLayout(0, 10));
        rightCard.setPreferredSize(new Dimension(355, 600));
        rightCard.setBackground(CARD_SURFACE);
        rightCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_SUBTLE, 1),
                new EmptyBorder(14, 16, 14, 16)
        ));

        // Top Live Perforated Boarding Pass
        boardingPassPanel = new BoardingPassPreviewPanel();
        boardingPassPanel.setPreferredSize(new Dimension(320, 150));

        // Customer Form Inputs
        JPanel formPanel = new JPanel(new GridLayout(8, 1, 0, 4));
        formPanel.setOpaque(false);

        custNameField = new JTextField("Kevin Bryan Galgo");
        custNameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        custPhoneField = new JTextField("0917-888-2026");
        custPhoneField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        custDiscountCombo = new JComboBox<>(new String[]{
            "Regular Customer (0% Off)",
            "Student ID Holder (15% Off)",
            "Senior Citizen / PWD (20% Off)",
            "VIP Premiere Club (10% Off)"
        });
        custDiscountCombo.setSelectedIndex(1);
        custDiscountCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        custDiscountCombo.addActionListener(e -> updateLiveReceipt());

        snackCombo = new JComboBox<>(new String[]{
            "No Snack Add-On (PHP 0)",
            "Butter Popcorn + Iced Tea (+PHP 95)",
            "Caramel Popcorn + 2 Sodas (+PHP 150)",
            "Director's Nacho Combo (+PHP 220)"
        });
        snackCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        snackCombo.addActionListener(e -> updateLiveReceipt());

        formPanel.add(createFormLabel("CUSTOMER FULL NAME"));
        formPanel.add(custNameField);
        formPanel.add(createFormLabel("CONTACT NUMBER"));
        formPanel.add(custPhoneField);
        formPanel.add(createFormLabel("DISCOUNT TIER (IF-ELSE CONDITIONALS)"));
        formPanel.add(custDiscountCombo);
        formPanel.add(createFormLabel("CONCESSION SNACK BUNDLE"));
        formPanel.add(snackCombo);

        JPanel centerStack = new JPanel(new BorderLayout(0, 8));
        centerStack.setOpaque(false);
        centerStack.add(formPanel, BorderLayout.NORTH);

        liveReceiptLabel = new JLabel();
        liveReceiptLabel.setOpaque(true);
        liveReceiptLabel.setBackground(new Color(15, 23, 42));
        liveReceiptLabel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_SUBTLE, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        centerStack.add(liveReceiptLabel, BorderLayout.CENTER);

        // Bottom 2 Action Buttons
        JPanel btnStack = new JPanel(new GridLayout(2, 1, 0, 8));
        btnStack.setOpaque(false);

        JButton btnEnqueue = new JButton("1. Enqueue to FIFO Box Office Line");
        btnEnqueue.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEnqueue.setBackground(ACCENT_INDIGO);
        btnEnqueue.setForeground(Color.WHITE);
        btnEnqueue.setFocusPainted(false);
        btnEnqueue.setBorder(new EmptyBorder(10, 12, 10, 12));
        btnEnqueue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEnqueue.addActionListener(e -> processSeatBooking(false));

        JButton btnInstant = new JButton("2. Instant Issue & Save to XAMPP");
        btnInstant.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnInstant.setBackground(ACCENT_EMERALD);
        btnInstant.setForeground(new Color(5, 46, 22));
        btnInstant.setFocusPainted(false);
        btnInstant.setBorder(new EmptyBorder(10, 12, 10, 12));
        btnInstant.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInstant.addActionListener(e -> processSeatBooking(true));

        btnStack.add(btnEnqueue);
        btnStack.add(btnInstant);

        rightCard.add(boardingPassPanel, BorderLayout.NORTH);
        rightCard.add(centerStack, BorderLayout.CENTER);
        rightCard.add(btnStack, BorderLayout.SOUTH);

        root.add(leftArena, BorderLayout.CENTER);
        root.add(rightCard, BorderLayout.EAST);
        updateLiveReceipt();
        return root;
    }

    private JLabel createFormLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(TEXT_MUTED);
        return l;
    }

    // ========================================================================
    // VIEW 3: LIVE FIFO QUEUE PIPELINE VISUALIZER (DSA Queue)
    // ========================================================================
    private JPanel buildFifoQueueView() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(STAGE_DARK);
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        topBar.setBackground(CARD_SURFACE);
        topBar.setBorder(new LineBorder(BORDER_SUBTLE, 1));

        JLabel title = new JLabel("  FIFO BOX OFFICE QUEUE (First-In, First-Out):");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(Color.WHITE);

        JButton dequeueBtn = new JButton("Dequeue & Issue Front Ticket (O(1))");
        dequeueBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dequeueBtn.setBackground(ACCENT_EMERALD);
        dequeueBtn.setForeground(new Color(5, 46, 22));
        dequeueBtn.setFocusPainted(false);
        dequeueBtn.setBorder(new EmptyBorder(8, 14, 8, 14));
        dequeueBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dequeueBtn.addActionListener(e -> handleDequeueFront());

        JButton processAllBtn = new JButton("Process Entire Queue (While Loop)");
        processAllBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        processAllBtn.setBackground(ACCENT_INDIGO);
        processAllBtn.setForeground(Color.WHITE);
        processAllBtn.setFocusPainted(false);
        processAllBtn.setBorder(new EmptyBorder(8, 14, 8, 14));
        processAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        processAllBtn.addActionListener(e -> {
            if (boxOfficeQueue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "FIFO Queue is currently empty.");
                return;
            }
            int served = 0;
            while (!boxOfficeQueue.isEmpty()) {
                Ticket t = boxOfficeQueue.dequeue();
                t.setStatus("CONFIRMED");
                confirmedTickets.add(t);
                CinemaDatabaseManager.saveTicket(t, movieCatalog, confirmedTickets);
                served++;
            }
            queueCanvas.triggerBurst();
            refreshAllMetricsAndTables();
            JOptionPane.showMessageDialog(this, "Dequeued and confirmed all " + served + " waiting tickets!");
        });

        JButton simWalkInBtn = new JButton("+ Simulate Walk-In Customer (Enqueue)");
        simWalkInBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        simWalkInBtn.setBackground(new Color(51, 65, 85));
        simWalkInBtn.setForeground(Color.WHITE);
        simWalkInBtn.setFocusPainted(false);
        simWalkInBtn.setBorder(new EmptyBorder(8, 14, 8, 14));
        simWalkInBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        simWalkInBtn.addActionListener(e -> simulateWalkInCustomer());

        topBar.add(title);
        topBar.add(dequeueBtn);
        topBar.add(processAllBtn);
        topBar.add(simWalkInBtn);

        JPanel split = new JPanel(new GridLayout(2, 1, 0, 14));
        split.setOpaque(false);

        queueCanvas = new QueuePipelineCanvas();
        split.add(queueCanvas);

        String[] cols = {"Queue Position", "Ticket ID", "Customer Name", "Discount Tier", "Animation Studio", "Movie Title", "Seats", "Total Amount"};
        queueTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable qTable = createCleanDataTable(queueTableModel);
        split.add(new JScrollPane(qTable));

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // ========================================================================
    // VIEW 4: BOX OFFICE RECORDS & XAMPP MYSQL MANAGER (Clean Light Table)
    // ========================================================================
    private JPanel buildRecordsDatabaseView() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchBar.setBackground(Color.WHITE);
        searchBar.setBorder(new LineBorder(new Color(226, 232, 240), 1));

        JLabel findLbl = new JLabel("  Search Ticket / Customer / Studio:");
        findLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        findLbl.setForeground(new Color(30, 41, 59));

        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton btnFind = createLightToolbarButton("Search");
        JButton btnAll = createLightToolbarButton("Show All");
        JButton btnSortPrice = createLightToolbarButton("Quick Sort by Highest Price");
        JButton btnSortName = createLightToolbarButton("Quick Sort by Customer (A-Z)");

        btnFind.addActionListener(e -> populateRecordsTable(searchField.getText().trim()));
        btnAll.addActionListener(e -> {
            searchField.setText("");
            populateRecordsTable(null);
        });
        btnSortPrice.addActionListener(e -> sortRecords(true));
        btnSortName.addActionListener(e -> sortRecords(false));

        searchBar.add(findLbl);
        searchBar.add(searchField);
        searchBar.add(btnFind);
        searchBar.add(btnAll);
        searchBar.add(btnSortPrice);
        searchBar.add(btnSortName);

        String[] cols = {"Ticket ID", "Customer Name", "Contact", "Discount Tier", "Animation Studio", "Movie Title", "Seats", "Snacks", "Total Paid", "Status"};
        recordsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        recordsTable = createCleanDataTable(recordsTableModel);

        JPanel bottomCrudBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        bottomCrudBar.setOpaque(false);

        JButton btnRefund = createLightToolbarButton("Cancel & Refund Selected Ticket (Free 2D Array Seats)");
        JButton btnSyncDb = createLightToolbarButton("Sync All Records to XAMPP MySQL (cinema_ticketing_db)");
        JButton btnOpenWeb = createLightToolbarButton("Open XAMPP Web Dashboard (localhost/cinema_ticketing)");

        btnRefund.addActionListener(e -> refundSelectedTicket());
        btnSyncDb.addActionListener(e -> {
            CinemaDatabaseManager.initializeAndSync(movieCatalog, confirmedTickets);
            refreshAllMetricsAndTables();
            JOptionPane.showMessageDialog(this, "Database Synced!\nStatus: " + CinemaDatabaseManager.getStatusMessage());
        });
        btnOpenWeb.addActionListener(e -> openLocalhostPortal());

        bottomCrudBar.add(btnRefund);
        bottomCrudBar.add(btnSyncDb);
        bottomCrudBar.add(btnOpenWeb);

        panel.add(searchBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(recordsTable), BorderLayout.CENTER);
        panel.add(bottomCrudBar, BorderLayout.SOUTH);
        return panel;
    }

    private JButton createLightToolbarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(new Color(241, 245, 249));
        btn.setForeground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1),
                new EmptyBorder(6, 12, 6, 12)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTable createCleanDataTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(new Color(30, 58, 138));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(new Color(51, 65, 85));
        header.setPreferredSize(new Dimension(100, 36));

        DefaultTableCellRenderer pad = new DefaultTableCellRenderer();
        pad.setBorder(new EmptyBorder(0, 10, 0, 10));
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(pad);
        }
        return table;
    }

    // ========================================================================
    // CORE BUSINESS LOGIC & CALCULATIONS
    // ========================================================================
    private double getSelectedSnackPrice() {
        int idx = snackCombo != null ? snackCombo.getSelectedIndex() : 0;
        switch (idx) {
            case 1: return 95.0;
            case 2: return 150.0;
            case 3: return 220.0;
            default: return 0.0;
        }
    }

    private String getSelectedSnackName() {
        int idx = snackCombo != null ? snackCombo.getSelectedIndex() : 0;
        switch (idx) {
            case 1: return "Butter Popcorn + Iced Tea";
            case 2: return "Caramel Popcorn + 2 Sodas";
            case 3: return "Director's Nacho Combo";
            default: return "None";
        }
    }

    private void updateLiveReceipt() {
        if (selectedMovie == null || liveReceiptLabel == null) return;
        List<String> seats = selectedMovie.getSelectedSeatCodes();
        int count = seats.size();
        int vipCount = selectedMovie.countSelectedVipSeats();

        String tier = (String) custDiscountCombo.getSelectedItem();
        Customer temp = new Customer("TMP", custNameField.getText(), custPhoneField.getText(),
                tier != null ? tier : "Regular (0% Off)");

        double baseCost = count * selectedMovie.getBasePrice();
        double vipFee = vipCount * 60.0;
        double sub = baseCost + vipFee;
        double disc = sub * temp.getDiscountRate();
        double snack = getSelectedSnackPrice();
        double total = Math.max(0.0, (sub - disc) + snack);

        liveReceiptLabel.setText(String.format(
                "<html><body style='font-family:Segoe UI;color:#E2E8F0;'>"
                + "<font size='2' color='#94A3B8'><b>PRICE CALCULATION BREAKDOWN</b></font><br>"
                + "Selected Seats (%d): <b>PHP %.2f</b><br>"
                + "VIP Row E-F Surcharge (%d x 60): <b>+PHP %.2f</b><br>"
                + "Discount (%d%% Off): <font color='#34D399'><b>-PHP %.2f</b></font><br>"
                + "Snack Add-On: <b>+PHP %.2f</b><br>"
                + "<font size='4' color='#FBBF24'><b>TOTAL DUE: PHP %.2f</b></font>"
                + "</body></html>",
                count, baseCost, vipCount, vipFee,
                (int) (temp.getDiscountRate() * 100), disc, snack, total
        ));

        if (boardingPassPanel != null) {
            boardingPassPanel.repaint();
        }
    }

    private void processSeatBooking(boolean immediateConfirm) {
        List<String> selected = selectedMovie.getSelectedSeatCodes();
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Click at least one green or purple seat on the 2D Auditorium Map first!",
                    "No Seats Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cname = custNameField.getText().trim();
        if (cname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer's full name.");
            return;
        }

        String tId = "TKT-" + (ticketCounter++);
        int vipCount = selectedMovie.countSelectedVipSeats();
        String[] seatArr = selected.toArray(new String[0]);

        selectedMovie.lockSelectedSeats(tId);

        Customer cust = new Customer("CUST-" + ticketCounter, cname,
                custPhoneField.getText().trim(), (String) custDiscountCombo.getSelectedItem());

        Ticket ticket = new Ticket(tId, cust, selectedMovie, seatArr,
                vipCount, getSelectedSnackPrice(), getSelectedSnackName(),
                immediateConfirm ? "CONFIRMED" : "IN QUEUE");

        seatCanvas.triggerConfetti();

        if (immediateConfirm) {
            confirmedTickets.add(ticket);
            CinemaDatabaseManager.saveTicket(ticket, movieCatalog, confirmedTickets);
            JOptionPane.showMessageDialog(this,
                    "Ticket Issued & Synced to XAMPP!\n\n"
                    + "Ticket ID : " + ticket.getTicketId() + "\n"
                    + "Studio    : " + selectedMovie.getStudio() + "\n"
                    + "Movie     : " + selectedMovie.getTitle() + "\n"
                    + "Seats     : " + ticket.getSeatListString() + "\n"
                    + "Total Paid: PHP " + String.format("%.2f", ticket.getTotalPrice()),
                    "Box Office Receipt", JOptionPane.INFORMATION_MESSAGE);
        } else {
            boxOfficeQueue.enqueue(ticket);
            JOptionPane.showMessageDialog(this,
                    "Enqueued to FIFO Box Office Line!\n\n"
                    + "Ticket " + ticket.getTicketId() + " is at Queue Position #" + boxOfficeQueue.getSize() + ".\n"
                    + "Open '03 FIFO Ticket Queue' on the left sidebar to process it!",
                    "Added to FIFO Queue", JOptionPane.INFORMATION_MESSAGE);
        }

        updateLiveReceipt();
        refreshAllMetricsAndTables();
    }

    private void handleDequeueFront() {
        if (boxOfficeQueue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "FIFO Queue is empty! Click '+ Simulate Walk-In Customer' or enqueue a seat booking.");
            return;
        }
        Ticket frontTicket = boxOfficeQueue.dequeue();
        frontTicket.setStatus("CONFIRMED");
        confirmedTickets.add(frontTicket);
        CinemaDatabaseManager.saveTicket(frontTicket, movieCatalog, confirmedTickets);
        queueCanvas.triggerBurst();
        refreshAllMetricsAndTables();

        JOptionPane.showMessageDialog(this,
                "FIFO Dequeue Complete!\n\n"
                + "Served Front Ticket: " + frontTicket.getTicketId() + "\n"
                + "Customer: " + frontTicket.getCustomer().getFullName() + "\n"
                + "Movie: " + frontTicket.getMovie().getTitle() + " (" + frontTicket.getMovie().getStudio() + ")\n"
                + "Seats: " + frontTicket.getSeatListString() + "\n"
                + "Total Paid: PHP " + String.format("%.2f", frontTicket.getTotalPrice()),
                "Dequeued Front Customer", JOptionPane.INFORMATION_MESSAGE);
    }

    private void simulateWalkInCustomer() {
        Random rand = new Random();
        String[] names = {"Rhea Santos", "Carl Mendoza", "Pierce Villanueva", "Jensell Cruz", "Kathlyne Reyes"};
        String[] tiers = {"Regular Customer (0% Off)", "Student ID Holder (15% Off)", "VIP Premiere Club (10% Off)"};
        Movie m = movieCatalog[rand.nextInt(movieCatalog.length)];

        String seatCode = null;
        int rowFound = 0;
        outer:
        for (int r = 0; r < Movie.ROWS; r++) {
            for (int c = 0; c < Movie.COLS; c++) {
                if (m.getSeatGrid()[r][c] == Movie.SEAT_AVAILABLE) {
                    seatCode = Movie.getSeatCode(r, c);
                    rowFound = r;
                    break outer;
                }
            }
        }
        if (seatCode == null) return;

        String tId = "TKT-" + (ticketCounter++);
        m.markSeatReservedByCode(seatCode, tId);
        Customer cust = new Customer("CUST-" + ticketCounter, names[rand.nextInt(names.length)],
                "0917-555-" + (1000 + rand.nextInt(8999)), tiers[rand.nextInt(tiers.length)]);
        Ticket t = new Ticket(tId, cust, m, new String[]{seatCode},
                Movie.isVipRow(rowFound) ? 1 : 0, 95.0, "Butter Popcorn + Iced Tea", "IN QUEUE");

        boxOfficeQueue.enqueue(t);
        refreshAllMetricsAndTables();
    }

    private void refundSelectedTicket() {
        int row = recordsTable.getSelectedRow();
        if (row < 0 || row >= confirmedTickets.size()) {
            JOptionPane.showMessageDialog(this, "Select a ticket row in the table first.");
            return;
        }
        Ticket t = confirmedTickets.get(row);
        t.getMovie().releaseSeatsByTicketId(t.getTicketId());
        confirmedTickets.remove(row);
        CinemaDatabaseManager.deleteTicket(t.getTicketId(), movieCatalog, confirmedTickets);
        refreshAllMetricsAndTables();
        JOptionPane.showMessageDialog(this,
                "Cancelled Ticket " + t.getTicketId() + " and released seat(s) [" + t.getSeatListString() + "] back to Available!");
    }

    private void sortRecords(boolean byPrice) {
        if (confirmedTickets.size() <= 1) return;
        Ticket[] arr = confirmedTickets.toArray(new Ticket[0]);
        if (byPrice) {
            TicketQuickSorter.sortByPriceDesc(arr, 0, arr.length - 1);
        } else {
            TicketQuickSorter.sortByNameAsc(arr, 0, arr.length - 1);
        }
        confirmedTickets.clear();
        for (Ticket t : arr) confirmedTickets.add(t);
        populateRecordsTable(null);
    }

    private void openLocalhostPortal() {
        CinemaDatabaseManager.initializeAndSync(movieCatalog, confirmedTickets);
        refreshAllMetricsAndTables();
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://localhost/cinema_ticketing/"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Open http://localhost/cinema_ticketing/ in your browser.");
        }
    }

    private void refreshAllMetricsAndTables() {
        double rev = 0;
        for (Ticket t : confirmedTickets) rev += t.getTotalPrice();

        updateMetricPillText(kpiSelectedMovieLabel, "ACTIVE AUDITORIUM",
                selectedMovie.getTitle() + " (" + selectedMovie.getStudio() + ")", selectedMovie.getAccentColor());
        updateMetricPillText(kpiAvailableSeatsLabel, "2D ARRAY SEATS",
                selectedMovie.getAvailableSeatCount() + " / 60 Available", ACCENT_EMERALD);
        updateMetricPillText(kpiQueueCountLabel, "FIFO QUEUE LINE",
                boxOfficeQueue.getSize() + " Customer(s) Waiting", ACCENT_GOLD);
        updateMetricPillText(kpiTotalRevenueLabel, "BOX OFFICE REVENUE",
                String.format("PHP %,.2f (%d Sold)", rev, confirmedTickets.size()), new Color(168, 85, 247));

        if (xamppStatusPillBtn != null) {
            xamppStatusPillBtn.setText(CinemaDatabaseManager.getStatusMessage());
            xamppStatusPillBtn.setBackground(CinemaDatabaseManager.isConnected()
                    ? new Color(6, 95, 70) : new Color(127, 29, 29));
            xamppStatusPillBtn.setForeground(Color.WHITE);
        }

        if (queueTableModel != null) {
            queueTableModel.setRowCount(0);
            Ticket[] qArr = boxOfficeQueue.toArray();
            for (int i = 0; i < qArr.length; i++) {
                Ticket t = qArr[i];
                queueTableModel.addRow(new Object[]{
                    (i == 0 ? "NEXT (FRONT)" : "Position #" + (i + 1)),
                    t.getTicketId(), t.getCustomer().getFullName(),
                    t.getCustomer().getCustomerCategory(),
                    t.getMovie().getStudio(), t.getMovie().getTitle(),
                    t.getSeatListString(), String.format("PHP %.2f", t.getTotalPrice())
                });
            }
        }

        populateRecordsTable(null);
    }

    private void populateRecordsTable(String filter) {
        if (recordsTableModel == null) return;
        recordsTableModel.setRowCount(0);
        for (Ticket t : confirmedTickets) {
            if (filter != null && !filter.isEmpty()) {
                String q = filter.toLowerCase();
                boolean hit = t.getTicketId().toLowerCase().contains(q)
                        || t.getCustomer().getFullName().toLowerCase().contains(q)
                        || t.getMovie().getStudio().toLowerCase().contains(q)
                        || t.getMovie().getTitle().toLowerCase().contains(q);
                if (!hit) continue;
            }
            recordsTableModel.addRow(new Object[]{
                t.getTicketId(), t.getCustomer().getFullName(),
                t.getCustomer().getContactNumber(), t.getCustomer().getCustomerCategory(),
                t.getMovie().getStudio(), t.getMovie().getTitle(),
                t.getSeatListString(), t.getSnackLabel(),
                String.format("PHP %.2f", t.getTotalPrice()), t.getStatus()
            });
        }
    }

    private void start60FpsLoop() {
        Timer timer = new Timer(16, e -> {
            if (showcaseCanvas != null && showcaseCanvas.isShowing()) {
                showcaseCanvas.tick();
                showcaseCanvas.repaint();
            }
            if (seatCanvas != null && seatCanvas.isShowing()) {
                seatCanvas.tick();
                seatCanvas.repaint();
            }
            if (queueCanvas != null && queueCanvas.isShowing()) {
                queueCanvas.tick();
                queueCanvas.repaint();
            }
        });
        timer.start();
    }

    // ========================================================================
    // CUSTOM COMPONENT 1: LIVE PERFORATED BOARDING PASS PREVIEW
    // ========================================================================
    private class BoardingPassPreviewPanel extends JPanel {
        public BoardingPassPreviewPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            Color accent = selectedMovie.getAccentColor();

            g2.setPaint(new GradientPaint(0, 0, new Color(15, 23, 42), w, h, new Color(24, 34, 56)));
            g2.fillRoundRect(0, 0, w - 1, h - 1, 16, 16);

            // Top Studio Accent Strip
            g2.setColor(accent);
            g2.fillRoundRect(0, 0, w - 1, 32, 16, 16);
            g2.fillRect(0, 16, w - 1, 16);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(new Color(11, 15, 25));
            g2.drawString("BOARDING PASS  •  " + selectedMovie.getStudio().toUpperCase(), 14, 20);
            g2.drawString(selectedMovie.getFormatTag(), w - 72, 20);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.setColor(Color.WHITE);
            g2.drawString(selectedMovie.getTitle(), 14, 54);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.setColor(TEXT_MUTED);
            g2.drawString(selectedMovie.getCinemaHall() + "   |   Showtime: " + selectedMovie.getShowtime(), 14, 72);

            List<String> sel = selectedMovie.getSelectedSeatCodes();
            String seatStr = sel.isEmpty() ? "Click seats on 2D map" : String.join(", ", sel);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.setColor(ACCENT_GOLD);
            g2.drawString("SEATS: " + seatStr, 14, 94);

            // Perforated Divider
            int lineY = 108;
            g2.setColor(CARD_SURFACE);
            g2.fillOval(-8, lineY - 8, 16, 16);
            g2.fillOval(w - 8, lineY - 8, 16, 16);

            Stroke dash = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
            g2.setStroke(dash);
            g2.setColor(BORDER_SUBTLE);
            g2.drawLine(12, lineY, w - 12, lineY);

            // Vector Barcode
            g2.setColor(new Color(226, 232, 240));
            for (int bx = 16; bx < w - 16; bx += 5) {
                int bw = (bx % 15 == 0) ? 3 : 1;
                g2.fillRect(bx, lineY + 10, bw, 22);
            }

            g2.setColor(accent);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
            g2.dispose();
        }
    }

    // ========================================================================
    // CUSTOM COMPONENT 2: 60 FPS STUDIO HERO BANNER + CLEAN 4x2 POSTER GRID
    // ========================================================================
    private class StudioShowcaseCanvas extends JPanel {
        private double phase = 0;
        private int hoveredIndex = -1;
        private final RoundRectangle2D[] cardRects = new RoundRectangle2D[8];
        private RoundRectangle2D heroBookBtnRect = new RoundRectangle2D.Double();
        private final List<CinemaParticle> ambientStars = new ArrayList<>();

        public StudioShowcaseCanvas() {
            setBackground(STAGE_DARK);
            for (int i = 0; i < 35; i++) {
                ambientStars.add(new CinemaParticle(
                        Math.random() * 1000, Math.random() * 200,
                        (Math.random() - 0.5) * 0.3, -0.25 - Math.random() * 0.4,
                        2 + Math.random() * 3, Color.WHITE, false
                ));
            }

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int found = -1;
                    for (int i = 0; i < cardRects.length; i++) {
                        if (cardRects[i] != null && cardRects[i].contains(e.getPoint())) {
                            found = i;
                            break;
                        }
                    }
                    hoveredIndex = found;
                    if (found != -1) {
                        selectedMovie = movieCatalog[found];
                    }
                    boolean overHeroBtn = heroBookBtnRect.contains(e.getPoint());
                    setCursor((found != -1 || overHeroBtn)
                            ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (hoveredIndex != -1) {
                        selectedMovie = movieCatalog[hoveredIndex];
                        auditoriumCombo.setSelectedIndex(hoveredIndex);
                        updateLiveReceipt();
                        switchView("SEATS");
                    } else if (heroBookBtnRect.contains(e.getPoint())) {
                        for (int i = 0; i < movieCatalog.length; i++) {
                            if (movieCatalog[i] == selectedMovie) {
                                auditoriumCombo.setSelectedIndex(i);
                                break;
                            }
                        }
                        updateLiveReceipt();
                        switchView("SEATS");
                    }
                }
            });
        }

        public void tick() {
            phase += 0.05;
            for (CinemaParticle p : ambientStars) {
                p.update(getWidth(), 180);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int pad = 20;

            // 1. TOP SPOTLIGHT HERO BANNER FOR SELECTED MOVIE (145px tall)
            int heroH = 145;
            Color heroCol = selectedMovie.getAccentColor();
            g2.setPaint(new GradientPaint(
                    pad, 14, new Color(heroCol.getRed() / 3, heroCol.getGreen() / 3, heroCol.getBlue() / 3),
                    w - pad, 14 + heroH, new Color(15, 23, 42)
            ));
            g2.fillRoundRect(pad, 14, w - pad * 2, heroH, 20, 20);

            for (CinemaParticle p : ambientStars) {
                g2.setColor(new Color(255, 255, 255, 75));
                g2.fillOval((int) p.x, (int) p.y + 14, (int) p.size, (int) p.size);
            }

            // Animated Concentric Light Rings on Right Side of Hero Banner
            int ringCx = w - 160;
            int ringCy = 14 + heroH / 2;
            for (int r = 1; r <= 3; r++) {
                int rad = (int) (28 * r + Math.sin(phase + r) * 6);
                g2.setColor(new Color(heroCol.getRed(), heroCol.getGreen(), heroCol.getBlue(), 75 - r * 18));
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(ringCx - rad, ringCy - rad, rad * 2, rad * 2);
            }

            // Studio Pill on Hero Banner
            g2.setColor(heroCol);
            g2.fillRoundRect(pad + 22, 30, 190, 24, 12, 12);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(new Color(11, 15, 25));
            g2.drawString("FEATURED  •  " + selectedMovie.getStudio().toUpperCase(), pad + 32, 46);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2.setColor(Color.WHITE);
            g2.drawString(selectedMovie.getTitle(), pad + 22, 82);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.setColor(new Color(203, 213, 225));
            g2.drawString(selectedMovie.getSynopsis(), pad + 22, 106);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.setColor(ACCENT_GOLD);
            g2.drawString(selectedMovie.getCinemaHall() + "   •   " + selectedMovie.getShowtime()
                    + "   •   " + selectedMovie.getFormatTag() + "   •   PHP " + (int) selectedMovie.getBasePrice(),
                    pad + 22, 132);

            // Hero CTA Button
            int btnW = 195;
            int btnH = 40;
            int btnX = w - pad - btnW - 24;
            int btnY = 14 + (heroH - btnH) / 2;
            heroBookBtnRect = new RoundRectangle2D.Double(btnX, btnY, btnW, btnH, 12, 12);
            g2.setColor(ACCENT_GOLD);
            g2.fillRoundRect(btnX, btnY, btnW, btnH, 12, 12);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.setColor(new Color(11, 15, 25));
            g2.drawString("Reserve Seats Now  ->", btnX + 26, btnY + 25);

            g2.setColor(heroCol);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(pad, 14, w - pad * 2, heroH, 20, 20);

            // 2. CLEAN 4x2 STUDIO CARDS GRID BELOW HERO BANNER
            int gridTop = 14 + heroH + 18;
            int cols = 4;
            int rows = 2;
            int gap = 16;
            int cardW = (w - pad * 2 - gap * (cols - 1)) / cols;
            int cardH = (h - gridTop - 18 - gap) / rows;

            for (int i = 0; i < movieCatalog.length; i++) {
                Movie m = movieCatalog[i];
                int r = i / cols;
                int c = i % cols;

                boolean match = activeStudioFilter.equals("All Studios")
                        || m.getStudio().equalsIgnoreCase(activeStudioFilter);
                boolean hovered = (i == hoveredIndex);

                int yLift = hovered ? -5 : 0;
                int x = pad + c * (cardW + gap);
                int y = gridTop + r * (cardH + gap) + yLift;

                cardRects[i] = new RoundRectangle2D.Double(x, y, cardW, cardH, 16, 16);

                // Card Surface
                g2.setColor(match ? CARD_SURFACE : new Color(19, 26, 40));
                g2.fillRoundRect(x, y, cardW, cardH, 16, 16);

                // Top Color Strip & Vector Studio Artwork
                int stripH = (int) (cardH * 0.36);
                Color ac = m.getAccentColor();
                g2.setPaint(new GradientPaint(
                        x, y, new Color(ac.getRed(), ac.getGreen(), ac.getBlue(), match ? 190 : 50),
                        x + cardW, y + stripH, new Color(30, 41, 59)
                ));
                g2.fillRoundRect(x, y, cardW, stripH, 16, 16);

                // Studio Category Badge
                g2.setColor(new Color(11, 15, 25, 190));
                g2.fillRoundRect(x + 12, y + 10, cardW - 24, 24, 8, 8);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2.setColor(ac);
                g2.drawString((i + 1) + ". " + m.getStudio().toUpperCase(), x + 20, y + 26);

                // Movie Title (Clean, Uncluttered)
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g2.setColor(match ? Color.WHITE : TEXT_MUTED);
                String cleanTitle = m.getTitle().length() > 24 ? m.getTitle().substring(0, 22) + "..." : m.getTitle();
                g2.drawString(cleanTitle, x + 14, y + stripH + 26);

                // Hall & Showtime Pill
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                g2.setColor(TEXT_MUTED);
                g2.drawString(m.getCinemaHall() + "  •  " + m.getShowtime(), x + 14, y + stripH + 46);

                // Seat Availability Progress Bar
                int avail = m.getAvailableSeatCount();
                int barY = y + cardH - 54;
                g2.setColor(new Color(15, 23, 42));
                g2.fillRoundRect(x + 14, barY, cardW - 28, 8, 4, 4);
                int fillW = (int) ((cardW - 28) * (avail / 60.0));
                g2.setColor(avail > 15 ? ACCENT_EMERALD : ACCENT_GOLD);
                g2.fillRoundRect(x + 14, barY, fillW, 8, 4, 4);

                // Bottom Price & Action Row
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor( new Color(203, 213, 225));
                g2.drawString(avail + "/60 Seats Left", x + 14, y + cardH - 20);

                // Price + Book Pill
                String priceStr = "PHP " + (int) m.getBasePrice() + "  ->";
                g2.setColor(hovered ? ACCENT_GOLD : new Color(51, 65, 85));
                g2.fillRoundRect(x + cardW - 102, y + cardH - 36, 88, 24, 8, 8);
                g2.setColor(hovered ? Color.BLACK : Color.WHITE);
                g2.drawString(priceStr, x + cardW - 92, y + cardH - 20);

                // Card Outline
                g2.setColor(hovered ? ACCENT_GOLD : (match ? new Color(71, 85, 105) : BORDER_SUBTLE));
                g2.setStroke(new BasicStroke(hovered ? 2.2f : 1f));
                g2.drawRoundRect(x, y, cardW, cardH, 16, 16);
            }

            g2.dispose();
        }
    }

    // ========================================================================
    // CUSTOM COMPONENT 3: REALISTIC 2D AUDITORIUM SEAT MAP CANVAS
    // ========================================================================
    private class AuditoriumSeatCanvas extends JPanel {
        private double screenGlow = 0;
        private Point hoveredSeat = null;
        private final RoundRectangle2D[][] seatBoxes = new RoundRectangle2D[Movie.ROWS][Movie.COLS];
        private final List<CinemaParticle> confetti = new ArrayList<>();

        public AuditoriumSeatCanvas() {
            setBackground(new Color(13, 18, 30));
            setBorder(new LineBorder(BORDER_SUBTLE, 1));

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    Point hit = null;
                    for (int r = 0; r < Movie.ROWS; r++) {
                        for (int c = 0; c < Movie.COLS; c++) {
                            if (seatBoxes[r][c] != null && seatBoxes[r][c].contains(e.getPoint())) {
                                hit = new Point(r, c);
                                break;
                            }
                        }
                    }
                    hoveredSeat = hit;
                    setCursor(hit != null ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (hoveredSeat != null) {
                        int r = hoveredSeat.x;
                        int c = hoveredSeat.y;
                        int current = selectedMovie.getSeatGrid()[r][c];
                        if (current == Movie.SEAT_AVAILABLE) {
                            selectedMovie.getSeatGrid()[r][c] = Movie.SEAT_SELECTED;
                        } else if (current == Movie.SEAT_SELECTED) {
                            selectedMovie.getSeatGrid()[r][c] = Movie.SEAT_AVAILABLE;
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                        updateLiveReceipt();
                        repaint();
                    }
                }
            });
        }

        public void triggerConfetti() {
            for (int i = 0; i < 55; i++) {
                double ang = Math.random() * Math.PI * 2;
                double spd = 2.0 + Math.random() * 6.5;
                confetti.add(new CinemaParticle(
                        getWidth() / 2.0, getHeight() / 2.0,
                        Math.cos(ang) * spd, Math.sin(ang) * spd - 2,
                        5 + Math.random() * 4,
                        (i % 2 == 0) ? ACCENT_GOLD : selectedMovie.getAccentColor(), true
                ));
            }
        }

        public void tick() {
            screenGlow += 0.06;
            Iterator<CinemaParticle> it = confetti.iterator();
            while (it.hasNext()) {
                if (!it.next().update(getWidth(), getHeight())) it.remove();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            Color studioCol = selectedMovie.getAccentColor();

            // 1. Curved IMAX Screen Beam at Top
            int scrW = Math.min(540, w - 100);
            int scrX = (w - scrW) / 2;
            int scrY = 26;

            int alpha = 45 + (int) (Math.sin(screenGlow) * 18);
            Polygon cone = new Polygon(
                    new int[]{scrX + 20, scrX + scrW - 20, scrX + scrW + 40, scrX - 40},
                    new int[]{scrY + 10, scrY + 10, scrY + 105, scrY + 105}, 4
            );
            g2.setPaint(new GradientPaint(
                    0, scrY, new Color(studioCol.getRed(), studioCol.getGreen(), studioCol.getBlue(), alpha),
                    0, scrY + 105, new Color(13, 18, 30, 0)
            ));
            g2.fillPolygon(cone);

            g2.setColor(studioCol);
            g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(scrX, scrY, scrW, 38, 15, 150);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.setColor(TEXT_MUTED);
            String scrTitle = "IMAX CURVED SCREEN  •  " + selectedMovie.getStudio().toUpperCase() + "  •  " + selectedMovie.getTitle().toUpperCase();
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(scrTitle, (w - fm.stringWidth(scrTitle)) / 2, scrY + 42);

            // 2. Render 2D Seat Matrix [6][10] with Realistic Theater Chair Geometry
            int seatW = 42;
            int seatH = 38;
            int gapX = 9;
            int gapY = 14;
            int aisleGap = 28;
            int totalGridW = Movie.COLS * seatW + (Movie.COLS - 1) * gapX + aisleGap;
            int startX = (w - totalGridW) / 2;
            int startY = 92;

            for (int r = 0; r < Movie.ROWS; r++) {
                boolean isVip = Movie.isVipRow(r);
                int rowY = startY + r * (seatH + gapY) + (r >= 4 ? 14 : 0);

                // Row Letters on Both Sides
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(isVip ? ACCENT_GOLD : TEXT_MUTED);
                g2.drawString(String.valueOf((char) ('A' + r)), startX - 24, rowY + 24);
                g2.drawString(String.valueOf((char) ('A' + r)), startX + totalGridW + 14, rowY + 24);

                for (int c = 0; c < Movie.COLS; c++) {
                    int aisle = (c >= 5) ? aisleGap : 0;
                    int sx = startX + c * (seatW + gapX) + aisle;

                    boolean hover = (hoveredSeat != null && hoveredSeat.x == r && hoveredSeat.y == c);
                    int state = selectedMovie.getSeatGrid()[r][c];

                    int dy = hover ? rowY - 3 : rowY;
                    seatBoxes[r][c] = new RoundRectangle2D.Double(sx, dy, seatW, seatH, 10, 10);

                    Color baseCol;
                    if (state == Movie.SEAT_RESERVED) {
                        baseCol = new Color(225, 29, 72); // Sold Crimson
                    } else if (state == Movie.SEAT_SELECTED) {
                        baseCol = ACCENT_GOLD; // Selected Gold
                    } else if (isVip) {
                        baseCol = new Color(139, 92, 246); // VIP Purple
                    } else {
                        baseCol = ACCENT_EMERALD; // Available Emerald
                    }

                    // Draw Chair Cushion
                    g2.setColor(baseCol);
                    g2.fillRoundRect(sx + 3, dy + 6, seatW - 6, seatH - 8, 8, 8);

                    // Draw Headrest Pillow
                    g2.setColor(baseCol.brighter());
                    g2.fillRoundRect(sx + 7, dy, seatW - 14, 9, 6, 6);

                    // Draw Left & Right Armrests
                    g2.setColor(baseCol.darker());
                    g2.fillRoundRect(sx, dy + 10, 4, seatH - 12, 4, 4);
                    g2.fillRoundRect(sx + seatW - 4, dy + 10, 4, seatH - 12, 4, 4);

                    if (hover) {
                        g2.setColor(Color.WHITE);
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawRoundRect(sx, dy, seatW, seatH, 10, 10);
                    }

                    // Seat Label
                    String code = Movie.getSeatCode(r, c);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    g2.setColor(state == Movie.SEAT_SELECTED ? new Color(11, 15, 25) : Color.WHITE);
                    FontMetrics sfm = g2.getFontMetrics();
                    g2.drawString(code, sx + (seatW - sfm.stringWidth(code)) / 2, dy + 25);
                }
            }

            // 3. Clean Seat Legend Bar at Bottom
            int legY = h - 26;
            int legStart = (w - 580) / 2;
            drawSeatKey(g2, legStart, legY, ACCENT_EMERALD, "Standard Available");
            drawSeatKey(g2, legStart + 150, legY, new Color(139, 92, 246), "VIP Recliner Row E-F (+PHP 60)");
            drawSeatKey(g2, legStart + 355, legY, ACCENT_GOLD, "Selected");
            drawSeatKey(g2, legStart + 455, legY, new Color(225, 29, 72), "Reserved / Sold");

            for (CinemaParticle p : confetti) {
                int a = (int) Math.max(0, Math.min(255, p.alpha * 255));
                g2.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), a));
                g2.fillOval((int) p.x, (int) p.y, (int) p.size, (int) p.size);
            }

            g2.dispose();
        }

        private void drawSeatKey(Graphics2D g2, int x, int y, Color col, String text) {
            g2.setColor(col);
            g2.fillRoundRect(x, y - 12, 14, 14, 4, 4);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(new Color(203, 213, 225));
            g2.drawString(text, x + 20, y);
        }
    }

    // ========================================================================
    // CUSTOM COMPONENT 4: 60 FPS FIFO QUEUE PIPELINE VISUALIZER
    // ========================================================================
    private class QueuePipelineCanvas extends JPanel {
        private double anim = 0;
        private final List<CinemaParticle> sparks = new ArrayList<>();

        public QueuePipelineCanvas() {
            setBackground(CARD_SURFACE);
            setBorder(new LineBorder(BORDER_SUBTLE, 1));
        }

        public void triggerBurst() {
            for (int i = 0; i < 40; i++) {
                double ang = Math.random() * Math.PI * 2;
                double spd = 2.0 + Math.random() * 6.0;
                sparks.add(new CinemaParticle(
                        120, getHeight() / 2.0,
                        Math.cos(ang) * spd, Math.sin(ang) * spd,
                        5 + Math.random() * 4, ACCENT_EMERALD, true
                ));
            }
        }

        public void tick() {
            anim += 0.07;
            Iterator<CinemaParticle> it = sparks.iterator();
            while (it.hasNext()) {
                if (!it.next().update(getWidth(), getHeight())) it.remove();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.setColor(ACCENT_GOLD);
            g2.drawString("FIFO LINKED-NODE QUEUE VISUALIZER   (Dequeue from FRONT  <--  Enqueue at REAR)", 20, 26);

            // Counter Window Box
            int bx = 20;
            int by = 44;
            int bw = 160;
            int bh = h - 62;
            g2.setColor(new Color(6, 78, 59));
            g2.fillRoundRect(bx, by, bw, bh, 14, 14);
            g2.setColor(ACCENT_EMERALD);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(bx, by, bw, bh, 14, 14);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.setColor(Color.WHITE);
            g2.drawString("CASHIER COUNTER", bx + 16, by + 36);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.setColor(new Color(167, 243, 208));
            g2.drawString("Serves FRONT Node", bx + 16, by + 56);
            g2.drawString("Time Complexity: O(1)", bx + 16, by + 74);

            Ticket[] items = boxOfficeQueue.toArray();
            if (items.length == 0) {
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g2.setColor(TEXT_MUTED);
                g2.drawString("Queue is empty! Click '+ Simulate Walk-In Customer' above to enqueue a node.", bx + 200, h / 2 + 8);
            } else {
                int cardW = 195;
                int cardH = bh - 8;
                int startX = bx + bw + 36;
                int maxVisible = Math.max(1, (w - startX) / 225);

                for (int i = 0; i < Math.min(items.length, maxVisible); i++) {
                    Ticket t = items[i];
                    int nx = startX + i * (cardW + 30);
                    int ny = by + 4 + (int) (Math.sin(anim + i) * 3);

                    // Arrow connecting nodes
                    g2.setColor(ACCENT_GOLD);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawLine(nx - 24, ny + cardH / 2, nx - 6, ny + cardH / 2);

                    g2.setColor(new Color(15, 23, 42));
                    g2.fillRoundRect(nx, ny, cardW, cardH, 12, 12);
                    g2.setColor(i == 0 ? ACCENT_GOLD : t.getMovie().getAccentColor());
                    g2.drawRoundRect(nx, ny, cardW, cardH, 12, 12);

                    g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    g2.setColor(i == 0 ? ACCENT_GOLD : new Color(56, 189, 248));
                    String role = (i == 0) ? "FRONT (NEXT TO SERVE)" : (i == items.length - 1 ? "REAR NODE" : "NODE #" + (i + 1));
                    g2.drawString(role, nx + 12, ny + 22);

                    g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    g2.setColor(Color.WHITE);
                    g2.drawString(t.getCustomer().getFullName(), nx + 12, ny + 44);

                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    g2.setColor(TEXT_MUTED);
                    g2.drawString(t.getMovie().getStudio(), nx + 12, ny + 62);
                    g2.drawString("Seats: " + t.getSeatListString(), nx + 12, ny + 80);

                    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    g2.setColor(ACCENT_EMERALD);
                    g2.drawString(String.format("PHP %.2f", t.getTotalPrice()), nx + 12, ny + 100);
                }
            }

            for (CinemaParticle p : sparks) {
                int a = (int) Math.max(0, Math.min(255, p.alpha * 255));
                g2.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), a));
                g2.fillOval((int) p.x, (int) p.y, (int) p.size, (int) p.size);
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CinemaTicketingSystem app = new CinemaTicketingSystem();
            app.setVisible(true);
        });
    }
}