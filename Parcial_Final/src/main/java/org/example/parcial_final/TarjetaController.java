package org.example.parcial_final;

import javafx.fxml.FXML; // 00227722 Importa las anotaciones FXML para el control de la vista
import javafx.fxml.FXMLLoader; // 00227722 Importa FXMLLoader para cargar archivos FXML
import javafx.scene.Parent; // 00227722 Importa Parent, el nodo raíz para la escena
import javafx.scene.Scene; // 00227722 Importa Scene para crear una nueva escena
import javafx.scene.control.Alert; // 00227722 Importa Alert para mostrar mensajes emergentes
import javafx.scene.control.Button; // 00227722 Importa Button para manejar los botones
import javafx.scene.control.TextField; // 00227722 Importa TextField para manejar los campos de texto
import javafx.stage.Stage; // 00227722 Importa Stage para representar la ventana principal

import java.io.IOException; // 00227722 Importa IOException para manejar excepciones de entrada/salida
import java.sql.Connection; // 00227722 Importa Connection para manejar la conexión a la base de datos
import java.sql.Date; // 00227722 Importa Date para manejar fechas
import java.sql.DriverManager; // 00227722 Importa DriverManager para gestionar controladores JDBC
import java.sql.PreparedStatement; // 00227722 Importa PreparedStatement para manejar sentencias SQL preparadas

public class TarjetaController {

    @FXML
    public Button btnAgregar; // 00227722 Botón para agregar una tarjeta

    @FXML
    public Button btnRegresar; // 00227722 Botón para regresar a la vista anterior
    @FXML
    private TextField clienteIdField; // 00227722 Campo para ingresar el ID del cliente
    @FXML
    private TextField numeroTarjetaField; // 00227722 Campo para ingresar el número de la tarjeta
    @FXML
    private TextField fechaExpiracionField; // 00227722 Campo para ingresar la fecha de expiración
    @FXML
    private TextField tipoTarjetaField; // 00227722 Campo para ingresar el tipo de tarjeta
    @FXML
    private TextField facilitadorField; // 00227722 Campo para ingresar el facilitador de la tarjeta
    @FXML
    private TextField saldoInicialField; // 00227722 Campo para ingresar el monto inicial

    @FXML
    private void initialize () { // 00227722 Método que se ejecuta al inicializar la vista
        btnAgregar.setOnAction(event -> { // 00227722 Acción al hacer clic en el botón Agregar
            String clienteIdStr = clienteIdField.getText(); // 00227722 Obtener el ID del cliente
            String numeroTarjeta = numeroTarjetaField.getText().replaceAll(" ", ""); // 00227722 Obtener el número de tarjeta sin espacios
            String fechaExpiracion = fechaExpiracionField.getText(); // 00227722 Obtener la fecha de expiración
            String tipoTarjeta = tipoTarjetaField.getText(); // 00227722 Obtener el tipo de tarjeta
            String facilitador = facilitadorField.getText(); // 00227722 Obtener el facilitador
            String saldoInicialStr = saldoInicialField.getText(); // 00227722 Obtener el monto inicial

            if (clienteIdStr.isEmpty() || numeroTarjeta.isEmpty() || fechaExpiracion.isEmpty() || tipoTarjeta.isEmpty() || facilitador.isEmpty() || saldoInicialStr.isEmpty()) {
                // 00227722 Validar que todos los campos estén completos
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios.");
                return;
            }
            try {
                int clienteId = Integer.parseInt(clienteIdStr); // 00227722 Convertir el ID del cliente a entero
                double saldoInicial = Double.parseDouble(saldoInicialStr); // 00227722 Convertir el monto inicial a double

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true");
                // 00227722 Establecer conexión con la base de datos
                String sql = "INSERT INTO Tarjetas (NumeroTarjeta, ID_Cliente, FechaExpiracion, TipoTarjeta, Facilitador, Saldo) VALUES (?, ?, ?, ?, ?, ?)";
                // 00227722 Consulta para insertar una nueva tarjeta
                PreparedStatement pstmt = conn.prepareStatement(sql); // 00227722 Preparar la consulta SQL
                pstmt.setString(1, numeroTarjeta); // 00227722 Establecer el número de tarjeta
                pstmt.setInt(2, clienteId); // 00227722 Establecer el ID del cliente
                pstmt.setDate(3, Date.valueOf(fechaExpiracion)); // 00227722 Establecer la fecha de expiración
                pstmt.setString(4, tipoTarjeta); // 00227722 Establecer el tipo de tarjeta
                pstmt.setString(5, facilitador); // 00227722 Establecer el facilitador
                pstmt.setDouble(6, saldoInicial); // 00227722 Establecer el saldo inicial

                int rowsInserted = pstmt.executeUpdate(); // 00227722 Ejecutar la inserción

                if (rowsInserted > 0) { // 00227722 Verificar si la inserción fue exitosa
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Tarjeta añadida exitosamente.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo añadir la tarjeta.");
                }

                clienteIdField.clear(); // 00227722 Limpiar el campo ID del cliente
                numeroTarjetaField.clear(); // 00227722 Limpiar el campo número de tarjeta
                fechaExpiracionField.clear(); // 00227722 Limpiar el campo fecha de expiración
                tipoTarjetaField.clear(); // 00227722 Limpiar el campo tipo de tarjeta
                facilitadorField.clear(); // 00227722 Limpiar el campo facilitador
                saldoInicialField.clear(); // 00227722 Limpiar el campo saldo inicial
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al añadir la tarjeta."); // 00227722 Mostrar alerta de error
            }
        });

        btnRegresar.setOnAction(event -> { // 00227722 Acción al hacer clic en el botón Regresar
            try {
                backServicio(); // 00227722 Llamar al método para regresar a la vista anterior
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Servicio."); // 00227722 Mostrar alerta de error
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00227722 Mostrar una alerta
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait(); // 00227722 Esperar a que se cierre la alerta
    }

    public void backServicio() throws IOException { // 00227722 Método para regresar a la vista de servicio
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/servicio-view.fxml")); // 00227722 Cargar el archivo FXML
        Parent root = fxmlLoader.load(); // 00227722 Cargar la vista
        Stage stage = (Stage) btnRegresar.getScene().getWindow(); // 00227722 Obtener la ventana actual
        stage.setScene(new Scene(root, 1050, 600)); // 00227722 Establecer la nueva escena
        stage.setTitle("Servicio"); // 00227722 Establecer el título de la ventana
    }
}
