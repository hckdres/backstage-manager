package org.example.ax0006.Manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ax0006.Controller.*;

import java.io.IOException;

public class SceneManager {

    private final Stage stage;
    private final ContextManager context;

    public SceneManager(Stage stage, ContextManager context) {
        this.stage = stage;
        this.context = context;
    }

    public void showLogin() throws IOException {
        LoginController controller = new LoginController(this, context.getAutenService(), context.getSesion());
        loadScene("/org/example/ax0006/login.fxml", controller);
    }

    public void showSignUp() throws IOException {
        SignUpController controller = new SignUpController(this, context.getAutenService(), context.getSesion());
        loadScene("/org/example/ax0006/signup.fxml", controller);
    }

    public void showMenu() throws IOException {
        MenuController controller = new MenuController(this, context.getSesion(), context.getConciertoService());
        loadScene("/org/example/ax0006/menu.fxml", controller);
    }

    public void showAdminUsuarios() throws IOException {
        AdminUsuariosController controller = new AdminUsuariosController(
                context.getSesion(),
                context.getRolService(),
                this,
                context.getConciertoService(),
                context.getStaffService()
        );
        loadScene("/org/example/ax0006/adminUsuarios.fxml", controller);
    }

    public void showProfile() throws IOException {
        ProfileController controller = new ProfileController(this, context.getSesion(), context.getProfileService());
        loadScene("/org/example/ax0006/profile.fxml", controller);
    }

    public void showEditProfile() throws IOException {
        EditProfileController controller = new EditProfileController(this, context.getSesion(), context.getProfileService());
        loadScene("/org/example/ax0006/editprofile.fxml", controller);
    }

    public void showChangePassword() throws IOException {
        ChangePasswordController controller = new ChangePasswordController(this, context.getSesion(), context.getProfileService());
        loadScene("/org/example/ax0006/changepassword.fxml", controller);
    }

    public void showConsultarSolicitudes() throws IOException {
        ConsultarSolicitudesController controller = new ConsultarSolicitudesController(
                context.getSesion(),
                context.getConciertoService(),
                this
        );
        loadScene("/org/example/ax0006/consultarsolicitudes.fxml", controller);
    }

    public void showCrearConcierto() throws IOException {
        CrearConciertoController controller = new CrearConciertoController(
                context.getSesion(),
                context.getConciertoService(),
                this
        );
        loadScene("/org/example/ax0006/crearconcierto.fxml", controller);
    }

    public void showConciertosProgramados() throws IOException {
        ConciertosProgramadosController controller = new ConciertosProgramadosController(
                context.getSesion(),
                context.getConciertoService(),
                this
        );
        loadScene("/org/example/ax0006/verconciertosprogramados.fxml", controller);
    }

    public void showMenuConcierto() throws IOException {
        MenuConciertoController controller = new MenuConciertoController(this, context.getSesion());
        loadScene("/org/example/ax0006/menuconcierto.fxml", controller);
    }

    public void showLiquidacionHoras() throws IOException {
        NominaController controller = new NominaController(
                this,
                context.getSesion(),
                context.getConciertoService(),
                context.getNominaService(),
                context.getStaffService()
        );
        loadScene("/org/example/ax0006/liquidacionhoras.fxml", controller);
    }

    private void loadScene(String fxmlPath, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}