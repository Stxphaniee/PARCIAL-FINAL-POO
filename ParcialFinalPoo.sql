USE Parcial_Final_POO;

-- Crear tabla Clientes
CREATE TABLE Clientes (
    ID INT PRIMARY KEY IDENTITY(1,1),
    NombreCompleto NVARCHAR(100) NOT NULL,
    Apellidos NVARCHAR(100) NOT NULL,
    Direccion NVARCHAR(255),
    NumeroTelefono NVARCHAR(15)
);

-- Verificar si la tabla Clientes se creó correctamente
EXEC sp_help Clientes;

-- Crear tabla Tarjetas
CREATE TABLE Tarjetas (
    NumeroTarjeta NVARCHAR(16) PRIMARY KEY,
    ID_Cliente INT,
    FechaExpiracion DATE,
    TipoTarjeta NVARCHAR(10),
    Facilitador NVARCHAR(50),
    CONSTRAINT FK_Tarjetas_Clientes FOREIGN KEY (ID_Cliente) REFERENCES Clientes(ID)
);

-- Verificar si la tabla Tarjetas se creó correctamente
EXEC sp_help Tarjetas;

-- Agregar columna Saldo a la tabla Tarjetas
ALTER TABLE Tarjetas ADD Saldo DECIMAL(10, 2);

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

-- Verificar si la tabla Compras se creó correctamente
EXEC sp_help Compras;

-- Consultar todas las filas de la tabla Clientes
SELECT * FROM Clientes;

-- Consultar todas las filas de la tabla Tarjetas
SELECT * FROM Tarjetas;

-- Consultar datos combinados de Clientes y Tarjetas
SELECT c.NombreCompleto, c.Apellidos, c.Direccion, c.NumeroTelefono, t.NumeroTarjeta, t.Saldo
FROM Clientes c
LEFT JOIN Tarjetas t ON c.ID = t.ID_Cliente;

--Consultar todas las filas de la tabla Compras
select * from Compras

--Consultar todas las compras de un Cliente 
SELECT * FROM Compras WHERE ID_Cliente = 4;

