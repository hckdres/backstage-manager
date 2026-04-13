package org.example.ax0006.Controller;

import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import org.example.ax0006.Entity.Concierto;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Service.ConciertoService;
import org.example.ax0006.Service.RolService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ax0006.Entity.Usuario;
import org.example.ax0006.Manager.SesionManager;
import javafx.event.ActionEvent;
import java.io.IOException;
import org.example.ax0006.Entity.Rol;
import javafx.scene.layout.VBox;
import org.example.ax0006.Service.StaffService;

import java.util.List;

public class AdminUsuariosController {

    private SesionManager sesion;
    private RolService rolService;
    private SceneManager sceneManager;
    private ConciertoService conciertoService;
    private StaffService staffService;

    public AdminUsuariosController(SesionManager sesion, RolService rolService, SceneManager sceneManager, ConciertoService conciertoService, StaffService staffService) {
        this.sesion = sesion;
        this.rolService = rolService;
        this.sceneManager = sceneManager;
        this.conciertoService = conciertoService;
        this.staffService = staffService;
    }

    @FXML
    private Label fid_Bienvenido;

    @FXML
    private Button fid_bt_volver;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colGmail;

    @FXML
    private TableColumn<Usuario, String> colNombreRol;

    @FXML
    private TableColumn<Usuario, Void> colAccion;

    @FXML
    private TableColumn<Usuario, Void> colDirectorioStaff;

    @FXML
    private ComboBox<Object> comboConciertoFiltro;

    @FXML
    public void initialize() {
        if (sesion.getUsuarioActual() != null) {
            fid_Bienvenido.setText("Bienvenido " + sesion.getUsuarioActual().getNombre());
        }

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colGmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));

        colNombreRol.setCellValueFactory(cellData ->
                new SimpleStringProperty(obtenerRolEnConcierto(cellData.getValue()))
        );

        cargarComboConciertoFiltro();
        agregarBoton();
        agregarBotonDirectorioStaff();

        comboConciertoFiltro.setOnAction(e -> actualizarTabla());

        cargarUsuariosSinAsignar();
    }

    private void cargarComboConciertoFiltro() {
        comboConciertoFiltro.getItems().clear();
        comboConciertoFiltro.getItems().add("Sin asignar");
        comboConciertoFiltro.getItems().addAll(
                conciertoService.obtenerConciertosSolos().stream()
                        .filter(c -> c.isProgramado())
                        .toList()
        );
        comboConciertoFiltro.setValue("Sin asignar");

        comboConciertoFiltro.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else if (item instanceof Concierto c)
                    setText(c.getNombreConcierto());
                else setText(item.toString());
            }
        });

        comboConciertoFiltro.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else if (item instanceof Concierto c)
                    setText(c.getNombreConcierto());
                else setText(item.toString());
            }
        });
    }

    private void actualizarTabla() {
        Object seleccionado = comboConciertoFiltro.getValue();
        if (seleccionado instanceof Concierto c) {
            cargarUsuariosPorConcierto(c);
        } else {
            cargarUsuariosSinAsignar();
        }
        tablaUsuarios.refresh();
    }

    private void cargarUsuariosSinAsignar() {
        List<Usuario> todos = rolService.obtenerUsuarios();
        List<Integer> asignados = staffService.obtenerIdsUsuariosAsignados();
        List<Usuario> sinAsignar = todos.stream()
                .filter(u -> !asignados.contains(u.getIdUsuario()))
                .toList();
        tablaUsuarios.setItems(FXCollections.observableArrayList(sinAsignar));
    }

    private void cargarUsuariosPorConcierto(Concierto concierto) {
        List<Usuario> usuarios = staffService.obtenerUsuariosPorConcierto(concierto.getIdConcierto());
        tablaUsuarios.setItems(FXCollections.observableArrayList(usuarios));
    }

    private String obtenerRolEnConcierto(Usuario u) {
        Object seleccionado = comboConciertoFiltro.getValue();
        if (seleccionado instanceof Concierto c) {
            return staffService.obtenerNombreRolEnConcierto(u.getIdUsuario(), c.getIdConcierto());
        }
        return "Sin asignar";
    }

    private void agregarBoton() {
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Asignar");

            {
                btn.setOnAction(event -> {
                    Usuario u = getTableView().getItems().get(getIndex());
                    mostrarPopupRol(u);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    btn.setDisable(false);
                    setGraphic(btn);
                }
            }
        });
    }

    private void agregarBotonDirectorioStaff() {
        colDirectorioStaff.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Directorio Staff");

            {
                btn.setOnAction(event -> {
                    Usuario u = getTableView().getItems().get(getIndex());
                    abrirDirectorioStaff(u);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                Usuario u = getTableView().getItems().get(getIndex());
                Object seleccionado = comboConciertoFiltro.getValue();
                String rol = obtenerRolEnConcierto(u);

                if (seleccionado instanceof Concierto && rol != null && rol.equalsIgnoreCase("Staff")) {
                    setGraphic(btn);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    private void abrirDirectorioStaff(Usuario u) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Directorio Staff");
        alert.setHeaderText("Directorio Staff");
        alert.setContentText("Aquí se abrirá el directorio de subroles para: " + u.getNombre());
        alert.showAndWait();
    }

    private void mostrarPopupRol(Usuario u) {
        List<Rol> roles = rolService.obtenerRolesAsignables();
        List<Concierto> conciertos = conciertoService.obtenerConciertosSolos();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Asignar Rol");
        dialog.setHeaderText("Asignar rol a: " + u.getNombre());

        ComboBox<Rol> comboRoles = new ComboBox<>();
        comboRoles.getItems().addAll(roles);
        comboRoles.setPromptText("Seleccionar rol");

        ComboBox<Concierto> comboConciertos = new ComboBox<>();
        comboConciertos.getItems().addAll(conciertos);
        comboConciertos.setPromptText("Seleccionar concierto");

        comboConciertos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Concierto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getNombreConcierto());
            }
        });

        comboConciertos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Concierto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getNombreConcierto());
            }
        });

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Rol:"), comboRoles,
                new Label("Concierto:"), comboConciertos
        );
        dialog.getDialogPane().setContent(content);

        ButtonType confirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmar, cancelar);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == confirmar) {
            Rol rolSeleccionado = comboRoles.getValue();
            Concierto conciertoSeleccionado = comboConciertos.getValue();

            if (rolSeleccionado == null || conciertoSeleccionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campos vacíos");
                alert.setHeaderText("Selecciona un rol y un concierto");
                alert.showAndWait();
                return;
            }

            staffService.asignarStaffAConcierto(
                    u.getIdUsuario(),
                    conciertoSeleccionado.getIdConcierto(),
                    rolSeleccionado.getIdRol()
            );
            actualizarTabla();
        }
    }

    @FXML
    void On_volver(ActionEvent event) throws IOException {
        sceneManager.showMenu();
    }
}