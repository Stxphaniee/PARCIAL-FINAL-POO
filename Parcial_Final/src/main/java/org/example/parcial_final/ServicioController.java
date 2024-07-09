package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ServicioController {

    @FXML
    public Button btnAnadir;
    @FXML
    public Button btnAgregarTarjeta;
    @FXML
    public Button btnReporteA;
    @FXML
    public Button btnReporteC;
    @FXML
    public Button btnEditarInfo;
    @FXML
    public Button btnVolver;
    @FXML
    public Button btnReporteD;
    @FXML
    public Button btnReporteB;
    @FXML
    public TextField apellidoField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;

    @FXML
    private void initialize () {
        btnAnadir.setOnAction(event -> {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String direccion = direccionField.getText();
            String telefono = telefonoField.getText();

            if (nombre.isEmpty() || telefono.isEmpty() || apellido.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Nombre completo y Teléfono son obligatorios.");
                return;
            }

            Connection conn = null;
            try {

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                String url = "jdbc:sqlserver://DESKTOP-RHFQOU4:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
                conn = DriverManager.getConnection(url);

                String sql = "INSERT INTO Clientes (NombreCompleto,Apellidos, Direccion, NumeroTelefono) VALUES (?,?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nombre);
                pstmt.setString(2, apellido);
                pstmt.setString(3, direccion);
                pstmt.setString(4, telefono);

                int rowsInserted = pstmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente añadido exitosamente.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo añadir el cliente.");
                }

                nombreField.clear();
                apellidoField.clear();
                direccionField.clear();
                telefonoField.clear();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al añadir el cliente.");
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnEditarInfo.setOnAction(event -> {
            try {
                showEditarInfo();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Editar Información.");
            }
        });

        btnAgregarTarjeta.setOnAction(event -> {
            try {
                showAgregarTarjeta();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Agregar Tarjeta.");
            }
        });

        btnReporteA.setOnAction(event -> {
            try {
                showReporteA();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Generar Reporte A.");
            }
        });
        btnReporteB.setOnAction(event -> {
            try {
                showReporteB();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Generar Reporte B");
            }
        });

        btnReporteC.setOnAction(event -> {
            try {
                showReporteC();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Generar Reporte C.");
            }
        });

        btnReporteD.setOnAction(event -> {
            try {
                showReporteD();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Generar Reporte D");
            }
        });

        btnVolver.setOnAction(event -> {
            try {
                volverMain();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al volver al Menú Principal.");
            }
        });
    }




    public void showReporteA() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reporteA-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnReporteA.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Generar Reporte A");
    }

    public void showReporteB() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reporteB-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnReporteB.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Generar Reporte B");
    }
    public void showReporteC() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reporteC-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnReporteC.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Generar Reporte C");
    }

    public void showReporteD() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reporteD-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnReporteD.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Generar Reporte D");
    }

    public void showAgregarTarjeta() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/tarjeta-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnAgregarTarjeta.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Agregar Tarjeta");
    }

    public void showEditarInfo() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/editar-informacion-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnEditarInfo.getScene().getWindow();
        stage.setScene(new Scene(root, 1250, 700));
        stage.setTitle("Editar Información del Cliente");
    }


    public void volverMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/main-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Sistema de Gestión");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
