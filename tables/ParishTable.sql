CREATE TABLE parish
(
    `id`           int NOT NULL AUTO_INCREMENT,
    `name`         varchar(255),
    `address`      varchar(255),
    `date_created` timestamp DEFAULT CURRENT_TIMESTAMP,
    `date_updated` timestamp ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);