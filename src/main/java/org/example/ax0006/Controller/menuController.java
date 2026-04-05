package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.ax0006.Entity.usuario;
import org.example.ax0006.Repository.usuarioRepository;
import org.example.ax0006.Repository.*;
import org.example.ax0006.Service.*;
import org.example.ax0006.db.*;

import java.io.IOException;

public class menuController {
    private H2 h2;
    private usuarioRepository usuarioRepo;
    private usuario usuarioLogin; //el usuario que esta logeado
    @FXML private Button bt_crearInventario;
    @FXML private Button bt_crearTipoObjeto;
    @FXML private Button bt_asignarObjeto;
    @FXML private Button bt_reservar;
    @FXML private Button bt_consultarInventario;
    /*CONSTRUCTOR DE LA CLASE*/


    public menuController(usuarioRepository usuarioRepo, usuario usuarioLogin) {
        this.usuarioRepo = usuarioRepo;
        this.usuarioLogin = usuarioLogin;
        this.h2 = new H2();
    }

    @FXML
    private Text fid_Bienvenido;

    /*METODO CAMBIA EL BIENVENIDO POR EL BIENVENIDO CON EL NOMBRE DEL USUARIO*/
    public void setNombreBienvenido(){
        fid_Bienvenido.setText("Bienvenido " + usuarioLogin.getNombre());
    }

    @FXML
    private Button fid_bt_volver;

    @FXML
    /*METODO QUE CAMBIA A LA PANTALLA DE LOGIN*/
    void On_btvolver(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/ax0006/login.fxml")
        );

        loginController loginControl = new loginController(usuarioRepo);

        loader.setController(loginControl);

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) fid_bt_volver.getScene().getWindow();
        stage.setScene(scene);
    }
    @FXML
    void on_bt_crearInventario(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/ax0006/crearInventario.fxml")
        );

        crearInventarioController controller =
                new crearInventarioController(
                        new InventarioService(new InventarioRepository(h2))
                );

        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) fid_bt_volver.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void on_bt_crearTipoObjeto(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/ax0006/crearTipoObjeto.fxml")
        );

        crearTipoObjetoController controller =
                new crearTipoObjetoController(
                        new crearTipoObjetoService(new TipoObjetoRepository(h2))
                );

        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) fid_bt_volver.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void on_bt_asignarObjeto(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/ax0006/asignarObjeto.fxml")
        );

        asignarObjetoController controller =
                new asignarObjetoController(
                        new InventarioObjetoService(
                                new InventarioObjetoRepository(h2)
                        )
                );

        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) fid_bt_volver.getScene().getWindow();
        stage.setScene(scene);
    }
    @FXML
    void on_bt_reservar(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/ax0006/asignarHorarioInventario.fxml")
        );

        asignarHorarioInventarioController controller =
                new asignarHorarioInventarioController(
                        new InventarioHorarioService(
                                new InventarioHorarioRepository(h2)
                        )
                );

        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) fid_bt_volver.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void on_bt_consultarInventario(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/ax0006/consultarInventario.fxml")
        );

        consultarInventarioController controller =
                new consultarInventarioController(
                        new consultarInventarioService(
                                new InventarioRepository(h2)
                        )
                );

        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) bt_crearInventario.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void on_bt_crearHorario(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/ax0006/horario.fxml")
        );

        horarioController controller =
                new horarioController(
                        new horarioService(
                                new horarioRepository(h2)
                        )
                );

        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) fid_bt_volver.getScene().getWindow();
        stage.setScene(scene);
    }
}
