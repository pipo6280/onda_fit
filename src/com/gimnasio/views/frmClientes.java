/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gimnasio.views;

import com.gimnasio.controller.*;
import com.gimnasio.model.*;
import com.gimnasio.model.enums.EGenero;
import com.gimnasio.util.Util;
import com.google.common.base.Joiner;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author rodolfo
 */
public class frmClientes extends javax.swing.JInternalFrame {

    private final ComboModel comboTipoDocumentos;
    private List<ComboDto> listTipoDocumentos;

    private UsuarioDto usuarioSessionDto;
    private final ClienteDto clienteDto;
    private final frmPrincipal padre;
    private Operaciones operacion;

    private String rutaHuellas = "huellas/";
    private String extension = ".fpt";

    /**
     *
     * @tutorial Creates new form frmCliente
     * @param padre
     * @param operacion
     * @throws java.lang.Exception
     */
    public frmClientes(frmPrincipal padre, Operaciones operacion) throws Exception {
        initComponents();
        this.operacion = operacion;
        this.clienteDto = new ClienteDto();
        this.padre = padre;

        ComboDto inicio;
        this.comboTipoDocumentos = new ComboModel();
        this.comboTipoDocumentos.getLista().clear();
        this.listTipoDocumentos = this.operacion.getTipoDocumentos();
        inicio = new ComboDto("", "-------------");
        this.listTipoDocumentos.add(0, inicio);
        this.comboTipoDocumentos.getLista().addAll(this.listTipoDocumentos);
        this.comboTipoDocumentos.setSelectedItem(inicio);
        this.cmbTipo_documento.setModel(this.comboTipoDocumentos);

        setNoFile(this.lblFotoCliente);
        setNoFile(this.lblHuellaDactilar);
    }

    /**
     *
     * @tutorial Creates new form frmCliente
     * @param padre
     * @param operacion
     * @param numeroDocumento
     * @throws java.lang.Exception
     */
    public frmClientes(frmPrincipal padre, Operaciones operacion, String numeroDocumento) throws Exception {
        initComponents();
        this.operacion = operacion;
        this.padre = padre;

        ComboDto inicio;
        this.comboTipoDocumentos = new ComboModel();
        this.comboTipoDocumentos.getLista().clear();
        this.listTipoDocumentos = this.operacion.getTipoDocumentos();
        inicio = new ComboDto("", "-------------");
        this.listTipoDocumentos.add(0, inicio);
        this.comboTipoDocumentos.getLista().addAll(this.listTipoDocumentos);
        this.comboTipoDocumentos.setSelectedItem(inicio);
        this.cmbTipo_documento.setModel(this.comboTipoDocumentos);

        ClienteDto clientDto = new ClienteDto();
        List<ClienteDto> listClientes = this.operacion.getClienteDatos(null, numeroDocumento);
        for (ClienteDto cliDto : listClientes) {
            if (cliDto.getPersonaDto().getNumeroIdentificacion().equals(numeroDocumento)) {
                clientDto = cliDto;
                break;
            }
        }
        this.clienteDto = clientDto;
        this.setCargarDatosClientes();
    }

    /**
     * @tutorial Method Description: llena la información del cliente en el
     * formulario
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     */
    public final void setCargarDatosClientes() {
        try {
            this.txtPrimer_nombre.setText(clienteDto.getPersonaDto().getPrimerNombre());
            this.txtSegundo_nombre.setText(clienteDto.getPersonaDto().getSegundoNombre());
            this.txtPrimer_apellido.setText(clienteDto.getPersonaDto().getPrimerApellido());
            this.txtSegundo_apellido.setText(clienteDto.getPersonaDto().getSegundoApellido());
            this.cmbTipo_documento.setSelectedIndex(clienteDto.getPersonaDto().getTipoIdentificacion());
            this.txtDocumento.setText(clienteDto.getPersonaDto().getNumeroIdentificacion());
            if (clienteDto.getPersonaDto().getGenero() == EGenero.MASCULINO.getId()) {
                this.rbtMasculino.setSelected(true);
            }
            if (clienteDto.getPersonaDto().getGenero() == EGenero.FEMENIMO.getId()) {
                this.rbtFemenino.setSelected(true);
            }
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(clienteDto.getPersonaDto().getFechaNacimiento());
            this.txtFecha_nacimiento.setDate(date);
            this.txtDireccion.setText(clienteDto.getPersonaDto().getDireccion());
            this.txtBarrio.setText(clienteDto.getPersonaDto().getBarrio());
            this.txtFijo.setText(clienteDto.getPersonaDto().getTelefono());
            this.txtMovil.setText(clienteDto.getPersonaDto().getMovil());
            this.txtEmail.setText(clienteDto.getPersonaDto().getEmail());

            if (this.clienteDto.getPersonaDto().getFotoPerfil() != null && this.clienteDto.getPersonaDto().getFotoPerfil().trim().length() > 0) {
                File file = new File("fotos/" + this.clienteDto.getPersonaDto().getFotoPerfil());
                if (file.exists()) {
                    Image image = new ImageIcon(file.getAbsolutePath()).getImage();
                    Util.setPintarFotoPerfil(image, this.lblFotoCliente);
                } else {
                    setNoFile(this.lblFotoCliente);
                }
            } else {
                setNoFile(this.lblFotoCliente);
            }
            File fileHuella = new File(this.rutaHuellas + this.clienteDto.getPersonaDto().getNumeroIdentificacion() + this.extension);
            if (fileHuella.exists()) {
                File file = new File("files/finger-print-128-128.png");
                if (file.exists()) {
                    Image image = new ImageIcon(file.getAbsolutePath()).getImage();
                    Util.setPintarFotoPerfil(image, this.lblHuellaDactilar);
                } else {
                    setNoFile(this.lblHuellaDactilar);
                }
            } else {
                setNoFile(this.lblHuellaDactilar);
            }
        } catch (Exception ex) {
            JLabel label = new JLabel("Se ha presentado un error, intente nuevamente");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, label, "Alerta de error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(frmClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected final void setNoFile(JLabel lblFoto) {
        File file = new File("files/no-file.png");
        if (file.exists()) {
            Image image = new ImageIcon(file.getAbsolutePath()).getImage();
            Util.setPintarFotoPerfil(image, lblFoto);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbtGenero = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblTitulo_foto = new javax.swing.JLabel();
        lblFotoCliente = new javax.swing.JLabel();
        btnCapturarFoto = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblTitulo_huella = new javax.swing.JLabel();
        btnCapturarHuella = new javax.swing.JButton();
        lblHuellaDactilar = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtSegundo_nombre = new javax.swing.JTextField();
        txtPrimer_nombre = new javax.swing.JTextField();
        txtDocumento = new javax.swing.JTextField();
        lblPrimer_nombre = new javax.swing.JLabel();
        lblSegundo_nombre = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblSegundo_apellido = new javax.swing.JLabel();
        cmbTipo_documento = new javax.swing.JComboBox();
        lblTipo_documento = new javax.swing.JLabel();
        lblDocumento = new javax.swing.JLabel();
        txtPrimer_apellido = new javax.swing.JTextField();
        txtSegundo_apellido = new javax.swing.JTextField();
        lblFecha_nacimiento = new javax.swing.JLabel();
        txtMovil = new javax.swing.JTextField();
        lblMovil = new javax.swing.JLabel();
        rbtMasculino = new javax.swing.JRadioButton();
        rbtFemenino = new javax.swing.JRadioButton();
        lblGenero = new javax.swing.JLabel();
        txtEmail = new javax.swing.JFormattedTextField();
        lblEmail = new javax.swing.JLabel();
        lblBarrio = new javax.swing.JLabel();
        txtBarrio = new javax.swing.JTextField();
        txtFijo = new javax.swing.JTextField();
        lblFijo = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        lblDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        txtFecha_nacimiento = new com.toedter.calendar.JDateChooser();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                setCloseIframeCliente(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "AGREGAR CLIENTE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTitulo_foto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTitulo_foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo_foto.setText("Foto");

        lblFotoCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFotoCliente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblFotoCliente.setPreferredSize(new java.awt.Dimension(128, 128));

        btnCapturarFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/camera-add.png"))); // NOI18N
        btnCapturarFoto.setText("Tomar Foto");
        btnCapturarFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCapturarFotoPerfil(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFotoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblTitulo_foto, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnCapturarFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(lblTitulo_foto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFotoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCapturarFoto)
                .addGap(10, 10, 10))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTitulo_huella.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTitulo_huella.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo_huella.setText("Huella");

        btnCapturarHuella.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/finger-print.png"))); // NOI18N
        btnCapturarHuella.setText("Capturar Huella");
        btnCapturarHuella.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCapturarHuella(evt);
            }
        });

        lblHuellaDactilar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHuellaDactilar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblHuellaDactilar.setPreferredSize(new java.awt.Dimension(128, 128));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCapturarHuella, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTitulo_huella, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblHuellaDactilar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(lblTitulo_huella)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHuellaDactilar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCapturarHuella)
                .addGap(10, 10, 10))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtSegundo_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidateSoloLetras(evt);
            }
        });

        txtPrimer_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidateSoloLetras(evt);
            }
        });

        txtDocumento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                setValidarNumeroDocumento(evt);
            }
        });
        txtDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidaSoloNumeros(evt);
            }
        });

        lblPrimer_nombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPrimer_nombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrimer_nombre.setText("Primer Nombre");

        lblSegundo_nombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSegundo_nombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSegundo_nombre.setText("Segundo Nombre");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Primer Apellido");

        lblSegundo_apellido.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSegundo_apellido.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSegundo_apellido.setText("Segundo Apellido");

        cmbTipo_documento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblTipo_documento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTipo_documento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTipo_documento.setText("Tipo Documento");

        lblDocumento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDocumento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDocumento.setText("Nro. Documento");

        txtPrimer_apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidateSoloLetras(evt);
            }
        });

        txtSegundo_apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidateSoloLetras(evt);
            }
        });

        lblFecha_nacimiento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblFecha_nacimiento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFecha_nacimiento.setText("Fecha Nacimiento");

        txtMovil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidaSoloNumeros(evt);
            }
        });

        lblMovil.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblMovil.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMovil.setText("Movil");

        rbtGenero.add(rbtMasculino);
        rbtMasculino.setText("Masculino");

        rbtGenero.add(rbtFemenino);
        rbtFemenino.setText("Femenino");

        lblGenero.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblGenero.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGenero.setText("Genero");

        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                setValidaEmail(evt);
            }
        });

        lblEmail.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmail.setText("E-mail");

        lblBarrio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblBarrio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBarrio.setText("Barrio");

        txtFijo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                setValidaSoloNumeros(evt);
            }
        });

        lblFijo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblFijo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFijo.setText("Fijo");

        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/floppy-icon.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setGuardarCliente(evt);
            }
        });

        lblDireccion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDireccion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDireccion.setText("Dirección");

        txtFecha_nacimiento.setDateFormatString("yyyy-MM-dd");
        txtFecha_nacimiento.setMaxSelectableDate(new java.util.Date(253370786466000L));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnGuardar))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTipo_documento)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblPrimer_nombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbTipo_documento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPrimer_apellido)
                            .addComponent(txtPrimer_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblSegundo_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblSegundo_apellido)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(lblDocumento)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSegundo_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSegundo_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lblDireccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(79, 79, 79)
                                        .addComponent(lblGenero))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblMovil, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblEmail, javax.swing.GroupLayout.Alignment.TRAILING))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMovil, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(rbtMasculino)
                                        .addGap(12, 12, 12)
                                        .addComponent(rbtFemenino))
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(lblFecha_nacimiento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFecha_nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(lblBarrio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(lblFijo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFijo, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(31, 31, 31))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrimer_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSegundo_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrimer_nombre)
                    .addComponent(lblSegundo_nombre))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrimer_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSegundo_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(lblSegundo_apellido))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbTipo_documento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTipo_documento))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDocumento)))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFecha_nacimiento)
                    .addComponent(rbtMasculino)
                    .addComponent(rbtFemenino)
                    .addComponent(lblGenero)
                    .addComponent(txtFecha_nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBarrio)
                        .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDireccion)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMovil, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMovil))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFijo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblFijo)))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail))
                .addGap(30, 30, 30)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public UsuarioDto getUsuarioSessionDto() {
        return usuarioSessionDto;
    }

    public void setUsuarioSessionDto(UsuarioDto usuarioSessionDto) {
        this.usuarioSessionDto = usuarioSessionDto;
    }

    public JLabel getLblFotoCliente() {
        return lblFotoCliente;
    }

    public void setLblFotoCliente(JLabel lblFotoCliente) {
        this.lblFotoCliente = lblFotoCliente;
    }

    public JLabel getLblHuellaDactilar() {
        return lblHuellaDactilar;
    }

    public void setLblHuellaDactilar(JLabel lblHuellaDactilar) {
        this.lblHuellaDactilar = lblHuellaDactilar;
    }

    /**
     * @tutorial Method Description: llena la información capturada del
     * ----------------------------- formulario en clienteDto
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     */
    protected void setLlenarClienteDto() {
        ComboDto cmbTipoDocumento = (ComboDto) this.cmbTipo_documento.getSelectedItem();
        clienteDto.getPersonaDto().setPrimerNombre(this.txtPrimer_nombre.getText());
        clienteDto.getPersonaDto().setSegundoNombre(this.txtSegundo_nombre.getText());
        clienteDto.getPersonaDto().setPrimerApellido(this.txtPrimer_apellido.getText());
        clienteDto.getPersonaDto().setSegundoApellido(this.txtSegundo_apellido.getText());
        if (!Util.getVacio(cmbTipoDocumento.getCodigo())) {
            clienteDto.getPersonaDto().setTipoIdentificacion(Short.parseShort(cmbTipoDocumento.getCodigo()));
        } else {
            clienteDto.getPersonaDto().setTipoIdentificacion(Short.parseShort("0"));
        }
        clienteDto.getPersonaDto().setNumeroIdentificacion(this.txtDocumento.getText());
        if (this.rbtFemenino.isSelected()) {
            clienteDto.getPersonaDto().setGenero(EGenero.FEMENIMO.getId());
        }
        if (this.rbtMasculino.isSelected()) {
            clienteDto.getPersonaDto().setGenero(EGenero.MASCULINO.getId());
        }
        if (this.txtFecha_nacimiento.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaNacimiento = sdf.format(this.txtFecha_nacimiento.getDate().getTime());
            clienteDto.getPersonaDto().setFechaNacimiento(fechaNacimiento);
        } else {
            clienteDto.getPersonaDto().setFechaNacimiento(null);
        }
        clienteDto.getPersonaDto().setDireccion(this.txtDireccion.getText());
        clienteDto.getPersonaDto().setBarrio(this.txtBarrio.getText());
        clienteDto.getPersonaDto().setMovil(this.txtMovil.getText());
        clienteDto.getPersonaDto().setTelefono(this.txtFijo.getText());
        clienteDto.getPersonaDto().setEmail(this.txtEmail.getText());
    }

    /**
     * @tutorial Method Description: valida la informacion de los clientes para
     * ----------------------------- realizar el proceso de guardado de los
     * ----------------------------- datos
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     * @param evt
     * @return void
     */
    private void setGuardarCliente(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setGuardarCliente
        try {
            setLlenarClienteDto();
            List<String> listMessage = this.operacion.setGuardarCliente(this.clienteDto, true);
            if (listMessage.size() < 1 && (this.clienteDto.getId() != null && this.clienteDto.getId() > 0)) {
                try {
                    frmRegistrarPagos frmPago = new frmRegistrarPagos(this, operacion, clienteDto);
                    frmPago.setSize(frmPrincipal.jdstPrincipal.getWidth(), frmPrincipal.jdstPrincipal.getHeight() - 1);
                    frmPrincipal.jdstPrincipal.add(frmPago);
                    frmPago.setTipoViene("1");
                    frmPago.setUsuarioSessionDto(usuarioSessionDto);
                    frmPago.setResizable(true);
                    frmPago.setClosable(true);
                    frmPago.setVisible(true);
                } catch (Exception ex) {
                    JLabel label = new JLabel("Se ha presentado un error para registrar el pago del paquete, intente nuevamente");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, label, "Alerta de error", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(frmClientes.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JLabel label = new JLabel("<html>Verífique la siguiente lista de campos obligatorios:\n<ol>" + Joiner.on("\n").join(listMessage) + "</ol></html>");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_setGuardarCliente

    private void setCloseIframeCliente(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_setCloseIframeCliente
        this.padre.setClienteView(null);
        if (this.clienteDto.getId() == null && this.clienteDto.getPersonaDto().getHuellaDactilar() != null) {
            File file = new File(this.rutaHuellas + this.clienteDto.getPersonaDto().getNumeroIdentificacion() + this.extension);
            if (file.exists()) {
                file.delete();
            }
        }
    }//GEN-LAST:event_setCloseIframeCliente

    private void setValidateSoloLetras(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_setValidateSoloLetras
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            getToolkit().beep();
            evt.consume();
            JLabel label = new JLabel("Solo esta permitido el ingreso de letras");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, label, "Error de ingreso de datos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_setValidateSoloLetras

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

    private void setValidarNumeroDocumento(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_setValidarNumeroDocumento
        setLlenarClienteDto();
        if (!Util.getVacio(this.clienteDto.getPersonaDto().getNumeroIdentificacion())) {
            try {
                String idPersona = "";
                if (this.clienteDto.getPersonaDto().getId() != null) {
                    idPersona = this.clienteDto.getPersonaDto().getId().toString();
                }
                boolean correcto = this.operacion.setValidaDocumentoCliene(idPersona, this.clienteDto.getPersonaDto().getNumeroIdentificacion());
                if (!correcto) {
                    JLabel label = new JLabel("<html>El cliente con número de documento: <b>" + this.clienteDto.getPersonaDto().getNumeroIdentificacion() + "</b> ya se encuentra registrado</html>");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, label, "Error de ingreso de datos", JOptionPane.WARNING_MESSAGE);
                    this.txtDocumento.setText(null);
                }
            } catch (SQLException ex) {
                Logger.getLogger(frmClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_setValidarNumeroDocumento

    private void setValidaEmail(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_setValidaEmail
        if (!this.txtEmail.getText().equals("") && !setValidaEmail(this.txtEmail.getText().toLowerCase())) {
            this.txtEmail.setText(null);
            JLabel label = new JLabel("<html>El correo ingresado: <b>" + this.txtEmail.getText() + "</b> no es correcto</html>");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, label, "Error de ingreso de datos", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_setValidaEmail

    /**
     *
     * @param evt
     */
    private void setCapturarHuella(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setCapturarHuella
        try {
            setLlenarClienteDto();
            List<String> listMessage;
            listMessage = this.operacion.setGuardarCliente(this.clienteDto, false);
            if (listMessage.size() < 1) {
                frmHuella frm = new frmHuella(this.operacion, this.padre, true, this.clienteDto, Short.parseShort("1"), this);
                frm.setVisible(true);
            } else {
                JLabel label = new JLabel("<html>Verífique la siguiente lista de campos obligatorios:\n<ol><li>Debe ingresar los datos del cliente <br>para realizar el proceso de captura de la huella</li></ol></html>");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_setCapturarHuella

    /**
     *
     * @param evt
     */
    private void setCapturarFotoPerfil(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setCapturarFotoPerfil
        try {
            setLlenarClienteDto();
            List<String> listMessage;
            listMessage = this.operacion.setGuardarCliente(this.clienteDto, false);
            if (listMessage.size() < 1) {
                WebcamViewer webCam = new WebcamViewer();
                webCam.setClienteDto(this.clienteDto);
                webCam.setFrmCliente(this);
                SwingUtilities.invokeLater(webCam);
            } else {
                JLabel label = new JLabel("<html>Verífique la siguiente lista de campos obligatorios:\n<ol><li>Debe ingresar los datos del cliente <br>para realizar el proceso de captura de la huella</li></ol></html>");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_setCapturarFotoPerfil
    /**
     * Valida si es correcta la dirección de correo electrónica dada.
     *
     * @param email
     * @return true si es correcta o false si no lo es.
     */
    protected static boolean setValidaEmail(String email) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]+[.[a-zA-Z0-9_-]+]*@[a-z0-9][\\w\\.-]*[a-z0-9]\\.[a-z][a-z\\.]*[a-z]$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapturarFoto;
    private javax.swing.JButton btnCapturarHuella;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox cmbTipo_documento;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblBarrio;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFecha_nacimiento;
    private javax.swing.JLabel lblFijo;
    private javax.swing.JLabel lblFotoCliente;
    private javax.swing.JLabel lblGenero;
    private javax.swing.JLabel lblHuellaDactilar;
    private javax.swing.JLabel lblMovil;
    private javax.swing.JLabel lblPrimer_nombre;
    private javax.swing.JLabel lblSegundo_apellido;
    private javax.swing.JLabel lblSegundo_nombre;
    private javax.swing.JLabel lblTipo_documento;
    private javax.swing.JLabel lblTitulo_foto;
    private javax.swing.JLabel lblTitulo_huella;
    private javax.swing.JRadioButton rbtFemenino;
    private javax.swing.ButtonGroup rbtGenero;
    private javax.swing.JRadioButton rbtMasculino;
    private javax.swing.JTextField txtBarrio;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JFormattedTextField txtEmail;
    private com.toedter.calendar.JDateChooser txtFecha_nacimiento;
    private javax.swing.JTextField txtFijo;
    private javax.swing.JTextField txtMovil;
    private javax.swing.JTextField txtPrimer_apellido;
    private javax.swing.JTextField txtPrimer_nombre;
    private javax.swing.JTextField txtSegundo_apellido;
    private javax.swing.JTextField txtSegundo_nombre;
    // End of variables declaration//GEN-END:variables
}
