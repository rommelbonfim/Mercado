CREATE TABLE estoque (
                            id bigint(30) NOT NULL AUTO_INCREMENT,
                            nome varchar(30) DEFAULT NULL,
                            quantidade int (30) NOT NULL,
                            vendidos int (30) NOT NULL,
                            valor decimal(19,2) NOT NULL,
                            descricao varchar(100) DEFAULT NULL,
                            fornecedor varchar(30) DEFAULT NULL,
                            PRIMARY KEY (id)
);