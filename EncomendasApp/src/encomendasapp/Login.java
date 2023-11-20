package encomendasapp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Cadastro cadastro;
    private int idUsuarioLogado;

    public Login() {
        setLayout(new GridLayout(3, 2));

        emailField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 204, 51));
        loginButton.setForeground(Color.BLACK);
        loginButton.setOpaque(true);

        cadastro = new Cadastro();
        cadastro.setVisible(false);

        registerButton = new JButton("Cadastre-se");
        registerButton.setBackground(new Color(0, 153, 255));
        registerButton.setForeground(Color.BLACK);
        registerButton.setOpaque(true);

        ImageIcon icon =
            IconManager.resizeIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\cadastro.png",
                                   20, 20);
        registerButton.setIcon(icon);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cadastro.isVisible()) {
                    cadastro.setVisible(true);
                }
            }
        });

        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Senha:"));
        add(passwordField);
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
                    return;
                }

                try {
                    Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clientes WHERE email = ?");
                    stmt.setString(1, email);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String storedPassword = rs.getString("senha");
                        int ativo = rs.getInt("ativo");
                        int admin = rs.getInt("admin");
                        String nomeUsuarioLogado = rs.getString("nome");

                        if (storedPassword.equals(password) && ativo == 1) {
                            JOptionPane.showMessageDialog(null, "Bem-Vindo");
                            idUsuarioLogado = rs.getInt("id");

                            // Cria a instância de EncomendasApp aqui
                            EncomendasApp encomendasApp = new EncomendasApp(admin, nomeUsuarioLogado);
                            encomendasApp.setVisible(true);
                            dispose();
                        }

                        if (storedPassword == null) {
                            PreparedStatement updateStmt =
                                conn.prepareStatement("UPDATE clientes SET senha = ? WHERE email = ?");
                            updateStmt.setString(1, password);
                            updateStmt.setString(2, email);
                            updateStmt.executeUpdate();
                            updateStmt.close();

                            JOptionPane.showMessageDialog(null, "Senha definida com sucesso. Faça login novamente.");
                        } else if (storedPassword.equals(password) && ativo == 1) {
                            JOptionPane.showMessageDialog(null, "Bem-Vindo");
                            idUsuarioLogado = rs.getInt("id");

                            EncomendasApp encomendasApp = new EncomendasApp(admin, nomeUsuarioLogado);
                            encomendasApp.setVisible(true);
                            dispose();
                        } else if (ativo == 0) {
                            JOptionPane.showMessageDialog(null, "Usuário desativado");
                        } else {
                            JOptionPane.showMessageDialog(null, "Dados incorretos");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Dados incorretos");
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public int getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    public static void main(String[] args) {
        new Login();
    }
}
