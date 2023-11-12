DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `trigger_1` */

/*!50003 CREATE */ /*!50003 TRIGGER `trigger_1` AFTER INSERT ON `foo_table` FOR EACH ROW 
BEGIN
    DECLARE operation_type VARCHAR(10);
    DECLARE record_identifier VARCHAR(255); -- Puedes ajustar la longitud según tus necesidades
    
    SET operation_type = 'INSERT';
    SET record_identifier = CONCAT('CINSTITUCION: ', NEW.`CINSTITUCION`, ', CPROGRAMA: ', NEW.`CPROGRAMA`);

    INSERT INTO log_table (operation_type, table_name, details)
    VALUES (operation_type, 'foo_table', record_identifier);
END */$$




DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `trigger_2` */

/*!50003 CREATE */ /*!50003 TRIGGER `trigger_2` AFTER UPDATE ON `foo_table` FOR EACH ROW 
BEGIN
    DECLARE operation_type VARCHAR(10);
    DECLARE record_identifier VARCHAR(255); -- Puedes ajustar la longitud según tus necesidades
    
    SET operation_type = 'UPDATE';
    SET record_identifier = CONCAT('CINSTITUCION: ', NEW.`CINSTITUCION`, ', CPROGRAMA: ', NEW.`CPROGRAMA`);

    INSERT INTO log_table (operation_type, table_name, details)
    VALUES (operation_type, 'foo_table', record_identifier);
END */$$