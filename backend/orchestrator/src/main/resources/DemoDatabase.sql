use tst_poker;
-- ---
-- Globals
-- ---

-- SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- SET FOREIGN_KEY_CHECKS=0;

-- ---
-- Table 'users'
-- 
-- ---

DROP TABLE IF EXISTS `users`;
		
CREATE TABLE `users` (
  `id_user` BIGINT NOT NULL AUTO_INCREMENT,
  `nick_name` VARCHAR(64) NOT NULL,
  `email` VARCHAR(128) NOT NULL,
  `password` VARCHAR(250) NOT NULL,
  `chips` BIGINT NOT NULL,
  `photo` VARCHAR(250) NOT NULL,
  `badLogins` SMALLINT NOT NULL,
  PRIMARY KEY (`id_user`)
);

-- ---
-- Table 'sessions'
-- 
-- ---

DROP TABLE IF EXISTS `sessions`;
		
CREATE TABLE `sessions` (
  `id_session` BIGINT NOT NULL AUTO_INCREMENT,
  `id_user` BIGINT NOT NULL,
  `jwt_passphrase` VARCHAR(250) NOT NULL,
  `expiration` DATETIME NOT NULL,
  PRIMARY KEY (`id_session`),
KEY (`id_user`)
);

-- ---
-- Table 'friendships'
-- 
-- ---

DROP TABLE IF EXISTS `friendships`;
		
CREATE TABLE `friendships` (
  `id_user_origin` BIGINT NOT NULL,
  `id_user_target` BIGINT NOT NULL,
  `requested` DATETIME NOT NULL,
  `accepted` bit NOT NULL,
  PRIMARY KEY (`id_user_origin`, `id_user_target`)
);

-- ---
-- Table 'rooms'
-- 
-- ---

DROP TABLE IF EXISTS `rooms`;
		
CREATE TABLE `rooms` (
  `id_room` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  `accessPassword` VARCHAR(250) NOT NULL,
  `securityToken` VARCHAR(128) NOT NULL,
  `gproto` VARCHAR(128) NOT NULL,
  `max_players` SMALLINT NOT NULL,
  `description` VARCHAR(128) NOT NULL,
  `minCoinForAccess` BIGINT NOT NULL,
  `recoveryEmail` VARCHAR(250) NOT NULL,
  `badLogins` SMALLINT NOT NULL,
  `now_connected` bit NOT NULL,
  `isOfficial` bit NOT NULL,
  PRIMARY KEY (`id_room`)
);

-- ---
-- Table 'users_in_rooms'
-- 
-- ---

DROP TABLE IF EXISTS `users_in_rooms`;
		
CREATE TABLE `users_in_rooms` (
  `id_user` BIGINT NOT NULL,
  `id_room` BIGINT NOT NULL,
  `registered` DATETIME NOT NULL,
  `position` INTEGER NOT NULL,
  PRIMARY KEY (`id_user`, `id_room`)
);

-- ---
-- Table 'challenges'
-- 
-- ---

DROP TABLE IF EXISTS `challenges`;
		
CREATE TABLE `challenges` (
  `challengeID` INTEGER NOT NULL AUTO_INCREMENT,
  `id_user` BIGINT NOT NULL,
  `id_room` BIGINT NOT NULL,
  `challenge` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`challengeID`),
KEY (`id_user`, `id_room`)
);

-- ---
-- Table 'bans'
-- 
-- ---

DROP TABLE IF EXISTS `bans`;
		
CREATE TABLE `bans` (
  `id_user` BIGINT NOT NULL,
  `reason` VARCHAR(128) NOT NULL,
  `registered` DATE NOT NULL,
  `expire` DATETIME NOT NULL,
  `reporter` VARCHAR(250) NOT NULL,
  `restarts` INTEGER NOT NULL,
  PRIMARY KEY (`id_user`)
);

-- ---
-- Table 'warnings'
-- 
-- ---

DROP TABLE IF EXISTS `warnings`;
		
CREATE TABLE `warnings` (
  `id_user` BIGINT NOT NULL,
  `message` VARCHAR(250) NOT NULL,
  `registered` DATE NOT NULL,
  `restarts` INTEGER NOT NULL,
  `last_reporter` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`id_user`)
);

-- ---
-- Table 'users_banned'
-- 
-- ---

DROP TABLE IF EXISTS `users_banned`;
		
CREATE TABLE `users_banned` (
  `id_user` BIGINT NOT NULL,
  `id_room` BIGINT NOT NULL,
  `registered` DATETIME NOT NULL,
  PRIMARY KEY (`id_user`, `id_room`)
);

-- ---
-- Foreign Keys 
-- ---

ALTER TABLE `sessions` ADD FOREIGN KEY (id_user) REFERENCES `users` (`id_user`);
ALTER TABLE `friendships` ADD FOREIGN KEY (id_user_origin) REFERENCES `users` (`id_user`);
ALTER TABLE `friendships` ADD FOREIGN KEY (id_user_target) REFERENCES `users` (`id_user`);
ALTER TABLE `users_in_rooms` ADD FOREIGN KEY (id_user) REFERENCES `users` (`id_user`);
ALTER TABLE `users_in_rooms` ADD FOREIGN KEY (id_room) REFERENCES `rooms` (`id_room`);
ALTER TABLE `challenges` ADD FOREIGN KEY (id_user) REFERENCES `users` (`id_user`);
ALTER TABLE `challenges` ADD FOREIGN KEY (id_room) REFERENCES `rooms` (`id_room`);
ALTER TABLE `bans` ADD FOREIGN KEY (id_user) REFERENCES `users` (`id_user`);
ALTER TABLE `warnings` ADD FOREIGN KEY (id_user) REFERENCES `users` (`id_user`);
ALTER TABLE `users_banned` ADD FOREIGN KEY (id_user) REFERENCES `users` (`id_user`);
ALTER TABLE `users_banned` ADD FOREIGN KEY (id_room) REFERENCES `rooms` (`id_room`);

-- ---
-- Table Properties
-- ---

-- ALTER TABLE `users` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `sessions` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `friendships` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `rooms` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `users_in_rooms` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `challenges` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `bans` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `warnings` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `users_banned` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ---
-- Test Data
-- ---

-- INSERT INTO `users` (`id_user`,`nick_name`,`email`,`password`,`chips`,`photo`,`badLogins`) VALUES
-- ('','','','','','','');
-- INSERT INTO `sessions` (`id_session`,`id_user`,`jwt_passphrase`,`expiration`) VALUES
-- ('','','','');
-- INSERT INTO `friendships` (`id_user_origin`,`id_user_target`,`requested`,`accepted`) VALUES
-- ('','','','');
-- INSERT INTO `rooms` (`id_room`,`name`,`accessPassword`,`securityToken`,`gproto`,`max_players`,`description`,`minCoinForAccess`,`recoveryEmail`,`badLogins`,`now_connected`,`isOfficial`) VALUES
-- ('','','','','','','','','','','','');
-- INSERT INTO `users_in_rooms` (`id_user`,`id_room`,`registered`,`position`) VALUES
-- ('','','','');
-- INSERT INTO `challenges` (`challengeID`,`id_user`,`id_room`,`challenge`) VALUES
-- ('','','','');
-- INSERT INTO `bans` (`id_user`,`reason`,`registered`,`expire`,`reporter`,`restarts`) VALUES
-- ('','','','','','');
-- INSERT INTO `warnings` (`id_user`,`message`,`registered`,`restarts`,`last_reporter`) VALUES
-- ('','','','','');
-- INSERT INTO `users_banned` (`id_user`,`id_room`,`registered`) VALUES
-- ('','','');