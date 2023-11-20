package encomendasapp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public EncomendasApp(int admin, String nomeUsuarioLogado) {
        this.admin = admin;
        this.nomeUsuarioLogado = nomeUsuarioLogado;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addButtons();
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addButtons() {
        Dimension buttonSize = new Dimension(200, 50); // Define o tamanho preferido para os botões

        if (admin == 1) {
            clientesButton = new JButton("Clientes");
            pedidosButton = new JButton("Pedidos");
            clientesButton.setPreferredSize(buttonSize);
            pedidosButton.setPreferredSize(buttonSize);

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

            add(clientesButton, gbc);
            add(pedidosButton, gbc);
        }

        meusPedidosButton = new JButton("Meus Pedidos");
        meusPedidosButton.setPreferredSize(buttonSize);

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
                    meusPedidos = new MeusPedidos();
                    meusPedidos.setVisible(true);
                } else if (!meusPedidos.isVisible()) {
                    meusPedidos.setVisible(true);
                }
            }
        });

        add(meusPedidosButton, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());

        logoffButton = new JButton("Logoff");
        logoffButton.setPreferredSize(buttonSize);
        logoffButton.setBackground(new Color(255, 51, 0)); // Define a cor de fundo para um vermelho mais escuro
        logoffButton.setForeground(Color.BLACK);
        logoffButton.setOpaque(true);

        ImageIcon icon =
            IconManager.resizeIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\sair.png",
                                   20, 20);
        logoffButton.setIcon(icon);

        logoffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fecha todas as janelas
                if (clientes != null) {
                    clientes.dispose();
                }
                if (pedidos != null) {
                    pedidos.dispose();
                }
                if (meusPedidos != null) {
                    meusPedidos.dispose();
                }

                // Abre a tela de login
                dispose();
                new Login().setVisible(true);
            }
        });

        buttonPanel.add(logoffButton);
        add(buttonPanel, gbc);
    }
}
