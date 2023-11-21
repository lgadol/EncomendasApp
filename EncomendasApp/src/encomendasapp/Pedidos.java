package encomendasapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class Pedidos extends JFrame implements AtualizarTabela {
    private JTable dataTable;
    private JButton addButton;
    private final int admin;
    private final String nomeUsuarioLogado;
    private Vector<String> columnNames;

    public Pedidos(final int admin, final String nomeUsuarioLogado) {
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

        dataTable = new JTable();
        addButton = new JButton("Adicionar pedido");

        add(new JScrollPane(dataTable), BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdicionarPedido(admin, nomeUsuarioLogado, Pedidos.this).setVisible(true);
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
    public void atualizarDadosTabela() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            Connection conn = DataBaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pedidos");

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
