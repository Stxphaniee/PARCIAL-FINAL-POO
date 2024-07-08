package org.example; //00197822 Define el paquete donde se encuentra la clase

import java.time.LocalDate; //00197822 Importa la clase LocalDate de java.time para manejar fechas

public class Tarjeta { //00197822 Define la clase publica Tarjeta que representa una tarjeta de credito o debito
    int id; //00197822 Identificador unico de la tarjeta
    String numeroTarjeta; //00197822 Numero de la tarjeta
    LocalDate fechaExpiracion; //00197822 Fecha de expiración de la tarjeta
    String tipoTarjeta; //00197822 Tipo de tarjeta (Credito o Debito)
    String facilitador;  //00197822 Facilitador de la tarjeta (Visa, MasterCard, etc.)
    int clienteId; //00197822 Identificador del cliente asociado


    public Tarjeta(int id, String numeroTarjeta, LocalDate fechaExpiracion, String tipoTarjeta, String facilitador, int clienteId) { //00197822 Constructor de la clase Tarjeta
        this.id = id; //00197822 Asignando el identificador
        this.numeroTarjeta = numeroTarjeta; //00197822 Asignando el número de tarjeta
        this.fechaExpiracion = fechaExpiracion; //00197822 Asignando la fecha de expiración
        this.tipoTarjeta = tipoTarjeta; //00197822 Asignando el tipo de tarjeta
        this.facilitador = facilitador; //00197822 Asignando el facilitador
        this.clienteId = clienteId; //00197822 Asignando el identificador del cliente asociado
    }
}
