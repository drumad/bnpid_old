DROP TABLE IF EXISTS `class_type`;

CREATE TABLE `class_type`
(
    `id`          tinyint NOT NULL AUTO_INCREMENT,
    `description` varchar(50) DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO `class_type` (`description`) VALUES ('Panawagan');
INSERT INTO `class_type` (`description`) VALUES ('Marian');
INSERT INTO `class_type` (`description`) VALUES ('Special');
INSERT INTO `class_type` (`description`) VALUES ('Little Lambs');
INSERT INTO `class_type` (`description`) VALUES ('Mission');
INSERT INTO `class_type` (`description`) VALUES ('Apostolic');
INSERT INTO `class_type` (`description`) VALUES ('Regular');
INSERT INTO `class_type` (`description`) VALUES ('Youth Ministry');
INSERT INTO `class_type` (`description`) VALUES ('Spiritual Workshop');
INSERT INTO `class_type` (`description`) VALUES ('Munting Pagsasanay');