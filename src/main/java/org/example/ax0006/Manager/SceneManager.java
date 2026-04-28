package org.example.ax0006.Manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ax0006.Controller.*;
import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private Stage stage;
    private ContextManager context;
    private Integer contratoTemporal;

    public SceneManager(Stage stage, ContextManager context) {
        this.stage = stage;
        this.context = context;
    }

    public void showLogin() throws IOException {
        LoginController loginController = new LoginController(this, context.getAutenService(), context.getSesion());
        loadScene("/org/example/ax0006/login.fxml", loginController);
    }

    public void showSignUp() throws IOException {
        SignUpController signUpControl = new SignUpController(this, context.getAutenService(), context.getSesion());
        loadScene("/org/example/ax0006/signup.fxml", signUpControl);
    }

    public void showMenu() throws IOException {
        MenuController menuControl = new MenuController(this, context.getSesion(), context.getConciertoService());
        loadScene("/org/example/ax0006/menu.fxml", menuControl);
    }

    public void showAdminUsuarios() throws IOException {
        AdminUsuariosController controller = new AdminUsuariosController(context.getSesion(), context.getRolService(), this, context.getConciertoService(), context.getStaffService());
        loadScene("/org/example/ax0006/adminUsuarios.fxml", controller);
    }

    public void showProfile() throws IOException {
        ProfileController profileController = new ProfileController(this, context.getSesion(), context.getProfileService());
        loadScene("/org/example/ax0006/profile.fxml", profileController);
    }

    public void showEditProfile() throws IOException {
        EditProfileController editProfileController = new EditProfileController(this, context.getSesion(), context.getProfileService());
        loadScene("/org/example/ax0006/editprofile.fxml", editProfileController);
    }

    public void showChangePassword() throws IOException {
        ChangePasswordController changePasswordController = new ChangePasswordController(this, context.getSesion(), context.getProfileService());
        loadScene("/org/example/ax0006/changepassword.fxml", changePasswordController);
    }

    public void showConsultarSolicitudes() throws IOException {
        ConsultarSolicitudesController controller = new ConsultarSolicitudesController(context.getSesion(), context.getConciertoService(), this);
        loadScene("/org/example/ax0006/consultarsolicitudes.fxml", controller);
    }

    public void showCrearConcierto() throws IOException {
        if (!"crearContrato".equals(context.getSesion().getPantallaOrigen())) {
            context.getSesion().setIdContratoTemporal(null);
            context.getSesion().setConciertoTemporal(null);
        }
        context.getSesion().setPantallaOrigen(null);
        CrearConciertoController controller = new CrearConciertoController(context.getSesion(), context.getConciertoService(), this);
        loadScene("/org/example/ax0006/crearconcierto.fxml", controller);
    }

    public void showConciertosProgramados() throws IOException {
        ConciertosProgramadosController controller = new ConciertosProgramadosController(context.getSesion(), context.getConciertoService(), this);
        loadScene("/org/example/ax0006/verconciertosprogramados.fxml", controller);
    }

    public void showCrearContrato() throws IOException {
        CrearContratoController controller = new CrearContratoController(this, context.getContratoService(), context.getSesion());
        loadScene("/org/example/ax0006/crearcontrato.fxml", controller);
    }

    public void showConsultarContrato() throws IOException {
        ConsultarContratoController controller = new ConsultarContratoController(this, context.getContratoService());
        loadScene("/org/example/ax0006/consultarcontrato.fxml", controller);
    }

    public void showMenuConcierto() throws IOException {
        MenuConciertoController controller = new MenuConciertoController(this, context.getSesion());
        loadScene("/org/example/ax0006/menuconcierto.fxml", controller);
    }

    public void showVerContrato() throws IOException {
        VerContratoController controller = new VerContratoController(this, context.getContratoService(), context.getSesion());
        loadScene("/org/example/ax0006/vercontrato.fxml", controller);
    }

    /* --- MÉTODOS DE INVENTARIO --- */

    public void showCrearObjeto() throws IOException {
        ObjetoController controller = new ObjetoController(context.getObjetoService(), this);
        loadScene("/org/example/ax0006/crearObjeto.fxml", controller);
    }

    public void showSeleccionarConciertoInventario() throws IOException {
        SeleccionarConciertoInventarioController controller = new SeleccionarConciertoInventarioController(
                context.getConciertoService(),
                this,
                context.getSesion()
        );
        loadScene("/org/example/ax0006/seleccionarConcierto.fxml", controller);
    }

    public void showCrearInventario() throws IOException {
        crearInventarioController controller = new crearInventarioController(
                context.getInventarioService(),
                context.getObjetoService(), // Añade esto
                this,
                context.getSesion()
        );
        loadScene("/org/example/ax0006/crearInventario.fxml", controller);
    }

    public void showDetallesConcierto() throws IOException {
        DetallesConciertoController controller = new DetallesConciertoController(
                this,
                context.getSesion(),
                context.getInventarioService()
        );

        loadScene("/org/example/ax0006/DetallesConcierto.fxml", controller);
    }

    public void setContratoTemporal(Integer id) { this.contratoTemporal = id; }
    public Integer getContratoTemporal() { return contratoTemporal; }

    private void loadScene(String fxml, Object controller) throws IOException {
        URL resource = getClass().getResource(fxml);

        if (resource == null) {
            System.err.println("[FATAL ERROR] No se encontró el FXML: " + fxml);
            throw new IOException("Location is not set: El archivo FXML no existe en la ruta: " + fxml);
        }

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setController(controller);

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }
}