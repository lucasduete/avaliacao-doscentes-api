package io.github.avaliacaodocentes.dao.postgres;

import io.github.avaliacaodocentes.dao.interfaces.CursoDaoInterface;
import io.github.avaliacaodocentes.factory.Conexao;
import io.github.avaliacaodocentes.model.Curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CursoDaoPostgres implements CursoDaoInterface {

    private Connection conn;

    public CursoDaoPostgres() throws SQLException, ClassNotFoundException{
        conn = Conexao.getConnection();
    }

    public boolean cadastrar(Curso curso) {

        String sql = "INSERT INTO Curso(Nome, Codigo, EmailAdministrador) VALUES (?,?,?);";

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getCodigo());
            stmt.setString(3, curso.getEmailAdministrador());

            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (SQLException ex) {

            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean editar(Curso curso, int codigo) {

        String sql = "UPDATE Curso SET Nome = ?, Codigo = ?, EmailAdministrador = ? WHERE Codigo = ?;";

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getCodigo());
            stmt.setString(3, curso.getEmailAdministrador());
            stmt.setInt(4, codigo);

            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (SQLException ex) {

            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean remover(int codigo) {

        String sql = "UPDATE Aluno SET CodCurso = NULL WHERE CodCurso = ?;" +
                "DELETE FROM Professor_Curso WHERE CodCurso = ?;" +
                "DELETE FROM Curso WHERE Codigo = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, codigo);
            stmt.setInt(2, codigo);
            stmt.setInt(3, codigo);
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }
}
