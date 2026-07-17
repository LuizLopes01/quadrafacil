package br.edu.ifrn.quadrafacil.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/quadrafacil"
            + "?useSSL=false&serverTimezone=America/Fortaleza"
            + "&allowPublicKeyRetrieval=true";

    // Os dados de acesso são lidos do computador do usuário.
    private static final String USUARIO =
            obterVariavel("QUADRAFACIL_DB_USER", "root");

    private static final String SENHA =
            obterVariavel("QUADRAFACIL_DB_PASSWORD", "");

    private Conexao() {
    }

    private static String obterVariavel(String nome, String valorPadrao) {
        String valor = System.getenv(nome);

        if (valor == null || valor.isBlank()) {
            return valorPadrao;
        }

        return valor;
    }

    public static Connection abrir() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
