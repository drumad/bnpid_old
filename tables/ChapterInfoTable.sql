CREATE TABLE chapter_info
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `name`      varchar(50)     DEFAULT NULL,
    `location`  varchar(255)    DEFAULT NULL,
    `started`   datetime,
    PRIMARY KEY (id)
);