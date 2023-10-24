package multiplayergolfgame.Client.Panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;

import multiplayergolfgame.Client.ClientView;

/**
 * Used for configuring connection to server and user customization
 * @author James Eastwood
 */
public class  ClientConnectionPanel extends JPanel 
{
    private JPanel connectionPanel;
    private JPanel customizationPanel;
    
    private JComboBox<String> ipField;
    private JTextField portField;
    private JButton serverConnectButton;

    private CanvasPanel customizationCanvasPanel;
    private JButton colorButton;
    private JTextField nicknameField;

    private String commonIPs[] =
    {
        "127.0.0.1",
        "cs.merrimack.edu"
    };

    /**
     *
     */
    public ClientConnectionPanel()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        connectionPanel = new JPanel();
        createConnectionPanel();

        customizationPanel = new JPanel()   ;
        createCustomizationPanel();

        this.add(Box.createRigidArea(new Dimension(200, 1)));
        this.add(connectionPanel);
        this.add(Box.createRigidArea(new Dimension(100, 1)));
        this.add(customizationPanel);
        this.add(Box.createRigidArea(new Dimension(200, 1)));
    }

    /**
     * Gets the IP inputted by the user
     * @return ip as a string
     */
    public String getIP()
    {
        return (String) this.ipField.getSelectedItem();
    }

    /**
     * The port specified by the user.
     * @return the port as an integer.
     */
    public int getPort()
    {
        return Integer.parseInt(this.portField.getText());
    }

    /**
     * Adds a action listener to the "Connect" button, triggers on user press.
     * @param l the action listener
     */
    public void addServerConnectListener(ActionListener l)
    {
        this.serverConnectButton.addActionListener(l);
    }

    /**
     * gets the nickname specified by the user
     * @return the nickname as a string
     */
    public String getNickname()
    {
        return this.nicknameField.getText();
    }

    /**
     * Gets the color speicified by the user
     * @return the color
     */
    public Color getChosenColor()
    {
        return this.customizationCanvasPanel.getColor();
    }

    /* Creates the connection panel */
    private void createConnectionPanel()
    {
        GridBagConstraints gbc = new GridBagConstraints();
        connectionPanel.setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;

        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 20;
        JLabel mainLabel = new JLabel("Connect to Server", SwingConstants.CENTER);
        mainLabel.setFont(new Font("Sans Serif", Font.BOLD, 32));
        connectionPanel.add(mainLabel, gbc);


        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.ipady = 0;
        JLabel ipLabel = new JLabel("IP: ", SwingConstants.RIGHT);
        ipLabel.setFont(ClientView.labelFont);
        connectionPanel.add(ipLabel, gbc);
        gbc.gridx = 1;
        ipField = new JComboBox<>(commonIPs);
        ipField.setEditable(true);
        ipField.setFont(ClientView.fieldFont);
        connectionPanel.add(ipField, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel portLabel = new JLabel("Port: ", SwingConstants.RIGHT);
        portLabel.setFont(ClientView.labelFont);
        connectionPanel.add(portLabel, gbc);
        gbc.gridx++;
        portField = new JTextField("5002", 12);
        portField.setFont(ClientView.fieldFont);
        connectionPanel.add(portField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        serverConnectButton = new JButton("Connect!");
        connectionPanel.add(serverConnectButton, gbc);
    }

    /* Creates the user cusomtization panel */
    private void createCustomizationPanel()
    {
        GridBagConstraints gbc = new GridBagConstraints();
        customizationPanel.setLayout(new GridBagLayout());

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;

        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 20;
        JLabel mainLabel = new JLabel("Customization", SwingConstants.CENTER);
        mainLabel.setFont(new Font("Sans Serif", Font.BOLD, 32));
        customizationPanel.add(mainLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.ipady = 256;
        customizationCanvasPanel = new CanvasPanel();
        customizationPanel.add(customizationCanvasPanel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.ipady = 0;
        JLabel nameLabel = new JLabel("Name: ", SwingConstants.RIGHT);
        nameLabel.setFont(ClientView.labelFont);
        customizationPanel.add(nameLabel, gbc);

        gbc.gridx++;
        Random randomGenerator = new Random();
        nicknameField = new JTextField("User" + (1000 + randomGenerator.nextInt(8999)), 12);
        nicknameField.setFont(ClientView.fieldFont);
        customizationPanel.add(nicknameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel colorLabel = new JLabel("Ball Color: ", SwingConstants.RIGHT);
        colorLabel.setFont(ClientView.labelFont);
        customizationPanel.add(colorLabel, gbc);

        gbc.gridx++;
        colorButton = new JButton(" ");
        colorButton.setBackground(customizationCanvasPanel.getColor());
        colorButton.addActionListener(new ColorListener(this));
        customizationPanel.add(colorButton, gbc);
    }

    /* Canvas panel used for drawing the ball */
    private class CanvasPanel extends JPanel
    {
        public Color ballColor = Color.RED;

        public void paintComponent(Graphics g)
        {
            g.setColor(ballColor);
            g.fillOval(this.getWidth() / 4, 0, 200, 200);
        }

        public void setColor(Color color)
        {
            this.ballColor = color;
        }
        
        public Color getColor()
        {
            return this.ballColor;
        }
    }

    /* Wrapper for the JColorChooser for the user's ball color */
    private class ColorListener implements ActionListener
    {
        private JPanel parentPanel;

        public ColorListener(JPanel parentPanel)
        {
            this.parentPanel = parentPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {            
            Color newColor = JColorChooser.showDialog(parentPanel, "New Ball Color", customizationCanvasPanel.getColor());

            if(newColor != null)
            {
                customizationCanvasPanel.setColor(newColor);
                colorButton.setBackground(newColor);
                parentPanel.repaint();
            }
        }
    }
}
