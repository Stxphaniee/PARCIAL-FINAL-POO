package org.example.parcial_final;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ReportesViewController {

    @FXML
    private Button btnRegresar; // 00197822 Botón para regresar al menú anterior
    @FXML
    private ComboBox<String> tipoReporteComboBox; // 00197822 ComboBox para seleccionar el tipo de reporte
    @FXML
    private ListView<String> reportesListView; // 00197822 ListView para mostrar los reportes disponibles
    @FXML
    private Button btnVerReporte; // 00197822 Botón para ver el reporte seleccionado

    @FXML
    private void initialize() { // 00197822 Método que se ejecuta al inicializar la vista
        List<String> tiposReportes = Arrays.asList("Reporte A", "Reporte B", "Reporte C", "Reporte D");
        tipoReporteComboBox.setItems(FXCollections.observableArrayList(tiposReportes)); // 00197822 Configura el ComboBox con los tipos de reportes

        tipoReporteComboBox.setOnAction(event -> { // 00197822 Acción al seleccionar un tipo de reporte
            String tipoReporte = tipoReporteComboBox.getValue();
            cargarReportes(tipoReporte); // 00197822 Cargar los reportes del tipo seleccionado
        });

        btnVerReporte.setOnAction(event -> { // 00197822 Acción al presionar el botón de ver reporte
            String reporteSeleccionado = reportesListView.getSelectionModel().getSelectedItem();
            if (reporteSeleccionado != null) {
                abrirReporte(reporteSeleccionado); // 00197822 Abrir el reporte seleccionado
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Seleccione un reporte para ver."); // 00197822 Mostrar alerta si no se selecciona ningún reporte
            }
        });

        btnRegresar.setOnAction(event -> { // 00197822 Acción al presionar el botón de regresar
            try {
                backServicio(); // 00197822 Llamar al método para regresar al menú anterior
            } catch (IOException e) {
                e.printStackTrace(); // 00197822 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al regresar al menú anterior."); // 00197822 Mostrar alerta de error
            }
        });
    }

    private void cargarReportes(String tipoReporte) { // 00197822 Método para cargar los reportes del tipo seleccionado
        File directorio = new File("Reportes");
        if (!directorio.exists() || !directorio.isDirectory()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se encontraron reportes."); // 00197822 Mostrar alerta si no se encuentran reportes
            return;
        }

        File[] archivos = directorio.listFiles((dir, name) -> name.startsWith(tipoReporte.replace(" ", "_")));
        if (archivos != null) {
            List<String> reportes = Arrays.asList(archivos).stream().map(File::getName).toList();
            reportesListView.setItems(FXCollections.observableArrayList(reportes)); // 00197822 Configura el ListView con los nombres de los reportes
        }
    }

    private void abrirReporte(String reporte) { // 00197822 Método para abrir el reporte seleccionado
        File archivo = new File("Reportes/" + reporte);
        if (archivo.exists()) {
            try {
                new ProcessBuilder("notepad.exe", archivo.getAbsolutePath()).start(); // 00197822 Abrir el archivo con el Bloc de notas
            } catch (IOException e) {
                e.printStackTrace(); // 00197822 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir el reporte."); // 00197822 Mostrar alerta de error
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se encontró el reporte."); // 00197822 Mostrar alerta si no se encuentra el archivo
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00197822 Método para mostrar alertas
        Alert alert = new Alert(alertType); // 00197822 Crear una nueva alerta
        alert.setTitle(title); // 00197822 Establecer el título de la alerta
        alert.setContentText(message); // 00197822 Establecer el mensaje de la alerta
        alert.setHeaderText(null); // 00197822 Eliminar el encabezado de la alerta
        alert.showAndWait(); // 00197822 Mostrar la alerta y esperar a que el usuario la cierre
    }

    public void backServicio() throws IOException { // 00197822 Método para regresar al menú de servicio
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/servicio-view.fxml")); // 00197822 Cargar el archivo FXML del menú de servicio
        Parent root = fxmlLoader.load(); // 00197822 Cargar el contenido del archivo FXML
        Stage stage = (Stage) btnRegresar.getScene().getWindow(); // 00197822 Obtener la ventana actual
        stage.setScene(new Scene(root, 1050, 600)); // 00197822 Establecer la nueva escena en la ventana
        stage.setTitle("Servicio"); // 00197822 Establecer el título de la ventana
    }
}
