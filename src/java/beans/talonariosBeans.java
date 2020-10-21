package beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

@Named(value = "talonariosBeans")
@SessionScoped
public class talonariosBeans implements Serializable {

//    private String inputContent = "Regueton";
    private String alumnoSolicitudNumeroL = "-";

    public talonariosBeans() {
    }

    public String getAlumnoSolicitudNumeroL() {
        return alumnoSolicitudNumeroL;
    }

    public void setAlumnoSolicitudNumeroL(String alumnoSolicitudNumeroL) {
        this.alumnoSolicitudNumeroL = alumnoSolicitudNumeroL;
    }

    public void creargs1() {
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

            writeStream();
        } catch (Exception e) {
            System.out.println("ERROR : " + e);
        }
        System.out.println("AQUI TOY");
    }

    public void writeStream() throws IOException {
        Writer writer = new FileWriter("C:\\barcodegs1\\outputOne.txt");

        try {
            writer.write(alumnoSolicitudNumeroL);
        } finally {
            writer.close();
        }
    }

}
