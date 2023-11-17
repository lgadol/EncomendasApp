package encomendasapp;

import java.awt.Image;

import javax.swing.ImageIcon;

public class IconManager {
    public static ImageIcon resizeIcon(String iconPath, int width, int height) {
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}
