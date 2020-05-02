CREATE TABLE class_info
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `classno`   varchar(10)  DEFAULT NULL,
    `location`  varchar(255) DEFAULT NULL,
    `begindate` datetime     DEFAULT NULL,
    `enddate`   datetime     DEFAULT NULL,
    `classtype` tinyint
);