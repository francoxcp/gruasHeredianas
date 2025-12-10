package gruasheredianas.camiones.view;

import gruasheredianas.camiones.model.Camion;
import gruasheredianas.camiones.dao.CamionDAO;
import javax.swing.*;

/**
 *
 * @author franco
 */
public class FrmCamion extends JFrame {
    
    private JTextField txtPlaca, txtModelo, txtAnio, txtKm, txtEstado, txtConsumo;
    private JButton btnGuardar;
    private CamionDAO camionDAO = new CamionDAO();

    public FrmCamion() {
        setTitle("Registro de Camiones");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setBounds(20, 20, 120, 25);
        add(lblPlaca);

        txtPlaca = new JTextField();
        txtPlaca.setBounds(150, 20, 200, 25);
        add(txtPlaca);

        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setBounds(20, 60, 120, 25);
        add(lblModelo);

        txtModelo = new JTextField();
        txtModelo.setBounds(150, 60, 200, 25);
        add(txtModelo);

        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setBounds(20, 100, 120, 25);
        add(lblAnio);

        txtAnio = new JTextField();
        txtAnio.setBounds(150, 100, 200, 25);
        add(txtAnio);

        JLabel lblKm = new JLabel("Kilometraje:");
        lblKm.setBounds(20, 140, 120, 25);
        add(lblKm);

        txtKm = new JTextField();
        txtKm.setBounds(150, 140, 200, 25);
        add(txtKm);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(20, 180, 120, 25);
        add(lblEstado);

        txtEstado = new JTextField();
        txtEstado.setBounds(150, 180, 200, 25);
        add(txtEstado);

        JLabel lblConsumo = new JLabel("Consumo:");
        lblConsumo.setBounds(20, 220, 120, 25);
        add(lblConsumo);

        txtConsumo = new JTextField();
        txtConsumo.setBounds(150, 220, 200, 25);
        add(txtConsumo);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 270, 120, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            Camion c = new Camion();
            c.setPlaca(txtPlaca.getText());
            c.setModelo(txtModelo.getText());
            c.setAnio(Integer.parseInt(txtAnio.getText()));
            c.setKilometraje(Double.parseDouble(txtKm.getText()));
            c.setEstado(txtEstado.getText());
            c.setConsumoCombustible(Double.parseDouble(txtConsumo.getText()));

            if (camionDAO.registrar(c)) {
                JOptionPane.showMessageDialog(this, "Camión registrado con éxito");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar");
            }
        });
    }

    public static void main(String[] args) {
        new FrmCamion().setVisible(true);
    }
    
}
