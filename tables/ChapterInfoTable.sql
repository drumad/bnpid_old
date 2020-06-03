CREATE TABLE chapter_info
(
    `chapter_id`   int NOT NULL AUTO_INCREMENT,
    `chapter_name` varchar(50)     DEFAULT NULL,
    `location`     varchar(255)    DEFAULT NULL,
    `started`      datetime,
    `date_created` timestamp DEFAULT CURRENT_TIMESTAMP,
    `date_updated` timestamp ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (chapter_id)
);