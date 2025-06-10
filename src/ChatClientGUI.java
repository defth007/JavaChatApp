import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;

public class ChatClientGUI {
    private static PrintWriter out;

    public static void main(String[] args) {
        // Create the main window
        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);

        // Create UI components
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        JTextField inputField = new JTextField();
        JButton sendButton = new JButton("Send");

        sendButton.setPreferredSize(new Dimension(100, 40));

        // Load and apply custom font
        try {
            InputStream fontStream = ChatClientGUI.class.getResourceAsStream("/MinecraftFont.otf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            chatArea.setFont(customFont);
            inputField.setFont(customFont);
            sendButton.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Layout
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Show window
        frame.setVisible(true);

        String username = JOptionPane.showInputDialog(frame, "Enter your username:");
        if (username == null || username.trim().isEmpty()) {
            System.exit(0); // Exit if they cancel or enter nothing
        }

        try {
            Socket socket = new Socket("localhost", 1234);

            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(username); // Send username before anything else

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread to listen for messages from server
            Thread readThread = new Thread(() -> {
                try {
                    String msgFromServer;
                    while ((msgFromServer = in.readLine()) != null) {
                        chatArea.append(msgFromServer + "\n");
                    }
                } catch (IOException e) {
                    chatArea.append("Connection closed.\n");
                }
            });
            readThread.start();

        } catch (IOException e) {
            chatArea.append("Failed to connect to server: " + e.getMessage() + "\n");
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
                    inputField.setText("");
                }
            }
        };

        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction); // allows hitting Enter to send
    }
}
