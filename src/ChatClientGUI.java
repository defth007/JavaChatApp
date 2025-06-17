import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;

// Chat client with Minecraft-inspired GUI
public class ChatClientGUI {
    private static PrintWriter out; // Used to send messages to the server

    public static void main(String[] args) {
        // Create the main window
        ImageIcon bgIcon = new ImageIcon(ChatClientGUI.class.getResource("/MinecraftBackground.png"));
        BackgroundPanel backgroundPanel = new BackgroundPanel(bgIcon.getImage());

        JFrame frame = new JFrame("Chat Client");
        frame.setContentPane(backgroundPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        ImageIcon icon = new ImageIcon(ChatClientGUI.class.getResource("/ChatApp.png"));
        frame.setIconImage(icon.getImage());

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        sendButton.setPreferredSize(new Dimension(100, 40));

        // Load and apply custom font
        try {
            InputStream fontStream = ChatClientGUI.class.getResourceAsStream("/MinecraftFont.otf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(25f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            Font chatFont = customFont.deriveFont(30f);
            chatArea.setFont(chatFont);
            chatArea.setOpaque(false);
            chatArea.setBackground(new Color(0, 0, 0, 0)); // Fully transparent
            chatArea.setMargin(new Insets(10, 10, 10, 10)); // top, left, bottom, right

            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);

            sendButton.setFont(customFont);
            sendButton.setFocusPainted(false);
            sendButton.setContentAreaFilled(false);
            sendButton.setOpaque(true);
            sendButton.setBackground(Color.GRAY);
            sendButton.setForeground(Color.WHITE);
            sendButton.setBorder(BorderFactory.createLineBorder(Color.PINK.brighter(), 5));

            inputField.setFont(customFont);
            inputField.setOpaque(true);
            inputField.setBackground(Color.PINK.brighter());
            inputField.setForeground(Color.BLACK); // Makes the text visible
            inputField.setCaretColor(Color.BLACK); // Cursor visibility
            inputField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Layout for components
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.setOpaque(false);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Show window
        frame.setVisible(true);

        // Prompt for username
        String username = JOptionPane.showInputDialog(frame, "Enter your username:");
        if (username == null || username.trim().isEmpty()) {
            System.exit(0); // Exit if they cancel or enter nothing
        }

        // Get connection details from user
        String connection = JOptionPane.showInputDialog(frame, "Enter ngrok connection URL:");
        if (connection == null || connection.trim().isEmpty()) {
            System.exit(0); // Exit if they cancel or enter nothing
        }

        String host;
        int port;

        try {
            // Parse ngrok TCP URL into host and port
            if (connection.startsWith("tcp://")) {
                String[] parts = connection.replace("tcp://", "").split(":");
                host = parts[0];
                port = Integer.parseInt(parts[1]);
            } else {
                throw new IllegalArgumentException("URL must start with tcp://");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid URL format. Please use format: tcp://host:port");
            System.exit(0);
            return;
        }

        // Try to connect to the server
        try {
            Socket socket = new Socket(host, port);

            // Output stream to send messages
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(username); // Send username before anything else

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread to listen for messages from server
            Thread readThread = new Thread(() -> {
                try {
                    String msgFromServer;
                    while ((msgFromServer = in.readLine()) != null) {
                        chatArea.append(msgFromServer + "\n");
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                    }
                } catch (IOException e) {
                    chatArea.append("Connection closed.\n");
                }
            });
            readThread.start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Server is not running. Please try again later.");
            sendButton.setEnabled(false);
            inputField.setEnabled(false);
        }

        // Send message on button click or Enter key
        Action sendAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText().trim();
                if (!message.isEmpty()) {
                    out.println(message);
                    chatArea.append(message + "\n");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                    inputField.setText("");
                }
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction); // allows hitting Enter to send
    }
}

class BackgroundPanel extends JPanel {
    private final Image backgroundImage;

    public BackgroundPanel(Image image) {
        this.backgroundImage = image;
        setLayout(new BorderLayout()); // So you can still add components
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the image to fill the entire panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}