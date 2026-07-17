package br.edu.ifrn.quadrafacil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifrn.quadrafacil.model.Quadra;
import br.edu.ifrn.quadrafacil.util.Conexao;

public class QuadraDAO {

    public void inserir(Quadra quadra) throws SQLException {
        String sql = "INSERT INTO quadras (nome, endereco, situacao) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, quadra.getNome());
            stmt.setString(2, quadra.getEndereco());
            stmt.setString(3, quadra.getSituacao());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    quadra.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Quadra> listar() throws SQLException {
        List<Quadra> quadras = new ArrayList<>();
        String sql = "SELECT id_quadra, nome, endereco, situacao FROM quadras ORDER BY nome";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                quadras.add(new Quadra(
                        rs.getInt("id_quadra"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("situacao")));
            }
        }
        return quadras;
    }

    public boolean atualizar(Quadra quadra) throws SQLException {
        String sql = "UPDATE quadras SET nome = ?, endereco = ?, situacao = ? WHERE id_quadra = ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, quadra.getNome());
            stmt.setString(2, quadra.getEndereco());
            stmt.setString(3, quadra.getSituacao());
            stmt.setInt(4, quadra.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM quadras WHERE id_quadra = ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
