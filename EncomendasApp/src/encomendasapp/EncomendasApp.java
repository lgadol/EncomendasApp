package encomendasapp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Map;

public class EncomendasApp extends JFrame {
    private JButton clientesButton;
    private JButton pedidosButton;
    private JButton meusPedidosButton;
    private JButton logoffButton;
    private int admin;
    private String nomeUsuarioLogado;
    private Clientes clientes;
    private Pedidos pedidos;
    private MeusPedidos meusPedidos;
    private GridBagConstraints gbc;
    private JLabel nomeUsuarioLabel;
    private JPanel panel;
    private JPanel buttonPanel;
    private MinhaConta minhaConta;
    private int idUsuarioLogado;
    private Map<String, String> usuario;

    public EncomendasApp(int admin, String nomeUsuarioLogado, int idUsuarioLogado) {
        this.admin = admin;
        this.nomeUsuarioLogado = nomeUsuarioLogado;
        this.idUsuarioLogado = idUsuarioLogado;
        this.usuario = new GerenciadorDeUsuarios().obterUsuarioDoBancoDeDados(idUsuarioLogado);

        nomeUsuarioLabel = new JLabel("<html>" + "Bem-vindo," + "<br>" + nomeUsuarioLogado + "!" + "<html>");
        nomeUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nomeUsuarioLabel.setHorizontalAlignment(JLabel.CENTER);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(nomeUsuarioLabel);

        gbc = new GridBagConstraints(); // Inicialize gbc aqui

        addButtons();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addButtons() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(200, 50); // Define o tamanho preferido para os botões

        if (admin == 1) {
            clientesButton = new JButton("Clientes");
            clientesButton.setMinimumSize(buttonSize);
            clientesButton.setMaximumSize(buttonSize);
            clientesButton.setPreferredSize(buttonSize);
            JPanel clientesButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            clientesButtonPanel.add(clientesButton);
            buttonPanel.add(clientesButtonPanel);

            pedidosButton = new JButton("Pedidos");
            pedidosButton.setMinimumSize(buttonSize);
            pedidosButton.setMaximumSize(buttonSize);
            pedidosButton.setPreferredSize(buttonSize);
            JPanel pedidosButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pedidosButtonPanel.add(pedidosButton);
            buttonPanel.add(pedidosButtonPanel);

            ImageIcon iconClientes =
                IconManager.resizeIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\clientes.png",
                                       20, 20);
            clientesButton.setIcon(iconClientes);
            clientesButton.setBackground(new Color(141, 218, 253));
            clientesButton.setForeground(Color.BLACK);

            ImageIcon iconPedidos =
                IconManager.resizeIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\pedidos.png",
                                       20, 20);
            pedidosButton.setIcon(iconPedidos);
            pedidosButton.setBackground(new Color(141, 218, 253));
            pedidosButton.setForeground(Color.BLACK);

            clientesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (clientes == null) {
                        clientes = new Clientes();
                        clientes.setVisible(true);
                    } else if (!clientes.isVisible()) {
                        clientes.setVisible(true);
                    }
                }
            });

            pedidosButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (pedidos == null) {
                        pedidos = new Pedidos(admin, nomeUsuarioLogado);
                        pedidos.setVisible(true);
                    } else if (!pedidos.isVisible()) {
                        pedidos.setVisible(true);
                    }
                }
            });
        }

        meusPedidosButton = new JButton("Meus Pedidos");
        meusPedidosButton.setPreferredSize(buttonSize);
        meusPedidosButton.setMinimumSize(buttonSize);
        meusPedidosButton.setMaximumSize(buttonSize);
        JPanel meusPedidosButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        meusPedidosButtonPanel.add(meusPedidosButton);
        buttonPanel.add(meusPedidosButtonPanel);

        ImageIcon iconMeusPedidos =
            IconManager.resizeIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\meus_pedidos.png",
                                   20, 20);
        meusPedidosButton.setIcon(iconMeusPedidos);
        meusPedidosButton.setBackground(new Color(141, 218, 253));
        meusPedidosButton.setForeground(Color.BLACK);

        meusPedidosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (meusPedidos == null) {
                    meusPedidos = new MeusPedidos(admin, nomeUsuarioLogado);
                    meusPedidos.setVisible(true);
                } else if (!meusPedidos.isVisible()) {
                    meusPedidos.setVisible(true);
                }
            }
        });

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JButton minhaContaButton = new JButton("Minha Conta");
        minhaContaButton.setPreferredSize(buttonSize);
        minhaContaButton.setMinimumSize(buttonSize);
        minhaContaButton.setMaximumSize(buttonSize);
        minhaContaButton.setBackground(new Color(141, 218, 253));
        minhaContaButton.setForeground(Color.BLACK);

        // Adiciona um ActionListener ao bot�o "Minha Conta"
        minhaContaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cria uma inst�ncia de GerenciadorDeUsuarios
                GerenciadorDeUsuarios gerenciadorDeUsuarios = new GerenciadorDeUsuarios();

                // Obtem as informa��es do usu�rio do banco de dados
                Map<String, String> usuario = gerenciadorDeUsuarios.obterUsuarioDoBancoDeDados(idUsuarioLogado);

                // Verifica se a janela j� est� aberta
                if (minhaConta != null && minhaConta.isVisible()) {
                    minhaConta.toFront();
                    minhaConta.repaint();
                } else {
                    // Cria e exibe a janela MinhaConta
                    minhaConta =
                            new MinhaConta(usuario, EncomendasApp.this); // Aqui voc� passa a inst�ncia atual de EncomendasApp
                    minhaConta.setVisible(true);
                }
            }
        });

        // Cria um JPanel para o bot�o "Minha Conta" e adiciona o bot�o a este painel
        JPanel minhaContaButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        minhaContaButtonPanel.add(minhaContaButton);
        ImageIcon iconMinhaConta =
            IconManager.resizeIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\perfil.png",
                                   20, 20);
        minhaContaButton.setIcon(iconMinhaConta);

        // Adiciona o painel do bot�o "Minha Conta" ao painel de bot�es
        buttonPanel.add(minhaContaButtonPanel);

        logoffButton = new JButton("Sair");
        logoffButton.setPreferredSize(buttonSize);
        logoffButton.setBackground(new Color(255, 51, 0));
        logoffButton.setForeground(Color.BLACK);
        logoffButton.setOpaque(true);
        logoffButton.setMinimumSize(buttonSize);
        logoffButton.setMaximumSize(buttonSize);
        JPanel logoffButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoffButtonPanel.add(logoffButton);
        buttonPanel.add(logoffButtonPanel);

        ImageIcon iconSair =
            IconManager.resizeIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\sair.png",
                                   20, 20);
        logoffButton.setIcon(iconSair);

        logoffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fecha todas as janelas
                for (Window window : Window.getWindows()) {
                    window.dispose();
                }
                // Abre a tela de login
                new Login().setVisible(true);
            }
        });

        // Cria um novo GridBagConstraints para centralizar o buttonPanel verticalmente
        GridBagConstraints gbcPanel = new GridBagConstraints();
        gbcPanel.gridwidth = GridBagConstraints.REMAINDER;
        gbcPanel.fill = GridBagConstraints.VERTICAL;
        gbcPanel.weighty = 1;

        panel.add(buttonPanel, BorderLayout.CENTER);
        buttonPanel.add(Box.createVerticalGlue());
    }

}
