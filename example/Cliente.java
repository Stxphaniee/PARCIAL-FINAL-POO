package org.example; //00197822 Define el paquete donde se encuentra la clase

public class Cliente {
    int id; //00197822 Identificador unico del cliente
    String nombre;  //00197822 Nombre del cliente
    String direccion; //00197822 Dirección del cliente
    String telefono;//00197822 Numero de telefono del cliente



    public Cliente(int id, String nombre, String direccion, String telefono) { //00197822 Constructor de la clase Cliente
        this.id = id; //00197822 Asignando el identificador
        this.nombre = nombre; //00197822 Asignando el nombre
        this.direccion = direccion; //00197822 Asignando la dirección
        this.telefono = telefono;//00197822 Asignando la dirección
    }
}
