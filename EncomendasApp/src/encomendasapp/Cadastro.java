package encomendasapp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

public class Cadastro extends JFrame {
    private JTextField nomeField, telefoneField, emailField, cidadeField, enderecoField, senhaField, confirmaSenhaField;
    private JComboBox<String> estadoBox;
    private JButton cadastrarButton, cancelButton;

    public Cadastro() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(9, 2));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        // Inicializando os campos
        nomeField = new JTextField();
        telefoneField = new JTextField();
        emailField = new JTextField();
        cidadeField = new JTextField();
        estadoBox = new JComboBox<>(new String[] {
                                    "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
                                    "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
            });
        enderecoField = new JTextField();
        senhaField = new JPasswordField();
        confirmaSenhaField = new JPasswordField();
        cadastrarButton = new JButton("Cadastrar");
        cancelButton = new JButton("Cancelar");

        // Alterando a cor dos botões
        cadastrarButton.setBackground(new Color(0, 153, 255));
        cancelButton.setBackground(new Color(255, 51, 0));

        // Adicionando os campos ao painel do formulário
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Telefone:"));
        formPanel.add(telefoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Cidade:"));
        formPanel.add(cidadeField);
        formPanel.add(new JLabel("Estado:"));
        formPanel.add(estadoBox);
        formPanel.add(new JLabel("Endereço:"));
        formPanel.add(enderecoField);
        formPanel.add(new JLabel("Senha:"));
        formPanel.add(senhaField);
        formPanel.add(new JLabel("Confirmação de Senha:"));
        formPanel.add(confirmaSenhaField);

        // Adicionando os botões ao painel de botões
        buttonPanel.add(cancelButton);
        buttonPanel.add(cadastrarButton);

        // Adicionando os painéis ao JFrame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText().toUpperCase();
                String telefone = telefoneField.getText();
                String email = emailField.getText();
                String cidade = cidadeField.getText().toUpperCase();
                String estado = (String) estadoBox.getSelectedItem();
                String endereco = enderecoField.getText().toUpperCase();
                String senha = new String(((JPasswordField) senhaField).getPassword());
                String confirmaSenha = new String(((JPasswordField) confirmaSenhaField).getPassword());

                // Validação dos campos obrigatórios
                if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos obrigatórios.");
                    return;
                }

                // Validação do formato do telefone
                if (!telefone.matches("^\\d{2} \\d{8,9}$")) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira o telefone no formato DD 999999999.");
                    return;
                }

                // Validação da senha
                if (!senha.equals(confirmaSenha)) {
                    JOptionPane.showMessageDialog(null, "As senhas não coincidem.");
                    return;
                }

                // Conexão com o banco de dados e inserção dos dados
                try {
                    Connection con = DataBaseConnection.getConnection();
                    PreparedStatement ps =
                        con.prepareStatement("INSERT INTO clientes (nome, telefone, email, cidade, estado, endereco, senha, admin, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, 0, 1)");
                    ps.setString(1, nome);
                    ps.setString(2, telefone);
                    ps.setString(3, email);
                    ps.setString(4, cidade);
                    ps.setString(5, estado);
                    ps.setString(6, endereco);
                    ps.setString(7, senha);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");

                    // Redireciona para a tela de login
                    new Login().setVisible(true);
                    dispose();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redireciona para a tela de login
                new Login().setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new Cadastro().setVisible(true);
    }
}
