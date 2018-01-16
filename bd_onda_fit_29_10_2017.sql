/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.7.14-log : Database - onda_fit
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`onda_fit` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `onda_fit`;

/*Table structure for table `cliente_ingresos` */

DROP TABLE IF EXISTS `cliente_ingresos`;

CREATE TABLE `cliente_ingresos` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id del ingreso del cliente',
  `cliente_paquete_id` bigint(20) unsigned NOT NULL COMMENT 'Id del cliente relacionado con el paquete',
  `cliente_id` bigint(20) unsigned NOT NULL COMMENT 'Id del cliente',
  `fecha_ingreso` datetime NOT NULL COMMENT 'Fecha de ingreso',
  `usuario_id` bigint(20) unsigned NOT NULL COMMENT 'Id del usuario que registra',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `cliente_ingresos` */

/*Table structure for table `cliente_paquete` */

DROP TABLE IF EXISTS `cliente_paquete`;

CREATE TABLE `cliente_paquete` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id principal',
  `cliente_id` bigint(20) unsigned NOT NULL COMMENT 'Id del cliente',
  `paquete_id` int(10) unsigned NOT NULL COMMENT 'Id del paquete',
  `descuento_id` int(10) unsigned DEFAULT NULL COMMENT 'Id del descuento',
  `numero_dias_tiquetera` smallint(2) unsigned DEFAULT NULL COMMENT 'Número de dias cuando es tiquetera',
  `dias_usados_tiquetera` smallint(2) unsigned DEFAULT NULL COMMENT 'Cada vez que ponga el dedo el cliente de tiquetera',
  `precio_base` decimal(12,2) NOT NULL COMMENT 'Precio base',
  `valor_total` decimal(12,2) NOT NULL COMMENT 'Valor total del paquete con descuento',
  `estado` smallint(1) unsigned NOT NULL DEFAULT '1' COMMENT 'Estado (1: Activo; 2: Vencido)',
  `fecha_inicia_paquete` date NOT NULL COMMENT 'Fecha de inicio paquete',
  `fecha_finaliza_paquete` date DEFAULT NULL COMMENT 'Fecha de finalizacion del paquete',
  `usuario_id` bigint(20) unsigned NOT NULL COMMENT 'Id del usuario registra',
  `fecha_registro` datetime NOT NULL COMMENT 'Fecha registro',
  `fecha_modificacion` datetime NOT NULL COMMENT 'Fecha modificación',
  `tipo_pago` smallint(1) unsigned NOT NULL COMMENT 'ETipoPago(1:Efectivo; 2:Tarjeta)',
  PRIMARY KEY (`id`),
  KEY `cliente_paquete_cliente_id_idx` (`paquete_id`),
  KEY `cliente_paquete_paquete_id_idx` (`cliente_id`),
  KEY `cliente_paquete_descuento_id_idx` (`descuento_id`),
  KEY `cliente_paquete_usuario_id_idx` (`usuario_id`),
  CONSTRAINT `cliente_paquete_cliente_id_fk` FOREIGN KEY (`paquete_id`) REFERENCES `paquetes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cliente_paquete_descuento_id_fk` FOREIGN KEY (`descuento_id`) REFERENCES `descuentos` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cliente_paquete_paquete_id_fk` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cliente_paquete_usuario_id_fk` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `cliente_paquete` */

/*Table structure for table `clientes` */

DROP TABLE IF EXISTS `clientes`;

CREATE TABLE `clientes` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `persona_id` bigint(20) unsigned NOT NULL,
  `peso` decimal(10,2) unsigned DEFAULT NULL,
  `talla` decimal(10,2) DEFAULT NULL,
  `muslo_ant` decimal(10,2) DEFAULT NULL,
  `triceps` decimal(10,2) DEFAULT NULL,
  `pectoral` decimal(10,2) DEFAULT NULL,
  `siliaco` decimal(10,2) DEFAULT NULL,
  `abdomen` decimal(10,2) DEFAULT NULL,
  `test_mmss` decimal(10,2) DEFAULT NULL,
  `test_mmii` decimal(10,2) DEFAULT NULL,
  `test_uno` decimal(10,2) DEFAULT NULL,
  `test_dos` decimal(10,2) DEFAULT NULL,
  `test_tres` decimal(10,2) DEFAULT NULL,
  `frecuencia_cardiaca` decimal(10,0) DEFAULT NULL,
  `tension_arterial` decimal(10,0) DEFAULT NULL,
  `peak_air` decimal(10,0) DEFAULT NULL,
  `observaciones` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `clientes_persona_id_idx` (`persona_id`),
  CONSTRAINT `clientes_persona_id_fk` FOREIGN KEY (`persona_id`) REFERENCES `personas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `clientes` */

insert  into `clientes`(`id`,`persona_id`,`peso`,`talla`,`muslo_ant`,`triceps`,`pectoral`,`siliaco`,`abdomen`,`test_mmss`,`test_mmii`,`test_uno`,`test_dos`,`test_tres`,`frecuencia_cardiaca`,`tension_arterial`,`peak_air`,`observaciones`) values (1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `descuentos` */

DROP TABLE IF EXISTS `descuentos`;

CREATE TABLE `descuentos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id del descuento',
  `nombre` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Nombre del descuento',
  `porcentaje` decimal(10,2) NOT NULL COMMENT 'Porcentaje del descuento',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `descuentos` */

/*Table structure for table `gastos` */

DROP TABLE IF EXISTS `gastos`;

CREATE TABLE `gastos` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(128) NOT NULL,
  `valor` decimal(10,0) unsigned NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `usuario_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `gastos` */

/*Table structure for table `paquetes` */

DROP TABLE IF EXISTS `paquetes`;

CREATE TABLE `paquetes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id del paquete',
  `nombre` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Nombre del paquete',
  `tipo` smallint(1) unsigned NOT NULL COMMENT 'ETipoPlan(1: Dia; 2: Mes; 3: Bimestre; 4: Trimestre; 5: Cuatrimestre; 6: Semestre; 7: Año)',
  `precio_base` decimal(10,0) unsigned NOT NULL COMMENT 'Precio base del paquete',
  `yn_tiquetera` smallint(1) unsigned NOT NULL DEFAULT '2' COMMENT 'Tiquetera(1: Si; 2: No)',
  `dias_aplazamiento` smallint(2) unsigned NOT NULL COMMENT 'Días de aplazamiento',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `paquetes` */

insert  into `paquetes`(`id`,`nombre`,`tipo`,`precio_base`,`yn_tiquetera`,`dias_aplazamiento`) values (1,'mensualidad',2,100000,2,5);

/*Table structure for table `personas` */

DROP TABLE IF EXISTS `personas`;

CREATE TABLE `personas` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id de la persona',
  `primer_nombre` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Primer nombre',
  `segundo_nombre` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Segundo Nombre',
  `primer_apellido` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Primer apellido',
  `segundo_apellido` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Segundo apellido',
  `tipo_identificacion` smallint(1) unsigned NOT NULL COMMENT 'Tipo identificación (1. Tarjeta Identidad; 2: Cédula de ciudadanía; 3: Cédula de extranjería)',
  `numero_identificacion` varchar(12) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Número de identificacion',
  `lugar_expedicion` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Ciudad de expedición',
  `genero` smallint(1) unsigned NOT NULL COMMENT 'Genero (1: Masculino; 2: Femenino)',
  `estado_civil` smallint(1) unsigned DEFAULT NULL COMMENT 'Estado civil (1: Soltero; 2: Casado; 3: Unión Libre; 4: Viudo; 5: Separado; 6: Divorciado)',
  `fecha_nacimiento` date NOT NULL COMMENT 'Fecha de nacimiento',
  `direccion` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Direción domicilio',
  `barrio` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Barrio domicilio',
  `telefono` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Teléfono domicilio',
  `movil` varchar(15) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Numero móvil o celular',
  `email` varchar(120) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Correo electrónico',
  `url_huella` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ruta donde se encuentra el archivo de la huella guardada',
  `huella_dactilar` longblob COMMENT 'Huella dactilar',
  `foto_perfil` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Foto del perfil',
  `fecha_registro` datetime NOT NULL COMMENT 'Fecha registro',
  `fecha_modificacion` datetime NOT NULL COMMENT 'Fecha modificación',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Tabla que representa las personas que intervienen en el sistema';

/*Data for the table `personas` */

insert  into `personas`(`id`,`primer_nombre`,`segundo_nombre`,`primer_apellido`,`segundo_apellido`,`tipo_identificacion`,`numero_identificacion`,`lugar_expedicion`,`genero`,`estado_civil`,`fecha_nacimiento`,`direccion`,`barrio`,`telefono`,`movil`,`email`,`url_huella`,`huella_dactilar`,`foto_perfil`,`fecha_registro`,`fecha_modificacion`) values (1,'admin',NULL,'admin',NULL,1,'1234',NULL,1,1,'2016-08-13',NULL,NULL,NULL,'304334455',NULL,NULL,NULL,'cshsdhdgsd.php','2016-08-13 16:29:51','2016-08-20 16:29:43');

/*Table structure for table `producto_ventas` */

DROP TABLE IF EXISTS `producto_ventas`;

CREATE TABLE `producto_ventas` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `productos_id` bigint(20) NOT NULL,
  `cantidad` double unsigned NOT NULL,
  `valor_total` double unsigned NOT NULL,
  `usuario_id` bigint(20) unsigned NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `cafeteria_usuario_id_fk_idx` (`usuario_id`),
  KEY `cafeteria_producto_id_fk_idx` (`productos_id`),
  CONSTRAINT `cafeteria_producto_id_fk` FOREIGN KEY (`productos_id`) REFERENCES `productos` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cafeteria_usuario_id_fk` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `producto_ventas` */

/*Table structure for table `productos` */

DROP TABLE IF EXISTS `productos`;

CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id del producto',
  `nombre` varchar(45) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Nombre del producto',
  `precio` decimal(10,2) NOT NULL COMMENT 'Precio del producto',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `productos` */

/*Table structure for table `usuarios` */

DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE `usuarios` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Id del usuario',
  `tipo_usuario` smallint(1) unsigned NOT NULL COMMENT 'Tipo de usuario(1: Admin; 2: Fisioterapeuta)',
  `persona_id` bigint(20) unsigned NOT NULL COMMENT 'Id de la persona',
  `loggin` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Loggin del usuario',
  `password` varchar(120) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Contrasena del usuario',
  `yn_activo` smallint(1) unsigned NOT NULL DEFAULT '1' COMMENT 'Activo(1: Si; 2: No)',
  `fecha_registro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro',
  `fecha_modificacion` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha modificación',
  PRIMARY KEY (`id`),
  UNIQUE KEY `loggin_UNIQUE` (`loggin`),
  KEY `usuarios_persona_id_idx` (`persona_id`),
  CONSTRAINT `usuarios_persona_id_fk` FOREIGN KEY (`persona_id`) REFERENCES `personas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `usuarios` */

insert  into `usuarios`(`id`,`tipo_usuario`,`persona_id`,`loggin`,`password`,`yn_activo`,`fecha_registro`,`fecha_modificacion`) values (1,1,1,'root','81dc9bdb52d04dc20036dbd8313ed055',1,'2016-08-13 09:30:10','2016-08-13 09:30:10');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
