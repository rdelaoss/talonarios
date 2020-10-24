package beans;

import dao.DaoEstudiante;
import dao.DaoPersona;
import entidades.Estudiante;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.model_talonario;

@Named(value = "talonariosBeans")
@SessionScoped
public class talonariosBeans implements Serializable {

//    private String inputContent = "abc";
    private EntityManagerFactory conexion = Persistence.createEntityManagerFactory("TalonariosPU");
    private DaoPersona personadao = new DaoPersona(conexion);
    private DaoEstudiante estudiantedao = new DaoEstudiante(conexion);
    private String documento = "-";
    private List<Estudiante> listado = estudiantedao.findEstudianteEntities();
    private ArrayList<model_talonario> talonarios;
    private String idpersona = "";
    private String nombre = "";
    private String apellido = "";
    private String activo = "";
    private String Tcarnet = "";
    private String Tnombre = "";
    private String Tapellido = "";
    private String Tmes = "";
    private String Tgrado = "";
    private String Tcuota = "";
    private String Tmora = "";
    private String Tfdesde = "";
    private String Tfhasta = "";
    private String Tnpe = "";
    private String Tcodebar = "";

    public talonariosBeans() {

    }

    @PostConstruct
    public void init() {
//        imprimir2();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public List<Estudiante> getListado() {
        return listado;
    }

    public void setListado(List<Estudiante> listado) {
        this.listado = listado;
    }

    public ArrayList<model_talonario> getTalonarios() {
        return talonarios;
    }

    public void setTalonarios(ArrayList<model_talonario> talonarios) {
        this.talonarios = talonarios;
    }

    public String getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(String idpersona) {
        this.idpersona = idpersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getTcarnet() {
        return Tcarnet;
    }

    public void setTcarnet(String Tcarnet) {
        this.Tcarnet = Tcarnet;
    }

    public String getTnombre() {
        return Tnombre;
    }

    public void setTnombre(String Tnombre) {
        this.Tnombre = Tnombre;
    }

    public String getTapellido() {
        return Tapellido;
    }

    public void setTapellido(String Tapellido) {
        this.Tapellido = Tapellido;
    }

    public String getTmes() {
        return Tmes;
    }

    public void setTmes(String Tmes) {
        this.Tmes = Tmes;
    }

    public String getTgrado() {
        return Tgrado;
    }

    public void setTgrado(String Tgrado) {
        this.Tgrado = Tgrado;
    }

    public String getTcuota() {
        return Tcuota;
    }

    public void setTcuota(String Tcuota) {
        this.Tcuota = Tcuota;
    }

    public String getTmora() {
        return Tmora;
    }

    public void setTmora(String Tmora) {
        this.Tmora = Tmora;
    }

    public String getTfdesde() {
        return Tfdesde;
    }

    public void setTfdesde(String Tfdesde) {
        this.Tfdesde = Tfdesde;
    }

    public String getTfhasta() {
        return Tfhasta;
    }

    public void setTfhasta(String Tfhasta) {
        this.Tfhasta = Tfhasta;
    }

    public String getTnpe() {
        return Tnpe;
    }

    public void setTnpe(String Tnpe) {
        this.Tnpe = Tnpe;
    }

    public String getTcodebar() {
        return Tcodebar;
    }

    public void setTcodebar(String Tcodebar) {
        this.Tcodebar = Tcodebar;
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
            writer.write(documento);
        } finally {
            writer.close();
        }
    }

    public void imprimir2() {
        talonarios = new ArrayList<model_talonario>();
        Integer valor = 0;
        for (Estudiante estudiante : listado) {
            for (int i = 0; i <= 12; i++) {
                Tcarnet = String.valueOf(estudiante.getPersonaId().getPersonaId());
                Tapellido = estudiante.getPersonaId().getPersonaApellidos();
                Tnombre = estudiante.getPersonaId().getPersonaNombre();
                Tcuota = String.valueOf(estudiante.getMatriculaList().get(0).getControlPagoList().get(i).getControlPagoArancelAsignado());
                Tmes = estudiante.getMatriculaList().get(0).getControlPagoList().get(i).getArancelId().getArancelAlias();
                Tgrado = estudiante.getMatriculaList().get(0).getConfiguracionGradoId().getGradoId().getGradoNombre();
                Tmora = String.valueOf(Double.parseDouble(Tcuota) + 17);
                Tfdesde = "2020-07-01";
                Tfhasta = "2020-07-17";
                Tnpe = "4157419700003137390200000080009620200716";
                Tcodebar = "4157419700003137390200000080009620200716802000002237041022020";
                //System.out.println(Tcarnet + "  " + Tnombre + "  " + Tapellido + "  " + Tmes + "  " + Tcuota + "  " + Tgrado + "  " + Tmora + "  " + Tfdesde + "  " + Tfhasta + "  " + Tnpe + "  " + Tcodebar);
                model_talonario t = new model_talonario(Tcarnet, Tnombre, Tapellido, Tmes, Tgrado, Tcuota, Tmora, Tfdesde, Tfhasta, Tnpe, Tcodebar);
                talonarios.add(t);
            }
            valor++;
        }
        System.out.println("valor -> " + valor);

//        for (model_talonario objtalonario : talonarios) {
//            System.out.println("carnet : " + objtalonario.getCarnet()
//                    + " nombre : " + objtalonario.getNombre() + " "
//                    + "apellido : " + objtalonario.getApellido()
//                    + " cuota : " + objtalonario.getCuota()
//                    + " mes : " + objtalonario.getMes()
//                    + " grado : " + objtalonario.getGrado()
//                    + " mora : " + objtalonario.getMora()
//            );
//        }
    }

}
