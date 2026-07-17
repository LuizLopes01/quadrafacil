package br.edu.ifrn.quadrafacil.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifrn.quadrafacil.model.Reserva;
import br.edu.ifrn.quadrafacil.util.Conexao;

public class ReservaDAO {

    public void inserir(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas "
                + "(data_reserva, hora_inicio, hora_fim, situacao, cpf_usuario, id_quadra, id_modalidade) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(stmt, reserva);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reserva.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<String> listarDetalhado() throws SQLException {
        List<String> reservas = new ArrayList<>();
        String sql = "SELECT r.id_reserva, r.data_reserva, r.hora_inicio, r.hora_fim, r.situacao, "
                + "u.nome AS usuario, q.nome AS quadra, m.nome AS modalidade "
                + "FROM reservas r "
                + "JOIN usuarios u ON u.cpf = r.cpf_usuario "
                + "JOIN quadras q ON q.id_quadra = r.id_quadra "
                + "JOIN modalidades m ON m.id_modalidade = r.id_modalidade "
                + "ORDER BY r.data_reserva, r.hora_inicio";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String linha = rs.getInt("id_reserva") + " | "
                        + rs.getDate("data_reserva") + " | "
                        + rs.getTime("hora_inicio") + " às " + rs.getTime("hora_fim") + " | "
                        + rs.getString("usuario") + " | "
                        + rs.getString("quadra") + " | "
                        + rs.getString("modalidade") + " | "
                        + rs.getString("situacao");
                reservas.add(linha);
            }
        }
        return reservas;
    }

    public boolean atualizar(Reserva reserva) throws SQLException {
        String sql = "UPDATE reservas SET data_reserva = ?, hora_inicio = ?, hora_fim = ?, "
                + "situacao = ?, cpf_usuario = ?, id_quadra = ?, id_modalidade = ? "
                + "WHERE id_reserva = ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            preencherParametros(stmt, reserva);
            stmt.setInt(8, reserva.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id_reserva = ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existeConflito(Reserva reserva) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservas "
                + "WHERE id_quadra = ? AND data_reserva = ? AND situacao <> 'CANCELADA' "
                + "AND hora_inicio < ? AND hora_fim > ? AND id_reserva <> ?";

        try (Connection conn = Conexao.abrir();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reserva.getIdQuadra());
            stmt.setDate(2, Date.valueOf(reserva.getData()));
            stmt.setTime(3, Time.valueOf(reserva.getHoraFim()));
            stmt.setTime(4, Time.valueOf(reserva.getHoraInicio()));
            stmt.setInt(5, reserva.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    private void preencherParametros(PreparedStatement stmt, Reserva reserva) throws SQLException {
        stmt.setDate(1, Date.valueOf(reserva.getData()));
        stmt.setTime(2, Time.valueOf(reserva.getHoraInicio()));
        stmt.setTime(3, Time.valueOf(reserva.getHoraFim()));
        stmt.setString(4, reserva.getSituacao());
        stmt.setString(5, reserva.getCpfUsuario());
        stmt.setInt(6, reserva.getIdQuadra());
        stmt.setInt(7, reserva.getIdModalidade());
    }
}
