ALTER TABLE `users`
	ADD COLUMN `validation_code` VARCHAR(6) NULL DEFAULT NULL AFTER `badLogins`;
ALTER TABLE `users`
	ADD COLUMN `validated` TINYINT NULL DEFAULT NULL AFTER `validation_code`;
