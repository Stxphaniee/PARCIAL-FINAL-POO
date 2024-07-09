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
    public Button btnRegresar; // 00227722 Botón para regresar al menú anterior
    @FXML
    public Button btnRegistrar; // 00227722 Botón para registrar un cliente
    @FXML
    public Button btnVerUsuario; // 00227722 Botón para ver la información del usuario
    @FXML
    public Button btnComprar; // 00227722 Botón para realizar una compra
    @FXML
    private TextField nombreField; // 00227722 Campo para el nombre del cliente
    @FXML
    private TextField apellidoField; // 00227722 Campo para el apellido del cliente
    @FXML
    private TextField telefonoField; // 00227722 Campo para el teléfono del cliente
    @FXML
    private TextField direccionField; // 00227722 Campo para la dirección del cliente

    @FXML
    private void initialize() { // 00227722 Método que se ejecuta al inicializar la vista
        btnRegistrar.setOnAction(event -> { // 00227722 Acción al presionar el botón de registrar
            String nombre = nombreField.getText(); // 00227722 Obtener el nombre ingresado
            String apellido = apellidoField.getText(); // 00227722 Obtener el apellido ingresado
            String telefono = telefonoField.getText(); // 00227722 Obtener el teléfono ingresado
            String direccion = direccionField.getText(); // 00227722 Obtener la dirección ingresada

            if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) { // 00227722 Validar que todos los campos estén llenos
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios."); // 00227722 Mostrar alerta si algún campo está vacío
                return;
            }

            Connection conn = null;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00227722 Registrar el controlador JDBC

                String url = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
                conn = DriverManager.getConnection(url); // 00227722 Establecer la conexión con la base de datos

                String sqlInsert = "INSERT INTO Clientes (NombreCompleto, Apellidos, Direccion, NumeroTelefono) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS); // 00227722 Crear el PreparedStatement para insertar el cliente
                pstmtInsert.setString(1, nombre); // 00227722 Establecer el nombre en el PreparedStatement
                pstmtInsert.setString(2, apellido); // 00227722 Establecer el apellido en el PreparedStatement
                pstmtInsert.setString(3, direccion); // 00227722 Establecer la dirección en el PreparedStatement
                pstmtInsert.setString(4, telefono); // 00227722 Establecer el teléfono en el PreparedStatement

                int rowsInserted = pstmtInsert.executeUpdate(); // 00227722 Ejecutar la inserción

                if (rowsInserted > 0) { // 00227722 Verificar si la inserción fue exitosa
                    ResultSet generatedKeys = pstmtInsert.getGeneratedKeys(); // 00227722 Obtener las claves generadas
                    if (generatedKeys.next()) { // 00227722 Verificar si se generaron claves
                        int idCliente = generatedKeys.getInt(1); // 00227722 Obtener el ID del cliente generado
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente registrado exitosamente."); // 00227722 Mostrar alerta de éxito
                        new HelloApplication().showTarjetaSaldo(idCliente); // 00227722 Mostrar ventana para establecer el saldo de la tarjeta
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar el cliente."); // 00227722 Mostrar alerta de error si la inserción falló
                }

                nombreField.clear(); // 00227722 Limpiar el campo de nombre
                apellidoField.clear(); // 00227722 Limpiar el campo de apellido
                telefonoField.clear(); // 00227722 Limpiar el campo de teléfono
                direccionField.clear(); // 00227722 Limpiar el campo de dirección

            } catch (Exception e) {
                e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al registrar el cliente."); // 00227722 Mostrar alerta de error general
            } finally {
                if (conn != null) {
                    try {
                        conn.close(); // 00227722 Cerrar la conexión con la base de datos
                    } catch (Exception e) {
                        e.printStackTrace(); // 00227722 Imprimir el stack trace del error al cerrar la conexión
                    }
                }
            }
        });

        btnVerUsuario.setOnAction(event -> { // 00227722 Acción al presionar el botón de ver usuario
            try {
                showUsuario(); // 00227722 Llamar al método para mostrar la información del usuario
            } catch (IOException e) {
                e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Ver Usuario."); // 00227722 Mostrar alerta de error
            }
        });

        btnRegresar.setOnAction(event -> { // 00227722 Acción al presionar el botón de regresar
            try {
                backMain(); // 00227722 Llamar al método para regresar al menú principal
            } catch (IOException e) {
                e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Sistema de Gestion."); // 00227722 Mostrar alerta de error
            }
        });

        btnComprar.setOnAction(event -> { // 00227722 Acción al presionar el botón de comprar
            try {
                showComprar(); // 00227722 Llamar al método para mostrar la ventana de compras
            } catch (IOException e) {
                e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Comprar."); // 00227722 Mostrar alerta de error
            }
        });
    }

    public void showUsuario() throws IOException { // 00227722 Método para mostrar la información del usuario
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/usuario-view.fxml")); // 00227722 Cargar el archivo FXML de usuario
        Parent root = fxmlLoader.load(); // 00227722 Cargar el contenido del archivo FXML
        Stage stage = (Stage) btnVerUsuario.getScene().getWindow(); // 00227722 Obtener la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00227722 Establecer la nueva escena en la ventana
        stage.setTitle("Ver Información del Usuario"); // 00227722 Establecer el título de la ventana
    }

    public void backMain() throws IOException { // 00227722 Método para regresar al menú principal
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/main-view.fxml")); // 00227722 Cargar el archivo FXML del menú principal
        Parent root = fxmlLoader.load(); // 00227722 Cargar el contenido del archivo FXML
        Stage stage = (Stage) btnRegresar.getScene().getWindow(); // 00227722 Obtener la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00227722 Establecer la nueva escena en la ventana
        stage.setTitle("Sistema de Gestión"); // 00227722 Establecer el título de la ventana
    }

    public void showComprar() throws IOException { // 00227722 Método para mostrar la ventana de compras
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/compra-view.fxml")); // 00227722 Cargar el archivo FXML de compras
        Parent root = fxmlLoader.load(); // 00227722 Cargar el contenido del archivo FXML
        Stage stage = (Stage) btnComprar.getScene().getWindow(); // 00227722 Obtener la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00227722 Establecer la nueva escena en la ventana
        stage.setTitle("Realizar Compra"); // 00227722 Establecer el título de la ventana
        stage.setMaximized(true); // 00227722 Maximizar la ventana
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00227722 Método para mostrar alertas
        Alert alert = new Alert(alertType); // 00227722 Crear una nueva alerta
        alert.setTitle(title); // 00227722 Establecer el título de la alerta
        alert.setContentText(message); // 00227722 Establecer el mensaje de la alerta
        alert.setHeaderText(null); // 00227722 Eliminar el encabezado de la alerta
        alert.showAndWait(); // 00227722 Mostrar la alerta y esperar a que el usuario la cierre
    }
}
