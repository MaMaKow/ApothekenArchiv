package com.apothekeammarienplatz.apothekenarchiv;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainMenu extends JFrame {

    private JPanel cardPanel, startJPanel, ausgangsstoffPanel, centerPanel, plausiJPanel;
    private JLabel startPanelLabel, centerJLabel;
    private JButton navigateToAusgangsstoffButton;
    private CardLayout cardLayout = new CardLayout();
    private final JButton navigateToPlausiJButton;
    private final JButton navigateToHerstellungJButton;
    private final JPanel buttonPanel;

    public MainMenu() {
        setTitle("Archiv Apotheke");

        cardPanel = new JPanel();
        cardPanel.setLayout(cardLayout);
        ausgangsstoffPanel = new JPanel();
        plausiJPanel = new PlausibilitätsArchiv();
        startPanelLabel = new JLabel("Dies ist das startPanel");
        centerJLabel = new JLabel("Bitte wählen Sie eine der obigen Optionen.");

        startJPanel = new JPanel();
        //startJPanel = new MainMenu();
        startJPanel.setLayout(new GridLayout(5, 1, 10, 30));
        startJPanel.setBorder(BorderFactory.createTitledBorder("\"startJPanelString\""));
        startJPanel.add(startPanelLabel);
        /**
         * Create a button panel, which holds the navigation buttons:
         */
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createTitledBorder("\"buttonPanelString\""));
        /**
         * Create an information panel in the center of the Frame
         */
        centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createTitledBorder("\"centerPanelString\""));
        centerPanel.add(centerJLabel);

        cardPanel.add(startJPanel, "startJPanelName");
        cardPanel.add(ausgangsstoffPanel, "ausgangsstoffJPanelName");
        cardPanel.add(plausiJPanel, "plausiJPanelName");

        navigateToAusgangsstoffButton = new JButton("Ausgangsstoff");
        navigateToAusgangsstoffButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "ausgangsstoffJPanelName");
            }
        });
        navigateToPlausiJButton = new JButton("Plausi");
        navigateToPlausiJButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "plausiJPanelName");
            }
        });
        navigateToHerstellungJButton = new JButton("Herstellungsprotokoll");
        navigateToHerstellungJButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "2");
            }
        });
        buttonPanel.add(navigateToAusgangsstoffButton, BorderLayout.LINE_START);
        buttonPanel.add(navigateToPlausiJButton);
        buttonPanel.add(navigateToHerstellungJButton);
        startJPanel.add(buttonPanel);
        startJPanel.add(centerPanel, BorderLayout.PAGE_END);
        //buttonPanel.add(btn1);
        //buttonPanel.add(btn2);
        add(cardPanel, BorderLayout.NORTH);

        pack();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                MainMenu frame = new MainMenu();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
