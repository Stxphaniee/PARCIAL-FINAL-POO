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
import java.sql.*;

public class ClienteController {

    @FXML
    public Button btnRegresar;
    @FXML
    public Button btnRegistrar;
    @FXML
    public Button btnVerUsuario;
    @FXML
    public Button btnComprar;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField direccionField;

    @FXML
    private void initialize () {
        btnRegistrar.setOnAction(event -> {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String telefono = telefonoField.getText();
            String direccion = direccionField.getText();

            if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios.");
                return;
            }

            Connection conn = null;
            try {

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                String url = "jdbc:sqlserver://DESKTOP-RHFQOU4:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
                conn = DriverManager.getConnection(url);

                // Crear el PreparedStatement para insertar el cliente
                String sqlInsert = "INSERT INTO Clientes (NombreCompleto, Apellidos, Direccion, NumeroTelefono) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                pstmtInsert.setString(1, nombre);
                pstmtInsert.setString(2, apellido);
                pstmtInsert.setString(3, direccion);
                pstmtInsert.setString(4, telefono);

                // Ejecutar la inserción
                int rowsInserted = pstmtInsert.executeUpdate();

                if (rowsInserted > 0) {
                    ResultSet generatedKeys = pstmtInsert.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int idCliente = generatedKeys.getInt(1);
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente registrado exitosamente.");
                        new HelloApplication().showTarjetaSaldo(idCliente);
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar el cliente.");
                }

                nombreField.clear();
                apellidoField.clear();
                telefonoField.clear();
                direccionField.clear();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al registrar el cliente.");
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

        btnVerUsuario.setOnAction(event -> {
            try {
                showUsuario();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Ver Usuario.");
            }
        });

        btnRegresar.setOnAction(event -> {
            try {
                backMain();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Sistema de Gestion.");
            }
        });

        btnComprar.setOnAction(event -> {
            try {
                showComprar();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Comprar.");
            }
        });
    }


    public void showUsuario() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/usuario-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnVerUsuario.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Ver Información del Usuario");
    }

    public void backMain() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/main-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Sistema de Gestión");
    }

    public void showComprar() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/compra-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnComprar.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Realizar Compra");
        stage.setMaximized(true);
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
