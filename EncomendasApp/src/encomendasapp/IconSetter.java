package encomendasapp;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class IconSetter {
    public static void setIcon(JFrame frame) {
        Image iconeTitulo =
            Toolkit.getDefaultToolkit().getImage("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\background\\logo.jpg");
        frame.setIconImage(iconeTitulo);
    }
}
