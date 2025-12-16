package gruasheredianas.inventario.view;


import gruasheredianas.inventario.dao.InventarioDAO;
import gruasheredianas.inventario.model.Item;
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
public class FrmInventario extends JFrame{
    
    private JTextField txtNombre, txtTipo, txtCantidad;
    private JButton btnGuardar, btnMostrar;
    private InventarioDAO inventarioDAO = new InventarioDAO();

    public FrmInventario() {
        setTitle("Registro de Inventario");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 120, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 20, 200, 25);
        add(txtNombre);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 60, 120, 25);
        add(lblTipo);

        txtTipo = new JTextField();
        txtTipo.setBounds(150, 60, 200, 25);
        add(txtTipo);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(20, 100, 120, 25);
        add(lblCantidad);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(150, 100, 200, 25);
        add(txtCantidad);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 140, 120, 30);
        add(btnGuardar);

        btnMostrar = new JButton("Mostrar Inventario");
        btnMostrar.setBounds(150, 180, 120, 30);
        add(btnMostrar);

        // Acción para guardar un artículo
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String tipo = txtTipo.getText();
                int cantidad = Integer.parseInt(txtCantidad.getText());

                if (nombre.isEmpty() || tipo.isEmpty() || cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "Ingrese datos válidos.");
                    return;
                }

                Item item = new Item(nombre, tipo, cantidad);
                inventarioDAO.addItem(item);
                JOptionPane.showMessageDialog(this, "Artículo registrado con éxito");
                limpiarCampos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad debe ser un número.");
            }
        });

        // Acción para mostrar el inventario completo
        btnMostrar.addActionListener(e -> {
            JTextArea textArea = new JTextArea();
            for (Item item : inventarioDAO.getInventario()) {
                textArea.append(item.toString() + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Inventario", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtTipo.setText("");
        txtCantidad.setText("");
    }

    public static void main(String[] args) {
        new FrmInventario().setVisible(true);
    }
    
}
