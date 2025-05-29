import javax.swing.*;
import java.awt.*;

public class ChatClientGUI {
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

        // Layout
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Show window
        frame.setVisible(true);
    }
}
