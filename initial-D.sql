CREATE SCHEMA `initial-D` DEFAULT CHARACTER SET utf8 ;
USE `initial-D` ;

CREATE TABLE `Vehiculos` (
  `matricula` CHAR(7) NOT NULL,
  `marca` VARCHAR(45) NULL,
  `modelo` VARCHAR(45) NULL,
  `precio de venta` FLOAT(10) NULL,
  `color` VARCHAR(45) NULL,
  PRIMARY KEY (`matricula`));
  
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('9849GSE', 'AUDI', 'RS	e-tron GT', 145760, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('8295XCK', 'Mercedes', 'Maybach Clase S', 249958.66, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('2365XQT', 'Renault', 'Megane', 23439, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('0104POM', 'Dacia', 'Dokker', 13338, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('3825YPY', 'Seat', 'Arona', 17245, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('2818AIR', 'AUDI', 'A1 Sportbach', '23310', 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('7413VEF', 'Mercedes', 'Maybach GLS', 191092, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('0034YYT', 'Renault', 'Captur', 185759, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('2404ARX', 'Dacia', 'Lodgy', 17667, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('0487CMR', 'Seat', 'Ibiza', 13088, 'rojo');
INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES ('6906PUE', 'Kia', 'Ceed', 13134, 'rojo');

CREATE TABLE `Clientes` (
  `nif` VARCHAR(9) NOT NULL,
  `nombre` VARCHAR(20) NULL,
  `apellido` VARCHAR(20) NULL,
  PRIMARY KEY (`nif`));
  
INSERT INTO `Clientes` (`nif`, `nombre`, `apellido`) VALUES ('12345678F', 'Miguel', 'Induráin');
INSERT INTO `Clientes` (`nif`, `nombre`, `apellido`) VALUES ('15935728G', 'Pedro', 'Delgado');
INSERT INTO `Clientes` (`nif`, `nombre`, `apellido`) VALUES ('16734891T', 'Alex', 'Zulle');
INSERT INTO `Clientes` (`nif`, `nombre`, `apellido`) VALUES ('65489354R', 'Tony', 'Canto');

CREATE TABLE `Descuentos` (
  `idDescuento` INT NOT NULL,
  `Descuento` INT NULL,
  `MotivoDesc` VARCHAR(200) NULL,
  PRIMARY KEY (`idDescuento`));
INSERT INTO `Descuentos` (`idDescuento`,  `Descuento`, `MotivoDesc`) VALUES ('1',  '0' , '');
INSERT INTO `Descuentos` (`idDescuento`,  `Descuento`, `MotivoDesc`) VALUES ('2',  '5' , 'porque si');
INSERT INTO `Descuentos` (`idDescuento`,  `Descuento`, `MotivoDesc`) VALUES ('3',  '10' , 'por que no?');
INSERT INTO `Descuentos` (`idDescuento`,  `Descuento`, `MotivoDesc`) VALUES ('4',  '25' , 'a cambio de un jamon');

CREATE TABLE `Ventas` (
  `idVenta` INT NOT NULL,
  `fecha` DATE NULL,
  `Vehiculo_matricula` CHAR(7) NOT NULL,
  `Cliente_nif` VARCHAR(9) NOT NULL,
  `id_descuento` INT NOT NULL,
  PRIMARY KEY (`idVenta`),
  CONSTRAINT `fk_Venta_Vehiculo`
    FOREIGN KEY (`Vehiculo_matricula`)
    REFERENCES `Vehiculos` (`matricula`),
  CONSTRAINT `fk_Venta_Cliente`
    FOREIGN KEY (`Cliente_nif`)
    REFERENCES `Clientes` (`nif`),
CONSTRAINT `fk_Venta_Descuento`
    FOREIGN KEY (`id_descuento`)
    REFERENCES `Descuentos` (`idDescuento`) ON UPDATE CASCADE ON DELETE CASCADE
);
    
INSERT INTO `Ventas` (`idVenta`, `fecha`,  `Vehiculo_matricula`, `Cliente_nif`, `id_descuento`) VALUES ('1', '2018-02-16', '0104POM','15935728G',1);
INSERT INTO `Ventas` (`idVenta`, `fecha`, `Vehiculo_matricula`, `Cliente_nif`, `id_descuento`) VALUES ('2', '2019-06-14', '8295XCK','12345678F',3);
INSERT INTO `Ventas` (`idVenta`, `fecha`,  `Vehiculo_matricula`, `Cliente_nif`, `id_descuento`) VALUES ('3', '2019-02-26',  '0034YYT','16734891T',2);
INSERT INTO `Ventas` (`idVenta`, `fecha`,  `Vehiculo_matricula`, `Cliente_nif`, `id_descuento`) VALUES ('5', '2018-08-25', '3825YPY','65489354R',4);

