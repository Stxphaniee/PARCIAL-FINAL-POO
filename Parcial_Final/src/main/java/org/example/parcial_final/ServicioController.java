package org.example.parcial_final;

import javafx.fxml.FXML; // 00100121 Importa las anotaciones FXML para el control de la vista
import javafx.fxml.FXMLLoader; // 00100121 Importa FXMLLoader para cargar archivos FXML
import javafx.scene.Parent; // 00100121 Importa Parent, el nodo raíz para la escena
import javafx.scene.Scene; // 00100121 Importa Scene para crear una nueva escena
import javafx.scene.control.Alert; // 00100121 Importa Alert para mostrar mensajes emergentes
import javafx.scene.control.Button; // 00100121 Importa Button para manejar los botones
import javafx.scene.control.TextField; // 00100121 Importa TextField para manejar los campos de texto
import javafx.stage.Stage; // 00100121 Importa Stage para representar la ventana principal

import java.io.IOException; // 00100121 Importa IOException para manejar excepciones de entrada/salida
import java.sql.Connection; // 00100121 Importa Connection para manejar la conexión a la base de datos
import java.sql.DriverManager; // 00100121 Importa DriverManager para manejar los controladores de la base de datos
import java.sql.PreparedStatement; // 00100121 Importa PreparedStatement para manejar las sentencias SQL preparadas

public class ServicioController {

    @FXML
    public Button btnAnadir; // 00100121 Declara el botón para añadir cliente
    @FXML
    public Button btnAgregarTarjeta; // 00100121 Declara el botón para agregar tarjeta
    @FXML
    public Button btnReporteA; // 00100121 Declara el botón para generar reporte A
    @FXML
    public Button btnReporteC; // 00100121 Declara el botón para generar reporte C
    @FXML
    public Button btnEditarInfo; // 00100121 Declara el botón para editar información
    @FXML
    public Button btnVolver; // 00100121 Declara el botón para volver al menú principal
    @FXML
    public Button btnReporteD; // 00100121 Declara el botón para generar reporte D
    @FXML
    public Button btnReporteB; // 00100121 Declara el botón para generar reporte B
    @FXML
    public TextField apellidoField; // 00100121 Declara el campo de texto para el apellido
    @FXML
    private TextField nombreField; // 00100121 Declara el campo de texto para el nombre
    @FXML
    private TextField direccionField; // 00100121 Declara el campo de texto para la dirección
    @FXML
    private TextField telefonoField; // 00100121 Declara el campo de texto para el teléfono
    @FXML
    public Button btnVerReportes; // 00197822 Botón para ver los reportes


    @FXML
    private void initialize() { // 00100121 Método de inicialización
        btnAnadir.setOnAction(event -> { // 00100121 Asigna la acción del botón añadir cliente
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String direccion = direccionField.getText();
            String telefono = telefonoField.getText();

            if (nombre.isEmpty() || telefono.isEmpty() || apellido.isEmpty()) { // 00100121 Verifica si los campos obligatorios están vacíos
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Nombre completo y Teléfono son obligatorios.");
                return;
            }

            Connection conn = null;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00100121 Carga el controlador JDBC

                String url = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
                conn = DriverManager.getConnection(url); // 00100121 Establece la conexión con la base de datos

                String sql = "INSERT INTO Clientes (NombreCompleto,Apellidos, Direccion, NumeroTelefono) VALUES (?,?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql); // 00100121 Prepara la sentencia SQL
                pstmt.setString(1, nombre);
                pstmt.setString(2, apellido);
                pstmt.setString(3, direccion);
                pstmt.setString(4, telefono);

                int rowsInserted = pstmt.executeUpdate(); // 00100121 Ejecuta la inserción

                if (rowsInserted > 0) { // 00100121 Verifica si la inserción fue exitosa
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente añadido exitosamente.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo añadir el cliente.");
                }

                nombreField.clear(); // 00100121 Limpia los campos de texto
                apellidoField.clear(); // 00100121 Limpia los campos de texto
                direccionField.clear(); // 00100121 Limpia los campos de texto
                telefonoField.clear(); // 00100121 Limpia los campos de texto

            } catch (Exception e) {
                e.printStackTrace(); // 00100121 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al añadir el cliente.");
            } finally {
                if (conn != null) { // 00100121 Cierra la conexión
                    try {
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnEditarInfo.setOnAction(event -> { // 00100121 Asigna la acción del botón editar información
            try {
                showEditarInfo(); // 00100121 Llama al método showEditarInfo al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00100121 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Editar Información.");
            }
        });

        btnAgregarTarjeta.setOnAction(event -> { // 00100121 Asigna la acción del botón agregar tarjeta
            try {
                showAgregarTarjeta(); // 00100121 Llama al método showAgregarTarjeta al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00100121 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Agregar Tarjeta.");
            }
        });

        btnReporteA.setOnAction(event -> { // 00100121 Asigna la acción del botón generar reporte A
            try {
                showReporteA(); // 00100121 Llama al método showReporteA al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00100121 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Generar Reporte A.");
            }
        });
        btnReporteB.setOnAction(event -> { // 00100121 Asigna la acción del botón generar reporte B
            try {
                showReporteB(); // 00100121 Llama al método showReporteB al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00100121 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Generar Reporte B");
            }
        });

        btnReporteC.setOnAction(event -> { // 00100121 Asigna la acción del botón generar reporte C
            try {
                showReporteC(); // 00100121 Llama al método showReporteC al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00100121 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Generar Reporte C.");
            }
        });

        btnReporteD.setOnAction(event -> { // 00100121 Asigna la acción del botón generar reporte D
            try {
                showReporteD(); // 00100121 Llama al método showReporteD al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00100121 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Generar Reporte D");
            }
        });

        btnVolver.setOnAction(event -> { // 00100121 Asigna la acción del botón volver al menú principal
            try {
                volverMain(); // 00100121 Llama al método volverMain al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00100121 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al volver al Menú Principal.");
            }
        });

        btnVerReportes.setOnAction(event -> { // 00197822 Acción al presionar el botón de ver reportes
            try {
                showVerReportes(); // 00197822 Llamar al método para mostrar la ventana de ver reportes
            } catch (IOException e) {
                e.printStackTrace(); // 00197822 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Ver Reportes."); // 00197822 Mostrar alerta de error
            }
        });
    }

    public void showReporteA() throws IOException { // 00100121 Método para mostrar la ventana de reporte A
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reporteA-view.fxml")); // 00100121 Carga el archivo FXML de reporte A
        Parent root = fxmlLoader.load(); // 00100121 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnReporteA.getScene().getWindow(); // 00100121 Obtiene la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00100121 Establece la nueva escena
        stage.setTitle("Generar Reporte A"); // 00100121 Establece el título de la ventana
    }

    public void showReporteB() throws IOException { // 00100121 Método para mostrar la ventana de reporte B
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reporteB-view.fxml")); // 00100121 Carga el archivo FXML de reporte B
        Parent root = fxmlLoader.load(); // 00100121 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnReporteB.getScene().getWindow(); // 00100121 Obtiene la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00100121 Establece la nueva escena
        stage.setTitle("Generar Reporte B"); // 00100121 Establece el título de la ventana
    }

    public void showReporteC() throws IOException { // 00100121 Método para mostrar la ventana de reporte C
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reporteC-view.fxml")); // 00100121 Carga el archivo FXML de reporte C
        Parent root = fxmlLoader.load(); // 00100121 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnReporteC.getScene().getWindow(); // 00100121 Obtiene la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00100121 Establece la nueva escena
        stage.setTitle("Generar Reporte C"); // 00100121 Establece el título de la ventana
    }

    public void showReporteD() throws IOException { // 00100121 Método para mostrar la ventana de reporte D
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reporteD-view.fxml")); // 00100121 Carga el archivo FXML de reporte D
        Parent root = fxmlLoader.load(); // 00100121 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnReporteD.getScene().getWindow(); // 00100121 Obtiene la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00100121 Establece la nueva escena
        stage.setTitle("Generar Reporte D"); // 00100121 Establece el título de la ventana
    }

    public void showAgregarTarjeta() throws IOException { // 00100121 Método para mostrar la ventana de agregar tarjeta
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/tarjeta-view.fxml")); // 00100121 Carga el archivo FXML de agregar tarjeta
        Parent root = fxmlLoader.load(); // 00100121 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnAgregarTarjeta.getScene().getWindow(); // 00100121 Obtiene la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00100121 Establece la nueva escena
        stage.setTitle("Agregar Tarjeta"); // 00100121 Establece el título de la ventana
    }

    public void showEditarInfo() throws IOException { // 00100121 Método para mostrar la ventana de editar información
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/editar-informacion-view.fxml")); // 00100121 Carga el archivo FXML de editar información
        Parent root = fxmlLoader.load(); // 00100121 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnEditarInfo.getScene().getWindow(); // 00100121 Obtiene la ventana actual
        stage.setScene(new Scene(root, 1250, 700)); // 00100121 Establece la nueva escena
        stage.setTitle("Editar Información del Cliente"); // 00100121 Establece el título de la ventana
    }
    public void showVerReportes() throws IOException { // 00197822 Método para mostrar la ventana de ver reportes
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/reportes-view.fxml")); // 00197822 Cargar el archivo FXML de ver reportes
        Parent root = fxmlLoader.load(); // 00197822 Cargar el contenido del archivo FXML
        Stage stage = new Stage(); // 00197822 Crear una nueva ventana
        stage.setScene(new Scene(root)); // 00197822 Establecer la escena en la nueva ventana
        stage.setTitle("Ver Reportes"); // 00197822 Establecer el título de la nueva ventana
        stage.show(); // 00197822 Mostrar la nueva ventana
    }

    public void volverMain() throws IOException { // 00100121 Método para volver al menú principal
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/main-view.fxml")); // 00100121 Carga el archivo FXML del menú principal
        Parent root = fxmlLoader.load(); // 00100121 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnVolver.getScene().getWindow(); // 00100121 Obtiene la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00100121 Establece la nueva escena
        stage.setTitle("Sistema de Gestión"); // 00100121 Establece el título de la ventana
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00100121 Método para mostrar alertas
        Alert alert = new Alert(alertType); // 00100121 Crea una nueva alerta
        alert.setTitle(title); // 00100121 Establece el título de la alerta
        alert.setContentText(message); // 00100121 Establece el mensaje de la alerta
        alert.setHeaderText(null); // 00100121 Elimina el encabezado de la alerta
        alert.showAndWait(); // 00100121 Muestra la alerta y espera a que el usuario la cierre
    }
}
