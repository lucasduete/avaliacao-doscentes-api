package io.github.avaliacaodocentes.dao;

import io.github.avaliacaodocentes.factory.Conexao;
import io.github.avaliacaodocentes.model.Avaliacao;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AvaliacaoDao {

    private final Connection conn;

    public AvaliacaoDao() throws SQLException, ClassNotFoundException {
        conn = Conexao.getConnection();
    }

    public boolean cadastrar(Avaliacao avaliacao) {

        String sql = "INSERT INTO Avaliacao(Data, Comentario) VALUES (?,?);";
        int idAvaliacao = -1;

        PreparedStatement stmt;
        try {

            //Salva Avaliaçao
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setDate(1, Date.valueOf(avaliacao.getData()));
            stmt.setString(2, avaliacao.getComentario());

            stmt.executeUpdate();

            //Recupera o Id da Avalicao para Proximas Querys
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idAvaliacao = rs.getInt(1);
            }

            //Salva Avaliacao_Aluno_Professor
            sql = "INSERT INTO Avaliacao_Aluno_Professor(MatAluno, MatProfessor, CodAvaliacao) VALUES (?,?,?)";

            stmt = conn.prepareStatement(sql);

            stmt.setString(1, avaliacao.getMatAluno());
            stmt.setString(2, avaliacao.getMatProfessor());
            stmt.setInt(3, idAvaliacao);

            stmt.executeUpdate();

            //Para Cada Obj Pontuacao dentro de getPontuacoes em Avaliacao
            //salva ele na tabela correta
            sql = String.format("INSERT INTO CRITERIO_AVALIACAO (CodAvaliacao, CodCriterio, Pontuacao) "
                    + "VALUES (%d,?,?)", idAvaliacao);
            final PreparedStatement internalStmt = conn.prepareStatement(sql);
            final String aux = "";

            avaliacao.getPontuacoes().forEach(pontuacao -> {
                try {
                    internalStmt.setInt(1, pontuacao.getCodCriterio());
                    internalStmt.setDouble(2, pontuacao.getPontuacao());
                    internalStmt.execute();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    aux.concat(pontuacao + ";");
                }
            });

            if (!aux.isEmpty()) {
                System.out.printf("\n\nLista de Pontuaçoes com Erros: \n" + aux);
                return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deletar(int codigo) {

        String sql_1 = "DELETE FROM CRITERIO_AVALIACAO WHERE CodAvaliacao = ?";
        String sql_2 = "DELETE FROM AVALIACAO_ALUNO_PROFESSOR WHERE CodAvaliacao = ?";
        try {

            PreparedStatement stmt = conn.prepareStatement(sql_1);
            stmt.setInt(1, codigo);
            stmt.execute();

            PreparedStatement stmt2 = conn.prepareStatement(sql_2);
            stmt.setInt(1, codigo);
            stmt.execute();

        } catch (SQLException ex) {
            Logger.getLogger(AvaliacaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
