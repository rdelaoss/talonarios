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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

@Named(value = "talonariosBeans")
@SessionScoped
public class talonariosBeans implements Serializable {

    public talonariosBeans() {
    }

    public void preba() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "C:\\barcodegs1\\creadorGs1.jar");
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s = "";
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
            int status = p.waitFor();
            System.out.println("Exited with status: " + status);

        } catch (Exception e) {
            System.out.println("ERROR : " + e);
        }
        System.out.println("AQUI TOY");
    }

}
