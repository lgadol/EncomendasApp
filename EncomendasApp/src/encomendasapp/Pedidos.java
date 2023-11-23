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
    private JButton clearButton;
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

        // Botão Adicionar Pedidos
        addButton = new JButton("Adicionar Pedido");
        ImageIcon addIcon =
            new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\adicionar-ao-carrinho.png");
        Image addImage = addIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        addButton.setBackground(new Color(0, 204, 51));
        addIcon = new ImageIcon(addImage);

        // Botão Limpar Registros
        clearButton = new JButton("Limpar Registros");
        ImageIcon clearIcon =
            new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\deletar-lixeira.png");
        Image clearImage = clearIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        clearButton.setBackground(new Color(255, 51, 0));
        clearIcon = new ImageIcon(clearImage);

        // Adicionando os botões
        addButton.setIcon(addIcon);
        clearButton.setIcon(clearIcon);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdicionarPedido(admin, nomeUsuarioLogado, Pedidos.this).setVisible(true);
                // Atualiza a tabela após adicionar o pedido
                atualizarDadosTabela();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Conecta ao banco de dados e verifica se existem registros
                    Connection conn = DataBaseConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM pedidos");
                    rs.next();
                    int count = rs.getInt(1);
                    rs.close();
                    stmt.close();

                    if (count == 0) {
                        // Se não houver registros, mostra uma mensagem
                        JOptionPane.showMessageDialog(null, "Não há nenhum pedido para excluir");
                    } else {
                        // Se houver registros, mostra um diálogo de confirmação
                        int confirm =
                            JOptionPane.showConfirmDialog(null, "Tem certeza de que deseja excluir todos os pedidos?",
                                                          "Confirmação", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Exclui todos os registros
                            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM pedidos");
                            deleteStmt.executeUpdate();
                            deleteStmt.close();
                        }
                    }

                    conn.close();

                    // Atualiza a tabela após todas as exclusões terem sido concluídas
                    atualizarDadosTabela();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Inicialize dataTable antes de chamar atualizarDadosTabela()
        dataTable = new JTable(new DefaultTableModel());

        add(new JScrollPane(dataTable), BorderLayout.CENTER);

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
