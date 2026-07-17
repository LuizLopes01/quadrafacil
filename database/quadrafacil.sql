CREATE DATABASE IF NOT EXISTS quadrafacil
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE quadrafacil;

DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS quadra_modalidade;
DROP TABLE IF EXISTS modalidades;
DROP TABLE IF EXISTS quadras;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    cpf VARCHAR(14) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    PRIMARY KEY (cpf)
);

CREATE TABLE quadras (
    id_quadra INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(150) NOT NULL,
    situacao VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL',
    PRIMARY KEY (id_quadra)
);

CREATE TABLE modalidades (
    id_modalidade INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    PRIMARY KEY (id_modalidade),
    UNIQUE KEY uk_modalidade_nome (nome)
);

-- Uma quadra pode receber várias modalidades e uma modalidade pode existir em várias quadras.
CREATE TABLE quadra_modalidade (
    id_quadra INT NOT NULL,
    id_modalidade INT NOT NULL,
    PRIMARY KEY (id_quadra, id_modalidade),
    CONSTRAINT fk_qm_quadra FOREIGN KEY (id_quadra)
        REFERENCES quadras(id_quadra) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_qm_modalidade FOREIGN KEY (id_modalidade)
        REFERENCES modalidades(id_modalidade) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE reservas (
    id_reserva INT NOT NULL AUTO_INCREMENT,
    data_reserva DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    situacao VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    cpf_usuario VARCHAR(14) NOT NULL,
    id_quadra INT NOT NULL,
    id_modalidade INT NOT NULL,
    PRIMARY KEY (id_reserva),
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (cpf_usuario)
        REFERENCES usuarios(cpf) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_reserva_quadra FOREIGN KEY (id_quadra)
        REFERENCES quadras(id_quadra) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_reserva_modalidade FOREIGN KEY (id_modalidade)
        REFERENCES modalidades(id_modalidade) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT ck_horario_reserva CHECK (hora_fim > hora_inicio)
);

INSERT INTO usuarios (cpf, nome, telefone) VALUES
('000.000.000-00', 'João Silva', '(84) 99999-0001'),
('111.111.111-11', 'Maria Santos', '(84) 99999-0002');

INSERT INTO quadras (nome, endereco, situacao) VALUES
('Ginásio Poliesportivo', 'Centro, Lajes-RN', 'DISPONIVEL'),
('Quadra da Cohab', 'Bairro Cohab, Lajes-RN', 'DISPONIVEL');

INSERT INTO modalidades (nome, descricao) VALUES
('Futsal', 'Futebol praticado em quadra'),
('Vôlei', 'Modalidade disputada com rede'),
('Basquete', 'Modalidade praticada com cesta');

INSERT INTO quadra_modalidade (id_quadra, id_modalidade) VALUES
(1, 1), (1, 2), (1, 3), (2, 1), (2, 2);

INSERT INTO reservas
(data_reserva, hora_inicio, hora_fim, situacao, cpf_usuario, id_quadra, id_modalidade)
VALUES
('2026-07-17', '18:00:00', '19:00:00', 'ATIVA', '000.000.000-00', 1, 1);
