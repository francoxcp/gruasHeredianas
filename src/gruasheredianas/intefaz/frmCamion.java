
package gruasheredianas.intefaz;
import gruasheredianas.camiones.model.Camion;
import gruasheredianas.camiones.dao.CamionDAO;
import gruasheredianas.conectionDB.ConexionDB;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class frmCamion extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmCamion.class.getName());

    private CamionDAO camionDAO = new CamionDAO();
    
    public frmCamion() {
        initComponents();
         cargarDatos();

   
    btnGuardar.addActionListener(e -> guardarCamion());
    }
    
    public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(() -> new frmCamion().setVisible(true));
    }

    private void guardarCamion() {
        try {
            Camion c = new Camion();
            c.setPlaca(txtPlaca.getText());
            c.setModelo(txtModelo.getText());
            c.setAnio(Integer.parseInt(txtAno.getText()));
            c.setKilometraje(Double.parseDouble(txtKilometraje.getText()));
            c.setEstado(txtEstado.getText());
            c.setConsumoCombustible(Double.parseDouble(txtConsumo.getText()));

            if (camionDAO.registrar(c)) {
                JOptionPane.showMessageDialog(this, "Camión registrado con éxito");
                limpiarCampos();
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el camión");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores válidos para Año, Kilometraje y Consumo");
        }
    }

    
private void limpiarCampos() {
    txtPlaca.setText("");
    txtModelo.setText("");
    txtKilometraje.setText("");
    txtAno.setText("");
    txtConsumo.setText("");
    txtEstado.setText("");
}

// --- Método para cargar datos en la tabla ---
 private void cargarDatos() {
        List<Camion> lista = camionDAO.listar();
        String[] columnas = {"Placa", "Modelo", "Año", "Kilometraje", "Estado", "Consumo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Camion c : lista) {
            Object[] fila = {
                c.getPlaca(),
                c.getModelo(),
                c.getAnio(),
                c.getKilometraje(),
                c.getEstado(),
                c.getConsumoCombustible()
            };
            modelo.addRow(fila);
        }

        tblCamiones.setModel(modelo);
    }
   
          

private void actualizarTablaCamiones() {
    DefaultTableModel modelo = (DefaultTableModel) tblCamiones.getModel();
    modelo.setRowCount(0); // Limpia la tabla antes de cargar datos

    String sql = "SELECT id, placa, modelo, anio, kilometraje, estado, consumo_combustible, fecha_registro FROM camiones";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Object fila[] = {
                rs.getInt("id"),
                rs.getString("placa"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                rs.getInt("kilometraje"),
                rs.getString("estado"),
                rs.getDouble("consumo_combustible"),
                rs.getTimestamp("fecha_registro")
            };
            modelo.addRow(fila);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error cargando camiones: " + e.getMessage());
    }
}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtPlaca = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        txtKilometraje = new javax.swing.JTextField();
        txtAno = new javax.swing.JTextField();
        txtConsumo = new javax.swing.JTextField();
        txtEstado = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCamiones = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro de camiones"));

        txtPlaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaActionPerformed(evt);
            }
        });

        jLabel2.setText("Placa:");

        jLabel3.setText("Modelo:");

        jLabel4.setText("Año:");

        jLabel5.setText("Kilometraje:");

        jLabel6.setText("Estado:");

        jLabel7.setText("Consumo:");

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/disquete.png"))); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar.png"))); // NOI18N
        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/eliminar.png"))); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.setAutoscrolls(true);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jLabel1.setText("Id:");

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lupa (1).png"))); // NOI18N
        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtConsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtKilometraje, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnGuardar)
                            .addComponent(btnEliminar))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtModelo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtKilometraje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtConsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnModificar))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(btnEliminar))
                .addGap(51, 51, 51))
        );

        jScrollPane1.setMaximumSize(null);
        jScrollPane1.setMinimumSize(null);

        tblCamiones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Placa", "Modelo", "Kilometraje", "Estado", "Consumo", "Fecha de Ingreso", "Año"
            }
        ));
        tblCamiones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(tblCamiones);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 861, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Menu");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Rutas");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Facturación");
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Usuario");
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPlacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
    String idStr = txtId.getText(); // ID del camión a modificar
    String placa = txtPlaca.getText();
    String modelo = txtModelo.getText();
    String anioStr = txtAno.getText();
    String kilometrajeStr = txtKilometraje.getText();
    String estado = txtEstado.getText();
    String consumoStr = txtConsumo.getText();

    // Validar campos
    if (idStr.isEmpty() || placa.isEmpty() || modelo.isEmpty() || anioStr.isEmpty()
            || kilometrajeStr.isEmpty() || estado.isEmpty() || consumoStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Todos los campos deben ser llenados.");
        return;
    }

    try {
        int id = Integer.parseInt(idStr);
        int anio = Integer.parseInt(anioStr);
        int kilometraje = Integer.parseInt(kilometrajeStr);
        double consumo = Double.parseDouble(consumoStr);

        String sql = "UPDATE camiones SET placa=?, modelo=?, anio=?, kilometraje=?, estado=?, consumo_combustible=? WHERE id=?";

        Connection con = ConexionDB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, placa);
        ps.setString(2, modelo);
        ps.setInt(3, anio);
        ps.setInt(4, kilometraje);
        ps.setString(5, estado);
        ps.setDouble(6, consumo);
        ps.setInt(7, id);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            JOptionPane.showMessageDialog(this, "Camión modificado correctamente.");
            actualizarTablaCamiones();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el camión con ese ID.");
        }

        ps.close();
        con.close();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID, Año y Kilometraje deben ser enteros; consumo decimal.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al modificar camión: " + e.getMessage());
    }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String placa = txtPlaca.getText();
    String modelo = txtModelo.getText();
    String anioStr = txtAno.getText();
    String kilometrajeStr = txtKilometraje.getText();
    String estado = txtEstado.getText();
    String consumoStr = txtConsumo.getText();

    // Validar campos obligatorios
    if (placa.isEmpty() || modelo.isEmpty() || anioStr.isEmpty() || kilometrajeStr.isEmpty() 
            || estado.isEmpty() || consumoStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Todos los campos deben ser llenados.");
        return;
    }

    try {
        int anio = Integer.parseInt(anioStr);
        int kilometraje = Integer.parseInt(kilometrajeStr);
        double consumo = Double.parseDouble(consumoStr);

        String sql = "INSERT INTO camiones (placa, modelo, anio, kilometraje, estado, consumo_combustible) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection con = ConexionDB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, placa);
        ps.setString(2, modelo);
        ps.setInt(3, anio);
        ps.setInt(4, kilometraje);
        ps.setString(5, estado);
        ps.setDouble(6, consumo);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            JOptionPane.showMessageDialog(this, "Camión agregado correctamente.");
            actualizarTablaCamiones();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo agregar el camión.");
        }

        ps.close();
        con.close();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Año y Kilometraje deben ser enteros, consumo debe ser decimal.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error con base de datos: " + e.getMessage());
    } 
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        String idStr = txtId.getText(); // ID del camión a eliminar

    if (idStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese el ID del camión a eliminar.");
        return;
    }

    try {
        int id = Integer.parseInt(idStr);

        // Confirmación antes de eliminar
        int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar el camión con ID " + id + "?", 
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return; // Usuario canceló
        }

        String sql = "DELETE FROM camiones WHERE id=?";

        Connection con = ConexionDB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            JOptionPane.showMessageDialog(this, "Camión eliminado correctamente.");
            actualizarTablaCamiones();
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el camión con ese ID.");
        }

        ps.close();
        con.close();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID debe ser un número entero.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar camión: " + e.getMessage());
    }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String[] columnas = {"ID", "Placa", "Modelo", "Año", "Kilometraje", "Estado", "Consumo", "Fecha Registro"};
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    String sql = "SELECT id, placa, modelo, anio, kilometraje, estado, consumo_combustible, fecha_registro FROM camiones";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Object[] fila = {
                rs.getInt("id"),
                rs.getString("placa"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                rs.getInt("kilometraje"),
                rs.getString("estado"),
                rs.getDouble("consumo_combustible"),
                rs.getTimestamp("fecha_registro")
            };
            modelo.addRow(fila);
        }

        tblCamiones.setModel(modelo); // tblCamiones es tu JTable

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar camiones: " + e.getMessage());
    }
    }//GEN-LAST:event_btnBuscarActionPerformed

  
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCamiones;
    private javax.swing.JTextField txtAno;
    private javax.swing.JTextField txtConsumo;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtKilometraje;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables
}
