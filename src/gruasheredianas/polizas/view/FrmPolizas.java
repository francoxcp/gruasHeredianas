package gruasheredianas.polizas.view;

import gruasheredianas.polizas.dao.PolizaDAO;
import gruasheredianas.polizas.model.Poliza;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author franc
 */
public class FrmPolizas extends JFrame{
    
    private JTextField txtNombreSeguro, txtGruaRelacionada, txtFechaInicio, txtFechaVencimiento;
    private JButton btnRegistrar, btnMostrar, btnAlertaVencimiento;
    private PolizaDAO polizaDAO = new PolizaDAO();

    public FrmPolizas() {
        setTitle("Registro de Pólizas y Seguros");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblNombreSeguro = new JLabel("Nombre del Seguro:");
        lblNombreSeguro.setBounds(20, 20, 160, 25);
        add(lblNombreSeguro);

        txtNombreSeguro = new JTextField();
        txtNombreSeguro.setBounds(200, 20, 300, 25);
        add(txtNombreSeguro);

        JLabel lblGruaRelacionada = new JLabel("Grúa Relacionada:");
        lblGruaRelacionada.setBounds(20, 60, 160, 25);
        add(lblGruaRelacionada);

        txtGruaRelacionada = new JTextField();
        txtGruaRelacionada.setBounds(200, 60, 300, 25);
        add(txtGruaRelacionada);

        JLabel lblFechaInicio = new JLabel("Fecha de Inicio (dd/MM/yyyy):");
        lblFechaInicio.setBounds(20, 100, 160, 25);
        add(lblFechaInicio);

        txtFechaInicio = new JTextField();
        txtFechaInicio.setBounds(200, 100, 300, 25);
        add(txtFechaInicio);

        JLabel lblFechaVencimiento = new JLabel("Fecha Vencimiento (dd/MM/yyyy):");
        lblFechaVencimiento.setBounds(20, 140, 180, 25);
        add(lblFechaVencimiento);

        txtFechaVencimiento = new JTextField();
        txtFechaVencimiento.setBounds(200, 140, 300, 25);
        add(txtFechaVencimiento);

        btnRegistrar = new JButton("Registrar Póliza");
        btnRegistrar.setBounds(20, 180, 160, 30);
        add(btnRegistrar);

        btnMostrar = new JButton("Mostrar Pólizas");
        btnMostrar.setBounds(200, 180, 160, 30);
        add(btnMostrar);

        btnAlertaVencimiento = new JButton("Alerta de Vencimientos");
        btnAlertaVencimiento.setBounds(380, 180, 190, 30);
        add(btnAlertaVencimiento);

        // Acciones de botones
        btnRegistrar.addActionListener(e -> {
            try {
                String nombreSeguro = txtNombreSeguro.getText();
                String gruaRelacionada = txtGruaRelacionada.getText();
                Date fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse(txtFechaInicio.getText());
                Date fechaVencimiento = new SimpleDateFormat("dd/MM/yyyy").parse(txtFechaVencimiento.getText());

                if (nombreSeguro.isEmpty() || gruaRelacionada.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                    return;
                }

                Poliza nuevaPoliza = new Poliza(nombreSeguro, gruaRelacionada, fechaInicio, fechaVencimiento);
                polizaDAO.registrarPoliza(nuevaPoliza);
                JOptionPane.showMessageDialog(this, "Póliza registrada exitosamente.");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido.");
            }
        });

        btnMostrar.addActionListener(e -> {
            JTextArea textArea = new JTextArea();
            for (Poliza poliza : polizaDAO.obtenerTodasPolizas()) {
                textArea.append(poliza.toString() + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Todas las Pólizas", JOptionPane.INFORMATION_MESSAGE);
        });

        btnAlertaVencimiento.addActionListener(e -> {
            JTextArea textArea = new JTextArea();
            for (Poliza poliza : polizaDAO.obtenerPolizasProximasAVencer()) {
                textArea.append(poliza.toString() + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Pólizas Próximas a Vencer", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void main(String[] args) {
        new FrmPolizas().setVisible(true);
    }
    
}
