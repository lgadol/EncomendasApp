package encomendasapp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.math.BigDecimal;

import java.sql.*;

import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

        // Bot√£o Adicionar Clientes
        addButton = new JButton("Adicionar Cliente");
        ImageIcon addIcon =
            new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\adicionar-usuario.png");
        Image addImage = addIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        addButton.setBackground(new Color(0, 204, 51));
        addIcon = new ImageIcon(addImage);

        // Bot√£o Limpar Registros
        clearButton = new JButton("Limpar Registros");
        ImageIcon clearIcon =
            new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\deletar-lixeira.png");
        Image clearImage = clearIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        clearButton.setBackground(new Color(255, 51, 0));
        clearIcon = new ImageIcon(clearImage);

        // Adicionando os botıes
        addButton.setIcon(addIcon);
        clearButton.setIcon(clearIcon);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        final CustomTableModel tableModel = new CustomTableModel(data, columnNames);
        dataTable = new JTable(tableModel);

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
                        // Se n√£o houver registros, mostra uma mensagem
                        JOptionPane.showMessageDialog(null, "N„o h· nenhum cliente para excluir");
                    } else {
                        // Se houver registros, mostra um di·logo de confirmaÁ„o
                        int confirm =
                            JOptionPane.showConfirmDialog(null, "Tem certeza de que deseja excluir todos os registros?",
                                                          "ConfirmaÁ„o", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Exclui todos os registros
                            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM clientes");
                            deleteStmt.executeUpdate();
                            deleteStmt.close();
                        }
                    }

                    conn.close();
                    atualizarDadosTabela();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Inicialize dataTable antes de adicionar o MouseListener
        dataTable = new JTable(new DefaultTableModel());

        final int[] selectedRow = new int[1];
        dataTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                selectedRow[0] = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    // Obtenha o ID do pedido na linha selecionada. Supondo que o ID seja a primeira coluna da tabela.
                    int clienteID = ((BigDecimal) dataTable.getValueAt(selectedRow[0], 0)).intValue();

                    // Abra a janela de edi√ß√£o passando o ID do pedido
                    new EditarCliente(clienteID).setVisible(true);
                }
                if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                    // Verifica se o bot√£o direito do mouse foi pressionado
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem menuItemEdit = new JMenuItem("Editar");
                    JMenuItem menuItemDelete = new JMenuItem("Excluir");
                    menuItemEdit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Obtenha o ID do pedido na linha selecionada. Supondo que o ID seja a primeira coluna da tabela.
                            int clienteID = ((BigDecimal) dataTable.getValueAt(selectedRow[0], 0)).intValue();

                            // Abra a janela de edi√ß√£o passando o ID do pedido
                            new EditarCliente(clienteID).setVisible(true);
                        }
                    });
                    menuItemDelete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Obtenha o ID do pedido na linha selecionada. Supondo que o ID seja a primeira coluna da tabela.
                            int clienteID = ((BigDecimal) dataTable.getValueAt(selectedRow[0], 0)).intValue();

                            try {
                                // Conecta ao banco de dados
                                Connection conn = DataBaseConnection.getConnection();

                                // Cria a query SQL para excluir o pedido com o ID especificado
                                String sql = "DELETE FROM clientes WHERE Id = ?";

                                // Cria um PreparedStatement para executar a query SQL
                                PreparedStatement pstmt = conn.prepareStatement(sql);

                                // Define o valor do par√¢metro na query SQL
                                pstmt.setInt(1, clienteID);

                                // Executa a query SQL
                                pstmt.executeUpdate();

                                // Fecha o PreparedStatement e a conex√£o
                                pstmt.close();
                                conn.close();

                                // Atualiza a tabela ap√≥s a exclus√£o
                                atualizarDadosTabela();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    popupMenu.add(menuItemEdit);
                    popupMenu.add(menuItemDelete);
                    popupMenu.show(table, mouseEvent.getX(), mouseEvent.getY());
                }
            }
        });

        add(new JScrollPane(dataTable), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);

        // Chame o m√©todo para preencher a tabela quando a janela for aberta
        atualizarDadosTabela();
    }

    private static final Logger LOGGER = Logger.getLogger(EditarCliente.class.getName());

    public class EditarCliente extends JFrame {
        private int clienteID;

        private JComboBox<String> campoAdmin;
        private JComboBox<String> campoAtivo;
        private JTextField campoNome;
        private JTextField campoTelefone;
        private JTextField campoEmail;
        private JTextField campoCidade;
        private JComboBox<String> campoEstado;
        private JTextField campoEndereco;

        private JButton botaoSalvar;

        public EditarCliente(int clienteID) {
            this.clienteID = clienteID;

            campoAdmin = new JComboBox<>(new String[] { "SIM", "N√O" });
            campoAtivo = new JComboBox<>(new String[] { "SIM", "N√O" });
            campoNome = new JTextField();
            campoTelefone = new JTextField();
            campoEmail = new JTextField();
            campoCidade = new JTextField();
            campoEstado = new JComboBox<>(new String[] {
                                          "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS",
                                          "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP",
                                          "SE", "TO"
                });
            campoEndereco = new JTextField();

            // Crie um painel e adicione os campos a ele
            JPanel panel = new JPanel(new GridLayout(9, 2));
            panel.add(new JLabel("Admin"));
            panel.add(campoAdmin);
            panel.add(new JLabel("Ativo"));
            panel.add(campoAtivo);
            panel.add(new JLabel("Nome"));
            panel.add(campoNome);
            panel.add(new JLabel("Telefone"));
            panel.add(campoTelefone);
            panel.add(new JLabel("E-mail"));
            panel.add(campoEmail);
            panel.add(new JLabel("Cidade"));
            panel.add(campoCidade);
            panel.add(new JLabel("Estado"));
            panel.add(campoEstado);
            panel.add(new JLabel("Endere√ßo"));
            panel.add(campoEndereco);

            // Inicialize e adicione o bot„o Salvar
            botaoSalvar = new JButton("Salvar");
            botaoSalvar.setBackground(new Color(0, 204, 51)); // Muda a cor do bot„o para azul

            botaoSalvar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    salvarDadosPedido();
                }
            });

            // Adicione o painel ao JFrame
            add(panel);

            // Depois de adicionar os componentes, preencha-os com os dados do pedido
            preencherDadosCliente();
        }

        private void preencherDadosCliente() {
            try {
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM clientes WHERE id = ?");
                pstmt.setInt(1, clienteID);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    campoAdmin.setSelectedItem(rs.getInt("admin") == 1 ? "SIM" : "N√O");
                    campoAtivo.setSelectedItem(rs.getInt("ativo") == 1 ? "SIM" : "N√O");
                    campoNome.setText(rs.getString("nome"));
                    campoTelefone.setText(rs.getString("telefone"));
                    campoEmail.setText(rs.getString("email"));
                    campoCidade.setText(rs.getString("cidade"));
                    campoEstado.setSelectedItem(rs.getString("estado"));
                    campoEndereco.setText(rs.getString("endereco"));
                }

                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(500, 400);
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void salvarDadosPedido() {
            LOGGER.info("Conex√£o estabelecida.");

            Pattern patternEmail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$");
            Matcher matcherEmail = patternEmail.matcher(campoEmail.getText());
            if (!matcherEmail.find()) {
                JOptionPane.showMessageDialog(null, "Email inv·lido");
                return;
            }

            // Valida√ß√£o de telefone
            Pattern patternTelefone = Pattern.compile("^\\d{2} \\d{8,9}$");
            Matcher matcherTelefone = patternTelefone.matcher(campoTelefone.getText());
            if (!matcherTelefone.find()) {
                JOptionPane.showMessageDialog(null, "Telefone inv·lido");
                return;
            }

            try {
                LOGGER.info("Conectando ao banco de dados...");
                Connection conn = DataBaseConnection.getConnection();
                LOGGER.info("Conex√£o estabelecida.");

                // Verificar se o email j· existe
                PreparedStatement checkStmt =
                    conn.prepareStatement("SELECT * FROM clientes WHERE email = ? AND id != ?");
                checkStmt.setString(1, campoEmail.getText());
                checkStmt.setInt(2, clienteID);
                ResultSet checkRs = checkStmt.executeQuery();
                if (checkRs.next()) {
                    JOptionPane.showMessageDialog(null, "Este email j· est· sendo usado por outro cliente.");
                    return;
                }
                checkRs.close();
                checkStmt.close();

                // Verificar se o telefone j· existe
                PreparedStatement checkTelefoneStmt =
                    conn.prepareStatement("SELECT * FROM clientes WHERE telefone = ? AND id != ?");
                checkTelefoneStmt.setString(1, campoTelefone.getText());
                checkTelefoneStmt.setInt(2, clienteID);
                ResultSet checkTelefoneRs = checkTelefoneStmt.executeQuery();
                if (checkTelefoneRs.next()) {
                    JOptionPane.showMessageDialog(null, "Este telefone j· est· sendo usado por outro cliente.");
                    return;
                }
                checkTelefoneRs.close();
                checkTelefoneStmt.close();

                String sql =
                    "UPDATE clientes SET admin = ?, ativo = ?, nome = ?, telefone = ?, email = ?, " +
                    "cidade = ?, estado = ?, endereco = ? WHERE id = ?";

                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, "SIM".equals(campoAdmin.getSelectedItem()) ? 1 : 0);
                pstmt.setInt(2, "SIM".equals(campoAtivo.getSelectedItem()) ? 1 : 0);
                pstmt.setString(3, campoNome.getText().toUpperCase());
                pstmt.setString(4, campoTelefone.getText().toUpperCase());
                pstmt.setString(5, campoEmail.getText());
                pstmt.setString(6, campoCidade.getText().toUpperCase());
                pstmt.setString(7, (String) campoEstado.getSelectedItem());
                pstmt.setString(8, campoEndereco.getText().toUpperCase());
                pstmt.setInt(9, clienteID);

                LOGGER.info("Executando a atualiza√ß√£o...");
                pstmt.executeUpdate();
                LOGGER.info("Atualiza√ß√£o executada.");

                pstmt.close();
                conn.close();
            } catch (SQLException ex) {
                LOGGER.severe("Uma exceÁ„o SQLException foi lanÁada.");
                ex.printStackTrace();
            }

            LOGGER.info("Finalizando salvarDadosPedido.");

            // Feche a janela de ediÁ„o
            dispose();

            // Atualize a lista
            atualizarDadosTabela();
        }

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
