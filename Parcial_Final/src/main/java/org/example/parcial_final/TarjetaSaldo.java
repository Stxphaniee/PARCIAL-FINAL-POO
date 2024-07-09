package org.example.parcial_final; // 00100121 Indica el paquete al que pertenece esta clase

import javafx.collections.FXCollections; // 00100121 Importa la clase FXCollections para manejar listas observables
import javafx.fxml.FXML; // 00100121 Importa la anotación FXML para inyección de elementos del FXML
import javafx.scene.control.Alert; // 00100121 Importa la clase Alert para mostrar cuadros de diálogo
import javafx.scene.control.ChoiceBox; // 00100121 Importa la clase ChoiceBox para desplegables
import javafx.scene.control.TextField; // 00100121 Importa la clase TextField para campos de texto

public class TarjetaSaldo { // 00100121 Define la clase TarjetaSaldo

    @FXML // 00100121 Anotación para inyectar el elemento desde el archivo FXML
    private ChoiceBox<String> tipoTarjetaChoiceBox; // 00100121 Campo para el ChoiceBox de tipos de tarjeta
    @FXML // 00100121 Anotación para inyectar el elemento desde el archivo FXML
    private TextField saldoField; // 00100121 Campo para el TextField del saldo

    private int idCliente; // 00100121 Campo para almacenar el ID del cliente

    @FXML // 00100121 Método que se llama al inicializar el controlador
    public void initialize() {
        tipoTarjetaChoiceBox.setItems(FXCollections.observableArrayList("Crédito", "Débito")); // 00100121 Establece los elementos del ChoiceBox
    }

    public void setIdCliente(int idCliente) { // 00100121 Método para establecer el ID del cliente
        this.idCliente = idCliente; // 00100121 Asigna el ID del cliente al campo correspondiente
    }

    @FXML // 00100121 Método que se llama al hacer clic en el botón para establecer saldo
    protected void onEstablecerSaldoButtonClick() {
        String tipoTarjeta = tipoTarjetaChoiceBox.getValue(); // 00100121 Obtiene el valor seleccionado del ChoiceBox
        String saldoStr = saldoField.getText(); // 00100121 Obtiene el texto del campo de saldo

        if (tipoTarjeta == null || saldoStr.isEmpty()) { // 00100121 Verifica si el tipo de tarjeta o el saldo están vacíos
            showAlert(Alert.AlertType.ERROR, "Error de Validación", "Debe seleccionar un tipo de tarjeta y establecer un saldo."); // 00100121 Muestra una alerta de error
            return; // 00100121 Sale del método si hay errores de validación
        }

        double saldo; // 00100121 Declara una variable para el saldo
        try {
            saldo = Double.parseDouble(saldoStr); // 00100121 Intenta convertir el texto del saldo a un número
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato", "El saldo debe ser un número válido."); // 00100121 Muestra una alerta si el saldo no es un número válido
            return; // 00100121 Sale del método si hay un error de formato
        }

        Tarjeta tarjeta = new Tarjeta(idCliente); // 00100121 Crea una nueva instancia de Tarjeta con el ID del cliente
        tarjeta.seleccionarTarjeta(tipoTarjeta, saldo); // 00100121 Selecciona la tarjeta y establece el saldo

        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Tarjeta y saldo establecidos exitosamente."); // 00100121 Muestra una alerta de éxito
        tipoTarjetaChoiceBox.setValue(null); // 00100121 Limpia la selección del ChoiceBox
        saldoField.clear(); // 00100121 Limpia el campo de saldo
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00100121 Método para mostrar alertas
        Alert alert = new Alert(alertType); // 00100121 Crea una nueva instancia de Alert
        alert.setTitle(title); // 00100121 Establece el título de la alerta
        alert.setContentText(message); // 00100121 Establece el contenido de texto de la alerta
        alert.setHeaderText(null); // 00100121 Establece el encabezado de la alerta como nulo
        alert.showAndWait(); // 00100121 Muestra la alerta y espera a que el usuario la cierre
    }
}
