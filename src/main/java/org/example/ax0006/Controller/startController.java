package org.example.ax0006.Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ax0006.Repository.usuarioRepository;
import org.example.ax0006.Service.autenticacionService;
import org.example.ax0006.db.H2;

import java.io.IOException;

public class startController extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // BASE DE DATOS
        H2 h2 = new H2();
        h2.inicializarDB();

        // REPOSITORIOS
        usuarioRepository usuarioRepo = new usuarioRepository(h2);

        // SERVICIOS
        autenticacionService authService = new autenticacionService(usuarioRepo);

        // SE CARGA LA PRIMERA VISTA
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/ax0006/login.fxml")
        );

        // SE CARGA EL CONTROLADOR DE LOGIN
        loginController controller = new loginController(usuarioRepo);

        loader.setController(controller);

        Scene scene = new Scene(loader.load());
        stage.setTitle("BACKSTAGE-MANAGER");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /*
    * CAMBIOS PRINCIPALES:
    * 1. LOS CONTROLADORES AHORA TIENEN CONSTRUCTOR
    * 2. SE CAMBIA DE ESCENA DE FORMA DIFERENTE, PERMITIENDO LA INYECCION DE DEPENDENCIAS SIN UTILIZAR SINGLETONS (COMO EL PROFE NOS DIJO)
    * 3. AHORA HAY VARIAS CLASES QUE SE INICIALIZAN EN EL MAIN (OSEA EN EL startController)
    * 4. AHORA LAS CLASES TIENEN LOS ATRIBUTOS DE REPOSITORY Y SERVICE PARA PERMITIR LA INYECCION DE DEPENDENCIAS
    * 5. CAMBIOS MENORES AL POM.XML
    * */

}