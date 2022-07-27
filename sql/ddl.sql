DROP TABLE IF EXISTS board CASCADE;
CREATE TABLE board
(
    id   bigint(5) NOT NULL AUTO_INCREMENT,
    title varchar(100) NOT NULL,
    author varchar(50) NOT NULL,
    content varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

SELECT * FROM board;