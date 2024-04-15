CREATE TABLE vendas (
                            id bigint(30) NOT NULL AUTO_INCREMENT,
                            iditem bigint(30) NOT NULL ,
                            quantidade int (30) NOT NULL,
                            valorvenda decimal(19,2) DEFAULT NULL ,
                            PRIMARY KEY (id)
);