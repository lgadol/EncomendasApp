package encomendasapp;

import javax.swing.*;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

import java.util.Vector;

public class MeusPedidos extends JFrame {
    private JTable dataTable;
    private JButton addButton;

    public MeusPedidos() {
        setLayout(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("id");
        columnNames.add("nome_cliente");
        columnNames.add("categoria");
        columnNames.add("tipo_carne");
        columnNames.add("tipo_corte");
        columnNames.add("pago");
        columnNames.add("pagamento_adiantado");
        columnNames.add("tipo_pagamento");
        columnNames.add("preco_pag");
        columnNames.add("kgs");
        columnNames.add("preco_kg");
        columnNames.add("hora_encomenda");

        Vector<Vector<Object>> data = new Vector<>();
        try {
            Connection conn = DataBaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pedidos WHERE id = nome_cliente");

            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= rs.getMetaData().getColumnCount(); columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        dataTable = new JTable(data, columnNames);
        addButton = new JButton("Adicionar pedido");

        add(new JScrollPane(dataTable), BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        /* addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdicionarPedido adicionarPedido = new AdicionarPedido();
                adicionarPedido.setVisible(true);
            }
        }); */


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
