-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.45-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ ifcontrol3;
USE ifcontrol3;
show tables;
DROP TABLE IF EXISTS `acao`;
CREATE TABLE `acao` (
  `idAcao` int(11) NOT NULL auto_increment,
  `idUser` int(11) NOT NULL,
  `nSala` int(11) NOT NULL,
  `tipoAcao` varchar(20) NOT NULL,
  `dataAcao` date NOT NULL,
  `horaAcao` time NOT NULL,
  `statusAcao` boolean NOT NULL,
  `login` varchar(45) NOT NULL,
  PRIMARY KEY  (`idAcao`),
  KEY `idUser_idx` (`idUser`),
  KEY `nSala_idx` (`nSala`),
  CONSTRAINT `idUser` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `nSala` FOREIGN KEY (`nSala`) REFERENCES `sala` (`nSala`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=891 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sala`;
CREATE TABLE `sala` (
  `nSala` int(11) NOT NULL,
  `est_Luzes` tinyint(1) default NULL,
  `est_Ar` tinyint(1) default NULL,
  `temperatura` double default NULL,
  `umidade` double default NULL,
  `temp_Ar` int(11) default NULL,
  `presenca` tinyint(1) default NULL,
  `horaAtivacao` time default NULL,
  `horaDesativacao` time default NULL,
  `est_sala` tinyint(1) default NULL,
  `est_datashow` tinyint(1) default NULL,
  `conexao` tinyint(1) default NULL,
  ip varchar(20) not null,
  PRIMARY KEY  (`nSala`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `idUser` int(11) NOT NULL auto_increment,
  `siap_id` int(20) NOT NULL,
  `login` varchar(20) NOT NULL,
  `senha` varchar(20) NOT NULL,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY  (`idUser`),
  foreign key (siap_id) references siaps(id)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `siaps`;
CREATE TABLE `siaps` (
	id INT PRIMARY KEY AUTO_INCREMENT,
    siap bigint UNIQUE NOT NULL
);

DROP TABLE IF EXISTS `codIr`;
CREATE TABLE `codIR` (
	id INT PRIMARY KEY AUTO_INCREMENT,
    cod varchar(200) UNIQUE NOT NULL,
    funcao varchar(20) unique not null,
    dispositivo_id INT NOT NULL,
    FOREIGN KEY (dispositivo_id) REFERENCES dis(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `dis`;
CREATE TABLE `dis` (
	id INT PRIMARY KEY AUTO_INCREMENT,
    tipo varchar(5) not null,
    modelo varchar(100),
    marca varchar(50) not null,
    config text not null,
    sala_id INT NOT NULL,
	FOREIGN KEY (sala_id) REFERENCES sala(nSala) ON DELETE CASCADE
);
CREATE TABLE `agendamento` (
	id int primary key auto_increment,
    titulo varchar(50) not null,
    autor varchar(50) not null,
    dataInicio date not null,
    dataFim date not null,
    hAtiv datetime not null,
    hDesat datetime not null
);
create table `diasDaSemana`(
	id int primary key auto_increment,
    agendamento_id int not null,
    dia int,
    foreign key (agendamento_id) references agendamento(id) on delete cascade
);
create table `nSalaAgendamento`(
	id int primary key auto_increment,
    agendamento_id int not null,
    nSala int,
    foreign key (agendamento_id) references agendamento(id) on delete cascade
);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
show tables;