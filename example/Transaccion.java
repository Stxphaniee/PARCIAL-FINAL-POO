package org.example;  //00197822 Define el paquete donde se encuentra la clase

import java.time.LocalDate; //00197822 Importa la clase LocalDate de java.time para manejar fechas

public class Transaccion {  //00197822 Define la clase publica Transaccion
    int id;   //00197822 Identificador unico de la transaccion
    LocalDate fechaTransaccion; //00197822 Fecha de la transaccion
    double monto; //00197822 Monto de la transacción
    String descripcion; //00197822 Descripcion de la transaccion
    int tarjetaId; //00197822 Identificador de la tarjeta utilizada



    public Transaccion(int id, LocalDate fechaTransaccion, double monto, String descripcion, int tarjetaId) { //00197822 Constructor de la clase Transaccion
        this.id = id; //00197822 Asinando el identificador
        this.fechaTransaccion = fechaTransaccion; //00197822 Asigna la fecha de la transaccion
        this.monto = monto; //00197822 Asignando el monto de la transaccion
        this.descripcion = descripcion; //00197822 Asignando la descripcion de la transaccion
        this.tarjetaId = tarjetaId; //00197822 Asignando el identificador de la tarjeta utilizada
    }
}
