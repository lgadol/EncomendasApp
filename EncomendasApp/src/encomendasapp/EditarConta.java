package encomendasapp;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.sql.PreparedStatement;

import java.sql.Connection;

import java.sql.ResultSet;

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
    private final MinhaConta minhaConta;
    private JFrame janelaPrincipal;
    private JFrame janelaEncomendasApp;
    private String novoNome;
    private String novoTelefone;
    private String novoEmail;
    private String novaCidade;
    private String novoEstado;
    private String novoEndereco;
    private JComboBox<String> estadoBox;

    String[] estados =
    { "", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
      "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" };

    public EditarConta(final Map<String, String> usuario, final MinhaConta minhaConta, JFrame janelaPrincipal,
                       JFrame janelaEncomendasApp) {

        this.usuarioArray = new Map[] { usuario };
        this.gerenciadorDeUsuarios = new GerenciadorDeUsuarios();
        this.minhaConta = minhaConta;
        this.janelaPrincipal = janelaPrincipal;
        final JFrame janelaPrincipalFinal = janelaPrincipal;
        this.janelaEncomendasApp = janelaEncomendasApp;
        final JFrame janelaEncomendasAppFinal = janelaEncomendasApp;

        // Cria JTextFields para a edição dos dados do usuário
        adminField = new JTextField(usuarioArray[0].get("admin"));
        adminField.setEditable(false);
        nomeUsuarioField = new JTextField(usuarioArray[0].get("nome"));
        telefoneField = new JTextField(usuarioArray[0].get("telefone"));
        emailField = new JTextField(usuarioArray[0].get("email"));
        cidadeField = new JTextField(usuarioArray[0].get("cidade"));
        estadoBox = new JComboBox<>(estados);
        estadoBox.setSelectedItem(usuarioArray[0].get("estado"));
        enderecoField = new JTextField(usuarioArray[0].get("endereco"));
        senhaField = new JPasswordField();

        // Cria um JPanel com um GridLayout
        JPanel panel = new JPanel(new GridLayout(9, 2));

        // Adiciona os JTextFields ao JPanel
        panel.add(new JLabel("Admin:"));
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
        panel.add(estadoBox);
        panel.add(new JLabel("Endereço:"));
        panel.add(enderecoField);
        panel.add(new JLabel("Confirme com sua Senha:"));
        panel.add(senhaField);

        // Cria um botão "Salvar"
        JButton salvarButton = new JButton("Salvar");
        salvarButton.setBackground(new Color(0, 204, 51));
        ImageIcon iconSalvar =
            new ImageIcon("C:\\Users\\PedroGado\\Documents\\Java Dev\\My Dev\\EncomendasApp\\lib\\icons\\salvar2.png");

        Image imgSalvar = iconSalvar.getImage();
        Image resizedImgSalvar = imgSalvar.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);

        ImageIcon resizedIconSalvar = new ImageIcon(resizedImgSalvar);
        salvarButton.setIcon(resizedIconSalvar);

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Atualiza os dados do usuário
                novoNome = nomeUsuarioField.getText().toUpperCase();
                novoTelefone = telefoneField.getText();
                novoEmail = emailField.getText();
                novaCidade = cidadeField.getText().toUpperCase();
                novoEstado = estadoBox.getSelectedItem().toString();
                novoEndereco = enderecoField.getText().toUpperCase();

                // Verifica a senha
                String senha = new String(senhaField.getPassword()).trim();

                if (!senha.equals(usuarioArray[0].get("senha"))) {
                    JOptionPane.showMessageDialog(null, "Senha incorreta!");
                    return;
                }

                // Verifica se todos os campos estão preenchidos
                if (novoNome.isEmpty() || novoTelefone.isEmpty() || novoEmail.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos!");
                    return;
                }

                // Verifica o formato do telefone
                if (!novoTelefone.matches("^\\d{2} \\d{8,9}$")) {
                    JOptionPane.showMessageDialog(null, "Formato de telefone inválido!");
                    return;
                }

                // Verifica o formato do email
                if (!novoEmail.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$")) {
                    JOptionPane.showMessageDialog(null, "Formato de email inválido!");
                    return;
                }

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Connection conn = DataBaseConnection.getConnection();

                        // Verificar se o email já existe, se foi alterado
                        if (!novoEmail.equals(usuarioArray[0].get("email"))) {
                            PreparedStatement checkEmailStmt =
                                conn.prepareStatement("SELECT * FROM clientes WHERE email = ?");
                            checkEmailStmt.setString(1, novoEmail);
                            ResultSet checkEmailRs = checkEmailStmt.executeQuery();
                            if (checkEmailRs.next()) {
                                JOptionPane.showMessageDialog(null,
                                                              "Este email já está sendo usado por outro cliente.");
                                return null;
                            }
                            checkEmailRs.close();
                            checkEmailStmt.close();
                        }

                        // Verificar se o telefone já existe, se foi alterado
                        if (!novoTelefone.equals(usuarioArray[0].get("telefone"))) {
                            PreparedStatement checkTelefoneStmt =
                                conn.prepareStatement("SELECT * FROM clientes WHERE telefone = ?");
                            checkTelefoneStmt.setString(1, novoTelefone);
                            ResultSet checkTelefoneRs = checkTelefoneStmt.executeQuery();
                            if (checkTelefoneRs.next()) {
                                JOptionPane.showMessageDialog(null,
                                                              "Este telefone já está sendo usado por outro cliente.");
                                return null;
                            }
                            checkTelefoneRs.close();
                            checkTelefoneStmt.close();
                        }

                        // Atualiza os dados do usuário no banco de dados
                        usuarioArray[0].put("nome", novoNome);
                        usuarioArray[0].put("telefone", novoTelefone);
                        usuarioArray[0].put("email", novoEmail);
                        usuarioArray[0].put("cidade", novaCidade);
                        usuarioArray[0].put("estado", novoEstado);
                        usuarioArray[0].put("endereco", novoEndereco);
                        gerenciadorDeUsuarios.atualizarUsuarioNoBancoDeDados(usuarioArray[0]);
                        return null;
                    }

                    @Override
                    protected void done() {
                        // Fecha a janela de edição
                        dispose();

                        // Atualiza os campos na janela MinhaConta
                        minhaConta.atualizarCampos(usuarioArray[0]);

                        JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");

                        // Habilita a janela MinhaConta
                        minhaConta.setEnabled(true);

                        // Define a instância de EditarConta como null na janela MinhaConta
                        setEditarContaInMinhaContaNullOnClose();
                    }
                };
                worker.execute();
            }
        });

        // Adiciona o botÃ£o "Salvar" ao JPanel
        panel.add(salvarButton);

        // Adiciona o JPanel ao JFrame
        add(panel);

        // Configura o JFrame
        pack();
        setSize(500, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                minhaConta.setEditarContaNull();
                if (janelaPrincipalFinal != null) {
                    janelaPrincipalFinal.setEnabled(true);
                }
                if (janelaEncomendasAppFinal != null) {
                    janelaEncomendasAppFinal.setEnabled(true);
                }
                instance = null; // Adicione esta linha
            }
        });
        setEditarContaInMinhaContaNullOnClose();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (janelaPrincipal != null) {
            janelaPrincipal.setEnabled(!b);
        }
        if (janelaEncomendasApp != null) {
            janelaEncomendasApp.setEnabled(!b);
        }
    }

    public static EditarConta getInstance(final Map<String, String> usuario, MinhaConta minhaConta,
                                          JFrame janelaPrincipal, JFrame janelaEncomendasApp) {
        if (instance == null || !instance.isShowing()) {
            instance = new EditarConta(usuario, minhaConta, janelaPrincipal, janelaEncomendasApp);
        }
        return instance;
    }

    public void atualizarUsuarioNoBancoDeDados(Map<String, String> usuario) {
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql =
                "UPDATE clientes SET nome = ?, telefone = ?, email = ?, cidade = ?, estado = ?, endereco = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario.get("nome"));
            pstmt.setString(2, usuario.get("telefone"));
            pstmt.setString(3, usuario.get("email"));
            pstmt.setString(4, usuario.get("cidade"));
            pstmt.setString(5, usuario.get("estado"));
            pstmt.setString(6, usuario.get("endereco"));
            pstmt.setInt(7, Integer.parseInt(usuario.get("id")));
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEditarContaInMinhaContaNullOnClose() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                minhaConta.setEditarContaNull();
            }
        });
    }
}


