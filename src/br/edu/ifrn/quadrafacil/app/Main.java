package br.edu.ifrn.quadrafacil.app;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import br.edu.ifrn.quadrafacil.dao.ModalidadeDAO;
import br.edu.ifrn.quadrafacil.dao.QuadraDAO;
import br.edu.ifrn.quadrafacil.dao.ReservaDAO;
import br.edu.ifrn.quadrafacil.dao.UsuarioDAO;
import br.edu.ifrn.quadrafacil.model.Modalidade;
import br.edu.ifrn.quadrafacil.model.Quadra;
import br.edu.ifrn.quadrafacil.model.Reserva;
import br.edu.ifrn.quadrafacil.model.Usuario;

public class Main {
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final UsuarioDAO USUARIO_DAO = new UsuarioDAO();
    private static final QuadraDAO QUADRA_DAO = new QuadraDAO();
    private static final ModalidadeDAO MODALIDADE_DAO = new ModalidadeDAO();
    private static final ReservaDAO RESERVA_DAO = new ReservaDAO();

    public static void main(String[] args) {
        int opcao;

        do {
            System.out.println("\n================ QUADRAFÁCIL ================");
            System.out.println("1 - Gerenciar usuários");
            System.out.println("2 - Gerenciar quadras");
            System.out.println("3 - Gerenciar modalidades");
            System.out.println("4 - Gerenciar reservas");
            System.out.println("0 - Sair");
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> menuUsuarios();
                case 2 -> menuQuadras();
                case 3 -> menuModalidades();
                case 4 -> menuReservas();
                case 0 -> System.out.println("Sistema encerrado.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void menuUsuarios() {
        int opcao;
        do {
            System.out.println("\n--- USUÁRIOS ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarUsuario();
                    case 2 -> listarUsuarios();
                    case 3 -> atualizarUsuario();
                    case 4 -> excluirUsuario();
                    case 0 -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                mostrarErroBanco(e);
            }
        } while (opcao != 0);
    }

    private static void cadastrarUsuario() throws SQLException {
        String cpf = lerTexto("CPF: ");
        String nome = lerTexto("Nome: ");
        String telefone = lerTexto("Telefone: ");

        USUARIO_DAO.inserir(new Usuario(cpf, nome, telefone));
        System.out.println("Usuário cadastrado.");
    }

    private static void listarUsuarios() throws SQLException {
        List<Usuario> usuarios = USUARIO_DAO.listar();
        System.out.println("\nCPF | NOME | TELEFONE");
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        usuarios.forEach(System.out::println);
    }

    private static void atualizarUsuario() throws SQLException {
        String cpf = lerTexto("CPF do usuário: ");
        String nome = lerTexto("Novo nome: ");
        String telefone = lerTexto("Novo telefone: ");

        boolean alterou = USUARIO_DAO.atualizar(new Usuario(cpf, nome, telefone));
        System.out.println(alterou ? "Usuário atualizado." : "CPF não encontrado.");
    }

    private static void excluirUsuario() throws SQLException {
        String cpf = lerTexto("CPF do usuário: ");
        boolean excluiu = USUARIO_DAO.excluir(cpf);
        System.out.println(excluiu ? "Usuário excluído." : "CPF não encontrado.");
    }

    private static void menuQuadras() {
        int opcao;
        do {
            System.out.println("\n--- QUADRAS ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarQuadra();
                    case 2 -> listarQuadras();
                    case 3 -> atualizarQuadra();
                    case 4 -> excluirQuadra();
                    case 0 -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                mostrarErroBanco(e);
            }
        } while (opcao != 0);
    }

    private static void cadastrarQuadra() throws SQLException {
        String nome = lerTexto("Nome da quadra: ");
        String endereco = lerTexto("Endereço: ");
        String situacao = lerTexto("Situação (DISPONIVEL, MANUTENCAO ou INATIVA): ").toUpperCase();

        Quadra quadra = new Quadra(0, nome, endereco, situacao);
        QUADRA_DAO.inserir(quadra);
        System.out.println("Quadra cadastrada com o código " + quadra.getId() + ".");
    }

    private static void listarQuadras() throws SQLException {
        List<Quadra> quadras = QUADRA_DAO.listar();
        System.out.println("\nID | NOME | ENDEREÇO | SITUAÇÃO");
        if (quadras.isEmpty()) {
            System.out.println("Nenhuma quadra cadastrada.");
            return;
        }
        quadras.forEach(System.out::println);
    }

    private static void atualizarQuadra() throws SQLException {
        int id = lerInteiro("Código da quadra: ");
        String nome = lerTexto("Novo nome: ");
        String endereco = lerTexto("Novo endereço: ");
        String situacao = lerTexto("Nova situação: ").toUpperCase();

        boolean alterou = QUADRA_DAO.atualizar(new Quadra(id, nome, endereco, situacao));
        System.out.println(alterou ? "Quadra atualizada." : "Quadra não encontrada.");
    }

    private static void excluirQuadra() throws SQLException {
        int id = lerInteiro("Código da quadra: ");
        boolean excluiu = QUADRA_DAO.excluir(id);
        System.out.println(excluiu ? "Quadra excluída." : "Quadra não encontrada.");
    }

    private static void menuModalidades() {
        int opcao;
        do {
            System.out.println("\n--- MODALIDADES ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarModalidade();
                    case 2 -> listarModalidades();
                    case 3 -> atualizarModalidade();
                    case 4 -> excluirModalidade();
                    case 0 -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                mostrarErroBanco(e);
            }
        } while (opcao != 0);
    }

    private static void cadastrarModalidade() throws SQLException {
        String nome = lerTexto("Nome da modalidade: ");
        String descricao = lerTexto("Descrição: ");

        Modalidade modalidade = new Modalidade(0, nome, descricao);
        MODALIDADE_DAO.inserir(modalidade);
        System.out.println("Modalidade cadastrada com o código " + modalidade.getId() + ".");
    }

    private static void listarModalidades() throws SQLException {
        List<Modalidade> modalidades = MODALIDADE_DAO.listar();
        System.out.println("\nID | NOME | DESCRIÇÃO");
        if (modalidades.isEmpty()) {
            System.out.println("Nenhuma modalidade cadastrada.");
            return;
        }
        modalidades.forEach(System.out::println);
    }

    private static void atualizarModalidade() throws SQLException {
        int id = lerInteiro("Código da modalidade: ");
        String nome = lerTexto("Novo nome: ");
        String descricao = lerTexto("Nova descrição: ");

        boolean alterou = MODALIDADE_DAO.atualizar(new Modalidade(id, nome, descricao));
        System.out.println(alterou ? "Modalidade atualizada." : "Modalidade não encontrada.");
    }

    private static void excluirModalidade() throws SQLException {
        int id = lerInteiro("Código da modalidade: ");
        boolean excluiu = MODALIDADE_DAO.excluir(id);
        System.out.println(excluiu ? "Modalidade excluída." : "Modalidade não encontrada.");
    }

    private static void menuReservas() {
        int opcao;
        do {
            System.out.println("\n--- RESERVAS ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarReserva();
                    case 2 -> listarReservas();
                    case 3 -> atualizarReserva();
                    case 4 -> excluirReserva();
                    case 0 -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                mostrarErroBanco(e);
            } catch (DateTimeParseException e) {
                System.out.println("Data ou horário em formato inválido.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (opcao != 0);
    }

    private static void cadastrarReserva() throws SQLException {
        Reserva reserva = lerDadosReserva(0);
        if (RESERVA_DAO.existeConflito(reserva)) {
            System.out.println("Já existe uma reserva nesse horário para a quadra escolhida.");
            return;
        }

        RESERVA_DAO.inserir(reserva);
        System.out.println("Reserva cadastrada com o código " + reserva.getId() + ".");
    }

    private static void listarReservas() throws SQLException {
        List<String> reservas = RESERVA_DAO.listarDetalhado();
        System.out.println("\nID | DATA | HORÁRIO | USUÁRIO | QUADRA | MODALIDADE | SITUAÇÃO");
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva cadastrada.");
            return;
        }
        reservas.forEach(System.out::println);
    }

    private static void atualizarReserva() throws SQLException {
        int id = lerInteiro("Código da reserva: ");
        Reserva reserva = lerDadosReserva(id);

        if (RESERVA_DAO.existeConflito(reserva)) {
            System.out.println("O novo horário entra em conflito com outra reserva.");
            return;
        }

        boolean alterou = RESERVA_DAO.atualizar(reserva);
        System.out.println(alterou ? "Reserva atualizada." : "Reserva não encontrada.");
    }

    private static void excluirReserva() throws SQLException {
        int id = lerInteiro("Código da reserva: ");
        boolean excluiu = RESERVA_DAO.excluir(id);
        System.out.println(excluiu ? "Reserva excluída." : "Reserva não encontrada.");
    }

    private static Reserva lerDadosReserva(int id) {
        System.out.println("Use data no formato AAAA-MM-DD e horário no formato HH:MM.");
        LocalDate data = LocalDate.parse(lerTexto("Data: "));
        LocalTime inicio = LocalTime.parse(lerTexto("Hora inicial: "));
        LocalTime fim = LocalTime.parse(lerTexto("Hora final: "));
        String situacao = lerTexto("Situação (ATIVA, CANCELADA ou CONCLUIDA): ").toUpperCase();
        String cpf = lerTexto("CPF do usuário: ");
        int idQuadra = lerInteiro("Código da quadra: ");
        int idModalidade = lerInteiro("Código da modalidade: ");

        if (!fim.isAfter(inicio)) {
            throw new IllegalArgumentException("A hora final deve ser maior que a hora inicial.");
        }

        return new Reserva(id, data, inicio, fim, situacao, cpf, idQuadra, idModalidade);
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = ENTRADA.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = ENTRADA.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("Esse campo não pode ficar vazio.");
        }
    }

    private static void mostrarErroBanco(SQLException e) {
        System.out.println("Não foi possível concluir a operação no banco.");
        System.out.println("Detalhe: " + e.getMessage());
    }
}
