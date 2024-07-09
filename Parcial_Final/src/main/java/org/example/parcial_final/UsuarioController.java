package org.example.parcial_final;

import javafx.fxml.FXML; // 00095322 Importa las anotaciones FXML para el control de la vista
import javafx.fxml.FXMLLoader; // 00095322 Importa FXMLLoader para cargar archivos FXML
import javafx.scene.Parent; // 00095322 Importa Parent, el nodo raíz para la escena
import javafx.scene.Scene; // 00095322 Importa Scene para crear una nueva escena
import javafx.scene.control.Alert; // 00095322 Importa Alert para mostrar mensajes emergentes
import javafx.scene.control.Button; // 00095322 Importa Button para manejar los botones
import javafx.scene.control.Label; // 00095322 Importa Label para manejar etiquetas
import javafx.scene.control.TextField; // 00095322 Importa TextField para manejar los campos de texto
import javafx.stage.Stage; // 00095322 Importa Stage para representar la ventana principal

import java.io.IOException; // 00095322 Importa IOException para manejar excepciones de entrada/salida
import java.sql.Connection; // 00095322 Importa Connection para manejar la conexión a la base de datos
import java.sql.DriverManager; // 00095322 Importa DriverManager para manejar los controladores de la base de datos
import java.sql.PreparedStatement; // 00095322 Importa PreparedStatement para manejar las sentencias SQL preparadas
import java.sql.ResultSet; // 00095322 Importa ResultSet para manejar los resultados de las consultas SQL

public class UsuarioController {

    @FXML
    public Button btnRegresar; // 00095322 Declara el botón para regresar

    @FXML
    public Button btnBuscar; // 00095322 Declara el botón para buscar
    @FXML
    private TextField idField; // 00095322 Declara el campo de texto para el ID del cliente
    @FXML
    private Label infoLabel; // 00095322 Declara la etiqueta para mostrar la información del cliente

    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true"; // 00095322 Define la URL de la base de datos

    @FXML
    private void initialize() { // 00095322 Método de inicialización
        btnBuscar.setOnAction(event -> { // 00095322 Asigna la acción del botón buscar

            String idClienteStr = idField.getText(); // 00095322 Obtiene el texto del campo ID del cliente
            if (idClienteStr.isEmpty()) { // 00095322 Verifica si el campo ID del cliente está vacío
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "El campo ID del Cliente es obligatorio.");
                return;
            }
            int idCliente = 0; // 00095322 Declara e inicializa la variable idCliente

            try {
                idCliente = Integer.parseInt(idClienteStr); // 00095322 Convierte el ID del cliente a entero
            } catch (NumberFormatException e) { // 00095322 Captura la excepción si la conversión falla
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "El ID del Cliente debe ser un número válido.");
            }

            Connection conn = null; // 00095322 Declara la variable de conexión
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00095322 Carga el controlador JDBC
                conn = DriverManager.getConnection(DB_URL); // 00095322 Establece la conexión con la base de datos

                String sql = "SELECT c.NombreCompleto, c.Direccion, c.NumeroTelefono, t.NumeroTarjeta, t.Saldo " +
                        "FROM Clientes c " +
                        "LEFT JOIN Tarjetas t ON c.ID = t.ID_Cliente " +
                        "WHERE c.ID = ?"; // 00095322 Define la consulta SQL para obtener la información del cliente y sus tarjetas
                PreparedStatement pstmt = conn.prepareStatement(sql); // 00095322 Prepara la sentencia SQL
                pstmt.setInt(1, idCliente); // 00095322 Establece el parámetro de la consulta
                ResultSet rs = pstmt.executeQuery(); // 00095322 Ejecuta la consulta y obtiene los resultados

                if (rs.next()) { // 00095322 Verifica si hay resultados
                    String info = "Nombre: " + rs.getString("NombreCompleto") + "\n" +
                            "Dirección: " + rs.getString("Direccion") + "\n" +
                            "Teléfono: " + rs.getString("NumeroTelefono") + "\n" +
                            "Tarjeta: " + (rs.getString("NumeroTarjeta") != null ? rs.getString("NumeroTarjeta") : "No tiene tarjeta asociada") + "\n" +
                            "Saldo: $" + rs.getDouble("Saldo"); // 00095322 Construye la cadena de información del cliente
                    infoLabel.setText(info); // 00095322 Establece el texto de la etiqueta con la información del cliente
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se encontró información para el usuario."); // 00095322 Muestra una alerta si no se encuentra el cliente
                }

            } catch (Exception e) { // 00095322 Captura cualquier excepción que ocurra
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al buscar la información del usuario: " + e.getMessage());
            } finally {
                if (conn != null) { // 00095322 Cierra la conexión si no es nula
                    try {
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnRegresar.setOnAction(event -> { // 00095322 Asigna la acción del botón regresar
            try {
                backCliente(); // 00095322 Llama al método backCliente al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Cliente"); // 00095322 Muestra una alerta si ocurre un error
            }
        });
    }

    public void backCliente() throws IOException { // 00095322 Método para volver a la ventana de cliente
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/cliente-view.fxml")); // 00095322 Carga el archivo FXML de cliente
        Parent root = fxmlLoader.load(); // 00095322 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnRegresar.getScene().getWindow(); // 00095322 Obtiene la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00095322 Establece la nueva escena
        stage.setTitle("Registrar Cliente"); // 00095322 Establece el título de la ventana
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00095322 Método para mostrar alertas
        Alert alert = new Alert(alertType); // 00095322 Crea una nueva alerta
        alert.setTitle(title); // 00095322 Establece el título de la alerta
        alert.setContentText(message); // 00095322 Establece el mensaje de la alerta
        alert.setHeaderText(null); // 00095322 Elimina el encabezado de la alerta
        alert.showAndWait(); // 00095322 Muestra la alerta y espera a que el usuario la cierre
    }
}
