package com.whatsapp.swing;

import javax.swing.*;
import java.awt.*;

public class UserListCellRenderer extends JLabel implements ListCellRenderer<String> {
    
    private ImageIcon userIcon; // Declare an ImageIcon for the user icon

    public UserListCellRenderer() {
        setOpaque(true);  // Allows background color to be visible
        userIcon = new ImageIcon(getClass().getResource("/com/whatsapp/icons/user.png")); // Adjust the path as necessary
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        
        setText(value);  // Set the text for the cell
        setIcon(userIcon); // Set the user icon

        Color background = Color.WHITE;
        Color foreground = Color.BLACK;

        // Apply styles for selected or hovered cells
        if (isSelected) {
            background = new Color(38, 211, 124);  // Light green for selected item
            foreground = Color.WHITE;
        } else if (cellHasFocus) {
            background = new Color(240, 240, 240);  // Light grey for focus
        }

        // Set background and foreground colors
        setBackground(background);
        setForeground(foreground);

        // Set the border with spacing
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(38, 211, 124)), 
            BorderFactory.createEmptyBorder(5, 5, 10, 5)
        ));

        // Set font and alignment
        setFont(new Font("SansSerif", Font.PLAIN, 18));
        setHorizontalAlignment(SwingConstants.LEFT);
        
        // Set the icon text position and gap
        setHorizontalTextPosition(SwingConstants.RIGHT); // Text is to the right of the icon
        setIconTextGap(10); // Set gap between the icon and text

        return this;
    }
}
