package encomendasapp;

import javax.swing.*;

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

        // Remova todos os ouvintes de ação existentes
        for (ActionListener al : addButton.getActionListeners()) {
            addButton.removeActionListener(al);
        }
        
        final AdicionarPedido[] adicionarPedidoWindow = new AdicionarPedido[1];

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LOGGER.info("Seu método foi chamado");
                if (adicionarPedidoWindow[0] == null || !adicionarPedidoWindow[0].isVisible()) {
                    adicionarPedidoWindow[0] = new AdicionarPedido(admin, nomeUsuarioLogado, Pedidos.this);
                    adicionarPedidoWindow[0].setVisible(true);
                } else {
                    adicionarPedidoWindow[0].toFront();
                    adicionarPedidoWindow[0].repaint();
                }
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
                    int pedidoId = ((BigDecimal)dataTable.getValueAt(selectedRow[0], 0)).intValue();

                    // Abra a janela de edição passando o ID do pedido
                    new EditarPedido(pedidoId).setVisible(true);
                }
                if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                    // Verifica se o botÃ£o direito do mouse foi pressionado
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem menuItemEdit = new JMenuItem("Editar");
                    JMenuItem menuItemDelete = new JMenuItem("Excluir");
                    menuItemEdit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Obtenha o ID do pedido na linha selecionada. Supondo que o ID seja a primeira coluna da tabela.
                            int pedidoId = ((BigDecimal)dataTable.getValueAt(selectedRow[0], 0)).intValue();

                            // Abra a janela de ediÃ§Ã£o passando o ID do pedido
                            new EditarPedido(pedidoId).setVisible(true);
                        }
                    });
                    menuItemDelete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Obtenha o ID do pedido na linha selecionada. Supondo que o ID seja a primeira coluna da tabela.
                            int pedidoId = ((BigDecimal)dataTable.getValueAt(selectedRow[0], 0)).intValue();

                            try {
                                // Conecta ao banco de dados
                                Connection conn = DataBaseConnection.getConnection();

                                // Cria a query SQL para excluir o pedido com o ID especificado
                                String sql = "DELETE FROM pedidos WHERE Id = ?";

                                // Cria um PreparedStatement para executar a query SQL
                                PreparedStatement pstmt = conn.prepareStatement(sql);

                                // Define o valor do parÃ¢metro na query SQL
                                pstmt.setInt(1, pedidoId);

                                // Executa a query SQL
                                pstmt.executeUpdate();

                                // Fecha o PreparedStatement e a conexÃ£o
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

    private static final Logger LOGGER = Logger.getLogger(EditarPedido.class.getName());

    public class EditarPedido extends JFrame {
        private int pedidoId;

        private JTextField campoNomeCliente;
        private JComboBox<String> campoCategoria;
        private JTextField campoTipoCarne;
        private JTextField campoTipoCorte;
        private JComboBox<String> campoPago;
        private JComboBox<String> campoPagamentoAdiantado;
        private JComboBox<String> campoTipoPagamento;
        private JTextField campoPrecoPago;
        private JTextField campoKgs;

        private JButton botaoSalvar;

        public EditarPedido(int pedidoId) {
            this.pedidoId = pedidoId;

            campoNomeCliente = new JTextField();
            campoCategoria = new JComboBox<>(new String[] { "GADO", "PORCO", "FRANGO", "OUTRO" });
            campoTipoCarne = new JTextField();
            campoTipoCorte = new JTextField();
            campoPago = new JComboBox<>(new String[] { "SIM", "NÃO" });
            campoPagamentoAdiantado = new JComboBox<>(new String[] { "SIM", "NÃO" });
            campoTipoPagamento = new JComboBox<>(new String[] { "DINHEIRO", "CREDITO", "DEBITO", "PIX", "OUTRO" });
            campoPrecoPago = new JTextField();
            campoKgs = new JTextField();

            // Crie um painel e adicione os campos a ele
            JPanel panel = new JPanel(new GridLayout(10, 2));
            panel.add(new JLabel("Nome do Cliente"));
            panel.add(campoNomeCliente);
            panel.add(new JLabel("Categoria"));
            panel.add(campoCategoria);
            panel.add(new JLabel("Tipo de Carne"));
            panel.add(campoTipoCarne);
            panel.add(new JLabel("Tipo de Corte"));
            panel.add(campoTipoCorte);
            panel.add(new JLabel("Pago"));
            panel.add(campoPago);
            panel.add(new JLabel("Pagamento Adiantado"));
            panel.add(campoPagamentoAdiantado);
            panel.add(new JLabel("Tipo de Pagamento"));
            panel.add(campoTipoPagamento);
            panel.add(new JLabel("Preço Pago"));
            panel.add(campoPrecoPago);
            panel.add(new JLabel("Kgs"));
            panel.add(campoKgs);

            // Inicialize e adicione o botão Salvar
            botaoSalvar = new JButton("Salvar");
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
            preencherDadosPedido();
        }

        private void preencherDadosPedido() {
            try {
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM pedidos WHERE id = ?");
                pstmt.setInt(1, pedidoId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    campoNomeCliente.setText(rs.getString("nome_cliente"));
                    campoCategoria.setSelectedItem(rs.getString("categoria"));
                    campoTipoCarne.setText(rs.getString("tipo_carne"));
                    campoTipoCorte.setText(rs.getString("tipo_corte"));
                    campoPago.setSelectedItem(rs.getInt("pago") == 1 ? "SIM" : "NÃO");
                    campoPagamentoAdiantado.setSelectedItem(rs.getInt("pagamento_adiantado") == 1 ? "SIM" : "NÃO");
                    campoTipoPagamento.setSelectedItem(rs.getString("tipo_pagamento"));
                    campoPrecoPago.setText(rs.getString("preco_pago"));
                    campoKgs.setText(rs.getString("kgs"));
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
            LOGGER.info("Iniciando salvarDadosPedido...");
            try {
                LOGGER.info("Conectando ao banco de dados...");
                Connection conn = DataBaseConnection.getConnection();
                LOGGER.info("ConexÃ£o estabelecida.");

                String sql =
                    "UPDATE pedidos SET nome_cliente = ?, categoria = ?, tipo_carne = ?, tipo_corte = ?, pago = ?, " +
                    "pagamento_adiantado = ?, tipo_pagamento = ?, preco_pago = ?, kgs = ? WHERE id = ?";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, campoNomeCliente.getText().toUpperCase());
                pstmt.setString(2, (String)campoCategoria.getSelectedItem());
                pstmt.setString(3, campoTipoCarne.getText().toUpperCase());
                pstmt.setString(4, campoTipoCorte.getText().toUpperCase());
                pstmt.setInt(5, "SIM".equals(campoPago.getSelectedItem()) ? 1 : 0);
                pstmt.setInt(6, "SIM".equals(campoPagamentoAdiantado.getSelectedItem()) ? 1 : 0);
                pstmt.setString(7, (String)campoTipoPagamento.getSelectedItem());
                pstmt.setString(8, campoPrecoPago.getText().toUpperCase());
                pstmt.setString(9, campoKgs.getText().toUpperCase());
                pstmt.setInt(10, pedidoId);

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
        DefaultTableModel model = (DefaultTableModel)dataTable.getModel();
        model.setDataVector(data, columnNames);
    }
}
