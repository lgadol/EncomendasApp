package encomendasapp;

import java.awt.Dimension;
import java.awt.Font;

import java.util.Map;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // Cria JLabels para exibir as informações do usuário
        adminLabel = new JLabel("Administrador: " + usuario.get("admin"));
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
        editarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        final Map<String, String> usuarioFinal = usuario;
        final JFrame janelaEncomendasAppFinal = janelaEncomendasApp;

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se a janela já está aberta
                if (editarConta != null && editarConta.isVisible()) {
                    editarConta.toFront();
                    editarConta.repaint();
                } else {
                    // Cria e exibe a janela EditarConta
                    editarConta = new EditarConta(usuarioFinal, MinhaConta.this, MinhaConta.this, janelaEncomendasAppFinal);
                    editarConta.setVisible(true);
                }
            }
        });

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
