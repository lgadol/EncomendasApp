package encomendasapp;

import java.awt.Dimension;
import java.awt.Font;

import java.util.Map;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinhaConta extends JFrame {
    public MinhaConta(Map<String, String> usuario) {
        // Cria JLabels para exibir as informações do usuário
        JLabel adminLabel = new JLabel("Admin: " + usuario.get("admin"));
        JLabel nomeUsuarioLabel = new JLabel("Nome: " + usuario.get("nome"));
        JLabel telefoneLabel = new JLabel("Telefone: " + usuario.get("telefone"));
        JLabel emailLabel = new JLabel("Email: " + usuario.get("email"));
        JLabel cidadeLabel = new JLabel("Cidade: " + usuario.get("cidade"));
        JLabel estadoLabel = new JLabel("Estado: " + usuario.get("estado"));
        JLabel enderecoLabel = new JLabel("Endereço: " + usuario.get("endereco"));

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

        // Adiciona um ActionListener ao botão "Editar"
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Adicione aqui o código que será executado quando o botão "Editar" for clicado
            }
        });

        // Adiciona o botão "Editar" ao JPanel
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Adiciona espaço entre as labels e o botão
        panel.add(editarButton);


        // Adiciona o JPanel ao JFrame
        add(panel);

        // Configura o JFrame
        pack();
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
