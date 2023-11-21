package encomendasapp;

import javax.swing.*;

import java.awt.*;

import java.sql.*;

import java.util.Vector;

public class Clientes extends JFrame {
    private JTable dataTable;
    private JButton addButton;

    public Clientes() {
        setLayout(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Id");
        columnNames.add("Admin");
        columnNames.add("Ativo");
        columnNames.add("Nome");
        columnNames.add("Telefone");
        columnNames.add("Email");
        columnNames.add("Cidade");
        columnNames.add("Estado");
        columnNames.add("Endereco");

        Vector<Vector<Object>> data = new Vector<>();
        try {
            Connection conn = DataBaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clientes");

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
        addButton = new JButton("Adicionar cliente");

        add(new JScrollPane(dataTable), BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        /* addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abra a janela de adicionar cliente aqui
            }
        }); */

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
