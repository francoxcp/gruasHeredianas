package gruasheredianas.grua.view;

import gruasheredianas.grua.dao.GruaDAO;
import gruasheredianas.grua.model.Grua;
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
public class FrmSeguimientoGruas extends JFrame{
    
    private JTextField txtNombre, txtUbicacion, txtEstado;
    private JButton btnGuardar, btnActualizar, btnMostrar;
    private GruaDAO gruaDAO = new GruaDAO();

    public FrmSeguimientoGruas() {
        setTitle("Seguimiento de Grúas");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblNombre = new JLabel("Nombre de Grúa:");
        lblNombre.setBounds(20, 20, 150, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(180, 20, 250, 25);
        add(txtNombre);

        JLabel lblUbicacion = new JLabel("Ubicación GPS:");
        lblUbicacion.setBounds(20, 60, 150, 25);
        add(lblUbicacion);

        txtUbicacion = new JTextField();
        txtUbicacion.setBounds(180, 60, 250, 25);
        add(txtUbicacion);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(20, 100, 150, 25);
        add(lblEstado);

        txtEstado = new JTextField();
        txtEstado.setBounds(180, 100, 250, 25);
        add(txtEstado);

        btnGuardar = new JButton("Registrar Grúa");
        btnGuardar.setBounds(20, 140, 150, 30);
        add(btnGuardar);

        btnActualizar = new JButton("Actualizar Ubicación");
        btnActualizar.setBounds(180, 140, 150, 30);
        add(btnActualizar);

        btnMostrar = new JButton("Mostrar Grúas");
        btnMostrar.setBounds(340, 140, 150, 30);
        add(btnMostrar);

        // Acciones de botones
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String ubicacion = txtUbicacion.getText();
            String estado = txtEstado.getText();

            if (nombre.isEmpty() || ubicacion.isEmpty() || estado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            Grua nuevaGrua = new Grua(nombre, ubicacion, estado);
            gruaDAO.registrarGrua(nuevaGrua);
            JOptionPane.showMessageDialog(this, "Grúa registrada exitosamente.");
        });

        btnActualizar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String nuevaUbicacion = txtUbicacion.getText();

                for (Grua grua : gruaDAO.obtenerGruas()) {
                    if (grua.getNombre().equals(nombre)) {
                        gruaDAO.actualizarUbicacion(grua.getId(), nuevaUbicacion);
                        JOptionPane.showMessageDialog(this, "Ubicación actualizada.");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Grúa no encontrada.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnMostrar.addActionListener(e -> {
            JTextArea textArea = new JTextArea();
            for (Grua grua : gruaDAO.obtenerGruas()) {
                textArea.append(grua.toString() + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Lista de Grúas", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void main(String[] args) {
        new FrmSeguimientoGruas().setVisible(true);
    }
    
}
