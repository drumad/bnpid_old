CREATE TABLE chapter_info
(
    `id`           int NOT NULL AUTO_INCREMENT,
    `name`         varchar(50)     DEFAULT NULL,
    `location`     varchar(255)    DEFAULT NULL,
    `started`      datetime,
    `date_created` timestamp DEFAULT CURRENT_TIMESTAMP,
    `date_updated` timestamp ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
);