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
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lsfer
 */
public class Mod_Reservaciones extends javax.swing.JFrame {

    /**
     * Creates new form Mod_Reservaciones
     */
    public Mod_Reservaciones() {
        initComponents();
        actualizarTabla();
        if(Menu_Principal.necesita_ayudas){
            lAyudaReservaciones.setVisible(true);
            lAyudaReservaciones.setText("Consejo: ubique el cursor en los campos -Precio- o -No. factura operador- y presione la tecla ENTER para agregar una reservación");
        } else {
            lAyudaReservaciones.setVisible(false);
        } 
        this.setTitle("Módulo Reservaciones");
        this.setIconImage(new ImageIcon(getClass().getResource("icono.png")).getImage());
    }
    
    private int tamaño_columnas = 70;
    private int[] ID_Cliente, ID_Operador, id_clientes_lista, id_operadores_lista;
    private int cantidad_id = 0;
    private String id_cliente_actual="", id_operador_actual="", clave_reservacion_actual="";
    
    private void listarID() {
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT reservaciones.Clientes_ID_Cliente, reservaciones.Operadores_idOperadores FROM reservaciones "
                    + "INNER JOIN clientes ON reservaciones.Clientes_ID_Cliente = clientes.ID_Cliente "
                    + "INNER JOIN operadores ON reservaciones.Operadores_idOperadores = operadores.idOperadores";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
          
            int contador_aux = 0; 
            while(rs.next()) {
                
                    id_clientes_lista[contador_aux] = Integer.parseInt(rs.getObject(1).toString());
                    id_operadores_lista[contador_aux] = Integer.parseInt(rs.getObject(2).toString());
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
            
            String sql = "SELECT reservaciones.Clientes_ID_Cliente, reservaciones.Operadores_idOperadores FROM reservaciones "
                    + "INNER JOIN clientes ON reservaciones.Clientes_ID_Cliente = clientes.ID_Cliente "
                    + "INNER JOIN operadores ON reservaciones.Operadores_idOperadores = operadores.idOperadores" + where;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
          
            int contador_aux = 0; 
            while(rs.next()) {
                
                   id_clientes_lista[contador_aux] = Integer.parseInt(rs.getObject(1).toString());
                   id_operadores_lista[contador_aux] = Integer.parseInt(rs.getObject(2).toString());
                   contador_aux++;
            }
            
            con.close();
        } catch (SQLException e) {}
    }
    
    private void actualizarTabla() {
        //------------------------ SECCION PARA ACTUALIZAR TABLA ----------------------
        try {    
            cantidad_id = 0;
            DefaultTableModel modelo = new CustomTableModel();
            jTReservaciones.setModel(modelo);
            
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT reservaciones.Clave_Reservacion, operadores.Nombre, clientes.Apellido_Paterno, "
                    + "clientes.Apellido_Materno, clientes.Nombre, clientes.Compañia, reservaciones.Forma_Pago_Cliente, "
                    + "reservaciones.Precio, reservaciones.Req_Factura, reservaciones.No_Factura_Cliente, reservaciones.No_Factura_Operador,"
                    + " reservaciones.Forma_Pago_Vendedor "
                    + "FROM reservaciones " +
            "INNER JOIN clientes ON reservaciones.Clientes_ID_Cliente = clientes.ID_Cliente " +
            "INNER JOIN operadores ON reservaciones.Operadores_idOperadores = operadores.idOperadores";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            ResultSetMetaData rsMd = rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            modelo.addColumn("Clave Reservacion");
            modelo.addColumn("Operador");
            modelo.addColumn("Ap. Paterno Cliente");
            modelo.addColumn("Ap. Materno Cliente");
            modelo.addColumn("Nombre Cliente");
            modelo.addColumn("Compañia");
            modelo.addColumn("Forma de Pago Cliente");
            modelo.addColumn("Precio");
            modelo.addColumn("¿Requiere Factura?");
            modelo.addColumn("No. Fac. Cliente");
            modelo.addColumn("No. Fac. Operador");
            modelo.addColumn("Forma de Pago Vendedor");
            
            
            for (int i=0; i <= 11; i++) {
              jTReservaciones.getColumnModel().getColumn(i).setPreferredWidth(tamaño_columnas);
            }
             
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
            JOptionPane.showMessageDialog(null, "Error:" + e);
        }
        
        id_clientes_lista = new int[cantidad_id];
        id_operadores_lista = new int[cantidad_id];
        
        this.listarID();
        
        // ----------------- SECCION ACTUALIZAR COMBOBOX REGISTRO Y CONSULTA DE CLIENTES ------------------------
        String Ap_Paterno, Ap_Materno, Nombre;
        int contador_filas=0;
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT Apellido_Paterno, Apellido_Materno, Nombre FROM clientes";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            try{
                comboClientes.removeAllItems();
            } catch (Exception e) {}
            
            try{
                comboClientesMod.removeAllItems();
            } catch (Exception e) {}
            
            while(rs.next()) {
                
                contador_filas++;
                Ap_Paterno = rs.getString(1);
                Ap_Materno = rs.getString(2);
                Nombre = rs.getString(3);
                
                comboClientes.addItem(Ap_Paterno + " " + Ap_Materno + " " + Nombre);
                comboClientesMod.addItem(Ap_Paterno + " " + Ap_Materno + " " + Nombre);
            }
            
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error:" + e);
        }
        
        ID_Cliente = new int[contador_filas];
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT ID_Cliente FROM clientes";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            int contador_auxiliar = 0;
            while(rs.next()) {
                
                ID_Cliente[contador_auxiliar] = rs.getInt(1);
                contador_auxiliar++;
                
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error:" + e);
        }
        
        // ----------------- SECCION ACTUALIZAR COMBOBOX REGISTRO Y CONSULTA DE OPERADORES ------------------------
        contador_filas = 0;
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT Nombre FROM operadores";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            try{
                comboOperadores.removeAllItems();
                
            } catch (Exception e) {}
            
            try { 
                comboOperadoresMod.removeAllItems();
            } catch (Exception e) {}
            
            while(rs.next()) {
                
                contador_filas++;
                comboOperadores.addItem(rs.getString(1));
                comboOperadoresMod.addItem(rs.getString(1));
            }
            
            con.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error:" + e);
        }
        
        ID_Operador = new int[contador_filas];
        try {
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT idOperadores FROM operadores";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            int contador_auxiliar = 0;
            while(rs.next()) {
                
                ID_Operador[contador_auxiliar] = rs.getInt(1);
                contador_auxiliar++;
                
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error:" + e);
        }
        
    }
    
    private void borrarDatos() {
        
        tClaveReservacion.setText("");
        comboOperadores.setSelectedIndex(0);
        comboClientes.setSelectedIndex(0);
        comboFormaPago.setSelectedIndex(0);
        tOtraFormaPago.setText("");
        tOtraFormaPago.setEnabled(false);
        tPrecio.setText("");
        comboReqFactura.setSelectedIndex(0);
        tNoFacturaCliente.setText("");
        tNoFacturaCliente.setEnabled(false);
        tNoFacturaOperador.setText("");
        comboFormaPagoVendedor.setSelectedIndex(0);
        
        tClaveReservacionMod.setText("");
        comboOperadoresMod.setSelectedIndex(0);
        comboClientesMod.setSelectedIndex(0);
        comboFormaPagoMod.setSelectedIndex(0);
        tOtraFormaPagoMod.setText("");
        tOtraFormaPagoMod.setEnabled(false);
        tPrecioMod.setText("");
        comboReqFacturaMod.setSelectedIndex(0);
        tNoFacturaClienteMod.setText("");
        tNoFacturaClienteMod.setEnabled(false);
        tNoFacturaOperadorMod.setText("");
        comboFormaPagoVendedorMod.setSelectedIndex(0);
        
        id_cliente_actual = "";
        id_operador_actual = "";
        clave_reservacion_actual = "";
        
    }
    
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
        bEliminar = new javax.swing.JButton();
        bEditar = new javax.swing.JButton();
        bBuscar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bBorrar = new javax.swing.JButton();
        tClaveReservacion = new javax.swing.JTextField();
        tPrecio = new javax.swing.JTextField();
        comboClientes = new javax.swing.JComboBox<>();
        comboFormaPago = new javax.swing.JComboBox<>();
        comboReqFactura = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        comboFormaPagoVendedor = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        comboOperadores = new javax.swing.JComboBox<>();
        tOtraFormaPago = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tNoFacturaCliente = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        tNoFacturaOperador = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        comboBuscarPorEntidad = new javax.swing.JComboBox<>();
        tBusqueda = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tClaveReservacionMod = new javax.swing.JTextField();
        comboClientesMod = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        comboFormaPagoMod = new javax.swing.JComboBox<>();
        tPrecioMod = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        comboReqFacturaMod = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        comboOperadoresMod = new javax.swing.JComboBox<>();
        tOtraFormaPagoMod = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        tNoFacturaClienteMod = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        tNoFacturaOperadorMod = new javax.swing.JTextField();
        bLimpiar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTReservaciones = new javax.swing.JTable();
        comboFormaPagoVendedorMod = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lAyudaReservaciones = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        setPreferredSize(new java.awt.Dimension(787, 692));
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
        bSalir.setMaximumSize(new java.awt.Dimension(30, 30));
        bSalir.setMinimumSize(new java.awt.Dimension(30, 30));
        bSalir.setPreferredSize(new java.awt.Dimension(33, 33));
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
        bAgregar.setMaximumSize(new java.awt.Dimension(30, 30));
        bAgregar.setMinimumSize(new java.awt.Dimension(30, 30));
        bAgregar.setPreferredSize(new java.awt.Dimension(33, 33));
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

        bEliminar.setBackground(new java.awt.Color(234, 177, 100));
        bEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sis_viajes/ico_eliminar.png"))); // NOI18N
        bEliminar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bEliminar.setEnabled(false);
        bEliminar.setFocusable(false);
        bEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bEliminar.setMaximumSize(new java.awt.Dimension(30, 30));
        bEliminar.setMinimumSize(new java.awt.Dimension(30, 30));
        bEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bEliminarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bEliminarMouseExited(evt);
            }
        });
        bEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(bEliminar);

        bEditar.setBackground(new java.awt.Color(234, 177, 100));
        bEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sis_viajes/ico_editar.png"))); // NOI18N
        bEditar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bEditar.setEnabled(false);
        bEditar.setFocusable(false);
        bEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bEditar.setMaximumSize(new java.awt.Dimension(30, 30));
        bEditar.setMinimumSize(new java.awt.Dimension(30, 30));
        bEditar.setPreferredSize(new java.awt.Dimension(30, 30));
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
        bBuscar.setEnabled(false);
        bBuscar.setFocusable(false);
        bBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bBuscar.setMaximumSize(new java.awt.Dimension(30, 30));
        bBuscar.setMinimumSize(new java.awt.Dimension(30, 30));
        bBuscar.setPreferredSize(new java.awt.Dimension(30, 30));
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
        jButton1.setMinimumSize(new java.awt.Dimension(15, 15));
        jButton1.setPreferredSize(new java.awt.Dimension(30, 30));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        getContentPane().add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 781, -1));

        jTabbedPane1.setBackground(new java.awt.Color(234, 177, 100));
        jTabbedPane1.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 12)); // NOI18N
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(234, 177, 100));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel1.setText("Clave Reservacion:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, -1, -1));

        jLabel2.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel2.setText("Cliente:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, 30));

        jLabel3.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel3.setText("Forma de Pago:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, -1));

        jLabel4.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel4.setText("Precio:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        jLabel5.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel5.setText("¿Requiere factura?:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

        bBorrar.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        bBorrar.setText("Limpiar");
        bBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBorrarActionPerformed(evt);
            }
        });
        jPanel1.add(bBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 100, 50));

        tClaveReservacion.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        tClaveReservacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tClaveReservacionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tClaveReservacionFocusLost(evt);
            }
        });
        jPanel1.add(tClaveReservacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 25, 413, -1));

        tPrecio.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        tPrecio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tPrecioFocusGained(evt);
            }
        });
        tPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tPrecioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tPrecioKeyReleased(evt);
            }
        });
        jPanel1.add(tPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 300, 610, -1));

        comboClientes.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        comboClientes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---lista de nombres---" }));
        comboClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboClientesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboClientesMouseExited(evt);
            }
        });
        jPanel1.add(comboClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 600, -1));

        comboFormaPago.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        comboFormaPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Transferencia bancaria", "Cheque", "Tarjeta de débito", "Tarjeta de crédito", "3 MSI", "6 MSI", "12 MSI", "Otro" }));
        comboFormaPago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboFormaPagoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboFormaPagoMouseExited(evt);
            }
        });
        comboFormaPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFormaPagoActionPerformed(evt);
            }
        });
        jPanel1.add(comboFormaPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, -1, -1));

        comboReqFactura.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        comboReqFactura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Sí" }));
        comboReqFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboReqFacturaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboReqFacturaMouseExited(evt);
            }
        });
        comboReqFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboReqFacturaActionPerformed(evt);
            }
        });
        jPanel1.add(comboReqFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 360, 65, -1));

        jLabel6.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel6.setText("Forma de pago del vendedor:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, -1, -1));

        comboFormaPagoVendedor.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        comboFormaPagoVendedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Depósito", "Transferencia Bancaria", "Cheque", "Efectivo" }));
        comboFormaPagoVendedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboFormaPagoVendedorMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboFormaPagoVendedorMouseExited(evt);
            }
        });
        jPanel1.add(comboFormaPagoVendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 480, 380, -1));

        jLabel7.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel7.setText("Operador:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 97, -1, -1));

        comboOperadores.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        comboOperadores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---lista de operadores---" }));
        comboOperadores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboOperadoresMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboOperadoresMouseExited(evt);
            }
        });
        jPanel1.add(comboOperadores, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 94, 600, -1));

        tOtraFormaPago.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        tOtraFormaPago.setEnabled(false);
        tOtraFormaPago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tOtraFormaPagoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tOtraFormaPagoFocusLost(evt);
            }
        });
        tOtraFormaPago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tOtraFormaPagoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tOtraFormaPagoMouseExited(evt);
            }
        });
        jPanel1.add(tOtraFormaPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 230, 240, -1));

        jLabel8.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel8.setText("No. factura cliente:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 360, -1, -1));

        tNoFacturaCliente.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        tNoFacturaCliente.setEnabled(false);
        tNoFacturaCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNoFacturaClienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNoFacturaClienteFocusLost(evt);
            }
        });
        tNoFacturaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tNoFacturaClienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tNoFacturaClienteMouseExited(evt);
            }
        });
        jPanel1.add(tNoFacturaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 360, 219, -1));

        jLabel19.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        jLabel19.setText("No. factura operador:");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, -1, -1));

        tNoFacturaOperador.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 24)); // NOI18N
        tNoFacturaOperador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNoFacturaOperadorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNoFacturaOperadorFocusLost(evt);
            }
        });
        tNoFacturaOperador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tNoFacturaOperadorKeyPressed(evt);
            }
        });
        jPanel1.add(tNoFacturaOperador, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 420, 467, -1));

        jTabbedPane1.addTab("Registro", jPanel1);

        jPanel2.setBackground(new java.awt.Color(234, 177, 100));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 14)); // NOI18N
        jLabel12.setText("Buscar por:");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, -1, -1));

        comboBuscarPorEntidad.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 14)); // NOI18N
        comboBuscarPorEntidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Clave Reservacion", "Operador", "Ap. Paterno Cliente", "Ap. Materno Cliente", "Nombre Cliente", "Compañia" }));
        jPanel2.add(comboBuscarPorEntidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 11, 206, -1));

        tBusqueda.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 14)); // NOI18N
        tBusqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tBusquedaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tBusquedaFocusLost(evt);
            }
        });
        jPanel2.add(tBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 11, 420, -1));

        jLabel11.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel11.setText("MODIFICAR DATOS");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        jLabel9.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel9.setText("Clave reservacion:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 140, -1));

        tClaveReservacionMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        tClaveReservacionMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tClaveReservacionModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tClaveReservacionModFocusLost(evt);
            }
        });
        jPanel2.add(tClaveReservacionMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 250, 580, -1));

        comboClientesMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        comboClientesMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---lista de nombres---" }));
        comboClientesMod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboClientesModMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboClientesModMouseExited(evt);
            }
        });
        comboClientesMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboClientesModActionPerformed(evt);
            }
        });
        jPanel2.add(comboClientesMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, 580, -1));

        jLabel10.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel10.setText("Cliente:");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, -1, -1));

        jLabel13.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel13.setText("Forma de pago:");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, -1, -1));

        comboFormaPagoMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        comboFormaPagoMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Transferencia bancaria", "Cheque", "Tarjeta de débito", "Tarjeta de crédito", "3 MSI", "6 MSI", "12 MSI", "Otro" }));
        comboFormaPagoMod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboFormaPagoModMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboFormaPagoModMouseExited(evt);
            }
        });
        comboFormaPagoMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFormaPagoModActionPerformed(evt);
            }
        });
        jPanel2.add(comboFormaPagoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 400, 120, -1));

        tPrecioMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        tPrecioMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tPrecioModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tPrecioModFocusLost(evt);
            }
        });
        tPrecioMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tPrecioModKeyPressed(evt);
            }
        });
        jPanel2.add(tPrecioMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 400, 230, -1));

        jLabel14.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel14.setText("Precio:");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 400, -1, -1));

        comboReqFacturaMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        comboReqFacturaMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "No", "Sí" }));
        comboReqFacturaMod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboReqFacturaModMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboReqFacturaModMouseExited(evt);
            }
        });
        comboReqFacturaMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboReqFacturaModActionPerformed(evt);
            }
        });
        jPanel2.add(comboReqFacturaMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 450, 70, -1));

        jLabel15.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel15.setText("¿Requiere factura?:");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, -1, -1));

        jLabel16.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel16.setText("Operador:");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, -1, -1));

        comboOperadoresMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        comboOperadoresMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---lista de operadores---" }));
        comboOperadoresMod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboOperadoresModMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboOperadoresModMouseExited(evt);
            }
        });
        comboOperadoresMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboOperadoresModActionPerformed(evt);
            }
        });
        jPanel2.add(comboOperadoresMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, 580, -1));

        tOtraFormaPagoMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        tOtraFormaPagoMod.setEnabled(false);
        tOtraFormaPagoMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tOtraFormaPagoModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tOtraFormaPagoModFocusLost(evt);
            }
        });
        tOtraFormaPagoMod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tOtraFormaPagoModMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tOtraFormaPagoModMouseExited(evt);
            }
        });
        jPanel2.add(tOtraFormaPagoMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 400, 156, 30));

        jLabel18.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 14)); // NOI18N
        jLabel18.setText("Forma de pago vendedor:");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1098, 833, -1, -1));

        jLabel17.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel17.setText("No. factura del cliente:");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 450, -1, -1));

        tNoFacturaClienteMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        tNoFacturaClienteMod.setEnabled(false);
        tNoFacturaClienteMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNoFacturaClienteModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNoFacturaClienteModFocusLost(evt);
            }
        });
        tNoFacturaClienteMod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tNoFacturaClienteModMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tNoFacturaClienteModMouseExited(evt);
            }
        });
        jPanel2.add(tNoFacturaClienteMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 450, 320, -1));

        jLabel20.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel20.setText("Forma pago vendedor:");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 500, -1, -1));

        tNoFacturaOperadorMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        tNoFacturaOperadorMod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tNoFacturaOperadorModFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tNoFacturaOperadorModFocusLost(evt);
            }
        });
        tNoFacturaOperadorMod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tNoFacturaOperadorModKeyPressed(evt);
            }
        });
        jPanel2.add(tNoFacturaOperadorMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 500, 160, -1));

        bLimpiar.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        bLimpiar.setText("Limpiar");
        bLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLimpiarActionPerformed(evt);
            }
        });
        jPanel2.add(bLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 210, -1, -1));

        jTReservaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTReservaciones.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTReservaciones.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTReservacionesMouseDragged(evt);
            }
        });
        jTReservaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTReservacionesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTReservacionesMouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(jTReservaciones);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 48, 730, 149));

        comboFormaPagoVendedorMod.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        comboFormaPagoVendedorMod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Depósito", "Transferencia Bancaria", "Cheque", "Efectivo" }));
        comboFormaPagoVendedorMod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comboFormaPagoVendedorModMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboFormaPagoVendedorModMouseExited(evt);
            }
        });
        jPanel2.add(comboFormaPagoVendedorMod, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 500, 190, -1));

        jLabel21.setFont(new java.awt.Font("Malgun Gothic Semilight", 0, 18)); // NOI18N
        jLabel21.setText("No. factura operador:");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, -1, -1));

        jTabbedPane1.addTab("Consultas", jPanel2);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 46, 761, 576));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lAyudaReservaciones.setFont(new java.awt.Font("Malgun Gothic Semilight", 1, 12)); // NOI18N
        jPanel3.add(lAyudaReservaciones);

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 633, 781, 30));

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, -40, 800, 680));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_bSalirActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        int pestaña_Seleccionada = jTabbedPane1.getSelectedIndex();
        if (pestaña_Seleccionada == 0) {
            bAgregar.setEnabled(true);
            bEliminar.setEnabled(false);
            bEditar.setEnabled(false);
            bBuscar.setEnabled(false);
        } else {
            bAgregar.setEnabled(false);
            bEliminar.setEnabled(true);
            bEditar.setEnabled(true);
            bBuscar.setEnabled(true);
        }
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void bAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAgregarActionPerformed
        boolean precio_correcto = this.comprobarPrecio(tPrecio.getText().trim());
        if(!(tClaveReservacion.getText().trim().equals("") || tPrecio.getText().trim().equals("") || precio_correcto == false)) {
            this.agregar();
        } else if (tClaveReservacion.getText().trim().equals("") || tPrecio.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Los campos -Clave Reservacion- y -Precio- son obligatorios", "Operación fallida", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "El formato del precio es incorrecto. No debe introducir el signo $, no debe contener letras y solo se permite un punto", "Operación fallida", JOptionPane.WARNING_MESSAGE);
        }         
    }//GEN-LAST:event_bAgregarActionPerformed

    private void agregar() {
        String clave_Reservacion = tClaveReservacion.getText(),
               req_factura = comboReqFactura.getSelectedItem().toString(),
               forma_pago_vendedor = comboFormaPagoVendedor.getSelectedItem().toString(),
               forma_pago_cliente,
               no_factura_cliente = "",
               no_factura_operador = tNoFacturaOperador.getText();
        
        int id_cliente = ID_Cliente[comboClientes.getSelectedIndex()],
            id_operador = ID_Operador[comboOperadores.getSelectedIndex()];
        
        float precio = Float.parseFloat(tPrecio.getText());
        
        if(tOtraFormaPago.isEnabled()) {
            forma_pago_cliente = tOtraFormaPago.getText();
        } else {
            forma_pago_cliente = comboFormaPago.getSelectedItem().toString();
        }
        
        if(comboReqFactura.getSelectedIndex() == 1) {
            no_factura_cliente = tNoFacturaCliente.getText();
        }
        
        Altas operacion = new Altas();
        operacion.alta(clave_Reservacion, id_operador, id_cliente, forma_pago_cliente, precio, req_factura, no_factura_cliente, no_factura_operador, forma_pago_vendedor);
        actualizarTabla();
        borrarDatos();
    }
    
    private void comboFormaPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFormaPagoActionPerformed
        if(comboFormaPago.getSelectedItem().toString().equalsIgnoreCase("Otro")) {
            tOtraFormaPago.setEnabled(true);
        } else {
            tOtraFormaPago.setEnabled(false);
        }
    }//GEN-LAST:event_comboFormaPagoActionPerformed

    private void comboFormaPagoModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFormaPagoModActionPerformed
        if(comboFormaPagoMod.getSelectedItem().toString().equalsIgnoreCase("Otro")) {
            tOtraFormaPagoMod.setEnabled(true);
        } else {
            tOtraFormaPagoMod.setEnabled(false); 
        }
    }//GEN-LAST:event_comboFormaPagoModActionPerformed

    private void bEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEliminarActionPerformed
        int realizar_eliminacion = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar la reservación seleccionada?", "¡Atención!", JOptionPane.YES_NO_OPTION);
        if (realizar_eliminacion == JOptionPane.YES_OPTION) {
            Bajas una_baja = new Bajas();
            una_baja.bajaReservacion(clave_reservacion_actual);
            actualizarTabla();
            borrarDatos();
        }
    }//GEN-LAST:event_bEliminarActionPerformed

    private void bEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEditarActionPerformed
        if(!(tClaveReservacionMod.getText().trim().equals("") && tPrecioMod.getText().trim().equals(""))) {
            this.editar();
        } else {
           JOptionPane.showMessageDialog(null, "Los campos -Clave Reservacion- y -Precio- son obligatorios", "Operación fallida", JOptionPane.WARNING_MESSAGE); 
        }    
    }//GEN-LAST:event_bEditarActionPerformed

    private void editar() {
        if (!clave_reservacion_actual.equals("")) {
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            PreparedStatement ps = null;
            
            try {
                ps = con.prepareStatement("UPDATE reservaciones SET Clave_Reservacion=?, Clientes_ID_Cliente=?, "
                        + "Operadores_idOperadores=?, Forma_Pago_Cliente=?, Precio=?, Req_Factura=?, No_Factura_Cliente=?, No_Factura_Operador=?,"
                        + " Forma_Pago_Vendedor=? "
                        + "WHERE Clave_Reservacion = ?");
                ps.setString(1, tClaveReservacionMod.getText());
                ps.setInt(2, Integer.parseInt(id_cliente_actual)); 
                ps.setInt(3, Integer.parseInt(id_operador_actual));
                
                //Meter forma de pago.
                if(tOtraFormaPagoMod.isEnabled()){
                    ps.setString(4, tOtraFormaPagoMod.getText());
                } else {
                    ps.setString(4, comboFormaPagoMod.getSelectedItem().toString());
                }

                ps.setFloat(5, Float.parseFloat(tPrecioMod.getText()));
                ps.setString(6, comboReqFacturaMod.getSelectedItem().toString());
                if(comboReqFacturaMod.getSelectedIndex() == 1) {
                    ps.setString(7, tNoFacturaClienteMod.getText());
                } else {
                    ps.setString(7, "");
                }
                ps.setString(8, tNoFacturaOperadorMod.getText());
                ps.setString(9, comboFormaPagoVendedorMod.getSelectedItem().toString());
                ps.setString(10, clave_reservacion_actual);
                int res = ps.executeUpdate();
                if(res > 0) {
                    JOptionPane.showMessageDialog(null,"¡Reservación actualizada!");
                } else {
                    JOptionPane.showMessageDialog(null, "Ocurrio un error al actualizar los datos de la reservación");
                }
                con.close();
                actualizarTabla();
                borrarDatos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Error: " + e.toString());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla para poder actualizar los datos de la reservación");
        }
    }
    private void bBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBuscarActionPerformed
        cantidad_id=0;
        String texto = tBusqueda.getText();
        String campo = comboBuscarPorEntidad.getSelectedItem().toString();
        String where = "";
        
        if (campo.equals("Clave Reservacion")) {
            where = " WHERE reservaciones.Clave_Reservacion LIKE '%" + texto + "%'";
        } else if (campo.equals("Operador")) {
            where = " WHERE operadores.Nombre LIKE '%" + texto + "%'";
        } else if (campo.equals("Ap. Paterno Cliente")) {
            where = " WHERE clientes.Apellido_Paterno LIKE '%" + texto + "%'";
        } else if (campo.equals("Ap. Materno Cliente")) {
            where = " WHERE clientes.Apellido_Materno LIKE '%" + texto + "%'";
        } else if (campo.equals("Compañia")) {
            where = " WHERE clientes.Compañia LIKE '%" + texto + "%'";
        } else {
            where = " WHERE clientes.nombre LIKE '%" + texto + "%'";
        }
        
        if ("".equals(texto)) {
            where = "";
        }
        
        try {
            DefaultTableModel modelo = new CustomTableModel();
            jTReservaciones.setModel(modelo);
            
            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            String sql = "SELECT reservaciones.Clave_Reservacion, operadores.Nombre, clientes.Apellido_Paterno, "
                    + "clientes.Apellido_Materno, clientes.Nombre, clientes.Compañia, reservaciones.Forma_Pago_Cliente, "
                    + "reservaciones.Precio, reservaciones.Req_Factura, reservaciones.No_Factura_Cliente, reservaciones.No_Factura_Operador,"
                    + " reservaciones.Forma_Pago_Vendedor "
                    + "FROM reservaciones " +
            "INNER JOIN clientes ON reservaciones.Clientes_ID_Cliente = clientes.ID_Cliente " +
            "INNER JOIN operadores ON reservaciones.Operadores_idOperadores = operadores.idOperadores" + where;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            ResultSetMetaData rsMd = rs.getMetaData();
            int cantidadColumnas = rsMd.getColumnCount();
            modelo.addColumn("Clave Reservacion");
            modelo.addColumn("Operador");
            modelo.addColumn("Ap. Paterno Cliente");
            modelo.addColumn("Ap. Materno Cliente");
            modelo.addColumn("Nombre Cliente");
            modelo.addColumn("Compañia");
            modelo.addColumn("Forma de Pago Cliente");
            modelo.addColumn("Precio");
            modelo.addColumn("¿Requiere Factura?");
            modelo.addColumn("No. Fac. Cliente");
            modelo.addColumn("No. Fac. Operador");
            modelo.addColumn("Forma de Pago Vendedor");
            
            for (int i=0; i <= 11; i++) {
              jTReservaciones.getColumnModel().getColumn(i).setPreferredWidth(tamaño_columnas);  
            }
            
            while(rs.next()) {
                Object[] filas = new Object[cantidadColumnas]; //ya que la tabla trabaja con objetos, metemos todo en objetos
                
                for (int i=0; i < cantidadColumnas; i++) {
                    filas[i] = rs.getObject(i + 1);
                }
                
                cantidad_id++;
                modelo.addRow(filas);
            }
            
            con.close();
            
        } catch (SQLException ex) {
            JOptionPane.showInputDialog("Error:" + ex);
        }
        
        id_clientes_lista = new int[cantidad_id];
        id_operadores_lista = new int[cantidad_id];
        
        this.listarID(where);
    }//GEN-LAST:event_bBuscarActionPerformed

    private void bBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBorrarActionPerformed
        tClaveReservacion.setText("");
        comboOperadores.setSelectedIndex(0);
        comboClientes.setSelectedIndex(0);
        comboFormaPago.setSelectedIndex(0);
        tOtraFormaPago.setText("");
        tOtraFormaPago.setEnabled(false);
        tPrecio.setText("");
        comboReqFactura.setSelectedIndex(0);
        comboFormaPagoVendedor.setSelectedIndex(0);
        
    }//GEN-LAST:event_bBorrarActionPerformed

    private void comboOperadoresModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboOperadoresModActionPerformed
        int id = comboOperadoresMod.getSelectedIndex() + 1;
        id_operador_actual = id + "";
    }//GEN-LAST:event_comboOperadoresModActionPerformed

    private void comboClientesModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboClientesModActionPerformed
        int id = comboClientesMod.getSelectedIndex() + 1;
        id_cliente_actual = id + "";
    }//GEN-LAST:event_comboClientesModActionPerformed

    private void comboReqFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboReqFacturaActionPerformed
        if(comboReqFactura.getSelectedIndex() == 1) {
            tNoFacturaCliente.setEnabled(true);
        } else {
            tNoFacturaCliente.setEnabled(false);
        }
    }//GEN-LAST:event_comboReqFacturaActionPerformed

    private void bLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLimpiarActionPerformed
        tClaveReservacionMod.setText("");
        comboOperadoresMod.setSelectedIndex(0);
        comboClientesMod.setSelectedIndex(0);
        comboFormaPagoMod.setSelectedIndex(0);
        tOtraFormaPagoMod.setText("");
        tOtraFormaPagoMod.setEnabled(false);
        tPrecioMod.setText("");
        comboReqFacturaMod.setSelectedIndex(0);
        tNoFacturaCliente.setText("");
        tNoFacturaCliente.setEnabled(false);
        tNoFacturaOperador.setText("");
        comboFormaPagoVendedorMod.setSelectedIndex(0);
    }//GEN-LAST:event_bLimpiarActionPerformed

    private void tPrecioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tPrecioKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.agregar();
        }
    }//GEN-LAST:event_tPrecioKeyPressed

    private void tNoFacturaOperadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tNoFacturaOperadorKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.agregar();
        }
    }//GEN-LAST:event_tNoFacturaOperadorKeyPressed

    private void tPrecioModKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tPrecioModKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.editar();
        }
    }//GEN-LAST:event_tPrecioModKeyPressed

    private void tNoFacturaOperadorModKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tNoFacturaOperadorModKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.editar();
        }
    }//GEN-LAST:event_tNoFacturaOperadorModKeyPressed

    private void comboReqFacturaModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboReqFacturaModActionPerformed
        if(comboReqFacturaMod.getSelectedIndex() == 1) {
            tNoFacturaClienteMod.setEnabled(true);
        } else {
            tNoFacturaClienteMod.setEnabled(false);
        }
    }//GEN-LAST:event_comboReqFacturaModActionPerformed

    private void jTReservacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTReservacionesMouseClicked
        try {
            int Fila = jTReservaciones.getSelectedRow();

            PreparedStatement ps = null;
            ResultSet rs = null;
            Conexion connection = new Conexion();
            Connection con = connection.getConexion();
            
            id_cliente_actual = id_clientes_lista[Fila]+"";
            id_operador_actual = id_operadores_lista[Fila]+"";
            
            DefaultTableModel tm = (DefaultTableModel) jTReservaciones.getModel(); 
            clave_reservacion_actual = String.valueOf(tm.getValueAt(jTReservaciones.getSelectedRow(),0)); 

            String sql = "SELECT Clave_Reservacion, Clientes_ID_Cliente, Operadores_idOperadores, Forma_Pago_Cliente"
                    + ", Precio, Req_Factura, No_Factura_Cliente, No_Factura_Operador, Forma_Pago_Vendedor"
                    + " FROM reservaciones WHERE Clave_Reservacion = '" + clave_reservacion_actual + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next()) {
                tClaveReservacionMod.setText(rs.getString(1));
                comboOperadoresMod.setSelectedIndex(rs.getInt(3)-1);
                comboClientesMod.setSelectedIndex(rs.getInt(2)-1);
                
                String Forma_Pago = rs.getString(4);
                boolean esta_en_lista = false;
                for(int i=0; i < 8; i++) {
                   if(Forma_Pago.equalsIgnoreCase(comboFormaPagoMod.getItemAt(i))) {
                       esta_en_lista = true;
                   } 
                }
                
                if(esta_en_lista){
                    comboFormaPago.setSelectedItem(Forma_Pago);
                } else {
                    comboFormaPago.setSelectedItem("Otro");
                    tOtraFormaPagoMod.setText(Forma_Pago);
                }
                
                tPrecioMod.setText(rs.getString(5));
                
                String cliente_requiere_fatcura = rs.getString(6);
                if(cliente_requiere_fatcura.equalsIgnoreCase("No")) {
                    comboReqFacturaMod.setSelectedIndex(0);
                    tNoFacturaClienteMod.setEnabled(false);
                    tNoFacturaClienteMod.setText("");
                } else {
                    comboReqFacturaMod.setSelectedIndex(1);
                    tNoFacturaClienteMod.setEnabled(true);
                    tNoFacturaClienteMod.setText(rs.getString(7));
                }
                
                tNoFacturaOperadorMod.setText(rs.getString(8));
                
                String forma_pago_vendedor = rs.getString(9);
                for(int i=0; i<4; i++){
                    if(forma_pago_vendedor.equalsIgnoreCase(comboFormaPagoVendedorMod.getItemAt(i))) {
                        comboFormaPagoVendedorMod.setSelectedIndex(i);
                    }
                }
            }

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e +"Ocurrio un error al conectarse con la base de datos al seleccionar el registro. Contacte con el administrador del sistema para solucionar el problema","Operación fallida",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jTReservacionesMouseClicked

    private void bSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSalirMouseEntered
        lAyudaReservaciones.setText("Salir al menú principal");
    }//GEN-LAST:event_bSalirMouseEntered

    private void bSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSalirMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_bSalirMouseExited

    private void bAgregarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAgregarMouseEntered
        if(bAgregar.isEnabled()) {
            lAyudaReservaciones.setText("Agregar reservación"); 
        } else {
            lAyudaReservaciones.setText("Boton deshabilitado en esta pestaña");
        }
    }//GEN-LAST:event_bAgregarMouseEntered

    private void bEliminarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bEliminarMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_bEliminarMouseExited

    private void bEditarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bEditarMouseEntered
        if(bEditar.isEnabled()) {
            lAyudaReservaciones.setText("Modificar reservación"); 
        } else {
            lAyudaReservaciones.setText("Boton deshabilitado en esta pestaña");
        }
    }//GEN-LAST:event_bEditarMouseEntered

    private void bEliminarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bEliminarMouseEntered
        if(bEliminar.isEnabled()) {
            lAyudaReservaciones.setText("Eliminar reservación"); 
        } else {
            lAyudaReservaciones.setText("Boton deshabilitado en esta pestaña");
        }
    }//GEN-LAST:event_bEliminarMouseEntered

    private void bBuscarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseEntered
        if(bEliminar.isEnabled()) {
            lAyudaReservaciones.setText("Buscar reservación"); 
        } else {
            lAyudaReservaciones.setText("Boton deshabilitado en esta pestaña");
        }
    }//GEN-LAST:event_bBuscarMouseEntered

    private void bAgregarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bAgregarMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_bAgregarMouseExited

    private void bEditarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bEditarMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_bEditarMouseExited

    private void bBuscarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBuscarMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_bBuscarMouseExited

    private void tClaveReservacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tClaveReservacionFocusGained
        lAyudaReservaciones.setText("Clave de reservación proporcionada por el sistema del operador (campo obligatorio)");
    }//GEN-LAST:event_tClaveReservacionFocusGained

    private void tClaveReservacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tClaveReservacionFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tClaveReservacionFocusLost

    private void comboOperadoresMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboOperadoresMouseEntered
        lAyudaReservaciones.setText("Seleccione el operador que ofreció la reservación");
    }//GEN-LAST:event_comboOperadoresMouseEntered

    private void comboOperadoresMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboOperadoresMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboOperadoresMouseExited

    private void comboClientesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboClientesMouseEntered
        lAyudaReservaciones.setText("Seleccione el cliente que solicitó la reservación");
    }//GEN-LAST:event_comboClientesMouseEntered

    private void comboClientesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboClientesMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboClientesMouseExited

    private void comboFormaPagoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboFormaPagoMouseEntered
        lAyudaReservaciones.setText("Seleccione la forma en que pagará el cliente");
    }//GEN-LAST:event_comboFormaPagoMouseEntered

    private void comboFormaPagoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboFormaPagoMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboFormaPagoMouseExited

    private void tOtraFormaPagoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tOtraFormaPagoMouseEntered
        if(!tOtraFormaPago.isEnabled()) {
            lAyudaReservaciones.setText("Este campo está deshabilitado. Se habilita si selecciona otra forma de pago.");
        }
    }//GEN-LAST:event_tOtraFormaPagoMouseEntered

    private void tOtraFormaPagoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tOtraFormaPagoFocusGained
        lAyudaReservaciones.setText("Introduzca otra forma de pago del cliente no listada.");
    }//GEN-LAST:event_tOtraFormaPagoFocusGained

    private void tOtraFormaPagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tOtraFormaPagoFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tOtraFormaPagoFocusLost

    private void tOtraFormaPagoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tOtraFormaPagoMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tOtraFormaPagoMouseExited

    private void tPrecioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPrecioFocusGained
        lAyudaReservaciones.setText("Precio de la reservación (sólo números, con un solo punto si es necesario)");
    }//GEN-LAST:event_tPrecioFocusGained

    private void tPrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tPrecioKeyReleased
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tPrecioKeyReleased

    private void comboReqFacturaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboReqFacturaMouseEntered
        lAyudaReservaciones.setText("Seleccione si el cliente requiere factura");
    }//GEN-LAST:event_comboReqFacturaMouseEntered

    private void comboReqFacturaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboReqFacturaMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboReqFacturaMouseExited

    private void tNoFacturaClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tNoFacturaClienteMouseEntered
        if(!tNoFacturaCliente.isEnabled()) {
            lAyudaReservaciones.setText("Este campo está deshabilitado. Se activa si el cliente requiere factura.");
        }
    }//GEN-LAST:event_tNoFacturaClienteMouseEntered

    private void tNoFacturaClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tNoFacturaClienteMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tNoFacturaClienteMouseExited

    private void tNoFacturaClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNoFacturaClienteFocusGained
        lAyudaReservaciones.setText("Introduzca el número de factura que usted generó para el cliente");
    }//GEN-LAST:event_tNoFacturaClienteFocusGained

    private void tNoFacturaClienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNoFacturaClienteFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tNoFacturaClienteFocusLost

    private void tNoFacturaOperadorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNoFacturaOperadorFocusGained
        lAyudaReservaciones.setText("Introduzca el número de factura que le envió el operador. Si esta vacío, se entiende que no aplica.");
    }//GEN-LAST:event_tNoFacturaOperadorFocusGained

    private void tNoFacturaOperadorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNoFacturaOperadorFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tNoFacturaOperadorFocusLost

    private void comboFormaPagoVendedorMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboFormaPagoVendedorMouseEntered
        lAyudaReservaciones.setText("Seleccione la forma en que realizará el pago de la reservación");
    }//GEN-LAST:event_comboFormaPagoVendedorMouseEntered

    private void comboFormaPagoVendedorMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboFormaPagoVendedorMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboFormaPagoVendedorMouseExited

    private void tBusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tBusquedaFocusGained
        lAyudaReservaciones.setText("Elija una característica para buscar una reservación. Deje vacio para listar todo nuevamente.");
    }//GEN-LAST:event_tBusquedaFocusGained

    private void tBusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tBusquedaFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tBusquedaFocusLost

    private void jTReservacionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTReservacionesMouseEntered
        lAyudaReservaciones.setText("Seleccione un registro para cargar los datos en el formulario y modificarlos");
    }//GEN-LAST:event_jTReservacionesMouseEntered

    private void jTReservacionesMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTReservacionesMouseDragged
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_jTReservacionesMouseDragged

    private void tClaveReservacionModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tClaveReservacionModFocusGained
        lAyudaReservaciones.setText("Modifique aquí la clave de reservación. No puede estar vacio.");
    }//GEN-LAST:event_tClaveReservacionModFocusGained

    private void tClaveReservacionModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tClaveReservacionModFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tClaveReservacionModFocusLost

    private void comboOperadoresModMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboOperadoresModMouseEntered
        lAyudaReservaciones.setText("Modifique aqui el operador que ofreció la reservación");
    }//GEN-LAST:event_comboOperadoresModMouseEntered

    private void comboOperadoresModMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboOperadoresModMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboOperadoresModMouseExited

    private void comboClientesModMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboClientesModMouseEntered
        lAyudaReservaciones.setText("Modifique aquí el cliente que solicitó la reservación");
    }//GEN-LAST:event_comboClientesModMouseEntered

    private void comboClientesModMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboClientesModMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboClientesModMouseExited

    private void comboFormaPagoModMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboFormaPagoModMouseEntered
        lAyudaReservaciones.setText("Modifique aquí la forma en que pagará el cliente");
    }//GEN-LAST:event_comboFormaPagoModMouseEntered

    private void comboFormaPagoModMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboFormaPagoModMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboFormaPagoModMouseExited

    private void tOtraFormaPagoModMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tOtraFormaPagoModMouseEntered
        if(!tOtraFormaPagoMod.isEnabled()) {
            lAyudaReservaciones.setText("Este campo está deshabilitado. Se habilita si selecciona otra forma de pago.");
        }
    }//GEN-LAST:event_tOtraFormaPagoModMouseEntered

    private void tOtraFormaPagoModMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tOtraFormaPagoModMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tOtraFormaPagoModMouseExited

    private void tOtraFormaPagoModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tOtraFormaPagoModFocusGained
        lAyudaReservaciones.setText("Modifique aquí otra forma de pago del cliente no listada.");
    }//GEN-LAST:event_tOtraFormaPagoModFocusGained

    private void tOtraFormaPagoModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tOtraFormaPagoModFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tOtraFormaPagoModFocusLost

    private void tPrecioModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPrecioModFocusGained
        lAyudaReservaciones.setText("Modifique el precio de la reservación. Solo números y un punto.");
    }//GEN-LAST:event_tPrecioModFocusGained

    private void tPrecioModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tPrecioModFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tPrecioModFocusLost

    private void comboReqFacturaModMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboReqFacturaModMouseEntered
        lAyudaReservaciones.setText("Modifique aquí si el cliente requiere factura");
    }//GEN-LAST:event_comboReqFacturaModMouseEntered

    private void comboReqFacturaModMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboReqFacturaModMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboReqFacturaModMouseExited

    private void tNoFacturaClienteModMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tNoFacturaClienteModMouseEntered
        if(!tNoFacturaClienteMod.isEnabled()) {
            lAyudaReservaciones.setText("Este campo está deshabilitado. Se habilita si selecciona que el cliente requiere factura.");
        }
    }//GEN-LAST:event_tNoFacturaClienteModMouseEntered

    private void tNoFacturaClienteModMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tNoFacturaClienteModMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tNoFacturaClienteModMouseExited

    private void tNoFacturaClienteModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNoFacturaClienteModFocusGained
        lAyudaReservaciones.setText("Modifique aquí el número de factura proporcionado al cliente");
    }//GEN-LAST:event_tNoFacturaClienteModFocusGained

    private void tNoFacturaClienteModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNoFacturaClienteModFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tNoFacturaClienteModFocusLost

    private void tNoFacturaOperadorModFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNoFacturaOperadorModFocusGained
        lAyudaReservaciones.setText("Modifique aquí el número de factura que le proporcionó el operador. Vacio quiere decir no aplica.");
    }//GEN-LAST:event_tNoFacturaOperadorModFocusGained

    private void tNoFacturaOperadorModFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tNoFacturaOperadorModFocusLost
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_tNoFacturaOperadorModFocusLost

    private void comboFormaPagoVendedorModMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboFormaPagoVendedorModMouseEntered
        lAyudaReservaciones.setText("Modifique aquí la forma en que pagará usted la reservación");
    }//GEN-LAST:event_comboFormaPagoVendedorModMouseEntered

    private void comboFormaPagoVendedorModMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboFormaPagoVendedorModMouseExited
        lAyudaReservaciones.setText("");
    }//GEN-LAST:event_comboFormaPagoVendedorModMouseExited

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Ayuda_Reservaciones ventana_ayuda_op = new Ayuda_Reservaciones();
        ventana_ayuda_op.setVisible(true);
        ventana_ayuda_op.setLocationRelativeTo(null);
        ventana_ayuda_op.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private boolean comprobarPrecio(String precio){
        boolean precio_correcto = true;
        for(int i=0; i<precio.length(); i++) {
            char letra = precio.charAt(i);
            if(!(letra == '0' || letra == '1' || letra == '2' || letra == '3' || letra == '4' || letra == '5' ||
                 letra == '6' || letra == '7' || letra == '8' || letra == '9' || letra == '.')) {
                precio_correcto = false;
            }
        }
        
        int puntos=0;
        
        for(int i=0; i<precio.length(); i++) {
           char letra = precio.charAt(i);
            if(letra == '.') {
                puntos++;
            } 
        }
        
        if(puntos!=0 || puntos!=1){
            precio_correcto = false;
        }
        
        return precio_correcto;
    }
    
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
            java.util.logging.Logger.getLogger(Mod_Reservaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mod_Reservaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mod_Reservaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mod_Reservaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mod_Reservaciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAgregar;
    private javax.swing.JButton bBorrar;
    private javax.swing.JButton bBuscar;
    private javax.swing.JButton bEditar;
    private javax.swing.JButton bEliminar;
    private javax.swing.JButton bLimpiar;
    private javax.swing.JButton bSalir;
    private javax.swing.JComboBox<String> comboBuscarPorEntidad;
    private javax.swing.JComboBox<String> comboClientes;
    private javax.swing.JComboBox<String> comboClientesMod;
    private javax.swing.JComboBox<String> comboFormaPago;
    private javax.swing.JComboBox<String> comboFormaPagoMod;
    private javax.swing.JComboBox<String> comboFormaPagoVendedor;
    private javax.swing.JComboBox<String> comboFormaPagoVendedorMod;
    private javax.swing.JComboBox<String> comboOperadores;
    private javax.swing.JComboBox<String> comboOperadoresMod;
    private javax.swing.JComboBox<String> comboReqFactura;
    private javax.swing.JComboBox<String> comboReqFacturaMod;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTReservaciones;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lAyudaReservaciones;
    private javax.swing.JTextField tBusqueda;
    private javax.swing.JTextField tClaveReservacion;
    private javax.swing.JTextField tClaveReservacionMod;
    private javax.swing.JTextField tNoFacturaCliente;
    private javax.swing.JTextField tNoFacturaClienteMod;
    private javax.swing.JTextField tNoFacturaOperador;
    private javax.swing.JTextField tNoFacturaOperadorMod;
    private javax.swing.JTextField tOtraFormaPago;
    private javax.swing.JTextField tOtraFormaPagoMod;
    private javax.swing.JTextField tPrecio;
    private javax.swing.JTextField tPrecioMod;
    // End of variables declaration//GEN-END:variables
}

