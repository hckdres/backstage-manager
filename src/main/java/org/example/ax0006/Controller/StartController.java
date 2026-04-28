package org.example.ax0006.Controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.example.ax0006.Manager.ContextManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Repository.*;
import org.example.ax0006.Service.*;
import org.example.ax0006.Validator.ConciertoValidator;
import org.example.ax0006.Validator.HorarioValidator;
import org.example.ax0006.db.H2;

import java.io.IOException;

public class StartController extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        H2 h2 = new H2();
        h2.inicializarDB();


        HorarioValidator horarioValidator = new HorarioValidator();
        ConciertoValidator conciertoValidator = new ConciertoValidator(horarioValidator);

        UsuarioRepository usuarioRepo = new UsuarioRepository(h2);
        RolRepository rolRepo = new RolRepository(h2);
        HorarioRepository horarioRepo = new HorarioRepository(h2);
        ConciertoRepository conciertoRepo = new ConciertoRepository(h2);
        AsignacionStaffRepository asignacionStaffRepo = new AsignacionStaffRepository(h2);
        ContratoRepository contratoRepo = new ContratoRepository(h2);

        InventarioRepository inventarioRepo = new InventarioRepository(h2);
        InventarioObjetoRepository inventarioObjetoRepo = new InventarioObjetoRepository(h2);
        ObjetoRepository objetoRepo = new ObjetoRepository(h2);

        AutenticacionService autenService = new AutenticacionService(usuarioRepo);
        ProfileService profileService = new ProfileService(usuarioRepo);
        RolService rolService = new RolService(rolRepo, usuarioRepo);
        ContratoService contratoService = new ContratoService(contratoRepo);

        InventarioService inventarioService = new InventarioService(inventarioRepo);
        ConciertoService conciertoService = new ConciertoService(conciertoRepo, horarioRepo, conciertoValidator, contratoService, inventarioService);
        StaffService staffService = new StaffService(usuarioRepo, asignacionStaffRepo);

        InventarioObjetoService inventarioObjetoService = new InventarioObjetoService(inventarioObjetoRepo);
        ObjetoService objetoService = new ObjetoService(objetoRepo);

        SesionManager sesion = new SesionManager();

        ContextManager context = new ContextManager(
                h2,
                usuarioRepo,
                rolRepo,
                horarioRepo,
                conciertoRepo,
                autenService,
                profileService,
                rolService,
                conciertoService,
                sesion,
                staffService,
                inventarioService,
                inventarioObjetoService,
                objetoService,
                contratoService,
                contratoRepo
        );

        SceneManager sceneManager = new SceneManager(stage, context);

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        try {
            sceneManager.showLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}