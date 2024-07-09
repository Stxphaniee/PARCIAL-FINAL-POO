package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class HelloController {
    @FXML
    private TextField nombreField; // 00095322 Campo de texto para ingresar el nombre del cliente
    @FXML
    private TextField direccionField; // 00095322 Campo de texto para ingresar la dirección del cliente
    @FXML
    private TextField telefonoField; // 00095322 Campo de texto para ingresar el número de teléfono del cliente

    @FXML
    protected void onAddButtonClick() { // 00095322 Método que se ejecuta al hacer clic en el botón de añadir
        String nombre = nombreField.getText(); // 00095322 Obtener el texto ingresado en el campo de nombre
        String direccion = direccionField.getText(); // 00095322 Obtener el texto ingresado en el campo de dirección
        String telefono = telefonoField.getText(); // 00095322 Obtener el texto ingresado en el campo de teléfono

        if (nombre.isEmpty() || telefono.isEmpty()) { // 00095322 Validar que los campos de nombre y teléfono no estén vacíos
            showAlert(Alert.AlertType.ERROR, "Error de Validación", "Nombre y Teléfono son obligatorios."); // 00095322 Mostrar alerta de error si los campos obligatorios están vacíos
            return; // 00095322 Salir del método si la validación falla
        }

        Connection conn = null; // 00095322 Declarar la variable de conexión a la base de datos
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00095322 Registrar el controlador JDBC

            String url = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true"; // 00095322 Establecer la URL de conexión a la base de datos
            conn = DriverManager.getConnection(url); // 00095322 Establecer la conexión con la base de datos

            String sql = "INSERT INTO Clientes (NombreCompleto, Direccion, NumeroTelefono) VALUES (?, ?, ?)"; // 00095322 SQL para insertar un nuevo cliente
            PreparedStatement pstmt = conn.prepareStatement(sql); // 00095322 Preparar la declaración SQL
            pstmt.setString(1, nombre); // 00095322 Establecer el primer parámetro del SQL con el nombre del cliente
            pstmt.setString(2, direccion); // 00095322 Establecer el segundo parámetro del SQL con la dirección del cliente
            pstmt.setString(3, telefono); // 00095322 Establecer el tercer parámetro del SQL con el número de teléfono del cliente

            int rowsInserted = pstmt.executeUpdate(); // 00095322 Ejecutar la inserción y obtener el número de filas insertadas

            if (rowsInserted > 0) { // 00095322 Verificar si la inserción fue exitosa
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente añadido exitosamente."); // 00095322 Mostrar alerta de éxito si la inserción fue exitosa
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo añadir el cliente."); // 00095322 Mostrar alerta de error si la inserción falló
            }

            nombreField.clear(); // 00095322 Limpiar el campo de nombre después de la inserción
            direccionField.clear(); // 00095322 Limpiar el campo de dirección después de la inserción
            telefonoField.clear(); // 00095322 Limpiar el campo de teléfono después de la inserción

        } catch (Exception e) { // 00095322 Capturar cualquier excepción que ocurra durante la ejecución del código
            e.printStackTrace(); // 00095322 Imprimir el stack trace de la excepción
            showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al añadir el cliente."); // 00095322 Mostrar alerta de error si ocurre una excepción
        } finally {
            if (conn != null) { // 00095322 Verificar si la conexión no es nula
                try {
                    conn.close(); // 00095322 Cerrar la conexión a la base de datos
                } catch (Exception e) { // 00095322 Capturar cualquier excepción al cerrar la conexión
                    e.printStackTrace(); // 00095322 Imprimir el stack trace de la excepción al cerrar la conexión
                }
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00095322 Método para mostrar una alerta
        Alert alert = new Alert(alertType); // 00095322 Crear una nueva alerta
        alert.setTitle(title); // 00095322 Establecer el título de la alerta
        alert.setContentText(message); // 00095322 Establecer el mensaje de la alerta
        alert.setHeaderText(null); // 00095322 Establecer el encabezado de la alerta como nulo
        alert.showAndWait(); // 00095322 Mostrar la alerta y esperar hasta que el usuario la cierre
    }
}
