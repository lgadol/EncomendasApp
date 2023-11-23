package encomendasapp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import java.sql.*;

import java.util.Vector;

public class Clientes extends JFrame implements AtualizarTabela {
    private JTable dataTable;
    private JButton addButton;
    private JButton clearButton;
    private int isEditing = -1;
    private Vector<Vector<Object>> data = new Vector<>();
    private Vector<String> columnNames = new Vector<>();

    public Clientes() {
        setLayout(new BorderLayout());
        columnNames = new Vector<>();
        columnNames.add("Id");
        columnNames.add("Admin");
        columnNames.add("Ativo");
        columnNames.add("Nome");
        columnNames.add("Telefone");
        columnNames.add("Email");
        columnNames.add("Cidade");
        columnNames.add("Estado");
        columnNames.add("Endereco");
        columnNames.add("R. Senha");
        columnNames.add("Editar");
        columnNames.add("Salvar");

        // Botão Adicionar Clientes
        addButton = new JButton("Adicionar Cliente");
        ImageIcon addIcon =
            new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\adicionar-usuario.png");
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

        final CustomTableModel tableModel = new CustomTableModel(data, columnNames);
        dataTable = new JTable(tableModel);

        dataTable.getColumn("Editar").setCellRenderer(new IconRenderer("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\editar.png"));
        dataTable.getColumn("R. Senha").setCellRenderer(new IconRenderer("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\reiniciar.png"));
        dataTable.getColumn("Salvar").setCellRenderer(new IconRenderer("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\salvar.png"));

        dataTable.addMouseListener(new JTableButtonMouseListener(dataTable));

        add(new JScrollPane(dataTable), BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdicionarCliente(Clientes.this);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Conecta ao banco de dados e verifica se existem registros
                    Connection conn = DataBaseConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM clientes");
                    rs.next();
                    int count = rs.getInt(1);
                    rs.close();
                    stmt.close();

                    if (count == 0) {
                        // Se não houver registros, mostra uma mensagem
                        JOptionPane.showMessageDialog(null, "Não há nenhum cliente para excluir");
                    } else {
                        // Se houver registros, mostra um diálogo de confirmação
                        int confirm =
                            JOptionPane.showConfirmDialog(null, "Tem certeza de que deseja excluir todos os registros?",
                                                          "Confirmação", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Exclui todos os registros
                            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM clientes");
                            deleteStmt.executeUpdate();
                            deleteStmt.close();

                            // Atualiza a tabela
                            atualizarDadosTabela();
                        }
                    }

                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);

        atualizarDadosTabela();
    }

    @Override
    public void atualizarDadosTabela() {
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

        // Atualiza o modelo da tabela
        dataTable.setModel(new CustomTableModel(data, columnNames));
    }

    class IconRenderer extends DefaultTableCellRenderer {
        private ImageIcon icon;

        public IconRenderer(String iconPath) {
            Image originalImage = new ImageIcon(iconPath).getImage();
            Image resizedImage = originalImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            this.icon = new ImageIcon(resizedImage);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = new JLabel(icon);
            return label;
        }
    }

    class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }
    }
}
