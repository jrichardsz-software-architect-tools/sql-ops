/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 */ /*!50003 TRIGGER before_user_profiles_modified
BEFORE UPDATE ON `user_profiles`
FOR EACH ROW BEGIN
	INSERT INTO user_profiles_modified (
		modified_on, user_id, user_name, user_email
	) VALUES(
		NOW(), OLD.user_id, OLD.user_name, OLD.user_email
	);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 */ /*!50003 TRIGGER after_user_profiles_modified
AFTER UPDATE ON `user_profiles`
FOR EACH ROW BEGIN
	INSERT INTO user_profiles_modified (
		modified_on, user_id, user_name, user_email
	) VALUES(
		NOW(), NEW.user_id, NEW.user_name, NEW.user_email
	);
END */;;