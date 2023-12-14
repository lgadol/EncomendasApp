package encomendasapp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.math.BigDecimal;

import java.sql.*;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
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
    private boolean isEditingWindowOpen = false;

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

        IconSetter.setIcon(this);

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
                JTable table = (JTable)mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                selectedRow[0] = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    // Obtenha o ID do pedido na linha selecionada. Supondo que o ID seja a primeira coluna da tabela.
                    if (!isEditingWindowOpen) {
                        int clienteID = ((BigDecimal)dataTable.getValueAt(selectedRow[0], 0)).intValue();

                        // Abra a janela de edição passando o ID do pedido
                        new EditarCliente(clienteID).setVisible(true);
                        isEditingWindowOpen = true;
                    }
                }
                if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                    // Verifica se o botão direito do mouse foi pressionado
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem menuItemEdit = new JMenuItem("Editar");
                    JMenuItem menuItemDelete = new JMenuItem("Excluir");

                    menuItemEdit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!isEditingWindowOpen) {
                                // Obtenha o ID do pedido na linha selecionada. Supondo que o ID seja a primeira coluna da tabela.
                                int clienteID = ((BigDecimal)dataTable.getValueAt(selectedRow[0], 0)).intValue();

                                // Abra a janela de edição passando o ID do pedido
                                new EditarCliente(clienteID).setVisible(true);
                                isEditingWindowOpen = true;
                            }
                        }
                    });
                    menuItemDelete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Obtenha o ID do pedido na linha selecionada. Supondo que o ID seja a primeira coluna da tabela.
                            int clienteID = ((BigDecimal)dataTable.getValueAt(selectedRow[0], 0)).intValue();

                            try {
                                // Conecta ao banco de dados
                                Connection conn = DataBaseConnection.getConnection();

                                // Cria a query SQL para excluir o pedido com o ID especificado
                                String sql = "DELETE FROM clientes WHERE Id = ?";

                                // Cria um PreparedStatement para executar a query SQL
                                PreparedStatement pstmt = conn.prepareStatement(sql);

                                // Define o valor do parametro na query SQL
                                pstmt.setInt(1, clienteID);

                                // Executa a query SQL
                                pstmt.executeUpdate();

                                // Fecha o PreparedStatement e a conexão
                                pstmt.close();
                                conn.close();

                                // Atualiza a tabela após a exclusão
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

        // Chame o método para preencher a tabela quando a janela for aberta
        atualizarDadosTabela();
    }

    private static final Logger LOGGER = Logger.getLogger(EditarCliente.class.getName());

    private class CustomTableModel extends DefaultTableModel {
        public CustomTableModel(Vector<Vector<Object>> data, Vector<String> columnNames) {
            super(data, columnNames);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
            case 1: // Segunda coluna
            case 2: // Terceira coluna
                return Icon.class;
            default:
                return super.getColumnClass(columnIndex);
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            if (column == 1 || column == 2) {
                Object value = super.getValueAt(row, column);
                ImageIcon icon;
                if (value instanceof Number && ((Number)value).intValue() == 1) {
                    icon =
new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\marca-de-verificacao.png");
                } else {
                    icon =
new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\fechar2.png");
                }
                Image img = icon.getImage();
                Image resizedImage = img.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(resizedImage);
            } else {
                return super.getValueAt(row, column);
            }
        }
    }

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
        private JButton botaoResetarSenha;
        private JButton botaoSalvar;

        public EditarCliente(int clienteID) {
            this.clienteID = clienteID;

            Image iconeTitulo =
                Toolkit.getDefaultToolkit().getImage("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\background\\logo.jpg");
            this.setIconImage(iconeTitulo);

            campoAdmin = new JComboBox<>(new String[] { "SIM", "NÃO" });
            campoAtivo = new JComboBox<>(new String[] { "SIM", "NÃO" });
            campoNome = new JTextField();
            campoTelefone = new JTextField();
            campoEmail = new JTextField();
            campoCidade = new JTextField();
            campoEstado =
                    new JComboBox<>(new String[] { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
                                                   "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS",
                                                   "RO", "RR", "SC", "SP", "SE", "TO" });
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
            panel.add(new JLabel("Endereço"));
            panel.add(campoEndereco);
            
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    // Quando a janela de edição é fechada, defina isEditingWindowOpen como false
                    isEditingWindowOpen = false;
                }
            });

            // Inicialize e adicione o botão Resetar Senha
            ImageIcon borrachaIcon =
                new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\borracha.png");
            ImageIcon borrachaRedi =
                new ImageIcon(borrachaIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

            botaoResetarSenha = new JButton("Resetar Senha", borrachaRedi);
            botaoResetarSenha.setBackground(new Color(255, 213, 0));
            panel.add(botaoResetarSenha);

            final int finalClienteID = clienteID;
            botaoResetarSenha.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection conn = DataBaseConnection.getConnection();
                        PreparedStatement checkStmt = conn.prepareStatement("SELECT senha FROM clientes WHERE id = ?");
                        checkStmt.setInt(1, finalClienteID);
                        ResultSet checkRs = checkStmt.executeQuery();
                        if (checkRs.next()) {
                            String senha = checkRs.getString("senha");
                            if (senha == null) {
                                JOptionPane.showMessageDialog(null, "Usuário não possui senha");
                                return;
                            }
                        }
                        checkRs.close();
                        checkStmt.close();

                        int confirm =
                            JOptionPane.showConfirmDialog(null, "Tem certeza de que deseja resetar a senha deste usuário?",
                                                          "Confirmação", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            String sql = "UPDATE clientes SET senha = NULL WHERE id = ?";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setInt(1, finalClienteID);
                            pstmt.executeUpdate();
                            pstmt.close();
                            JOptionPane.showMessageDialog(null, "Senha Resetada");
                        }
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Inicialize e adicione o botão Salvar
            ImageIcon salvarIcon =
                new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\salvar2.png");
            ImageIcon SalvarRedi = new ImageIcon(salvarIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

            botaoSalvar = new JButton("Salvar", SalvarRedi);
            botaoSalvar.setBackground(new Color(0, 204, 51));
            panel.add(botaoSalvar);

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
                    campoAdmin.setSelectedItem(rs.getInt("admin") == 1 ? "SIM" : "NÃO");
                    campoAtivo.setSelectedItem(rs.getInt("ativo") == 1 ? "SIM" : "NÃO");
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
            LOGGER.info("Conexão estabelecida.");

            Pattern patternEmail = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$");
            Matcher matcherEmail = patternEmail.matcher(campoEmail.getText());
            if (!matcherEmail.find()) {
                JOptionPane.showMessageDialog(null, "Email inválido");
                return;
            }

            // Validação de telefone
            Pattern patternTelefone = Pattern.compile("^\\d{2} \\d{8,9}$");
            Matcher matcherTelefone = patternTelefone.matcher(campoTelefone.getText());
            if (!matcherTelefone.find()) {
                JOptionPane.showMessageDialog(null, "Telefone inválido");
                return;
            }

            try {
                LOGGER.info("Conectando ao banco de dados...");
                Connection conn = DataBaseConnection.getConnection();
                LOGGER.info("Conexão estabelecida.");

                // Verificar se o email já existe
                PreparedStatement checkStmt =
                    conn.prepareStatement("SELECT * FROM clientes WHERE email = ? AND id != ?");
                checkStmt.setString(1, campoEmail.getText());
                checkStmt.setInt(2, clienteID);
                ResultSet checkRs = checkStmt.executeQuery();
                if (checkRs.next()) {
                    JOptionPane.showMessageDialog(null, "Este email já está sendo usado por outro cliente.");
                    return;
                }
                checkRs.close();
                checkStmt.close();

                // Verificar se o telefone já existe
                PreparedStatement checkTelefoneStmt =
                    conn.prepareStatement("SELECT * FROM clientes WHERE telefone = ? AND id != ?");
                checkTelefoneStmt.setString(1, campoTelefone.getText());
                checkTelefoneStmt.setInt(2, clienteID);
                ResultSet checkTelefoneRs = checkTelefoneStmt.executeQuery();
                if (checkTelefoneRs.next()) {
                    JOptionPane.showMessageDialog(null, "Este telefone já está sendo usado por outro cliente.");
                    return;
                }
                checkTelefoneRs.close();
                checkTelefoneStmt.close();

                String sql =
                    "UPDATE clientes SET admin = ?, ativo = ?, nome = ?, telefone = ?, email = ?, " + "cidade = ?, estado = ?, endereco = ? WHERE id = ?";

                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, "SIM".equals(campoAdmin.getSelectedItem()) ? 1 : 0);
                pstmt.setInt(2, "SIM".equals(campoAtivo.getSelectedItem()) ? 1 : 0);
                pstmt.setString(3, campoNome.getText().toUpperCase());
                pstmt.setString(4, campoTelefone.getText().toUpperCase());
                pstmt.setString(5, campoEmail.getText());
                pstmt.setString(6, campoCidade.getText().toUpperCase());
                pstmt.setString(7, (String)campoEstado.getSelectedItem());
                pstmt.setString(8, campoEndereco.getText().toUpperCase());
                pstmt.setInt(9, clienteID);

                LOGGER.info("Executando a atualização...");
                pstmt.executeUpdate();
                LOGGER.info("Atualização executada.");

                pstmt.close();
                conn.close();
            } catch (SQLException ex) {
                LOGGER.severe("Uma exceção SQLException foi lançada.");
                ex.printStackTrace();
            }

            LOGGER.info("Finalizando salvarDadosPedido.");

            // Feche a janela de edição
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
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
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
