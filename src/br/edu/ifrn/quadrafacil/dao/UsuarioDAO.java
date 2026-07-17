package br.edu.ifrn.quadrafacil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifrn.quadrafacil.model.Usuario;
import br.edu.ifrn.quadrafacil.util.Conexao;

public class UsuarioDAO {

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (cpf, nome, telefone) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getTelefone());
            stmt.executeUpdate();
        }
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT cpf, nome, telefone FROM usuarios ORDER BY nome";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("telefone")));
            }
        }
        return usuarios;
    }

    public boolean atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, telefone = ? WHERE cpf = ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getTelefone());
            stmt.setString(3, usuario.getCpf());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(String cpf) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE cpf = ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            return stmt.executeUpdate() > 0;
        }
    }
}
