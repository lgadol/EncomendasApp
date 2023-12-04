package encomendasapp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Map;

public class EditarPerfil extends JFrame {
    private JTextField nomeField;
    private JTextField emailField;
    private JTextField cidadeField;
    private JPasswordField senhaField;
    private JButton confirmarButton;

    public EditarPerfil(Map<String, String> usuario) {
        setLayout(new GridLayout(0, 2));

        add(new JLabel("Nome:"));
        nomeField = new JTextField(usuario.get("nome"));
        add(nomeField);

        add(new JLabel("Email:"));
        emailField = new JTextField(usuario.get("email"));
        add(emailField);

        add(new JLabel("Cidade:"));
        cidadeField = new JTextField(usuario.get("cidade"));
        add(cidadeField);

        add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        add(senhaField);

        confirmarButton = new JButton("Confirmar alterações");
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Adicione aqui o código para verificar a senha e atualizar as informações do usuário no banco de dados
            }
        });
        add(confirmarButton);

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
