package simulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;



public class Simulator extends Frame {
    private TextField textField1;

    public Simulator() {
        setTitle("Attendance Marking");
        setSize(350, 180);
        setLayout(new BorderLayout());
        Image icon = Toolkit.getDefaultToolkit().getImage("assets/fingerprint.png");
        setIconImage(icon);
        Panel inputPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        Label label1 = new Label("Employee ID:");
        label1.setFont(new Font("Poppins", Font.PLAIN, 14)); // Set font to Poppins
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(label1, gbc);

        textField1 = new TextField(20);
        textField1.setFont(new Font("Poppins", Font.PLAIN, 14)); // Set font to Poppins
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(textField1, gbc);

        // Load the image for the button
        BufferedImage markAttendanceImage = loadImage("assets/fingerprintButton.png");
        JButton markAttendanceButton = new JButton(new ImageIcon(markAttendanceImage.getScaledInstance(70, 50, Image.SCALE_SMOOTH)));
        markAttendanceButton.setContentAreaFilled(false); // Remove default button background
        markAttendanceButton.setBorderPainted(false);
//        Image markAttendanceImage = Toolkit.getDefaultToolkit().getImage("assets/fingerprint.png");
//        Image scaledImage = markAttendanceImage.getScaledInstance(70, 70, Image.SCALE_DEFAULT); // Set desired width and height
//        Simulator.ImageButton markAttendanceButton = new Simulator.ImageButton(scaledImage);
        markAttendanceButton.setPreferredSize(new Dimension(70, 70));
        markAttendanceButton.setBackground(Color.decode("#4CAF50")); // Green color
        markAttendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int input1 = Integer.parseInt(textField1.getText());
                System.out.println("Input from Field 1: " + input1);
                LocalDate date = LocalDate.now();
                LocalTime time = LocalTime.now();
                time = time.truncatedTo(ChronoUnit.SECONDS);
                System.out.println(date);
                System.out.println(time);
                try{
                    Socket socket = new Socket("localhost",3000);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                    Data data = new Data(input1, time, date);
                    System.out.println("Data Object Created Successfully");
                    out.writeObject(data);

                    socket.close();
                }catch (IOException err){
                    err.printStackTrace();
                }
            }
        });

        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.white); // Set background color to white
        buttonPanel.add(markAttendanceButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void setSize(int width, int height) {
        int minWidth = 720;
        int minHeight = 360;
        width = Math.max(width, minWidth);
        height = Math.max(height, minHeight);
        super.setSize(width, height);
    }
    private BufferedImage loadImage(String imagePath) {
        try {
            File file = new File(imagePath);
            return ImageIO.read(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        // Define gradient colors
        Color color1 = new Color(135, 206, 250); // Light sky blue
        Color color2 = new Color(0, 191, 255); // Deep sky blue

        // Create gradient
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);

        // Set gradient paint
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.setVisible(true);
    }
}

//import java.awt.*;
//import java.awt.event.*;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.temporal.ChronoUnit;
//
//public class Simulator.Simulator extends Frame {
//    private TextField textField1;
//
//    public Simulator.Simulator() {
//        setTitle("Biometric Simulation Tool");
//        setSize(350, 180);
//        setLayout(new BorderLayout());
//        Image icon = Toolkit.getDefaultToolkit().getImage("assets/fingerprint.png");
//        setIconImage(icon);
//        Panel inputPanel = new Panel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5); // Padding
//
//        Label label1 = new Label("Employee ID:");
//        label1.setFont(new Font("Poppins", Font.PLAIN, 14)); // Set font to Poppins
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        inputPanel.add(label1, gbc);
//
//        textField1 = new TextField(20);
//        textField1.setFont(new Font("Poppins", Font.PLAIN, 14)); // Set font to Poppins
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        inputPanel.add(textField1, gbc);
//
//        Button submitButton = new Button("Mark Attendance");
//        submitButton.setBackground(Color.decode("#4CAF50")); // Green color
//        submitButton.setForeground(Color.white);
//        submitButton.setFont(new Font("Poppins", Font.BOLD, 14)); // Set font to Poppins
//        submitButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int input1 = Integer.parseInt(textField1.getText());
//                System.out.println("Input from Field 1: " + input1);
//                LocalDate date = LocalDate.now();
//                LocalTime time = LocalTime.now();
//                time = time.truncatedTo(ChronoUnit.SECONDS);
//                System.out.println(date);
//                System.out.println(time);
//            }
//        });
//
//        add(inputPanel, BorderLayout.CENTER);
//
//        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
//        buttonPanel.setBackground(Color.white); // Set background color to white
//        buttonPanel.add(submitButton);
//
//        add(buttonPanel, BorderLayout.SOUTH);
//
//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                dispose();
//            }
//        });
//    }
//
//    @Override
//    public void setSize(int width, int height) {
//        int minWidth = 720;
//        int minHeight = 360;
//        width = Math.max(width, minWidth);
//        height = Math.max(height, minHeight);
//        super.setSize(width, height);
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        super.paint(g);
//        Graphics2D g2d = (Graphics2D) g;
//        int w = getWidth();
//        int h = getHeight();
//
//        // Define gradient colors
//        Color color1 = new Color(135, 206, 250); // Light sky blue
//        Color color2 = new Color(0, 191, 255); // Deep sky blue
//
//        // Create gradient
//        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
//
//        // Set gradient paint
//        g2d.setPaint(gp);
//        g2d.fillRect(0, 0, w, h);
//    }
//
//    public static void main(String[] args) {
//        Simulator.Simulator simulator = new Simulator.Simulator();
//        simulator.setVisible(true);
//    }
//}
//
