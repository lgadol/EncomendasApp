package encomendasapp;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

public class AdicionarPedido extends JFrame {

    private JTextField nomeClienteField, tipoCarneField, tipoCorteField, precoPagoField, kgsField;
    private JComboBox<String> categoriaBox, tipoPagamentoBox;
    private JCheckBox pagoBox, pagamentoAdiantadoBox;
    private JButton confirmarButton;
    private final int admin;
    private final String nomeUsuarioLogado;
    private final AtualizarTabela pedidosWindow;

    public AdicionarPedido(final int admin, final String nomeUsuarioLogado, final AtualizarTabela pedidosWindow) {
        this.admin = admin;
        this.nomeUsuarioLogado = nomeUsuarioLogado;
        this.pedidosWindow = pedidosWindow;
        IconSetter.setIcon(this);
        
        setLayout(new GridLayout(10, 2));

        // Inicializando os campos
        nomeClienteField = new JTextField(nomeUsuarioLogado);
        categoriaBox = new JComboBox<>(new String[] { "GADO", "PORCO", "FRANGO", "OUTRO" });
        tipoCarneField = new JTextField();
        tipoCorteField = new JTextField();
        pagoBox = new JCheckBox();
        pagamentoAdiantadoBox = new JCheckBox();
        tipoPagamentoBox = new JComboBox<>(new String[] { "DINHEIRO", "CREDITO", "DEBITO", "PIX", "OUTRO" });
        precoPagoField = new JTextField();
        kgsField = new JTextField();
        
        // Adicionar bot�o confirmar
        ImageIcon confirmarIcon = new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\visto.png");
        ImageIcon confirmarRedi = new ImageIcon(confirmarIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        
        confirmarButton = new JButton("Confirmar", confirmarRedi);
        confirmarButton.setBackground(new Color(0, 204, 51));

        // Habilita ou desabilita os campos com base no valor de admin
        nomeClienteField.setEnabled(admin == 1);
        tipoCorteField.setEnabled(admin == 1);
        pagoBox.setEnabled(admin == 1);
        pagamentoAdiantadoBox.setEnabled(admin == 1);
        precoPagoField.setEnabled(admin == 1);

        // Adicionando os campos ao JFrame
        add(new JLabel("Nome do Cliente:"));
        add(nomeClienteField);
        add(new JLabel("Categoria:"));
        add(categoriaBox);
        add(new JLabel("Tipo de Carne:"));
        add(tipoCarneField);
        add(new JLabel("Tipo de Desenho:"));
        add(tipoCorteField);
        add(new JLabel("Pago:"));
        add(pagoBox);
        add(new JLabel("Pagamento Adiantado:"));
        add(pagamentoAdiantadoBox);
        add(new JLabel("Tipo de Pagamento:"));
        add(tipoPagamentoBox);
        add(new JLabel("Pre�o Pago:"));
        add(precoPagoField);
        add(new JLabel("Kgs:"));
        add(kgsField);

        // Crie um novo JPanel com BorderLayout
        JPanel buttonPanel = new JPanel(new BorderLayout());
        // Adicione o bot�o confirmarButton ao painel
        buttonPanel.add(confirmarButton, BorderLayout.CENTER);
        // Adicione o painel ao JFrame
        add(buttonPanel);

        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCliente = nomeClienteField.getText();
                String categoria = (String)categoriaBox.getSelectedItem();
                String tipoCarne = tipoCarneField.getText().toUpperCase();
                String tipoCorte = tipoCorteField.getText();
                boolean pago = pagoBox.isSelected();
                boolean pagamentoAdiantado = pagamentoAdiantadoBox.isSelected();
                String tipoPagamento = (String)tipoPagamentoBox.getSelectedItem();
                String precoPago = precoPagoField.getText();
                String kgs = kgsField.getText();

                // Valida��o dos campos obrigat�rios
                if (nomeCliente.isEmpty() || categoria.isEmpty() || tipoCarne.isEmpty() || tipoPagamento.isEmpty() ||
                    kgs.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos obrigat�rios.");
                    return;
                }

                // Validação do formato dos kgs
                if (!kgs.matches("^\\d{1,3}(\\,\\d{1,2})?$")) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira os kgs no formato 999,99.");
                    return;
                }

                // Validação do preço
                if (admin == 1) {
                    if (!precoPago.matches("^\\d{1,3}(\\,\\d{1,2})?$")) {
                        JOptionPane.showMessageDialog(null, "Por favor, insira o pre�o no formato 999,99.");
                        return;
                    }
                }

                // Conexão com o banco de dados e inserção dos dados
                try {
                    Connection con = DataBaseConnection.getConnection();
                    PreparedStatement ps =
                        con.prepareStatement("INSERT INTO pedidos (nome_cliente, categoria, tipo_carne, tipo_corte, pago, pagamento_adiantado, tipo_pagamento, preco_pago, kgs) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setString(1, nomeCliente);
                    ps.setString(2, categoria);
                    ps.setString(3, tipoCarne);
                    ps.setString(4, tipoCorte);
                    ps.setBoolean(5, pago);
                    ps.setBoolean(6, pagamentoAdiantado);
                    ps.setString(7, tipoPagamento);
                    ps.setString(8, precoPago);
                    ps.setString(9, kgs);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Pedido adicionado com sucesso!");
                    pedidosWindow.atualizarDadosTabela();
                    dispose(); // Fecha a janela
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
