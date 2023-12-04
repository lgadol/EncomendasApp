package encomendasapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeUsuarios {
    public Map<String, String> obterUsuarioDoBancoDeDados(String nomeUsuarioLogado) {
        Map<String, String> usuario = new HashMap<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DataBaseConnection.getConnection();
            stmt = conn.createStatement();
            
            String sql = "SELECT admin, nome, telefone, email, cidade, estado, endereco FROM clientes WHERE nome = '" + nomeUsuarioLogado + "'";

            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                usuario.put("admin", rs.getString("admin"));
                usuario.put("nome", rs.getString("nome"));
                usuario.put("telefone", rs.getString("telefone"));
                usuario.put("email", rs.getString("email"));
                usuario.put("cidade", rs.getString("cidade"));
                usuario.put("estado", rs.getString("estado"));
                usuario.put("endereco", rs.getString("endereco"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Fecha o ResultSet e o Statement
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignorado */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignorado */}
            }
        }

        return usuario;
    }
}
