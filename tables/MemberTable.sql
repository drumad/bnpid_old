CREATE TABLE members
(
    `id`                      int(11) NOT NULL AUTO_INCREMENT,
    `barcodeid`               varchar(12)  DEFAULT NULL,
    `last_name`               varchar(50)  DEFAULT NULL,
    `first_name`              varchar(50)  DEFAULT NULL,
    `address`                 varchar(150) DEFAULT NULL,
    `city`                    varchar(50)  DEFAULT NULL,
    `state`                   varchar(50)  DEFAULT NULL,
    `zipcode`                 int(11)      DEFAULT '0',
    `homephone`               varchar(50)  DEFAULT NULL,
    `workphone`               varchar(50)  DEFAULT NULL,
    `cellular`                varchar(50)  DEFAULT NULL,
    `email`                   varchar(50)  DEFAULT NULL,
    `dateofbirth`             datetime     DEFAULT NULL,
    `age`                     int(11)      DEFAULT '0',
    `maritalstatus`           varchar(50)  DEFAULT NULL,
    `sex`                     varchar(50)  DEFAULT NULL,
    `religion`                varchar(50)  DEFAULT NULL,
    `degree`                  varchar(50)  DEFAULT NULL,
    `illness`                 varchar(50)  DEFAULT NULL,
    `physicallimitations`     varchar(150) DEFAULT NULL,
    `dietaryrequirements`     varchar(150) DEFAULT NULL,
    `dietaryrequirementsdesc` varchar(200) DEFAULT NULL,
    `contactid`               int(11)      DEFAULT NULL,
    `sponsorid`               int(11)      DEFAULT NULL,
    `chapterid`               int(11)      DEFAULT NULL,
    `dateAdorer`              datetime     DEFAULT NULL,
    `dateHirang`              datetime     DEFAULT NULL,
    `dateCouncil`             datetime     DEFAULT NULL,
    `isactive`                boolean      DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (contactid) REFERENCES members (id),
    FOREIGN KEY (sponsorid) REFERENCES members (id),
    FOREIGN KEY (chapterid) REFERENCES chapter_info (id)
);