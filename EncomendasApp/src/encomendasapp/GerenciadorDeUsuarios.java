package encomendasapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeUsuarios {

    public Map<String, String> obterUsuarioDoBancoDeDados(int idUsuario) {
        Map<String, String> usuario = new HashMap<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DataBaseConnection.getConnection();
            stmt = conn.createStatement();
            String sql =
                "SELECT id, admin, nome, telefone, email, cidade, estado, endereco, senha FROM clientes WHERE id = " +
                idUsuario;

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                usuario.put("id", rs.getString("id"));
                usuario.put("admin", rs.getString("admin"));
                usuario.put("nome", rs.getString("nome"));
                usuario.put("telefone", rs.getString("telefone"));
                usuario.put("email", rs.getString("email"));
                usuario.put("cidade", rs.getString("cidade"));
                usuario.put("estado", rs.getString("estado"));
                usuario.put("endereco", rs.getString("endereco"));
                // Supondo que a senha esteja armazenada na coluna "senha" do banco de dados
                usuario.put("senha", rs.getString("senha"));
                System.out.println("Senha obtida do banco de dados: " +
                                   rs.getString("senha")); // Imprime a senha obtida do banco de dados
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Fecha o ResultSet e o Statement
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    /* ignorado */
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    /* ignorado */
                }
            }
        }

        return usuario;
    }

    public void atualizarUsuarioNoBancoDeDados(Map<String, String> usuario) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DataBaseConnection.getConnection();
            String sql =
                "UPDATE clientes SET nome = ?, telefone = ?, email = ?, cidade = ?, estado = ?, endereco = ? WHERE id = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, usuario.get("nome"));
            pstmt.setString(2, usuario.get("telefone"));
            pstmt.setString(3, usuario.get("email"));
            pstmt.setString(4, usuario.get("cidade"));
            pstmt.setString(5, usuario.get("estado"));
            pstmt.setString(6, usuario.get("endereco"));
            pstmt.setString(7, usuario.get("id"));

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Fecha o PreparedStatement
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    /* ignorado */
                }
            }
        }
    }
}
