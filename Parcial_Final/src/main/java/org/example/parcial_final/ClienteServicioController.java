package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ServiciosController {

    @FXML
    public Button btnClientes;

    @FXML
    public Button btnCompras;

    @FXML
    public Button btnTarjetas;

    @FXML
    public Button btnReportes;

    @FXML
    public Button btnSalir;

    @FXML
    private void initialize() {
        btnCompras.setOnAction(event -> {
            try {
                ComprasPantalla();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnReportes.setOnAction(event -> {
            try {
                ReportesPantalla();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnClientes.setOnAction(event -> {
            try {
                ClientesPantalla();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnTarjetas.setOnAction(event -> {
            try {
                TarjetasPantalla();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnSalir.setOnAction(event -> {
            Salir();
        });
    }

    private void ComprasPantalla() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/Compras_Screen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnCompras.getScene().getWindow();
        stage.setScene(new Scene(root, 1250, 700));
    }

    private void ReportesPantalla() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/Reportes_Screen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnReportes.getScene().getWindow();
        stage.setScene(new Scene(root, 1250, 700));
    }

    private void ClientesPantalla() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/Clientes_Screen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnClientes.getScene().getWindow();
        stage.setScene(new Scene(root, 1250, 700));
    }

    private void TarjetasPantalla() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/Tarjetas_Screen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnTarjetas.getScene().getWindow();
        stage.setScene(new Scene(root, 1250, 700));
    }

    private void Salir() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/Main_Screen.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.setScene(new Scene(root, 1250, 700));
    }
}
