package com.gimnasio.views;

import com.gimnasio.controller.Operaciones;
import com.gimnasio.model.ClienteDto;
import com.gimnasio.model.ClientePaqueteDto;
import com.gimnasio.model.ComboDto;
import com.gimnasio.model.ComboModel;
import com.gimnasio.model.MiRender;
import com.gimnasio.model.TablaDto;
import com.gimnasio.model.TablaModelo;
import com.gimnasio.model.UsuarioDto;
import com.gimnasio.model.enums.EEstadoPlan;
import com.gimnasio.model.enums.EGenero;
import com.gimnasio.model.enums.ESiNo;
import com.gimnasio.model.enums.ETipoPago;
import com.gimnasio.util.Util;
import com.google.common.base.Joiner;
import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author rodolfo
 */
public final class frmRegistrarPagos extends javax.swing.JInternalFrame {

    private List<ComboDto> listComboDescuentos;
    private List<ComboDto> listComboPaquetes;
    private final String[] headTable;
    private final TablaModelo table;
    private ComboModel comboDescuentos;
    private ComboModel comboPaquetes;

    protected ClientePaqueteDto clientePaqueteDto;
    protected UsuarioDto usuarioSessionDto;
    protected ClienteDto clienteDto;
    protected Operaciones operacion;

    private frmClientes clientePadre;
    protected String tipoViene;
    protected boolean registraAsistencia;
    final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;

    
    /**
     * Creates new form frmPagos
     *
     * @param frmCliente
     * @param operacion
     * @param clienteDto
     * @throws java.lang.Exception
     */
    public frmRegistrarPagos(frmClientes frmCliente, Operaciones operacion, ClienteDto clienteDto) throws Exception {
        initComponents();
        this.clientePaqueteDto = new ClientePaqueteDto();
        this.registraAsistencia = false;
        this.clientePadre = frmCliente;
        this.clienteDto = clienteDto;
        this.operacion = operacion;
        this.setInitCombos();
        this.panelTiquetera.setVisible(false); 
        this.btnDelete.setVisible(false);
        this.btnRecibo.setVisible(false);
        
        //Pintar tabla       
        this.headTable = new String[]{"Id", "Nombre",  "Precio", "Fecha Inicio", "Fecha Fin"};
        int widthColumna[] = {50, 100, 100, 100, 100};
        this.table = new TablaModelo(this.headTable);
        
        //asignar width
        this.tblPaquetes.setModel(this.table);
        int columnas = this.tblPaquetes.getColumnCount();
        for (int i = 0; i < columnas; i++) {
            this.tblPaquetes.getColumnModel().getColumn(i).setPreferredWidth(widthColumna[i]);
        }
        
        if (!Util.getVacio(clienteDto.getPersonaDto().getNombreCompleto())) { 
            setConsultarTablePaquetes(); 
            this.panelTiquetera.setVisible(false);
            if (!Util.getVacio(clienteDto.getPersonaDto().getNombreCompleto())) {
                this.lblNombre_cliente.setText(clienteDto.getPersonaDto().getNombreCompleto());
                this.lblDocumento_cliente.setText(clienteDto.getPersonaDto().getNumeroIdentificacion());
            }                                            
        }     
        this.setAsignatFoto();   
    }

    /**
     *
     * @param operacion
     * @param documento
     * @throws Exception
     */
    public frmRegistrarPagos(Operaciones operacion, String documento) throws Exception {
        initComponents();
        this.btnDelete.setVisible(false);
        this.btnRecibo.setVisible(false);
        this.registraAsistencia = false;
        this.operacion = operacion;
        List<ClienteDto> listCliente = this.operacion.getClienteDatos(null, documento);
        if (listCliente.size() > 0) {
            ClienteDto clienteTemp = new ClienteDto();
            for (ClienteDto cliente : listCliente) {
                if (documento.equals(cliente.getPersonaDto().getNumeroIdentificacion())) {
                    clienteTemp = cliente;
                    break;
                }
            }
            this.clienteDto = clienteTemp;
        }
        
        //Pintar tabla        
        this.headTable = new String[]{"Id", "Nombre",  "Precio", "Fecha Inicio", "Fecha Fin"};
        int widthColumna[] = {50, 100, 100, 100, 100};
        this.table = new TablaModelo(this.headTable);
        this.tblPaquetes.setModel(this.table);
        
        //asignar width
        int columnas = this.tblPaquetes.getColumnCount();
        for (int i = 0; i < columnas; i++) {
            this.tblPaquetes.getColumnModel().getColumn(i).setPreferredWidth(widthColumna[i]);
        }
        
        if (this.clienteDto.getId() != null) {     
            this.setInitCombos();
            setConsultarTablePaquetes(); 
            this.panelTiquetera.setVisible(false);
            if (!Util.getVacio(clienteDto.getPersonaDto().getNombreCompleto())) {
                this.lblNombre_cliente.setText(clienteDto.getPersonaDto().getNombreCompleto());
                this.lblDocumento_cliente.setText(clienteDto.getPersonaDto().getNumeroIdentificacion());
            }
            this.clientePaqueteDto = new ClientePaqueteDto();
            this.setAsignarValores();    
            this.setAsignatFoto();   
        }
    }        

    public final void setInitCombos() {
        try {
            ComboDto inicio;
            this.comboPaquetes = new ComboModel();
            this.comboPaquetes.getLista().clear();
            this.listComboPaquetes = this.operacion.getPaquetesEnumerado();
            inicio = new ComboDto("", "-------------");
            this.listComboPaquetes.add(0, inicio);
            this.comboPaquetes.getLista().addAll(this.listComboPaquetes);
            this.comboPaquetes.setSelectedItem(inicio);
            this.cmbPaquete.setModel(this.comboPaquetes);

            this.comboDescuentos = new ComboModel();
            this.comboDescuentos.getLista().clear();
            this.listComboPaquetes = this.operacion.getDescuentosEnumerado();
            inicio = new ComboDto("", "-------------");
            this.listComboPaquetes.add(0, inicio);
            this.comboDescuentos.getLista().addAll(this.listComboPaquetes);
            this.comboDescuentos.setSelectedItem(inicio);
            this.cmbDescuento.setModel(this.comboDescuentos);
            this.txtFecha_inicio.setDate(new Date());
        } catch (Exception ex) {
            Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void setClear() {
        try {
            setInitCombos();
            this.txtFecha_fin.cleanup();
            this.txtPrecio_base.setText("");
            this.txtTolal_pagar.setText("");
            this.btnDelete.setVisible(false);
            this.btnRecibo.setVisible(false);
            this.panelTiquetera.setVisible(false);
        } catch (Exception ex) {
            Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public final void setAsignatFoto() {
        if (this.clienteDto.getPersonaDto().getFotoPerfil() != null && this.clienteDto.getPersonaDto().getFotoPerfil().trim().length() > 0) {
            File file = new File("fotos/" + this.clienteDto.getPersonaDto().getFotoPerfil());
            if (file.exists()) {
                Image image = new ImageIcon(file.getAbsolutePath()).getImage();
                lblFotoCliente.setIcon(new ImageIcon(image.getScaledInstance(128, 128, Image.SCALE_DEFAULT)));
                lblFotoCliente.repaint();
            } else {
                this.setNoFile(lblFotoCliente);
            }
        } else {
            setNoFile(lblFotoCliente);
        }
    }

    /**
     * 
     */
    public final void setAsignarValores() {
        try {
            if (this.clientePaqueteDto.getPaqueteId() != null && this.clientePaqueteDto.getPaqueteId() > 0) {
                this.cmbPaquete.setSelectedIndex(Integer.parseInt(this.clientePaqueteDto.getPaqueteId().toString()));
                this.cmbPaquete.repaint();
                this.btnDelete.setVisible(true);
                this.btnRecibo.setVisible(true);
            }
            if (this.clientePaqueteDto.getDescuentoId() != null && this.clientePaqueteDto.getDescuentoId() > 0) {
                this.cmbDescuento.setSelectedIndex(Integer.parseInt(this.clientePaqueteDto.getDescuentoId().toString()));
                this.cmbDescuento.repaint();
            }
            if (this.clientePaqueteDto.getNumeroDiasTiquetera() > 0) {
                this.txtDias_tiquetera.setText(String.valueOf(this.clientePaqueteDto.getNumeroDiasTiquetera()));
            }
            if(clientePaqueteDto.getFechaIniciaPaquete() != null) {
                Date dateIni = new SimpleDateFormat("yyyy-MM-dd").parse(clientePaqueteDto.getFechaIniciaPaquete());
                this.txtFecha_inicio.setDate(dateIni);
            }
            if(clientePaqueteDto.getFechaFinalizaPaquete() != null) {
                Date dateFin = new SimpleDateFormat("yyyy-MM-dd").parse(clientePaqueteDto.getFechaFinalizaPaquete());
                this.txtFecha_fin.setDate(dateFin);
            }
            if (this.clientePaqueteDto.getPrecioBase() > 0) {
                this.txtPrecio_base.setText(String.valueOf(this.clientePaqueteDto.getPrecioBase()));
            }
            if (this.clientePaqueteDto.getValorTotal() > 0) {
                this.txtTolal_pagar.setText(String.valueOf(this.clientePaqueteDto.getValorTotal()));
            }
            
            if (this.clientePaqueteDto.getTipoPago() == ETipoPago.EFECTIVO.getId() ) {                
                this.rbtEfectivo.setSelected(true);
            }
            
            if (this.clientePaqueteDto.getTipoPago() == ETipoPago.TARJETA.getId() ) {
                this.rbtTarjeta.setSelected(true);
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void setNoFile(JLabel lblFoto) {
        File file = new File("files/no-file.png");
        if (file.exists()) {
            Image image = new ImageIcon(file.getAbsolutePath()).getImage();
            Util.setPintarFotoPerfil(image, lblFoto);
        }
    }
    
    /**
     * 
     * @param idClientePaquete 
     * @throws java.lang.Exception 
     */
    protected void editarPaquete(String idClientePaquete) throws Exception {                          
        this.clientePaqueteDto = this.operacion.getPaqueteActivoCliente(null,null,idClientePaquete);
        
        if (this.clientePaqueteDto.getId() != null) {
            this.setAsignarValores();
        }       
    }
    
    /**
     *
     * @throws SQLException
     */
    protected void setConsultarTablePaquetes() throws SQLException {
                    
        List<TablaDto> lista = this.operacion.getPaquetesClienteDatosTablaDto(String.valueOf(this.clienteDto.getId()), null);
        this.table.getData().clear();
        this.lblCantidad_paquetes.setText(String.valueOf(lista.size()));
        
        lista.stream().forEach((dto) -> {
            this.table.setAgregar(dto);
        });
        
        this.tblPaquetes.setDefaultRenderer(Object.class, new MiRender(this.table));
        this.tblPaquetes.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel6 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        cmbPaquete = new javax.swing.JComboBox();
        lblPaquete = new javax.swing.JLabel();
        cmbDescuento = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        panelTiquetera = new javax.swing.JPanel();
        txtDias_tiquetera = new javax.swing.JTextField();
        lblTiquetera = new javax.swing.JLabel();
        panelCliente = new javax.swing.JPanel();
        lblNombre_cliente = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblFotoCliente = new javax.swing.JLabel();
        lblDocumento_cliente = new javax.swing.JLabel();
        txtTolal_pagar = new javax.swing.JTextField();
        txtPrecio_base = new javax.swing.JTextField();
        lblPrecio_base = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtFecha_inicio = new com.toedter.calendar.JDateChooser();
        txtFecha_fin = new com.toedter.calendar.JDateChooser();
        lbl_apazamiento = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnRecibo = new javax.swing.JButton();
        rbtEfectivo = new javax.swing.JRadioButton();
        rbtTarjeta = new javax.swing.JRadioButton();
        lblGenero = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblResultado = new javax.swing.JLabel();
        lblCantidad_paquetes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPaquetes = new javax.swing.JTable();

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "AGREGAR UN NUEVO PAQUETE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/floppy-icon.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setRegistrarPagoPlan(evt);
            }
        });

        cmbPaquete.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbPaquete.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                setVerificaPaqueteTiquetera(evt);
            }
        });

        lblPaquete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPaquete.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPaquete.setText("Paquete");

        cmbDescuento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDescuento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                setCalculaDescuento(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Descuento");

        txtDias_tiquetera.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                setValidaDiasTiquetera(evt);
            }
        });
        txtDias_tiquetera.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidaSoloNumeros(evt);
            }
        });

        lblTiquetera.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTiquetera.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTiquetera.setText("Numero de días");

        javax.swing.GroupLayout panelTiqueteraLayout = new javax.swing.GroupLayout(panelTiquetera);
        panelTiquetera.setLayout(panelTiqueteraLayout);
        panelTiqueteraLayout.setHorizontalGroup(
            panelTiqueteraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelTiqueteraLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblTiquetera)
                .addGap(5, 5, 5)
                .addComponent(txtDias_tiquetera, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
        );
        panelTiqueteraLayout.setVerticalGroup(
            panelTiqueteraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTiqueteraLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panelTiqueteraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDias_tiquetera, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTiquetera))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelCliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblNombre_cliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNombre_cliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Foto");

        lblFotoCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFotoCliente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblFotoCliente.setPreferredSize(new java.awt.Dimension(128, 128));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFotoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFotoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        lblDocumento_cliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDocumento_cliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelClienteLayout = new javax.swing.GroupLayout(panelCliente);
        panelCliente.setLayout(panelClienteLayout);
        panelClienteLayout.setHorizontalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDocumento_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelClienteLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblNombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelClienteLayout.setVerticalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDocumento_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtTolal_pagar.setEditable(false);

        txtPrecio_base.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                setCalculaValorTotalConPrecioBase(evt);
            }
        });
        txtPrecio_base.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidaSoloNumeros(evt);
            }
        });

        lblPrecio_base.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPrecio_base.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrecio_base.setText("Precio Base");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Total a pagar");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Fecha de Inicio");

        txtFecha_inicio.setDateFormatString("yyyy-MM-dd");
        txtFecha_inicio.setPreferredSize(new java.awt.Dimension(6, 20));

        txtFecha_fin.setDateFormatString("yyyy-MM-dd");
        txtFecha_fin.setPreferredSize(new java.awt.Dimension(6, 20));

        lbl_apazamiento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_apazamiento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_apazamiento.setText("Fecha de Fin");

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/delete_icon.png"))); // NOI18N
        btnDelete.setText("Eliminar");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletesetRegistrarPagoPlan(evt);
            }
        });

        btnRecibo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRecibo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/file-powerpoint-icon.png"))); // NOI18N
        btnRecibo.setText("Recibo");
        btnRecibo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecibosetRegistrarPagoPlan(evt);
            }
        });

        buttonGroup1.add(rbtEfectivo);
        rbtEfectivo.setSelected(true);
        rbtEfectivo.setText("Efectivo");
        rbtEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtEfectivoActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtTarjeta);
        rbtTarjeta.setText("Tarjeta");
        rbtTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtTarjetaActionPerformed(evt);
            }
        });

        lblGenero.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblGenero.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGenero.setText("Tipo pago");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lblPrecio_base)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPrecio_base, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lblPaquete)
                        .addGap(5, 5, 5)
                        .addComponent(cmbPaquete, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(5, 5, 5)
                        .addComponent(cmbDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelTiquetera, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFecha_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lbl_apazamiento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFecha_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTolal_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(btnRecibo)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lblGenero)
                        .addGap(10, 10, 10)
                        .addComponent(rbtEfectivo)
                        .addGap(12, 12, 12)
                        .addComponent(rbtTarjeta)))
                .addGap(30, 30, 30))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbPaquete, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPaquete))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(15, 15, 15)
                        .addComponent(panelTiquetera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFecha_inicio, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_apazamiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtFecha_fin, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbtEfectivo)
                            .addComponent(rbtTarjeta)
                            .addComponent(lblGenero))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecio_base, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPrecio_base))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTolal_pagar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panelCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "LISTA DE PAQUETES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N
        jPanel3.setMaximumSize(new java.awt.Dimension(500, 500));

        lblResultado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblResultado.setText("Resultados");

        lblCantidad_paquetes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblCantidad_paquetes.setText("0");

        tblPaquetes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPaquetes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPaquetessetEditarProducto(evt);
            }
        });
        jScrollPane1.setViewportView(tblPaquetes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblResultado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCantidad_paquetes, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblResultado)
                    .addComponent(lblCantidad_paquetes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt
     */
    private void setRegistrarPagoPlan(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setRegistrarPagoPlan
        List<String> listMessages = new ArrayList();
        ComboDto comboPaq = (ComboDto) this.cmbPaquete.getSelectedItem();
        ComboDto comboDes = (ComboDto) this.cmbDescuento.getSelectedItem();
        boolean calculaFin = false;
        if (Util.getVacio(comboPaq.getCodigo())) {
            listMessages.add("<li>" + this.lblPaquete.getText() + "</li>");
        }
        if (!Util.getVacio(comboPaq.getCodigo())) {
            if (!Util.getVacio(comboPaq.getAuxiliar()) && (comboPaq.getAuxiliar().equals(String.valueOf(ESiNo.SI.getId())))) {
                if (this.txtDias_tiquetera.getText().equals("")) {
                    listMessages.add("<li>" + this.lblTiquetera.getText() + "</li>");
                }
            }
        }
        if (this.txtFecha_inicio.getDate() == null) {
            listMessages.add("<li>Fecha inicio del " + this.lblPaquete.getText().toLowerCase() + "</li>");
        }
        try {
            Date dateFin = new SimpleDateFormat("yyyy-MM-dd").parse(clientePaqueteDto.getFechaFinalizaPaquete());
            if (dateFin.getTime() > this.txtFecha_fin.getDate().getTime()) {
                listMessages.add("<li>Fecha Fin del " + this.lblPaquete.getText().toLowerCase() + " no puede ser menor a la fecha original</li>");
                this.txtFecha_fin.setDate(dateFin);
            } else {                
                long days = ( this.txtFecha_fin.getDate().getTime() - dateFin.getTime() )/MILLSECS_PER_DAY; 
                if(days > clientePaqueteDto.getPaqueteDto().getDiasAplazamiento() ) {
                    listMessages.add("<li> No puede aplazar el plan por mas de  " + clientePaqueteDto.getPaqueteDto().getDiasAplazamiento() + " Días. </li>");
                    this.txtFecha_fin.setDate(dateFin);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaFin = sdf.format(this.txtFecha_fin.getDate().getTime());
                    this.clientePaqueteDto.setFechaFinalizaPaquete(fechaFin);
                }
            }
        }catch (Exception ex) {}        
        
        if (this.txtPrecio_base.getText().equals("")) {
            listMessages.add("<li>" + this.lblPrecio_base.getText() + "</li>");
        }
        if (listMessages.size() > 0) {
            JLabel label = new JLabel("<html>Verífique la siguiente lista de campos obligatorios:\n<ol>" + Joiner.on("\n").join(listMessages) + "</ol></html>");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
        } else {
            this.clientePaqueteDto.setClienteId(this.clienteDto.getId());
            this.clientePaqueteDto.setPaqueteId(new Long(comboPaq.getCodigo()));
            if (!Util.getVacio(comboDes.getCodigo())) {
                this.clientePaqueteDto.setDescuentoId(new Long(comboDes.getCodigo()));
            } else {
                this.clientePaqueteDto.setDescuentoId(null);
            }
            if (!Util.getVacio(comboPaq.getAuxiliar()) && (comboPaq.getAuxiliar().equals(String.valueOf(ESiNo.SI.getId())))) {
                this.clientePaqueteDto.setNumeroDiasTiquetera(Short.parseShort(this.txtDias_tiquetera.getText()));                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFin = sdf.format(this.txtFecha_inicio.getDate().getTime());
                this.clientePaqueteDto.setFechaFinalizaPaquete(fechaFin);
                calculaFin = true;
            }
            this.clientePaqueteDto.setPrecioBase(Double.parseDouble(this.txtPrecio_base.getText()));
            this.clientePaqueteDto.setValorTotal(Double.parseDouble(this.txtTolal_pagar.getText()));
            this.clientePaqueteDto.setEstado(EEstadoPlan.ACTIVO.getId());
            
            if (this.rbtEfectivo.isSelected()) {
                this.clientePaqueteDto.setTipoPago(ETipoPago.EFECTIVO.getId());
            }
            if (this.rbtTarjeta.isSelected()) {
                this.clientePaqueteDto.setTipoPago(ETipoPago.TARJETA.getId());
            }
            
            if (this.txtFecha_inicio.getDate() != null) {
                String fechaComp = (this.clientePaqueteDto.getFechaIniciaPaquete() != null) ? this.clientePaqueteDto.getFechaIniciaPaquete() :"";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaNacimiento = sdf.format(this.txtFecha_inicio.getDate().getTime());
                if(!fechaNacimiento.equals(fechaComp)) {
                    calculaFin = true;
                }
                this.clientePaqueteDto.setFechaIniciaPaquete(fechaNacimiento);
            }
            
            this.clientePaqueteDto.setUsuarioId(this.usuarioSessionDto.getId());
            try {
                try {
                    //if (!Util.getVacio(comboPaq.getAuxiliar()) && (comboPaq.getAuxiliar().equals(String.valueOf(ESiNo.NO.getId())))) {
                    if(calculaFin) {
                        this.clientePaqueteDto.setFechaFinalizaPaquete(this.setVerificaFechaFinalizaPaquete(comboPaq, comboDes));
                    }
                    //}
                    boolean correct = this.operacion.setGuardaPagoPaqueteCliente(this.clientePaqueteDto, this.isRegistraAsistencia());
                    if (correct) {
                        this.generarRecibo();
                        
                        /* autoclose();                        
                        JLabel label = new JLabel(this.isRegistraAsistencia() ? "El registro para el pago y asistencia del cliente se ha realizado correctamente" : "El registro para el pago se ha realizado correctamente");
                        label.setFont(new Font("consolas", Font.PLAIN, 14));
                        JOptionPane.showMessageDialog(this, label, "Información", JOptionPane.INFORMATION_MESSAGE);
                         */
                        
                        if (Short.parseShort(this.tipoViene) == 1) {
                            this.clientePadre.setVisible(false);
                            this.setVisible(false);
                        } else {
                            this.setVisible(false);
                        }

                    }
                } catch (Exception ex) {
                    JLabel label = new JLabel("Se presentó un error para guardar el proceso de pago del paquete, intente nuevamente");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, label, "Alerta de error", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (Exception ex) {
                JLabel label = new JLabel("Se presentó un error para guardar el proceso de pago del paquete, intente nuevamente");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_setRegistrarPagoPlan

    /**
     *
     * @param comboPaq
     * @param comboDes
     * @return
     * @throws Exception
     */
    public String setVerificaFechaFinalizaPaquete(ComboDto comboPaq, ComboDto comboDes) throws Exception {
        String fechaFinal = "";
        try {
            Date fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(this.clientePaqueteDto.getFechaIniciaPaquete()); //Date();//
            SimpleDateFormat fechaTemporal = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaRetorna = fechaInicio;
            switch (Short.parseShort(comboPaq.getCoAssistant())) {
                case 1: {
                    //fechaRetorna = sumarRestarHorasFecha(fechaInicio, Calendar.MONTH, 1);
                }
                break;
                case 2: {// ETipoPlan.MES
                    fechaRetorna = sumarRestarHorasFecha(fechaInicio, Calendar.MONTH, 1);
                }
                break;
                case 3: {// ETipoPlan.BIMESTRE
                    fechaRetorna = sumarRestarHorasFecha(fechaInicio, Calendar.MONTH, 2);
                }
                break;
                case 4: {// ETipoPlan.TRIMETESTRE
                    fechaRetorna = sumarRestarHorasFecha(fechaInicio, Calendar.MONTH, 3);
                }
                break;
                case 5: {// ETipoPlan.CUATRIMETESTRE
                    fechaRetorna = sumarRestarHorasFecha(fechaInicio, Calendar.MONTH, 4);
                }
                break;
                case 6: {// ETipoPlan.SEMESTRE
                    fechaRetorna = sumarRestarHorasFecha(fechaInicio, Calendar.MONTH, 6);
                }
                break;
                case 7: {// ETipoPlan.ANIO
                    fechaRetorna = sumarRestarHorasFecha(fechaInicio, Calendar.YEAR, 1);
                }
                break;
            }
            fechaFinal = fechaTemporal.format(fechaRetorna);
        } catch (ParseException | NumberFormatException ex) {
            throw ex;
        }
        return fechaFinal;
    }

    /**
     *
     * @param fecha
     * @param tipo
     * @param horas
     * @return
     */
    public Date sumarRestarHorasFecha(Date fecha, int tipo, int horas) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(tipo, horas); // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
    }
    
    /**
     * 
     */
    private void generarRecibo() {                                                                              
        Long id_clinte_paquete = (this.clientePaqueteDto.getId() != null && this.clientePaqueteDto.getId() > 0) ? this.clientePaqueteDto.getId() :  this.operacion.getLastIdClientePaquete(String.valueOf(this.clienteDto.getId()));
        String ruta = "recibo_mini.jrxml";
        Map params = new HashMap<>();            
        params.put("ID_CLIENTE_PAQUETE", id_clinte_paquete.toString());                        
        Util.generarReportes(ruta, params);        
    }   

    /**
     *
     * @param evt
     */
    private void setVerificaPaqueteTiquetera(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_setVerificaPaqueteTiquetera
        Double valorTotal = 0.0;
        ComboDto comboPaq = (ComboDto) this.cmbPaquete.getSelectedItem();
        ComboDto comboDes = (ComboDto) this.cmbDescuento.getSelectedItem();
        if (!Util.getVacio(comboPaq.getCodigo())) {
            valorTotal += Double.parseDouble(comboPaq.getAssistant());
            if (!Util.getVacio(comboPaq.getAuxiliar()) && (comboPaq.getAuxiliar().equals(String.valueOf(ESiNo.SI.getId())))) {
                this.panelTiquetera.setVisible(true);
                this.lbl_apazamiento.setVisible(false);
                this.txtFecha_fin.setVisible(false);
            } else {
                this.panelTiquetera.setVisible(false);
                this.lbl_apazamiento.setVisible(true);
                this.txtFecha_fin.setVisible(true);
            }
            if (!this.txtDias_tiquetera.getText().equals("")) {
                int diasTiquetera = Short.parseShort(this.txtDias_tiquetera.getText());
                //valorTotal = (diasTiquetera * valorTotal);
            }
            if (!Util.getVacio(comboDes.getCodigo())) {
                double descuento = Double.parseDouble(comboDes.getAuxiliar());
                if (descuento > 100) {
                    valorTotal -= descuento;
                } else {
                    descuento = descuento / 100;
                    descuento = valorTotal * descuento;
                    valorTotal -= descuento;
                }
            }
        } else {
            this.panelTiquetera.setVisible(false);
            this.lbl_apazamiento.setVisible(false);
            this.txtFecha_fin.setVisible(false);
        }
        this.txtPrecio_base.setText(comboPaq.getAssistant());
        this.txtTolal_pagar.setText(valorTotal.toString());
    }//GEN-LAST:event_setVerificaPaqueteTiquetera

    /**
     *
     * @param evt
     */
    private void setValidaDiasTiquetera(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_setValidaDiasTiquetera
        if (!this.txtDias_tiquetera.getText().equals("")) {
            int diasTiquetera = Short.parseShort(this.txtDias_tiquetera.getText());
            if (diasTiquetera > 30) {
                JLabel label = new JLabel("El valor para el numero de dias de tiquetera no puede exceder los 30 días");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
            } else if (diasTiquetera < 1) {
                JLabel label = new JLabel("El valor para el número de dias de tiquetera no puede ser menor a 1");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
            } else {
                Double valorTotal = 0.0;
                ComboDto comboPaq = (ComboDto) this.cmbPaquete.getSelectedItem();
                ComboDto comboDes = (ComboDto) this.cmbDescuento.getSelectedItem();

                valorTotal += Double.parseDouble(comboPaq.getAssistant());
                //valorTotal = (diasTiquetera * valorTotal);
                if (!Util.getVacio(comboDes.getCodigo())) {
                    double descuento = Double.parseDouble(comboDes.getAuxiliar());
                    if (descuento > 100) {
                        valorTotal -= descuento;
                    } else {
                        descuento = descuento / 100;
                        descuento = valorTotal * descuento;
                        valorTotal -= descuento;
                    }
                }
                this.txtPrecio_base.setText(comboPaq.getAssistant());
                this.txtTolal_pagar.setText(valorTotal.toString());
            }
        }
    }//GEN-LAST:event_setValidaDiasTiquetera

    /**
     *
     * @param evt
     */
    private void setValidaSoloNumeros(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_setValidaSoloNumeros
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            getToolkit().beep();
            evt.consume();
            JLabel label = new JLabel("Solo esta permitido el ingreso de números");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, label, "Error de ingreso de datos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_setValidaSoloNumeros

    
    public void autoclose() {
        //Must schedule the close before the dialog becomes visible
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();     
        s.schedule(new Runnable() {
            public void run() {                                
                try {
                    Robot robot = new Robot();
                    //se presiona la tecla escape
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                } catch (AWTException ex) {
                    Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 5, TimeUnit.SECONDS);
    }
    
    /**
     *
     * @param evt
     */
    private void setCalculaDescuento(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_setCalculaDescuento
        setVerificaPaqueteTiquetera(evt);
    }//GEN-LAST:event_setCalculaDescuento

    /**
     *
     * @param evt
     */
    private void setCalculaValorTotalConPrecioBase(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_setCalculaValorTotalConPrecioBase
        if (!this.txtPrecio_base.getText().equals("")) {
            double precioBase = Double.parseDouble(this.txtPrecio_base.getText());
            if (precioBase > 1000) {
                Double valorTotal = 0.0;
                ComboDto comboPaq = (ComboDto) this.cmbPaquete.getSelectedItem();
                ComboDto comboDes = (ComboDto) this.cmbDescuento.getSelectedItem();
                if (!Util.getVacio(comboPaq.getCodigo())) {
                    valorTotal += precioBase;
                    if (!this.txtDias_tiquetera.getText().equals("")) {
                        int diasTiquetera = Short.parseShort(this.txtDias_tiquetera.getText());
                        valorTotal = (diasTiquetera * valorTotal);
                    }
                    if (!Util.getVacio(comboDes.getCodigo())) {
                        double descuento = Double.parseDouble(comboDes.getAuxiliar());
                        if (descuento > 100) {
                            valorTotal -= descuento;
                        } else {
                            descuento = descuento / 100;
                            descuento = precioBase * descuento;
                            valorTotal -= descuento;
                        }
                    }
                }
                this.txtTolal_pagar.setText(valorTotal.toString());
            } else {
                JLabel label = new JLabel("El precio base no debe ser inferior a 1000 pesos");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Error de ingreso de datos", JOptionPane.WARNING_MESSAGE);
                this.txtPrecio_base.setText("");
            }
        }
    }//GEN-LAST:event_setCalculaValorTotalConPrecioBase

    private void tblPaquetessetEditarProducto(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPaquetessetEditarProducto
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            int fila = this.tblPaquetes.getSelectedRow();
            TablaDto dto = (TablaDto) this.table.getData().get(fila);
            try {        
                editarPaquete(dto.getDato1());
            } catch (Exception ex) {
                Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_tblPaquetessetEditarProducto

    /**
     * 
     * @param evt 
     */
    private void btnDeletesetRegistrarPagoPlan(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletesetRegistrarPagoPlan
        // TODO add your handling code here:
        try {
            int option = JOptionPane.showConfirmDialog(null, "está a punto de eliminar este registro. ¿Desea continuar?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(option == JOptionPane.YES_OPTION) {
                boolean delete = false;
                String idPaquete = String.valueOf(this.clientePaqueteDto.getId());
                delete = this.operacion.deleteClientePaquete(idPaquete);   
                if (delete) { 
                    this.clientePaqueteDto = new ClientePaqueteDto();
                    setClear();
                    setConsultarTablePaquetes(); 
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmRegistrarPagos.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_btnDeletesetRegistrarPagoPlan

    private void btnRecibosetRegistrarPagoPlan(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecibosetRegistrarPagoPlan
        // TODO add your handling code here:
        this.generarRecibo();
    }//GEN-LAST:event_btnRecibosetRegistrarPagoPlan

    private void rbtEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtEfectivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtEfectivoActionPerformed

    private void rbtTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtTarjetaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtTarjetaActionPerformed

    public ClienteDto getClienteDto() {
        return clienteDto;
    }

    public void setClienteDto(ClienteDto clienteDto) {
        this.clienteDto = clienteDto;
    }

    public UsuarioDto getUsuarioSessionDto() {
        return usuarioSessionDto;
    }

    public void setUsuarioSessionDto(UsuarioDto usuarioSessionDto) {
        this.usuarioSessionDto = usuarioSessionDto;
    }

    public String getTipoViene() {
        return tipoViene;
    }

    public void setTipoViene(String tipoViene) {
        this.tipoViene = tipoViene;
    }

    public boolean isRegistraAsistencia() {
        return registraAsistencia;
    }

    public void setRegistraAsistencia(boolean registraAsistencia) {
        this.registraAsistencia = registraAsistencia;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnRecibo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbDescuento;
    private javax.swing.JComboBox cmbPaquete;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCantidad_paquetes;
    private javax.swing.JLabel lblDocumento_cliente;
    private javax.swing.JLabel lblFotoCliente;
    private javax.swing.JLabel lblGenero;
    private javax.swing.JLabel lblNombre_cliente;
    private javax.swing.JLabel lblPaquete;
    private javax.swing.JLabel lblPrecio_base;
    private javax.swing.JLabel lblResultado;
    private javax.swing.JLabel lblTiquetera;
    private javax.swing.JLabel lbl_apazamiento;
    private javax.swing.JPanel panelCliente;
    private javax.swing.JPanel panelTiquetera;
    private javax.swing.JRadioButton rbtEfectivo;
    private javax.swing.JRadioButton rbtTarjeta;
    private javax.swing.JTable tblPaquetes;
    private javax.swing.JTextField txtDias_tiquetera;
    private com.toedter.calendar.JDateChooser txtFecha_fin;
    private com.toedter.calendar.JDateChooser txtFecha_inicio;
    private javax.swing.JTextField txtPrecio_base;
    private javax.swing.JTextField txtTolal_pagar;
    // End of variables declaration//GEN-END:variables
}
