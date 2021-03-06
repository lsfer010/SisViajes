/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sis_viajes;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lsfer
 */
public class Mod_Operadores extends javax.swing.JFrame {

    /**
     * Creates new form Mod_Operadores
     */
    public Mod_Operadores() {
        initComponents();
        actualizarTabla();
        if(Menu_Principal.necesita_ayudas){
            lAyudaOperadores.setVisible(true);
            lAyudaOperadores.setText("Consejo: ubique el cursor en el campo en blanco, escriba el nombre de un operador y preisone enter para agregarlo");
        } else {
            lAyudaOperadores.setVisible(false);
        } 
        
        this.setTitle("Módulo Operadores");
        this.setIconImage(new ImageIcon(getClass().getResource("icono.png")).getImage());
    }
    
    String id_operador_actual="";
    
    /*PARA LISTAR OPERADORES EN VECTORES SIN USAR LA TABLA:
    * PONER VARIABLES GLOBALES EL VECTOR Y LA CANTIDA DE ID
    * HACER LAS MODIFICACIONES ADECUADAS, QUITANDO LAS COLUMNAS DE ID, EN LAS CONSULTAS AUMENTAR LA VARIABLE QUE CUENTA FILAS
    * DESPUES, INSTANCIAR EL VECTOR CON ESA CANTIDAD Y LLAMAR AL METODO QUE HACE CONSULTA DE LOS ID PARA LISTARLOS EN VECTOR
    * EN EL EVENTO DE SELECCIONAR*/
    private int[] id_operadores;
    private int cantidad_id=0;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        bSalir = new javax.swing.JButton();
        bAgregar = new javax.swing.JButton();
        bEditar = new javax.swing.JButton();
        bBuscar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lAyudaOperadores = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tEmpresa = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTOperadores = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jToolBar1.setBackground(new java.awt.Color(180, 52, 29));
        jToolBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        bSalir.setBackground(new java.awt.Color(234, 177, 100));
        bSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sis_viajes/ico_salir.png"))); // NOI18N
        bSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bSalir.setFocusable(false);
        bSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bSalirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bSalirMouseExited(evt);
            }
        });
        bSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSalirActionPerformed(evt);
            }
        });
        jToolBar1.add(bSalir);

        bAgregar.setBackground(new java.awt.Color(234, 177, 100));
        bAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sis_viajes/ico_agregar.png"))); // NOI18N
        bAgregar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bAgregar.setFocusable(false);
        bAgregar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bAgregar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bAgregarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bAgregarMouseExited(evt);
            }
        });
        bAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAgregarActionPerformed(evt);
            }
        });
        jToolBar1.add(bAgregar);

        bEditar.setBackground(new java.awt.Color(234, 177, 100));
        bEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sis_viajes/ico_editar.png"))); // NOI18N
        bEditar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bEditar.setFocusable(false);
        bEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bEditarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bEditarMouseExited(evt);
            }
        });
        bEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEditarActionPerformed(evt);
            }
        });
        jToolBar1.add(bEditar);

        bBuscar.setBackground(new java.awt.Color(234, 177, 100));
        bBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sis_viajes/ico_buscar.png"))); // NOI18N
        bBuscar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bBuscar.setFocusable(false);
        bBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bBuscar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bBuscarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bBuscarMouseExited(evt);
            }
        });
        bBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBuscarActionPerformed(evt);
            }
        });
        jToolBar1.add(bBuscar);

        jButton1.setBackground(new java.awt.Color(234, 177, 100));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sis_viajes/ico_ayuda.png"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setPreferredSize(new java.awt.Dimension(31, 31));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lAyudaOperadores.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 10)); // NOI18N
        jPanel1.add(lAyudaOperadores);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 580, 32));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 481, -1, -1));

        jPanel4.setBackground(new java.awt.Color(234, 177, 100));

        jLabel1.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel1.setText("Empresa:");

        tEmpresa.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        tEmpresa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tEmpresaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tEmpresaFocusLost(evt);
            }
        });
        tEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tEmpresaKeyPressed(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTOperadores.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 12)); // NOI18N
        jTOperadores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nombre Compañia", "idOperadores"
            }
        ));
        jTOperadores.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTOperadores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTOperadoresMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTOperadoresMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTOperadoresMouseExited(evt);
            }
        });
        jScrollPane2.setViewportView(jTOperadores);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 560, 420));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 580, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_bSalirActionPerformed
    
    private void agregar() {
        if(!tEmpresa.getText().equals("")){
            String empresa = tEmpresa.getText();

             Altas operacion = new Altas();

            operacion.alta(empresa);
        
            actualizarTabla();
            borrarDatos();
        } else {
            JOptionPane.showMessageDialog(null,"Introduzca un nombre para dar de alta una empresa","Operación fallida",JOptionPane.WARNING_MESSAGE);
        }
    } 
    
    private void bAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAgregarActionPerformed
        this.agregar();
    }//GEN-LAST:event_bAgregarActionPerformed
    
    private void borrarDatos() {
        tEmpresa.setText("");
        actualizarTabla();
        id_operador_actual="";
    }
    
    private void listarID() {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT idOperadores FROM operadores";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
          
            int contador_aux = 0; 
            while(rs.next()) {
                
                    id_operadores[contador_aux] = Integer.parseInt(rs.getObject(1).toString());
                    contador_aux++;
            }
            
            con.close();
        } catch (SQLException e) {}
    }
    
    private void listarID(String where) {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT idOperadores FROM operadores" + where;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
          
            int contador_aux = 0; 
            while(rs.next()) {
                
                    id_operadores[contador_aux] = Integer.parseInt(rs.getObject(1).toString());
                    contador_aux++;
            }
            
            con.close();
        } catch (SQLException e) {}
    }
    
    private void actualizarTabla() {
        cantidad_id = 0;
        try {
            
            DefaultTableModel modelo = new CustomTableModel();
            jTOperadores.setModel(modelo);
            
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT Nombre FROM operadores";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            ResultSetMetaData rsMd = rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            modelo.addColumn("Nombre Compañia");
            
              jTOperadores.getColumnModel().getColumn(0).setPreferredWidth(550);  
             
            while(rs.next()) {
                Object[] filas = new Object[cantidadColumnas]; //ya que la tabla trabaja con objetos, metemos todo en objetos
                
                for (int i=0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                cantidad_id++;
                
                modelo.addRow(filas);
            }
            
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Ocurrio un erro al conectarse con la base de datos. Contacte con el administrador del sistema para resolver el problema","Operación fallida",JOptionPane.ERROR_MESSAGE);
        }
        
        id_operadores = new int[cantidad_id];
        
        this.listarID();
         
    }
    
    private void bEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEditarActionPerformed
        if (!id_operador_actual.equals("")) {
            if(!tEmpresa.getText().trim().equals("")){
               Conexion connection = new Conexion();
               Connection con = connection.getConexion();
               PreparedStatement ps = null;

               try {
                    ps = con.prepareStatement("UPDATE operadores SET nombre=? WHERE idOperadores = ?");
                    ps.setString(1, tEmpresa.getText());
                    ps.setInt(2, Integer.parseInt(id_operador_actual));
                
                    int res = ps.executeUpdate();

                    if(res > 0) {
                        JOptionPane.showMessageDialog(null,"¡Nombre del operador actualizado!","Operación exitosa",JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,"Ocurrio un error al modificar el nombre de la empresa, contacte con"
                                + "el administrador del sistema para solucionar el problema","Operación fallida",
                                JOptionPane.ERROR_MESSAGE);
                    }

                con.close();
                actualizarTabla();
                borrarDatos();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,"Error: " + e.toString());
                } 
            } else {
               JOptionPane.showMessageDialog(null,"Se detectó que el campo del nombre de la empresa está vacio, favor de introducir el nombre de la empresa.","Operación fallida",JOptionPane.WARNING_MESSAGE); 
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un elemento de la tabla para poder actualizar el nombre de la empresa.","Operación fallida",JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_bEditarActionPerformed

    private void bBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBuscarActionPerformed
        cantidad_id=0;
        String texto = tEmpresa.getText();
        String where = "";
        id_operador_actual="";

        if ("".equals(texto)) {
            where = "";
        } else {
            where = " WHERE nombre LIKE '%" + texto + "%'";
        }

        try {
            DefaultTableModel modelo = new CustomTableModel();
            jTOperadores.setModel(modelo);

            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();

            String sql = "SELECT Nombre FROM operadores" + where;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetMetaData rsMd = rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            modelo.addColumn("Nombre Compañia");

            jTOperadores.getColumnModel().getColumn(0).setPreferredWidth(550);
            

            while(rs.next()) {
                Object[] filas = new Object[cantidadColumnas]; //ya que la tabla trabaja con objetos, metemos todo en objetos

                for (int i=0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                cantidad_id++;
                
                modelo.addRow(filas);
            }

            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Ocurrio un error al buscar los datos. Contacte con el administrador del sistema para solucionar el problema","Operación fallida",JOptionPane.ERROR_MESSAGE);
        }
        
        id_operadores = new int[cantidad_id];
        
        this.listarID(where);
        
    }//GEN-LAST:event_bBuscarActionPerformed

    private void jTOperadoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTOperadoresMouseClicked
        try {
            int Fila = jTOperadores.getSelectedRow();

            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            id_operador_actual = id_operadores[Fila]+"";

            String sql = "SELECT Nombre FROM operadores WHERE idOperadores = " + id_operador_actual;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                tEmpresa.setText(rs.getString(1));
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Ocurrio un error al conectarse con la base de datos al seleccionar el registro. Contacte con el administrador del sistema para solucionar el problema","Operación fallida",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jTOperadoresMouseClicked

    private void tEmpresaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tEmpresaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.agregar();
        }
    }//GEN-LAST:event_tEmpresaKeyPressed

    private void tEmpresaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tEmpresaFocusGained
        lAyudaOperadores.setText("Escriba el nombre de un operador que desee agregar o buscar.");
    }//GEN-LAST:event_tEmpresaFocusGained

    private void tEmpresaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tEmpresaFocusLost
        lAyudaOperadores.setText("");
    }//GEN-LAST:event_tEmpresaFocusLost

    private void jTOperadoresMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTOperadoresMouseEntered
        lAyudaOperadores.setText("Seleccione un registro para cargar el nombre del operador al campo en blanco y poder editarlo.");
    }//GEN-LAST:event_jTOperadoresMouseEntered

    private void jTOperadoresMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTOperadoresMouseExited
        lAyudaOperadores.setText("");
    }//GEN-LAST:event_jTOperadoresMouseExited

    private void bSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSalirMouseEntered
        lAyudaOperadores.setText("Salir al menú principal");
    }//GEN-LAST:event_bSalirMouseEntered

    private void bSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSalirMouseExited
        lAyudaOperadores.setText("");
    }//GEN-LAST:event_bSalirMouseExited

    private void bAgregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAgregarMouseEntered
        lAyudaOperadores.setText("Agregar un operador (se tomará el que se encuentra en el campo en blanco)");
    }//GEN-LAST:event_bAgregarMouseEntered

    private void bAgregarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAgregarMouseExited
        lAyudaOperadores.setText("");
    }//GEN-LAST:event_bAgregarMouseExited

    private void bEditarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bEditarMouseEntered
        lAyudaOperadores.setText("Editar un operador (se tomará el que se encuentra en el campo en blanco)");
    }//GEN-LAST:event_bEditarMouseEntered

    private void bEditarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bEditarMouseExited
        lAyudaOperadores.setText("");
    }//GEN-LAST:event_bEditarMouseExited

    private void bBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseEntered
        lAyudaOperadores.setText("Buscar un operador (deje el campo de búsqueda en blanco para listar todo nuevamente)");
    }//GEN-LAST:event_bBuscarMouseEntered

    private void bBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseExited
        lAyudaOperadores.setText("");
    }//GEN-LAST:event_bBuscarMouseExited

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Ayuda_Operadores ventana_ayuda_op = new Ayuda_Operadores();
        ventana_ayuda_op.setVisible(true);
        ventana_ayuda_op.setLocationRelativeTo(null);
        ventana_ayuda_op.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Mod_Operadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mod_Operadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mod_Operadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mod_Operadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mod_Operadores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAgregar;
    private javax.swing.JButton bBuscar;
    private javax.swing.JButton bEditar;
    private javax.swing.JButton bSalir;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTOperadores;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lAyudaOperadores;
    private javax.swing.JTextField tEmpresa;
    // End of variables declaration//GEN-END:variables
}
