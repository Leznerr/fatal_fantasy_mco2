package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The player character management view for Fatal Fantasy: Tactics Game.
 * <p>
 * Allows users to view, create, edit, and delete characters belonging to a given player.
 * This view is strictly responsible for GUI rendering and event forwarding.
 */
public class PlayerCharManagementView extends JFrame {
    private int playerID;

    // Button labels
    public static final String VIEW_CHAR = "View Characters";
    public static final String CREATE_CHAR = "Create Character";
    public static final String EDIT_CHAR = "Edit Character";
    public static final String DELETE_CHAR = "Delete Character";
    public static final String RETURN = "Return";

    // UI components
    private JButton btnViewChar;
    private JButton btnCreateChar;
    private JButton btnEditChar;
    private JButton btnDeleteChar;
    private JButton btnReturn;

    private ActionListener externalListener;

    /**
     * Constructs the Player Character Management UI for a specific player.
     * 
     * @param playerID The ID of the player
     */
    public PlayerCharManagementView(int playerID) {
        super("Fatal Fantasy: Tactics | Player " + playerID + " Management");

        this.playerID = playerID;

        initUI();

        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    PlayerCharManagementView.this,
                    "Are you sure you want to quit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION && externalListener != null) {
                    ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Exit");
                    externalListener.actionPerformed(evt);
                }
            }
        });

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Initializes the GUI layout and components.
     */
    private void initUI() {
        JPanel backgroundPanel = new JPanel() {
            private Image bg = new ImageIcon("view/assets/CharAndPlayerCharManagBG.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int imgWidth = bg.getWidth(this);
                int imgHeight = bg.getHeight(this);
                double scale = Math.max(
                    panelWidth / (double) imgWidth,
                    panelHeight / (double) imgHeight
                );
                int width = (int) (imgWidth * scale);
                int height = (int) (imgHeight * scale);
                int x = (panelWidth - width) / 2;
                int y = (panelHeight - height) / 2;
                g.drawImage(bg, x, y, width, height, this);
            }
        };

        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.add(Box.createVerticalStrut(60));

        String headlineImagePath = String.format("view/assets/Player%dCharManagLogo.png", playerID);
        ImageIcon logoIcon = new ImageIcon(headlineImagePath);
        Image logoImg = logoIcon.getImage().getScaledInstance(500, -1, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(logoLabel);

        backgroundPanel.add(Box.createVerticalStrut(60));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        btnViewChar = new RoundedButton(VIEW_CHAR);
        btnCreateChar = new RoundedButton(CREATE_CHAR);
        btnEditChar = new RoundedButton(EDIT_CHAR);
        btnDeleteChar = new RoundedButton(DELETE_CHAR);
        btnReturn = new RoundedButton(RETURN);

        buttonPanel.add(btnViewChar);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnCreateChar);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnEditChar);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnDeleteChar);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(btnReturn);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(buttonPanel);

        backgroundPanel.add(Box.createVerticalGlue());
        setContentPane(backgroundPanel);
    }

    /**
     * Sets the action listener for all button events.
     *
     * @param listener The listener
     */
    public void setActionListener(ActionListener listener) {
        this.externalListener = listener;
        btnViewChar.addActionListener(listener);
        btnCreateChar.addActionListener(listener);
        btnEditChar.addActionListener(listener);
        btnDeleteChar.addActionListener(listener);
        btnReturn.addActionListener(listener);
    }

    /**
     * Gets the player ID assigned to this view.
     *
     * @return player ID
     */
    public int getPlayerID() {
        return playerID;
    }
}
