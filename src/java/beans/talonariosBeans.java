package beans;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.swing.Icon;
import javax.swing.ImageIcon;
// |...:: [ ------------------- ] ::... |
import com.idautomation.linear.*;
import com.idautomation.linear.encoder.*;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

@Named(value = "talonariosBeans")
@SessionScoped
public class talonariosBeans implements Serializable {

    private BarCode bc;
    private BufferedImage image2;

    public talonariosBeans() {

    }

    public void preba() {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(Arrays.asList("javac", "C:\\NetBeans_JSFPF\\NetbeansProject\\gs1demo\\src\\gs1demo\\prueba.java"));
        } catch (Exception e) {
            System.out.println("ERROR : " + e);
        }
        System.out.println("AQUI TOY");
    }

}
