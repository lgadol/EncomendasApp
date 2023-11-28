import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ListarContatos extends JFrame {
    JPanel painelFundo;
    JTable tabela;
    JScrollPane barraRolagem;

    Object[][] dados =
    { { "Ana Monteiro", "48 9923-7898", "ana.monteiro@gmail.com" }, { "João da Silva", "48 8890-3345",
                                                                      "joaosilva@hotmail.com" },
      { "Pedro Cascaes", "48 9870-5634", "pedrinho@gmail.com" } };

    String[] colunas = { "Nome", "Telefone", "Email" };
    private JLabel jLabel1 = new JLabel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JCheckBox jCheckBox1 = new JCheckBox();

    public ListarContatos() {
        super("Contatos");
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criaJanela() {
        painelFundo = new JPanel();
        painelFundo.setLayout(new GridLayout(1, 1));

        tabela = new JTable(dados, colunas);
        barraRolagem = new JScrollPane(tabela);

        painelFundo.add(barraRolagem);

        getContentPane().add(painelFundo);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 120);
        setVisible(true);
    }

    public static void main(String[] args) {
        ListarContatos lc = new ListarContatos();
        lc.criaJanela();
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(400, 300));
        jLabel1.setText("jLabel1");
        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jCheckBox1_actionPerformed(e);
                }
            });
        jScrollPane1.getViewport().add(jCheckBox1, null);
        this.getContentPane().add(jScrollPane1, null);
    }

    private void jCheckBox1_actionPerformed(ActionEvent e) {
    }
}
