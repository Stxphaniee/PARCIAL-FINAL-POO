CREATE DATABASE Parcial_Final_POO;

USE Parcial_Final_POO;


-- Crear tabla Clientes
CREATE TABLE Clientes (
    ID INT PRIMARY KEY IDENTITY(1,1),
    NombreCompleto NVARCHAR(100) NOT NULL,
    Direccion NVARCHAR(255),
    NumeroTelefono NVARCHAR(15)
);


-- Crear tabla Tarjetas
CREATE TABLE Tarjetas (
    NumeroTarjeta NVARCHAR(16) PRIMARY KEY,
    ID_Cliente INT,
    FechaExpiracion DATE,
    TipoTarjeta NVARCHAR(10),
    Facilitador NVARCHAR(50),
    CONSTRAINT FK_Tarjetas_Clientes FOREIGN KEY (ID_Cliente) REFERENCES Clientes(ID)
);


-- Crear tabla Compras
CREATE TABLE Compras (
    ID INT PRIMARY KEY IDENTITY(1,1),
    ID_Cliente INT,
    NumeroTarjeta NVARCHAR(16),
    FechaCompra DATETIME,
    MontoTotal DECIMAL(10, 2),
    DescripcionCompra NVARCHAR(255),
    CONSTRAINT FK_Compras_Clientes FOREIGN KEY (ID_Cliente) REFERENCES Clientes(ID),
    CONSTRAINT FK_Compras_Tarjetas FOREIGN KEY (NumeroTarjeta) REFERENCES Tarjetas(NumeroTarjeta)
);


SELECT * FROM Clientes;