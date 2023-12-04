package encomendasapp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Map;

import javax.swing.*;

public class EditarConta extends JFrame {
    private final Map<String, String>[] usuarioArray;
    private JTextField adminField;
    private JTextField nomeUsuarioField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField enderecoField;
    private JPasswordField senhaField;
    private static EditarConta instance = null;
    private GerenciadorDeUsuarios gerenciadorDeUsuarios;

    public EditarConta(final Map<String, String>[] usuarioArray) {
        this.usuarioArray = usuarioArray;
        this.gerenciadorDeUsuarios = new GerenciadorDeUsuarios();

        // Cria JTextFields para a edição dos dados do usuário
        adminField = new JTextField(usuarioArray[0].get("admin"));
        adminField.setEditable(false);
        nomeUsuarioField = new JTextField(usuarioArray[0].get("nome"));
        telefoneField = new JTextField(usuarioArray[0].get("telefone"));
        emailField = new JTextField(usuarioArray[0].get("email"));
        cidadeField = new JTextField(usuarioArray[0].get("cidade"));
        estadoField = new JTextField(usuarioArray[0].get("estado"));
        enderecoField = new JTextField(usuarioArray[0].get("endereco"));
        senhaField = new JPasswordField();

        // Cria um JPanel com um GridLayout
        JPanel panel = new JPanel(new GridLayout(9, 2));

        // Adiciona os JTextFields ao JPanel
        panel.add(new JLabel("Adminss:"));
        panel.add(adminField);
        panel.add(new JLabel("Nome do usuário:"));
        panel.add(nomeUsuarioField);
        panel.add(new JLabel("Telefone:"));
        panel.add(telefoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Cidade:"));
        panel.add(cidadeField);
        panel.add(new JLabel("Estado:"));
        panel.add(estadoField);
        panel.add(new JLabel("Endereço:"));
        panel.add(enderecoField);
        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);

        // Cria um botão "Salvar"
        JButton salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Atualiza os dados do usuário
                usuarioArray[0].put("nome", nomeUsuarioField.getText());
                usuarioArray[0].put("telefone", telefoneField.getText());
                usuarioArray[0].put("email", emailField.getText());
                usuarioArray[0].put("cidade", cidadeField.getText());
                usuarioArray[0].put("estado", estadoField.getText());
                usuarioArray[0].put("endereco", enderecoField.getText());

                // Verifica a senha
                String senha = new String(senhaField.getPassword());
                if (!senha.equals(usuarioArray[0].get("senha"))) {
                    JOptionPane.showMessageDialog(null, "Senha incorreta!");
                    return;
                }
                System.out.println("Senha armazenada: " + usuarioArray[0].get("senha"));

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Atualiza os dados do usuário no banco de dados
                        gerenciadorDeUsuarios.atualizarUsuarioNoBancoDeDados(usuarioArray[0]);
                        return null;
                    }

                    @Override
                    protected void done() {
                        // Fecha a janela de edição
                        dispose();
                    }
                };
                worker.execute();
            }
        });

        // Adiciona o botão "Salvar" ao JPanel
        panel.add(salvarButton);

        // Adiciona o JPanel ao JFrame
        add(panel);

        // Configura o JFrame
        pack();
        setSize(500, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static EditarConta getInstance(final Map<String, String>[] usuarioArray) {
        if (instance == null) {
            instance = new EditarConta(usuarioArray);
        }
        return instance;
    }
}


