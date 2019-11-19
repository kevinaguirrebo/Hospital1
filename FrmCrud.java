
import java.sql.ResultSetMetaData;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Jean carlos
 */
public class FrmCrud extends javax.swing.JFrame {

    /**
     * Creates new form FrmCrud
     */
    boolean encontrado = false; 
    public FrmCrud() {
        initComponents();
        btnActualizar.setVisible(false);
        btnEliminar.setVisible(false);
        this.setLocationRelativeTo(null);
        Func_buscarEps();
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("folder2.png"));


        return retValue;
    }
    
    public void Func_buscarEps(){
        Conexxion conn = new Conexxion();
        Connection conx = conn.Conectar(); 
        try{
            Statement st = conx.createStatement();
            ResultSet rs;
            st.executeQuery("SELECT Descripcion FROM tbleps ");
            rs = st.getResultSet();
            while(rs.next())
            {
                cbxEps.addItem(rs.getString(1));
            }
            
        }catch(Exception e){
            System.out.println("no conectado"+e.getMessage());
        }   
    }
    
    public void Func_Limpiar(){
        encontrado = false;
        txtDocumento.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        cbxEps.setSelectedIndex(0);
        FechaN.setCalendar(null);
        btnActualizar.setVisible(false);
        btnEliminar.setVisible(false);
        txtDocumento.setEnabled(true);
        txtDocumento.setForeground(Color.BLACK);
        
    }
    
    public void Func_Guardar(){
          
               Date fecha = FechaN.getDate();
               DateFormat f = new SimpleDateFormat("YYYY-MM-dd");
               String fecha2 = f.format(fecha);
        // limpia tu codigo
              
               Conexxion conn = new Conexxion();
               Connection conx = conn.Conectar();
               try{
                   PreparedStatement pst = conx.prepareStatement("INSERT INTO tblpacientes(PKDocumento, Nombre, Apellido, FechaNacimiento, Correo, Telefono, FKId_TblEps) VALUES (?,?,?,?,?,?,?)");
                   pst.setString(1,txtDocumento.getText());
                   pst.setString(2,txtNombre.getText());
                   pst.setString(3,txtApellido.getText());
                   pst.setString(4,fecha2);  
                   pst.setString(5,txtCorreo.getText());
                   pst.setString(6,txtTelefono.getText());
                   pst.setInt(7,cbxEps.getSelectedIndex());
                   pst.executeUpdate();
                   Func_Limpiar();
                   JOptionPane.showMessageDialog(null,"Guardado","YEAH!",JOptionPane.INFORMATION_MESSAGE);
               }catch(SQLException e){
                   System.out.println("No guardó"+ e.getMessage());
               }
   }

    public void  Func_Buscar(){
      Conexxion conn = new Conexxion();
      Connection conx = conn.Conectar(); 
      try{
          Statement st = conx.createStatement();
          ResultSet rs;
          st.executeQuery("SELECT * FROM tblpacientes where PKDocumento="+txtDocumento.getText()+" ");
          rs = st.getResultSet();
          if(rs.next())
          {
            txtDocumento.setEnabled(false);
            txtDocumento.setText(rs.getString(1));
            txtNombre.setText(rs.getString(2));
            txtApellido.setText(rs.getString(3));
            FechaN.setDate(rs.getDate(4));
            txtCorreo.setText(rs.getString(5));
            txtTelefono.setText(rs.getString(6));
            cbxEps.setSelectedIndex(rs.getInt(7));
            btnActualizar.setVisible(true);
            btnEliminar.setVisible(true);
            encontrado=true;
          }
          else{
              JOptionPane.showMessageDialog(null,"NO EXISTE ","Mensaje de Error",JOptionPane.ERROR_MESSAGE);
          }
      }catch(Exception e){
          System.out.println("no conectado"+e.getMessage());
      }
  }
    
     public void  Func_ValidarExistencia(){
      Conexxion conn = new Conexxion();
      Connection conx = conn.Conectar(); 
      try{
          Statement st = conx.createStatement();
          ResultSet rs;
          st.executeQuery("SELECT COUNT(PKDocumento) FROM tblpacientes where PKDocumento="+txtDocumento.getText()+" ");
          rs = st.getResultSet();
          if(rs.next())
          {
              int dato = Integer.parseInt( rs.getString(1));
             if(dato >= 1)
             {
                 JOptionPane.showMessageDialog(null,"Numero De Documento ya registrado","",JOptionPane.ERROR_MESSAGE);
                 txtDocumento.setForeground(Color.red);
             }
             else
             {
               Func_Guardar();
               txtDocumento.setForeground(Color.BLACK);
             }
          }
      }catch(Exception e){
          System.out.println("no conectado"+e.getMessage());
      }
  }

    public void Func_Actualizar(){
        if(encontrado)
        {
            Date fecha = FechaN.getDate();
            DateFormat f = new SimpleDateFormat("YYYY-MM-dd");
            String fecha2 = f.format(fecha);
            Conexxion conn = new Conexxion();
            Connection conx = conn.Conectar();
            try {
                 PreparedStatement pst = conx.prepareStatement("UPDATE tblpacientes SET Nombre=?, Apellido=?, FechaNacimiento=?, Correo=?, Telefono=?, FKId_TblEps=? WHERE PKDocumento = ?");
                 pst.setString(1, txtNombre.getText());
                 pst.setString(2, txtApellido.getText());
                 pst.setString(3, fecha2);
                 pst.setString(4, txtCorreo.getText());
                 pst.setString(5, txtTelefono.getText());
                 pst.setInt(6, cbxEps.getSelectedIndex());
                 pst.setString(7, txtDocumento.getText());
                 pst.executeUpdate();
                 JOptionPane.showMessageDialog(null,"Datos Actualizados");
                 Func_Limpiar(); 
            } 
            catch (Exception e) 
            {
                System.out.println(e.getMessage());
            }
        }else{JOptionPane.showMessageDialog(null,"LO siento  pero no puedo actualizar","Head shoot",JOptionPane.WARNING_MESSAGE);}
        
        
       
    }
    
    public void Func_Eliminar(){
        if(encontrado)
        {
             Conexxion conn = new Conexxion();
            Connection conx = conn.Conectar();
            try {
                String dato = JOptionPane.showInputDialog("Ingrese el documento a eliminar");
                Statement st = conx.createStatement();
                if(JOptionPane.showConfirmDialog(null,"¿Esta seguro de eliminar los datos de esta persona ["+dato+"] ?")==0)
                {
                    st.executeUpdate("delete from tblpacientes where PKDocumento="+dato+"  ");
                    Func_Limpiar();
                    JOptionPane.showMessageDialog(null,"Dato borrado con exito");
                } 
                Func_Limpiar();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
        }else{JOptionPane.showMessageDialog(null,"LO siento  pero no puedo Eliminar","Head shoot",JOptionPane.WARNING_MESSAGE);}
    }
   
    public void Func_mostrar(){
        int posicion = btnMostrar.getX();
        if(posicion == 0)
        {
            Func_CargarDatos();
            panelRegistro.setVisible(false);
            Animacion.Animacion.mover_derecha(-430,0,2, 2, PanelMostarDatos);
            Animacion.Animacion.mover_derecha(0,430,2, 2, btnMostrar);
        }
        else
        {
            Animacion.Animacion.mover_izquierda(0, -430, 2, 2, PanelMostarDatos);
            Animacion.Animacion.mover_izquierda(430, 0, 2, 2, btnMostrar);
            panelRegistro.setVisible(true);
            
        }
        
    }
    
    public void Func_CargarDatos(){
        
        Conexxion conn = new Conexxion();
        Connection conx = conn.Conectar(); 
        DefaultTableModel modelo =  new DefaultTableModel();
        modelo.addColumn("Documento");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Fecha Nacimiento");
        modelo.addColumn("Correo");
        modelo.addColumn("Telefono");
        modelo.addColumn("Eps");
        try{
            Statement st = conx.createStatement();
            ResultSet rs;
            ResultSetMetaData rsm;
            st.executeQuery("SELECT PKDocumento, Nombre , Apellido, FechaNacimiento, Correo, Telefono, TblEps.Descripcion FROM tblpacientes,tbleps WHERE tbleps.Id = tblpacientes.FKId_TblEps");
            rs = st.getResultSet();
                while (rs.next())
                {
                    String [] Fila = new String[7]; 
                    Fila[0] = rs.getString(1);
                    Fila[1] = rs.getString(2);
                    Fila[2] = rs.getString(3);
                    Fila[3] = rs.getString(4);
                    Fila[4] = rs.getString(5);
                    Fila[5] = rs.getString(6);
                    Fila[6] = rs.getString(7);
                    modelo.addRow(Fila);
                    
                }
                tabla.setModel(modelo);

            
        }catch(Exception e){
            System.out.println("no conectado"+e.getMessage());
        }   
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelFondo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnMostrar = new javax.swing.JLabel();
        PanelMostarDatos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        panelRegistro = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        FechaN = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbxEps = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());

        PanelFondo.setBackground(new java.awt.Color(85, 143, 242));
        PanelFondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        PanelFondo.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 490, -1));

        jPanel2.setBackground(new java.awt.Color(0, 0, 50));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        PanelFondo.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 490, -1));

        btnMostrar.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnMostrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_theapplication_3365.png"))); // NOI18N
        btnMostrar.setText("Mostrar");
        btnMostrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMostrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMostrarMouseClicked(evt);
            }
        });
        PanelFondo.add(btnMostrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 14, -1, 60));

        PanelMostarDatos.setBackground(new java.awt.Color(85, 143, 242));
        PanelMostarDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(85, 143, 242), 3), "Mostrar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        tabla.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Documento", "Nombre", "Apellido", "Fecha nacimiento", "Correo", "Telefono", "Eps"
            }
        ));
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout PanelMostarDatosLayout = new javax.swing.GroupLayout(PanelMostarDatos);
        PanelMostarDatos.setLayout(PanelMostarDatosLayout);
        PanelMostarDatosLayout.setHorizontalGroup(
            PanelMostarDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelMostarDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                .addContainerGap())
        );
        PanelMostarDatosLayout.setVerticalGroup(
            PanelMostarDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelMostarDatosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PanelFondo.add(PanelMostarDatos, new org.netbeans.lib.awtextra.AbsoluteConstraints(-430, 20, 430, 440));

        panelRegistro.setBackground(new java.awt.Color(85, 143, 242));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel8.setText("Registro de Pacientes");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Documento:");

        txtDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocumentoActionPerformed(evt);
            }
        });
        txtDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocumentoKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("Nombre:");

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("Apellido:");

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Fecha\n Nacimiento:");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Correo:");

        txtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Telefono:");

        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("Eps:");

        cbxEps.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));

        btnGuardar.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnGuardar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Guardar.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarMouseClicked(evt);
            }
        });

        btnActualizar.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnActualizar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/actualizar.png"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarMouseClicked(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnEliminar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eliminar.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnEliminar.setPreferredSize(new java.awt.Dimension(63, 17));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnLimpiar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Limpiar.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelRegistroLayout = new javax.swing.GroupLayout(panelRegistro);
        panelRegistro.setLayout(panelRegistroLayout);
        panelRegistroLayout.setHorizontalGroup(
            panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegistroLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel8))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel1)
                        .addGap(36, 36, 36)
                        .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel2)
                        .addGap(46, 46, 46)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel3)
                        .addGap(54, 54, 54)
                        .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FechaN, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel5)
                        .addGap(63, 63, 63)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel6)
                        .addGap(53, 53, 53)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jLabel7)
                        .addGap(53, 53, 53)
                        .addComponent(cbxEps, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnActualizar)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        panelRegistroLayout.setVerticalGroup(
            panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegistroLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(41, 41, 41)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(FechaN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(cbxEps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGuardar)
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        PanelFondo.add(panelRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 430, 430));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        Func_Actualizar();
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
       Func_Eliminar();
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void txtDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocumentoActionPerformed
        Func_Buscar();
    }//GEN-LAST:event_txtDocumentoActionPerformed

    private void btnGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseClicked
       if(!(txtDocumento.getText().equals("")||txtNombre.getText().equals("")||txtTelefono.getText().equals("")|| txtApellido.getText().equals("") || txtCorreo.getText().equals("") || cbxEps.getSelectedIndex() == 0 || FechaN.getCalendar() == null))
           {
            if(encontrado)
             {
                JOptionPane.showMessageDialog(null,"Lo siento pero no puedo guardar","Head shoot",JOptionPane.WARNING_MESSAGE);
             }else{Func_ValidarExistencia();}
          }else{JOptionPane.showMessageDialog(null,"Debe llenar todos los campos","Head shoot",JOptionPane.WARNING_MESSAGE);}
    }//GEN-LAST:event_btnGuardarMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        Func_Limpiar();
    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
            char c =evt.getKeyChar();
          if(Character.isDigit(c)) {
              getToolkit().beep();
              evt.consume();
          }
          int canti = txtNombre.getText().length();
        if(canti >=50)
        {
          evt.consume();
        }
    }//GEN-LAST:event_txtNombreKeyTyped

    private void btnMostrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMostrarMouseClicked
        Func_mostrar();
    }//GEN-LAST:event_btnMostrarMouseClicked

    private void txtDocumentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocumentoKeyTyped
        char dato = evt.getKeyChar();
        if(!(dato>=48 && dato<=57))
        {
            evt.consume();
        }
     int canti = txtDocumento.getText().length();
     if(canti >=11)
     {
       evt.consume();
     }
    }//GEN-LAST:event_txtDocumentoKeyTyped

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
          char c =evt.getKeyChar();
          if(Character.isDigit(c)) {
              getToolkit().beep();
              evt.consume();
          }
           int canti = txtApellido.getText().length();
        if(canti >=50)
        {
          evt.consume();
        }
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
       char dato = evt.getKeyChar();
        if(!(dato>=48 && dato<=57))
        {
            evt.consume();
        }
         int canti = txtTelefono.getText().length();
        if(canti >=10)
        {
          evt.consume();
        }
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void txtCorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoKeyTyped
        int canti = txtCorreo.getText().length();
        if(canti >=150)
        {
          evt.consume();
        }
    }//GEN-LAST:event_txtCorreoKeyTyped

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
            java.util.logging.Logger.getLogger(FrmCrud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmCrud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmCrud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmCrud.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmCrud().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser FechaN;
    private javax.swing.JPanel PanelFondo;
    private javax.swing.JPanel PanelMostarDatos;
    private javax.swing.JLabel btnActualizar;
    private javax.swing.JLabel btnEliminar;
    private javax.swing.JLabel btnGuardar;
    private javax.swing.JLabel btnLimpiar;
    private javax.swing.JLabel btnMostrar;
    private javax.swing.JComboBox<String> cbxEps;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelRegistro;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
