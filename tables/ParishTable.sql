CREATE TABLE parish
(
    `parish_id`      int NOT NULL AUTO_INCREMENT,
    `parish_name`    varchar(255),
    `parish_address` varchar(255),
    `date_created`   timestamp DEFAULT CURRENT_TIMESTAMP,
    `date_updated`   timestamp ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (parish_id)
);