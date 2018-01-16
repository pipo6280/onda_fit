ALTER TABLE `onda_fit`.`producto_ventas`   
  ADD COLUMN `tipo_pago` SMALLINT(1) UNSIGNED DEFAULT 1  NOT NULL  COMMENT 'ETipoPago(1:Efectivo; 2:Tarjeta)' AFTER `fecha_registro`;
