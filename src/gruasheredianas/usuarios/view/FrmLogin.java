package gruasheredianas.usuarios.view;

import gruasheredianas.usuarios.dao.UsuarioDAO;
import gruasheredianas.usuarios.model.Usuario;
import javax.swing.*;
/**
 *
 * @author franco
 */
public class FrmLogin extends JFrame  {
    
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public FrmLogin() {
        setTitle("Login - Grúas Heredianas Gimome S.A.");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 50, 100, 25);
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(150, 50, 180, 25);
        add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(50, 90, 100, 25);
        add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(150, 90, 180, 25);
        add(txtContrasena);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(150, 140, 150, 30);
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String contrasena = new String(txtContrasena.getPassword());

            Usuario u = usuarioDAO.login(usuario, contrasena);
            if (u != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");
                dispose();
                // Aquí según rol abrimos menú principal
                new FrmMenu(u).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }
        });
    }

    public static void main(String[] args) {
        new FrmLogin().setVisible(true);
    }
    
}
