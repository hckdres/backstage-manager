[1mdiff --git a/src/main/java/org/example/ax0006/Controller/MenuController.java b/src/main/java/org/example/ax0006/Controller/MenuController.java[m
[1mindex 8017d24..f07365c 100644[m
[1m--- a/src/main/java/org/example/ax0006/Controller/MenuController.java[m
[1m+++ b/src/main/java/org/example/ax0006/Controller/MenuController.java[m
[36m@@ -47,6 +47,10 @@[m [mpublic class MenuController {[m
     @FXML[m
     private Button fid_Menu_Conciertos;[m
 [m
[32m+[m[32m    @FXML private Button bt_crearInventario;[m
[32m+[m[32m    @FXML private Button bt_crearTipoObjeto;[m
[32m+[m[32m    @FXML private Button bt_consultarInventario;[m
[32m+[m
     @FXML[m
     public void initialize() {[m
         if (sesion != null && sesion.getUsuarioActual() != null && fid_Bienvenido != null) {[m
[36m@@ -89,4 +93,19 @@[m [mpublic class MenuController {[m
     void On_Menu_Conciertos(ActionEvent event) throws IOException {[m
         sceneManager.showMenuConcierto();[m
     }[m
[32m+[m
[32m+[m[32m    @FXML[m
[32m+[m[32m    void on_bt_crearInventario(ActionEvent event) throws IOException {[m
[32m+[m[32m        sceneManager.showCrearInventario();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    @FXML[m
[32m+[m[32m    void on_bt_crearTipoObjeto(ActionEvent event) throws IOException {[m
[32m+[m[32m        sceneManager.showCrearTipoObjeto();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    @FXML[m
[32m+[m[32m    void on_bt_consultarInventario(ActionEvent event) throws IOException {[m
[32m+[m[32m        sceneManager.showConsultarInventario();[m
[32m+[m[32m    }[m
 }[m
\ No newline at end of file[m
