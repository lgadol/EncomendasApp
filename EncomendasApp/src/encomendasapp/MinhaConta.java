package encomendasapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.util.Map;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;

import javax.swing.ImageIcon;

public class MinhaConta extends JFrame {
    private EditarConta editarConta;
    private JFrame janelaEncomendasApp;
    private JFrame janelaPrincipal;
    private JLabel adminLabel;
    private JLabel nomeUsuarioLabel;
    private JLabel telefoneLabel;
    private JLabel emailLabel;
    private JLabel cidadeLabel;
    private JLabel estadoLabel;
    private JLabel enderecoLabel;

    public MinhaConta(Map<String, String> usuario, JFrame janelaPrincipal) {
        this.janelaPrincipal = janelaPrincipal;

        ImageIcon iconeVisto =
            new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\marca-de-verificacao.png");
        ImageIcon iconeX =
            new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\marca-de-verificacao.png");

        // Redimensiona os ícones para 15x15
        iconeVisto = new ImageIcon(iconeVisto.getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        iconeX = new ImageIcon(iconeX.getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));

        // Cria JLabels para exibir as informações do usuário
        String admin = usuario.get("admin");
        if (admin.equals("1")) {
            adminLabel = new JLabel("Administrador: ");
            adminLabel.setIcon(iconeVisto);
            adminLabel.setHorizontalTextPosition(JLabel.LEFT);
        } else {
            adminLabel = new JLabel("Administrador: ");
            adminLabel.setIcon(iconeX);
            adminLabel.setHorizontalTextPosition(JLabel.LEFT);
        }
        nomeUsuarioLabel = new JLabel("Nome: " + usuario.get("nome"));
        telefoneLabel = new JLabel("Telefone: " + usuario.get("telefone"));
        emailLabel = new JLabel("Email: " + usuario.get("email"));
        cidadeLabel = new JLabel("Cidade: " + usuario.get("cidade"));
        estadoLabel = new JLabel("Estado: " + usuario.get("estado"));
        enderecoLabel = new JLabel("Endereço: " + usuario.get("endereco"));

        // Estiliza os JLabels
        adminLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nomeUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        telefoneLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cidadeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        estadoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        enderecoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Cria um JPanel com um BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Adiciona os JLabels ao JPanel
        panel.add(adminLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(nomeUsuarioLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(telefoneLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(emailLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(cidadeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(estadoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(enderecoLabel);

        // Centraliza os JLabels
        adminLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomeUsuarioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        telefoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cidadeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        estadoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        enderecoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton editarButton = new JButton("Editar");
        editarButton.setBackground(new Color(0, 153, 255));
        editarButton.setOpaque(true);
        editarButton.setBorderPainted(false);
        editarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        final Map<String, String> usuarioFinal = usuario;
        final JFrame janelaEncomendasAppFinal = janelaEncomendasApp;

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se a janela já está aberta
                if (editarConta == null) {
                    editarConta =
                            new EditarConta(usuarioFinal, MinhaConta.this, MinhaConta.this, janelaEncomendasAppFinal);
                    editarConta.setVisible(true);
                } else {
                    editarConta.toFront();
                    editarConta.repaint();
                }
            }
        });

        // Adiciona um Box.Filler ao JPanel
        panel.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, Integer.MAX_VALUE)));

        // Adiciona o botão "Editar" ao JPanel
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(editarButton);

        // Adiciona o JPanel ao JFrame
        add(panel);

        // Configura o JFrame
        pack();
        setSize(500, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void setEditarContaNull() {
        this.editarConta = null;
    }

    public void atualizarCampos(Map<String, String> usuario) {
        adminLabel.setText("Admin: " + usuario.get("admin"));
        nomeUsuarioLabel.setText("Nome: " + usuario.get("nome"));
        telefoneLabel.setText("Telefone: " + usuario.get("telefone"));
        emailLabel.setText("Email: " + usuario.get("email"));
        cidadeLabel.setText("Cidade: " + usuario.get("cidade"));
        estadoLabel.setText("Estado: " + usuario.get("estado"));
        enderecoLabel.setText("Endereço: " + usuario.get("endereco"));
    }
}
