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
import javax.imageio.ImageIO;
import javax.swing.JLabel;

@Named(value = "talonariosBeans")
@SessionScoped
public class talonariosBeans implements Serializable {

    private BarCode bc2;
    private BufferedImage image2;

    public talonariosBeans() {
        try {
            bc2 = new BarCode();
            bc2.barType = bc2.CODE128;

            bc2.setDataToEncode("4157419700003137390200000080009620200716802000002237041022020");

            bc2.setSize(400, 200);

            image2 = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image2.createGraphics();
            bc2.paint(g2);

            JLabel jLabelLogo2 = new JLabel();
            ImageIcon imagen2 = new ImageIcon(image2);
            Icon icono2 = new ImageIcon(imagen2.getImage().getScaledInstance(jLabelLogo2.getWidth(), jLabelLogo2.getHeight(), Image.SCALE_DEFAULT));
            jLabelLogo2.setSize(icono2.getIconWidth() + 10, icono2.getIconHeight() + 40);
            jLabelLogo2.setIcon(icono2);
            jLabelLogo2.repaint();

            // Get icon from label
            ImageIcon icon = (ImageIcon) jLabelLogo2.getIcon();
            // Copy image
            BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g3 = image.createGraphics();
            g3.drawImage(icon.getImage(), 0, 0, icon.getImageObserver());
            g3.dispose();
            BufferedImage dest = image.getSubimage(0, 13, 825, 40);
            ImageIO.write(dest, "jpg", new File("C:\\Users\\Administrador.ARRUPE\\Desktop\\barcode\\GS1.jpg"));
        } catch (Exception e) {
        }

    }

}
