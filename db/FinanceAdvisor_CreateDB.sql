-- MySQL Script generated by MySQL Workbench
-- Thu Oct 20 00:45:33 2016
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema FinanceAdvisorDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema FinanceAdvisorDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `FinanceAdvisorDB` DEFAULT CHARACTER SET utf8 ;
USE `FinanceAdvisorDB` ;

-- -----------------------------------------------------
-- Table `FinanceAdvisorDB`.`tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FinanceAdvisorDB`.`tag` ;

CREATE TABLE IF NOT EXISTS `FinanceAdvisorDB`.`tag` (
  `idtag` INT(11) NOT NULL AUTO_INCREMENT,
  `tag_name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`idtag`),
  UNIQUE INDEX `idtag_UNIQUE` (`idtag` ASC),
  UNIQUE INDEX `tag_name_UNIQUE` (`tag_name` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 69
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `FinanceAdvisorDB`.`budget`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FinanceAdvisorDB`.`budget` ;

CREATE TABLE IF NOT EXISTS `FinanceAdvisorDB`.`budget` (
  `idbudget` INT(11) NOT NULL AUTO_INCREMENT,
  `date` DATETIME NULL DEFAULT NULL,
  `idtag` INT(11) NULL DEFAULT NULL,
  `spent` FLOAT NULL DEFAULT '0',
  `limit` FLOAT NULL DEFAULT '0',
  PRIMARY KEY (`idbudget`),
  UNIQUE INDEX `idbudget_UNIQUE` (`idbudget` ASC),
  INDEX `idtag_fk_idx` (`idtag` ASC),
  INDEX `budget_idtag_fk_idx` (`idtag` ASC),
  CONSTRAINT `budget_idtag_fk`
    FOREIGN KEY (`idtag`)
    REFERENCES `FinanceAdvisorDB`.`tag` (`idtag`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `FinanceAdvisorDB`.`vendor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FinanceAdvisorDB`.`vendor` ;

CREATE TABLE IF NOT EXISTS `FinanceAdvisorDB`.`vendor` (
  `idvendor` INT(11) NOT NULL AUTO_INCREMENT,
  `vendor_name` VARCHAR(45) NULL DEFAULT NULL,
  `idtag` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idvendor`),
  UNIQUE INDEX `idvendor_UNIQUE` (`idvendor` ASC),
  UNIQUE INDEX `name_UNIQUE` (`vendor_name` ASC),
  INDEX `vendor_idtag_fk_idx` (`idtag` ASC),
  CONSTRAINT `vendor_idtag_fk`
    FOREIGN KEY (`idtag`)
    REFERENCES `FinanceAdvisorDB`.`tag` (`idtag`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 320
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `FinanceAdvisorDB`.`transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FinanceAdvisorDB`.`transaction` ;

CREATE TABLE IF NOT EXISTS `FinanceAdvisorDB`.`transaction` (
  `idtransaction` INT(11) NOT NULL AUTO_INCREMENT,
  `value` FLOAT NULL DEFAULT NULL,
  `date` DATETIME NULL DEFAULT NULL,
  `card` MEDIUMTEXT NULL DEFAULT NULL,
  `idvendor` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idtransaction`),
  UNIQUE INDEX `idnew_table_UNIQUE` (`idtransaction` ASC),
  INDEX `vendor_fk_idx` (`idvendor` ASC),
  CONSTRAINT `vendor_fk`
    FOREIGN KEY (`idvendor`)
    REFERENCES `FinanceAdvisorDB`.`vendor` (`idvendor`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 121
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
