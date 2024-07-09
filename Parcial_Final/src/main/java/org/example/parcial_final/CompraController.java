package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CompraController {

    @FXML
    public Button btnRegresar; // 00227722 Botón para regresar a la ventana anterior
    @FXML
    public Button btnCalcular; // 00227722 Botón para calcular el total a pagar
    @FXML
    public Button btnRealizarCompra; // 00227722 Botón para realizar la compra
    @FXML
    private TextField nombreField; // 00227722 Campo para el nombre del cliente
    @FXML
    private TextField apellidoField; // 00227722 Campo para el apellido del cliente
    @FXML
    private TextField productoField; // 00227722 Campo para el nombre del producto
    @FXML
    private TextField precioField; // 00227722 Campo para el precio del producto
    @FXML
    private TextField cantidadField; // 00227722 Campo para la cantidad del producto
    @FXML
    private DatePicker fechaCompraPicker; // 00227722 Campo para seleccionar la fecha de la compra
    @FXML
    private ChoiceBox<String> tipoTarjetaChoiceBox; // 00227722 Campo para seleccionar el tipo de tarjeta
    @FXML
    private Label totalLabel; // 00227722 Etiqueta para mostrar el total a pagar

    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true"; // 00227722 URL de la base de datos

    private double totalPagar; // 00227722 Variable para almacenar el total a pagar

    @FXML
    public void initialize() { // 00227722 Método que se ejecuta al inicializar la vista
        tipoTarjetaChoiceBox.getItems().addAll("Crédito", "Débito"); // 00227722 Agregar opciones al ChoiceBox

        btnRegresar.setOnAction(event -> { // 00227722 Acción al presionar el botón de regresar
            try {
                backCliente(); // 00227722 Llamar al método para regresar a la ventana de cliente
            } catch (IOException e) {
                e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Cliente."); // 00227722 Mostrar alerta de error
            }
        });
        btnCalcular.setOnAction(event -> { // 00227722 Acción al presionar el botón de calcular
            String precioStr = precioField.getText(); // 00227722 Obtener el precio ingresado
            String cantidadStr = cantidadField.getText(); // 00227722 Obtener la cantidad ingresada

            if (precioStr.isEmpty() || cantidadStr.isEmpty()) { // 00227722 Validar que ambos campos estén llenos
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Precio y Cantidad son obligatorios."); // 00227722 Mostrar alerta si algún campo está vacío
                return;
            }

            double precio; // 00227722 Variable para almacenar el precio
            int cantidad; // 00227722 Variable para almacenar la cantidad
            try {
                precio = Double.parseDouble(precioStr); // 00227722 Convertir el precio a número
                cantidad = Integer.parseInt(cantidadStr); // 00227722 Convertir la cantidad a número
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "Precio debe ser un número y Cantidad un entero."); // 00227722 Mostrar alerta si los valores no son válidos
                return;
            }

            totalPagar = precio * cantidad; // 00227722 Calcular el total a pagar
            totalLabel.setText("Total a pagar: $" + totalPagar); // 00227722 Mostrar el total a pagar
        });

        btnRealizarCompra.setOnAction(event -> { // 00227722 Acción al presionar el botón de realizar compra
            String nombre = nombreField.getText(); // 00227722 Obtener el nombre ingresado
            String apellido = apellidoField.getText(); // 00227722 Obtener el apellido ingresado
            String producto = productoField.getText(); // 00227722 Obtener el nombre del producto ingresado
            String precioStr = precioField.getText(); // 00227722 Obtener el precio ingresado
            String cantidadStr = cantidadField.getText(); // 00227722 Obtener la cantidad ingresada
            LocalDate fechaCompra = fechaCompraPicker.getValue(); // 00227722 Obtener la fecha seleccionada
            String tipoTarjeta = tipoTarjetaChoiceBox.getValue(); // 00227722 Obtener el tipo de tarjeta seleccionado

            if (nombre.isEmpty() || apellido.isEmpty() || producto.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty() || fechaCompra == null || tipoTarjeta == null) { // 00227722 Validar que todos los campos estén llenos
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios."); // 00227722 Mostrar alerta si algún campo está vacío
                return;
            }

            double precio; // 00227722 Variable para almacenar el precio
            int cantidad; // 00227722 Variable para almacenar la cantidad
            try {
                precio = Double.parseDouble(precioStr); // 00227722 Convertir el precio a número
                cantidad = Integer.parseInt(cantidadStr); // 00227722 Convertir la cantidad a número
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "Precio debe ser un número y Cantidad un entero."); // 00227722 Mostrar alerta si los valores no son válidos
                return;
            }

            Connection conn = null; // 00227722 Variable para la conexión con la base de datos
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00227722 Registrar el controlador JDBC
                conn = DriverManager.getConnection(DB_URL); // 00227722 Establecer la conexión con la base de datos

                String sqlCliente = "SELECT ID FROM Clientes WHERE NombreCompleto = ? AND Apellidos = ?"; // 00227722 Consulta para obtener el ID del cliente
                PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente); // 00227722 Preparar la consulta
                pstmtCliente.setString(1, nombre); // 00227722 Establecer el nombre en la consulta
                pstmtCliente.setString(2, apellido); // 00227722 Establecer el apellido en la consulta
                ResultSet rsCliente = pstmtCliente.executeQuery(); // 00227722 Ejecutar la consulta

                if (rsCliente.next()) { // 00227722 Verificar si se encontró el cliente
                    int idCliente = rsCliente.getInt("ID"); // 00227722 Obtener el ID del cliente

                    String sqlTarjeta = "SELECT NumeroTarjeta, Saldo FROM Tarjetas WHERE ID_Cliente = ? AND TipoTarjeta = ?"; // 00227722 Consulta para obtener la tarjeta del cliente
                    PreparedStatement pstmtTarjeta = conn.prepareStatement(sqlTarjeta); // 00227722 Preparar la consulta
                    pstmtTarjeta.setInt(1, idCliente); // 00227722 Establecer el ID del cliente en la consulta
                    pstmtTarjeta.setString(2, tipoTarjeta); // 00227722 Establecer el tipo de tarjeta en la consulta
                    ResultSet rsTarjeta = pstmtTarjeta.executeQuery(); // 00227722 Ejecutar la consulta

                    if (rsTarjeta.next()) { // 00227722 Verificar si se encontró la tarjeta
                        String numeroTarjeta = rsTarjeta.getString("NumeroTarjeta"); // 00227722 Obtener el número de tarjeta
                        double saldo = rsTarjeta.getDouble("Saldo"); // 00227722 Obtener el saldo de la tarjeta

                        if (saldo >= totalPagar) { // 00227722 Verificar si hay saldo suficiente
                            saldo -= totalPagar; // 00227722 Restar el total a pagar del saldo

                            String sqlUpdateTarjeta = "UPDATE Tarjetas SET Saldo = ? WHERE NumeroTarjeta = ?"; // 00227722 Consulta para actualizar el saldo de la tarjeta
                            PreparedStatement pstmtUpdateTarjeta = conn.prepareStatement(sqlUpdateTarjeta); // 00227722 Preparar la consulta
                            pstmtUpdateTarjeta.setDouble(1, saldo); // 00227722 Establecer el nuevo saldo en la consulta
                            pstmtUpdateTarjeta.setString(2, numeroTarjeta); // 00227722 Establecer el número de tarjeta en la consulta
                            pstmtUpdateTarjeta.executeUpdate(); // 00227722 Ejecutar la actualización

                            String sqlInsertCompra = "INSERT INTO Compras (ID_Cliente, NumeroTarjeta, FechaCompra, MontoTotal, DescripcionCompra) VALUES (?, ?, ?, ?, ?)"; // 00227722 Consulta para insertar la compra
                            PreparedStatement pstmtInsertCompra = conn.prepareStatement(sqlInsertCompra); // 00227722 Preparar la consulta
                            pstmtInsertCompra.setInt(1, idCliente); // 00227722 Establecer el ID del cliente en la consulta
                            pstmtInsertCompra.setString(2, numeroTarjeta); // 00227722 Establecer el número de tarjeta en la consulta
                            pstmtInsertCompra.setString(3, fechaCompra.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))); // 00227722 Establecer la fecha y hora de la compra en la consulta
                            pstmtInsertCompra.setDouble(4, totalPagar); // 00227722 Establecer el monto total en la consulta
                            pstmtInsertCompra.setString(5, producto + " x" + cantidad); // 00227722 Establecer la descripción de la compra en la consulta
                            pstmtInsertCompra.executeUpdate(); // 00227722 Ejecutar la inserción

                            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Compra realizada exitosamente."); // 00227722 Mostrar alerta de éxito
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Fondos insuficientes para realizar la compra."); // 00227722 Mostrar alerta de error si no hay fondos suficientes
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "No se encontró una tarjeta asociada al cliente."); // 00227722 Mostrar alerta de error si no se encontró la tarjeta
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se encontró un cliente con ese nombre."); // 00227722 Mostrar alerta de error si no se encontró el cliente
                }

            } catch (Exception e) {
                e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al realizar la compra: " + e.getMessage()); // 00227722 Mostrar alerta de error si ocurre una excepción
            } finally {
                if (conn != null) { // 00227722 Verificar si la conexión no es nula
                    try {
                        conn.close(); // 00227722 Cerrar la conexión con la base de datos
                    } catch (Exception e) {
                        e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                    }
                }
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00227722 Método para mostrar una alerta
        Alert alert = new Alert(alertType); // 00227722 Crear una nueva alerta
        alert.setTitle(title); // 00227722 Establecer el título de la alerta
        alert.setContentText(message); // 00227722 Establecer el mensaje de la alerta
        alert.setHeaderText(null); // 00227722 Eliminar el encabezado de la alerta
        alert.showAndWait(); // 00227722 Mostrar la alerta y esperar a que el usuario la cierre
    }

    public void backCliente() throws IOException { // 00227722 Método para regresar a la ventana de cliente
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/cliente-view.fxml")); // 00227722 Cargar el archivo FXML de la ventana de cliente
        Parent root = fxmlLoader.load(); // 00227722 Cargar el contenido del archivo FXML
        Stage stage = (Stage) btnRegresar.getScene().getWindow(); // 00227722 Obtener la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00227722 Establecer la nueva escena con las dimensiones especificadas
        stage.setTitle("Registrar Cliente"); // 00227722 Establecer el título de la ventana
    }
}
