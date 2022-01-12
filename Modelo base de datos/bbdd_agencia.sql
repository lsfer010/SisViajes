-- MySQL Script generated by MySQL Workbench
-- Wed Jan 12 01:09:18 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema sis_viajes
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sis_viajes
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sis_viajes` DEFAULT CHARACTER SET utf8 ;
USE `sis_viajes` ;

-- -----------------------------------------------------
-- Table `sis_viajes`.`clientes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sis_viajes`.`clientes` (
  `ID_Cliente` INT NOT NULL AUTO_INCREMENT,
  `Compañia` VARCHAR(45) NULL DEFAULT NULL,
  `Apellido_Paterno` VARCHAR(45) NOT NULL,
  `Apellido_Materno` VARCHAR(45) NULL DEFAULT NULL,
  `Nombre` VARCHAR(45) NOT NULL,
  `Fecha_Nacimiento` VARCHAR(10) NULL DEFAULT NULL,
  `No_Club_Premier` INT NULL DEFAULT NULL,
  `Sexo` VARCHAR(1) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_Cliente`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sis_viajes`.`operadores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sis_viajes`.`operadores` (
  `idOperadores` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idOperadores`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `sis_viajes`.`reservaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sis_viajes`.`reservaciones` (
  `Clave_Reservacion` VARCHAR(45) NOT NULL,
  `Clientes_ID_Cliente` INT NOT NULL,
  `Operadores_idOperadores` INT NOT NULL,
  `Forma_Pago_Cliente` VARCHAR(45) NOT NULL,
  `Precio` FLOAT NOT NULL,
  `Req_Factura` VARCHAR(2) NOT NULL,
  `No_Factura_Cliente` VARCHAR(32) NULL DEFAULT NULL,
  `No_Factura_Operador` VARCHAR(32) NULL DEFAULT NULL,
  `Forma_Pago_Vendedor` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`Clave_Reservacion`),
  INDEX `fk_Reservaciones_Clientes_idx` (`Clientes_ID_Cliente` ASC) VISIBLE,
  INDEX `fk_Reservaciones_Operadores1_idx` (`Operadores_idOperadores` ASC) VISIBLE,
  CONSTRAINT `fk_Reservaciones_Clientes`
    FOREIGN KEY (`Clientes_ID_Cliente`)
    REFERENCES `sis_viajes`.`clientes` (`ID_Cliente`),
  CONSTRAINT `fk_Reservaciones_Operadores1`
    FOREIGN KEY (`Operadores_idOperadores`)
    REFERENCES `sis_viajes`.`operadores` (`idOperadores`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
