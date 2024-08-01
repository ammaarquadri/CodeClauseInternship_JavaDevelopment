import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class HospitalInfoSystem {

    private static Connection connect() {
        // Connect to MySQL database
        String url = "jdbc:mysql://localhost:3306/HospitalDB";
        String user = "root";
        String password = "root"; 
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/HospitalDB", "root", "root");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // Setting up the JFrame
        JFrame frame = new JFrame("Hospital Information System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnAddPatient = new JButton("Add Patient");
        JButton btnViewAppointments = new JButton("View Appointments");
        JButton btnMedicalHistory = new JButton("Medical History");

        panel.add(btnAddPatient);
        panel.add(btnViewAppointments);
        panel.add(btnMedicalHistory);

        frame.add(panel, BorderLayout.WEST);

        // Panel for main content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());

        // Add Patient Panel
        JPanel addPatientPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JTextField txtDOB = new JTextField();
        JTextField txtGender = new JTextField();

        addPatientPanel.add(new JLabel("First Name:"));
        addPatientPanel.add(txtFirstName);
        addPatientPanel.add(new JLabel("Last Name:"));
        addPatientPanel.add(txtLastName);
        addPatientPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        addPatientPanel.add(txtDOB);
        addPatientPanel.add(new JLabel("Gender:"));
        addPatientPanel.add(txtGender);
        JButton btnSavePatient = new JButton("Save Patient");
        addPatientPanel.add(btnSavePatient);

        contentPanel.add(addPatientPanel, "Add Patient");

        // View Appointments Panel
        JPanel viewAppointmentsPanel = new JPanel();
        JTextArea txtAppointments = new JTextArea(20, 50);
        viewAppointmentsPanel.add(new JScrollPane(txtAppointments));
        contentPanel.add(viewAppointmentsPanel, "View Appointments");

        // Medical History Panel
        JPanel medicalHistoryPanel = new JPanel();
        JTextArea txtMedicalHistory = new JTextArea(20, 50);
        medicalHistoryPanel.add(new JScrollPane(txtMedicalHistory));
        contentPanel.add(medicalHistoryPanel, "Medical History");

        frame.add(contentPanel, BorderLayout.CENTER);

        // Button Actions
        btnAddPatient.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Add Patient"));
        btnViewAppointments.addActionListener(e -> {
            try (Connection conn = connect()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Appointments");
                txtAppointments.setText("");
                while (rs.next()) {
                    txtAppointments.append("Appointment ID: " + rs.getInt("AppointmentID") + "\n");
                    txtAppointments.append("Patient ID: " + rs.getInt("PatientID") + "\n");
                    txtAppointments.append("Doctor: " + rs.getString("DoctorName") + "\n");
                    txtAppointments.append("Date: " + rs.getDate("AppointmentDate") + "\n");
                    txtAppointments.append("Reason: " + rs.getString("Reason") + "\n\n");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "View Appointments");
        });

        btnViewAppointments.addActionListener(e -> {
            try (Connection conn = connect()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Appointments");
                txtAppointments.setText(""); // Clear existing text
        
                boolean hasData = false; // Flag to check if data exists
                while (rs.next()) {
                    hasData = true;
                    txtAppointments.append("Appointment ID: " + rs.getInt("AppointmentID") + "\n");
                    txtAppointments.append("Patient ID: " + rs.getInt("PatientID") + "\n");
                    txtAppointments.append("Doctor: " + rs.getString("DoctorName") + "\n");
                    txtAppointments.append("Date: " + rs.getDate("AppointmentDate") + "\n");
                    txtAppointments.append("Reason: " + rs.getString("Reason") + "\n\n");
                }
                if (!hasData) {
                    txtAppointments.setText("No appointments found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "View Appointments");
        });
        
        
        btnMedicalHistory.addActionListener(e -> {
            try (Connection conn = connect()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM MedicalHistory");
                txtMedicalHistory.setText(""); // Clear existing text
        
                boolean hasData = false; // Flag to check if data exists
                while (rs.next()) {
                    hasData = true;
                    txtMedicalHistory.append("History ID: " + rs.getInt("HistoryID") + "\n");
                    txtMedicalHistory.append("Patient ID: " + rs.getInt("PatientID") + "\n");
                    txtMedicalHistory.append("Diagnosis: " + rs.getString("Diagnosis") + "\n");
                    txtMedicalHistory.append("Treatment: " + rs.getString("Treatment") + "\n\n");
                }
                if (!hasData) {
                    txtMedicalHistory.setText("No medical history found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Medical History");
        });
        
        
        btnSavePatient.addActionListener(e -> {
            try (Connection conn = connect()) {
                if (conn != null) {
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Patients (FirstName, LastName, DOB, Gender) VALUES (?, ?, ?, ?)");
                    pstmt.setString(1, txtFirstName.getText());
                    pstmt.setString(2, txtLastName.getText());
                    pstmt.setDate(3, Date.valueOf(txtDOB.getText()));
                    pstmt.setString(4, txtGender.getText());
                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(frame, "Patient added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to add patient.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please use YYYY-MM-DD.");
            }
        });
        
        frame.setVisible(true);
    }
}
