package encomendasapp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.regex.Pattern;

public class AdicionarCliente extends JFrame {
    
    private JTextField nomeField, telefoneField, emailField, cidadeField, enderecoField;
    private JCheckBox adminCheck, ativoCheck;
    private JButton salvarButton;
    private AtualizarTabela atualizarTabela;
    private static boolean isOpen = false;

    String[] estados =
    { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
      "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" };
    
    JComboBox<String> estadoBox = new JComboBox<>(estados);

    public AdicionarCliente(final AtualizarTabela atualizarTabela) {
        if (isOpen) {
            return;
        }
        isOpen = true;

        this.atualizarTabela = atualizarTabela;

        setLayout(new GridLayout(0, 2));
        adminCheck = new JCheckBox("Admin");
        ativoCheck = new JCheckBox("Ativo");
        nomeField = new JTextField();
        telefoneField = new JTextField();
        emailField = new JTextField();
        cidadeField = new JTextField();
        enderecoField = new JTextField();
        salvarButton = new JButton("Confirmar");
        salvarButton.setBackground(new Color(0, 204, 51)); // Muda a cor do bot�o para azul

        add(new JLabel("Admin:"));
        add(adminCheck);
        add(new JLabel("Ativo:"));
        add(ativoCheck);
        add(new JLabel("Nome:"));
        add(nomeField);
        add(new JLabel("Telefone:"));
        add(telefoneField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Cidade:"));
        add(cidadeField);
        add(new JLabel("Estado:"));
        add(estadoBox);
        add(new JLabel("Endereço:"));
        add(enderecoField);
        add(salvarButton);

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nomeField.getText().isEmpty() || telefoneField.getText().isEmpty() ||
                    emailField.getText().isEmpty() || cidadeField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!");
                } else if (!telefoneField.getText().matches("^\\d{2} \\d{8,9}$")) {
                    JOptionPane.showMessageDialog(null,
                                                  "Por favor, insira o telefone no formato 'DD 12345678' ou 'DD 123456789'!");
                } else if (!Pattern.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$",
                                            emailField.getText())) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um e-mail v�lido!");
                } else {
                    try {
                        Connection conn = DataBaseConnection.getConnection();

                        // Verificar se o email j� existe
                        PreparedStatement checkEmailStmt =
                            conn.prepareStatement("SELECT * FROM clientes WHERE email = ?");
                        checkEmailStmt.setString(1, emailField.getText());
                        ResultSet checkEmailRs = checkEmailStmt.executeQuery();
                        if (checkEmailRs.next()) {
                            JOptionPane.showMessageDialog(null, "Este email j� est� sendo usado por outro cliente.");
                            return;
                        }
                        checkEmailRs.close();
                        checkEmailStmt.close();

                        // Verificar se o telefone j� existe
                        PreparedStatement checkTelefoneStmt =
                            conn.prepareStatement("SELECT * FROM clientes WHERE telefone = ?");
                        checkTelefoneStmt.setString(1, telefoneField.getText());
                        ResultSet checkTelefoneRs = checkTelefoneStmt.executeQuery();
                        if (checkTelefoneRs.next()) {
                            JOptionPane.showMessageDialog(null,
                                                          "Este telefone j� est� sendo usado por outro cliente.");
                            return;
                        }
                        checkTelefoneRs.close();
                        checkTelefoneStmt.close();

                        PreparedStatement stmt =
                            conn.prepareStatement("INSERT INTO clientes (admin, ativo, nome, telefone, email, cidade, estado, endereco) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

                        stmt.setInt(1, adminCheck.isSelected() ? 1 : 0);
                        stmt.setInt(2, ativoCheck.isSelected() ? 1 : 0);
                        stmt.setString(3, nomeField.getText().toUpperCase());
                        stmt.setString(4, telefoneField.getText());
                        stmt.setString(5, emailField.getText());
                        stmt.setString(6, cidadeField.getText().toUpperCase());
                        stmt.setString(7, estadoBox.getSelectedItem().toString());
                        stmt.setString(8, enderecoField.getText().toUpperCase());

                        stmt.executeUpdate();

                        stmt.close();
                        conn.close();

                        JOptionPane.showMessageDialog(null, "Cliente adicionado com sucesso!");

                        atualizarTabela.atualizarDadosTabela();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isOpen = false;
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
