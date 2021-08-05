
package sis_viajes;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Statement;
import javax.swing.JOptionPane;

public class Conexion {
    
    private Connection conectar = null;
    
    public Connection getConexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conectar = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/sis_viajes","root","");
            //JOptionPane.showInputDialog(null, "Conexion Establecida", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectarse :" + e.toString());
        }
        
        return conectar;
    }
}
