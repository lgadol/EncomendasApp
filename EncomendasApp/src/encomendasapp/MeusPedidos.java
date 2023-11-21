package encomendasapp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class MeusPedidos extends JFrame implements AtualizarTabela {
    private JTable dataTable;
    private JButton addButton;
    private final int admin;
    private final String nomeUsuarioLogado;
    private Vector<String> columnNames;

    public MeusPedidos(final int admin, final String nomeUsuarioLogado) {
        this.admin = admin;
        this.nomeUsuarioLogado = nomeUsuarioLogado;
        setLayout(new BorderLayout());

        columnNames = new Vector<>();
        columnNames.add("Id");
        columnNames.add("Nome do Cliente");
        columnNames.add("Categoria");
        columnNames.add("Tipo de carne");
        columnNames.add("Tipo do Desenho");
        columnNames.add("Pago");
        columnNames.add("Pagamento Adiantado");
        columnNames.add("Tipo de Pagamento");
        columnNames.add("Preço Pago");
        columnNames.add("Quilos");
        columnNames.add("Preço por Kg");
        columnNames.add("Data da Encomenda");

        Vector<Vector<Object>> data = new Vector<>();
        try {
            Connection conn = DataBaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pedidos WHERE nome_cliente = ?");
            stmt.setString(1, nomeUsuarioLogado);
            ResultSet rs = stmt.executeQuery();
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

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdicionarPedido(admin, nomeUsuarioLogado, MeusPedidos.this).setVisible(true);
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);

        // Chame o método para preencher a tabela quando a janela for aberta
        atualizarDadosTabela();
    }

    // Método para atualizar os dados da tabela
    @Override
    public void atualizarDadosTabela() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            Connection conn = DataBaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pedidos WHERE nome_cliente = ?");
            stmt.setString(1, nomeUsuarioLogado);
            ResultSet rs = stmt.executeQuery();
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

        // Atualiza os dados da tabela
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setDataVector(data, columnNames);
    }
}
