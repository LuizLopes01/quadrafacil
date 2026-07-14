CREATE DATABASE IF NOT EXISTS quadrafacil
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE quadrafacil;

-- Apaga as tabelas antigas na ordem correta, caso já existam
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS quadra_modalidade;
DROP TABLE IF EXISTS modalidades;
DROP TABLE IF EXISTS quadras;
DROP TABLE IF EXISTS usuarios;

-- Entidade 1: usuários
CREATE TABLE usuarios (
    cpf VARCHAR(14) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    telefone VARCHAR(20) NOT NULL,

    PRIMARY KEY (cpf)
);

-- Entidade 2: quadras
CREATE TABLE quadras (
    id_quadra INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(150) NOT NULL,
    situacao ENUM('DISPONIVEL', 'MANUTENCAO', 'INATIVA')
        NOT NULL DEFAULT 'DISPONIVEL',

    PRIMARY KEY (id_quadra)
);

-- Entidade 3: modalidades
CREATE TABLE modalidades (
    id_modalidade INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),

    PRIMARY KEY (id_modalidade),
    UNIQUE (nome)
);

-- Relacionamento entre quadras e modalidades
CREATE TABLE quadra_modalidade (
    id_quadra INT NOT NULL,
    id_modalidade INT NOT NULL,

    PRIMARY KEY (id_quadra, id_modalidade),

    CONSTRAINT fk_quadra_modalidade_quadra
        FOREIGN KEY (id_quadra)
        REFERENCES quadras(id_quadra)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_quadra_modalidade_modalidade
        FOREIGN KEY (id_modalidade)
        REFERENCES modalidades(id_modalidade)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- Entidade 4: reservas
CREATE TABLE reservas (
    id_reserva INT NOT NULL AUTO_INCREMENT,
    data_reserva DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    situacao ENUM('ATIVA', 'CANCELADA', 'CONCLUIDA')
        NOT NULL DEFAULT 'ATIVA',

    cpf_usuario VARCHAR(14) NOT NULL,
    id_quadra INT NOT NULL,
    id_modalidade INT NOT NULL,

    PRIMARY KEY (id_reserva),

    CONSTRAINT fk_reserva_usuario
        FOREIGN KEY (cpf_usuario)
        REFERENCES usuarios(cpf)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_reserva_quadra
        FOREIGN KEY (id_quadra)
        REFERENCES quadras(id_quadra)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_reserva_modalidade
        FOREIGN KEY (id_modalidade)
        REFERENCES modalidades(id_modalidade)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT horario_reserva_valido
        CHECK (hora_fim > hora_inicio),

    UNIQUE (id_quadra, data_reserva, hora_inicio)
);

-- Usuários para teste
INSERT INTO usuarios (cpf, nome, telefone) VALUES
('000.000.000-00', 'João Silva', '(84) 99999-0001'),
('111.111.111-11', 'Maria Santos', '(84) 99999-0002'),
('222.222.222-22', 'José Oliveira', '(84) 99999-0003');

-- Quadras para teste
INSERT INTO quadras (nome, endereco, situacao) VALUES
('Ginásio Poliesportivo', 'Centro, Lajes-RN', 'DISPONIVEL'),
('Quadra da Cohab', 'Bairro Cohab, Lajes-RN', 'DISPONIVEL'),
('Quadra do Alto da Maternidade', 'Alto da Maternidade, Lajes-RN', 'MANUTENCAO');

-- Modalidades para teste
INSERT INTO modalidades (nome, descricao) VALUES
('Futsal', 'Modalidade de futebol praticada em quadra'),
('Vôlei', 'Modalidade disputada com rede'),
('Basquete', 'Modalidade praticada com cesta'),
('Handebol', 'Modalidade coletiva praticada com as mãos');

-- Modalidades permitidas em cada quadra
INSERT INTO quadra_modalidade (id_quadra, id_modalidade) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 1),
(2, 2);

-- Reservas para teste
INSERT INTO reservas (
    data_reserva,
    hora_inicio,
    hora_fim,
    cpf_usuario,
    id_quadra,
    id_modalidade
) VALUES
('2026-07-15', '18:00:00', '19:00:00', '000.000.000-00', 1, 1),
('2026-07-16', '19:00:00', '20:00:00', '111.111.111-11', 1, 2);
