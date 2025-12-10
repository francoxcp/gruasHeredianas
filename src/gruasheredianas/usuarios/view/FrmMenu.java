package gruasheredianas.usuarios.view;

import gruasheredianas.usuarios.model.Usuario;
import javax.swing.*;

/**
 *
 * @author franco
 */
public class FrmMenu extends JFrame{
    
     private Usuario usuario;

    public FrmMenu(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Menú Principal - " + usuario.getRol());
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuCamiones = new JMenu("Camiones");
        JMenu menuRutas = new JMenu("Rutas");
        JMenu menuInventario = new JMenu("Inventario");
        JMenu menuFinanzas = new JMenu("Finanzas");
        JMenu menuUsuarios = new JMenu("Usuarios");

        //control de roles
        if (!usuario.getRol().equals("ADMIN")) {
            menuUsuarios.setEnabled(false); //solo el admin puede gestionar usuarios
        }

        menuBar.add(menuCamiones);
        menuBar.add(menuRutas);
        menuBar.add(menuInventario);
        menuBar.add(menuFinanzas);
        menuBar.add(menuUsuarios);
        
        setJMenuBar(menuBar);
    }
    
}
