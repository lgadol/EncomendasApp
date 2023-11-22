package encomendasapp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.regex.Pattern;

public class AdicionarCliente extends JFrame {
    private JTextField nomeField, telefoneField, emailField, cidadeField, enderecoField;
    private JCheckBox adminCheck, ativoCheck;
    private JButton salvarButton;
    private AtualizarTabela atualizarTabela;
    String[] estados = {
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
        "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    };
    JComboBox estadoBox = new JComboBox(estados);

    public AdicionarCliente(AtualizarTabela atualizarTabela) {
        this.atualizarTabela = atualizarTabela;
        
        setLayout(new GridLayout(0, 2));
        adminCheck = new JCheckBox("Admin");
        ativoCheck = new JCheckBox("Ativo");
        nomeField = new JTextField();
        telefoneField = new JTextField();
        emailField = new JTextField();
        cidadeField = new JTextField();
        enderecoField = new JTextField();
        salvarButton = new JButton("Salvar");

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
                    emailField.getText().isEmpty() || cidadeField.getText().isEmpty() ||
                    enderecoField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!");
                } else if (!telefoneField.getText().matches("^\\d{2} \\d{8,9}$")) {
                    JOptionPane.showMessageDialog(null,
                                                  "Por favor, insira o telefone no formato 'DD 12345678' ou 'DD 123456789'!");
                } else if (!Pattern.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$",
                                            emailField.getText())) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um e-mail válido!");
                } else {
                    try {
                        Connection conn = DataBaseConnection.getConnection();
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
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
