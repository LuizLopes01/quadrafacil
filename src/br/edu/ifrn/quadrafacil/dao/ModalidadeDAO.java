package br.edu.ifrn.quadrafacil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifrn.quadrafacil.model.Modalidade;
import br.edu.ifrn.quadrafacil.util.Conexao;

public class ModalidadeDAO {

    public void inserir(Modalidade modalidade) throws SQLException {
        String sql = "INSERT INTO modalidades (nome, descricao) VALUES (?, ?)";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, modalidade.getNome());
            stmt.setString(2, modalidade.getDescricao());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    modalidade.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Modalidade> listar() throws SQLException {
        List<Modalidade> modalidades = new ArrayList<>();
        String sql = "SELECT id_modalidade, nome, descricao FROM modalidades ORDER BY nome";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modalidades.add(new Modalidade(
                        rs.getInt("id_modalidade"),
                        rs.getString("nome"),
                        rs.getString("descricao")));
            }
        }
        return modalidades;
    }

    public boolean atualizar(Modalidade modalidade) throws SQLException {
        String sql = "UPDATE modalidades SET nome = ?, descricao = ? WHERE id_modalidade = ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, modalidade.getNome());
            stmt.setString(2, modalidade.getDescricao());
            stmt.setInt(3, modalidade.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM modalidades WHERE id_modalidade = ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
