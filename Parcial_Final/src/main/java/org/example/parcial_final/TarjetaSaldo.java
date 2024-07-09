package org.example.parcial_final;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class TarjetaSaldo {

    @FXML
    private ChoiceBox<String> tipoTarjetaChoiceBox;
    @FXML
    private TextField saldoField;

    private int idCliente;

    @FXML
    public void initialize() {
        tipoTarjetaChoiceBox.setItems(FXCollections.observableArrayList("Crédito", "Débito"));
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @FXML
    protected void onEstablecerSaldoButtonClick() {
        String tipoTarjeta = tipoTarjetaChoiceBox.getValue();
        String saldoStr = saldoField.getText();

        if (tipoTarjeta == null || saldoStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error de Validación", "Debe seleccionar un tipo de tarjeta y establecer un saldo.");
            return;
        }

        double saldo;
        try {
            saldo = Double.parseDouble(saldoStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato", "El saldo debe ser un número válido.");
            return;
        }

        Tarjeta tarjeta = new Tarjeta(idCliente);
        tarjeta.seleccionarTarjeta(tipoTarjeta, saldo);

        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Tarjeta y saldo establecidos exitosamente.");
        tipoTarjetaChoiceBox.setValue(null);
        saldoField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
