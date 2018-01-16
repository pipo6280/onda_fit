/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gimnasio.views;

import com.gimnasio.controller.*;
import com.gimnasio.model.*;
import com.gimnasio.model.enums.EGenero;
import com.gimnasio.model.enums.ESiNo;
import com.gimnasio.util.Util;
import com.google.common.base.Joiner;
import java.awt.Font;
import java.awt.HeadlessException;
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
public class frmUsuarios extends javax.swing.JInternalFrame {

    private ComboModel comboTipoDocumentos;
    private ComboModel comboActivo;
    private ComboModel comboPerfil;

    private List<ComboDto> listTipoDocumentos;
    private List<ComboDto> listPerfiles;
    private List<ComboDto> listActivos;

    private UsuarioDto usuarioSessionDto;
    private UsuarioDto usuarioDto;
    private final frmPrincipal padre;
    private Operaciones operacion;
    private frmBuscarUsuario frmBusca;

    /**
     *
     * @tutorial Creates new form frmCliente
     * @param padre
     * @param operacion
     * @throws java.lang.Exception
     */
    public frmUsuarios(frmPrincipal padre, Operaciones operacion) throws Exception {
        initComponents();
        this.operacion = operacion;
        this.usuarioDto = new UsuarioDto();
        this.padre = padre;
        this.setInitCombos();
        this.btnCambiarClave.setEnabled(false);
        setNoFile(this.lblFotoUsuario);
    }

    /**
     * @tutorial Creates new form frmCliente
     * @param frmBusca
     * @param padre
     * @param operacion
     * @param numeroDocumento
     * @throws java.lang.Exception
     */
    public frmUsuarios(frmPrincipal padre, Operaciones operacion, String numeroDocumento, frmBuscarUsuario frmBusca) throws Exception {
        initComponents();
        this.operacion = operacion;
        this.frmBusca = frmBusca;
        this.padre = padre;

        this.setInitCombos();

        UsuarioDto usuarioTempDto = new UsuarioDto();
        if (!Util.getVacio(numeroDocumento)) {
            List<UsuarioDto> listUsuarios = this.operacion.getUsuarioPersonaDatos(null, numeroDocumento);
            for (UsuarioDto userDto : listUsuarios) {
                if (userDto.getPersonaDto().getNumeroIdentificacion().equals(numeroDocumento)) {
                    usuarioTempDto = userDto;
                    break;
                }
            }
        }
        this.usuarioDto = usuarioTempDto;
        this.setCargarDatosClientes();
    }

    private void setInitCombos() {
        try {
            ComboDto inicio;
            this.comboTipoDocumentos = new ComboModel();
            this.comboTipoDocumentos.getLista().clear();
            this.listTipoDocumentos = this.operacion.getTipoDocumentos();
            inicio = new ComboDto("", "-------------");
            this.listTipoDocumentos.add(0, inicio);
            this.comboTipoDocumentos.getLista().addAll(this.listTipoDocumentos);
            this.comboTipoDocumentos.setSelectedItem(inicio);
            this.cmbTipo_documento.setModel(this.comboTipoDocumentos);

            this.comboPerfil = new ComboModel();
            this.comboPerfil.getLista().clear();
            this.listPerfiles = this.operacion.getPerfiles();
            this.listPerfiles.add(0, inicio);
            this.comboPerfil.getLista().addAll(this.listPerfiles);
            this.comboPerfil.setSelectedItem(inicio);
            this.cmbTipo_usuario.setModel(this.comboPerfil);

            this.comboActivo = new ComboModel();
            this.comboActivo.getLista().clear();
            this.listActivos = this.operacion.getYnActivo();
            this.listActivos.add(0, inicio);
            this.comboActivo.getLista().addAll(this.listActivos);
            this.cmbYn_activo.setModel(this.comboActivo);
            this.cmbYn_activo.setSelectedIndex(ESiNo.SI.getId());
        } catch (Exception ex) {
            Logger.getLogger(frmUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @tutorial Method Description: llena la información del cliente en el
     * formulario
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     */
    public final void setCargarDatosClientes() {
        try {
            this.txtPrimer_nombre.setText(this.usuarioDto.getPersonaDto().getPrimerNombre());
            this.txtSegundo_nombre.setText(this.usuarioDto.getPersonaDto().getSegundoNombre());
            this.txtPrimer_apellido.setText(this.usuarioDto.getPersonaDto().getPrimerApellido());
            this.txtSegundo_apellido.setText(this.usuarioDto.getPersonaDto().getSegundoApellido());
            this.cmbTipo_documento.setSelectedIndex(this.usuarioDto.getPersonaDto().getTipoIdentificacion());
            this.txtDocumento.setText(this.usuarioDto.getPersonaDto().getNumeroIdentificacion());
            if (this.usuarioDto.getPersonaDto().getGenero() == EGenero.MASCULINO.getId()) {
                this.rbtMasculino.setSelected(true);
            }
            if (this.usuarioDto.getPersonaDto().getGenero() == EGenero.FEMENIMO.getId()) {
                this.rbtFemenino.setSelected(true);
            }
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(this.usuarioDto.getPersonaDto().getFechaNacimiento());
            this.txtFecha_nacimiento.setDate(date);
            this.txtDireccion.setText(this.usuarioDto.getPersonaDto().getDireccion());
            this.txtBarrio.setText(this.usuarioDto.getPersonaDto().getBarrio());
            this.txtFijo.setText(this.usuarioDto.getPersonaDto().getTelefono());
            this.txtMovil.setText(this.usuarioDto.getPersonaDto().getMovil());
            this.txtEmail.setText(this.usuarioDto.getPersonaDto().getEmail());

            this.cmbTipo_usuario.setSelectedIndex(this.usuarioDto.getTipoUsuario());
            this.cmbYn_activo.setSelectedIndex(this.usuarioDto.getYnActivo());
            this.txtLoggin.setText(this.usuarioDto.getLoggin());

            if (this.usuarioDto.getPersonaDto().getFotoPerfil().trim().length() > 0) {
                File file = new File("fotos/" + this.usuarioDto.getPersonaDto().getFotoPerfil());
                if (file.exists()) {
                    Image image = new ImageIcon(file.getAbsolutePath()).getImage();
                    Util.setPintarFotoPerfil(image, this.lblFotoUsuario);
                } else {
                    setNoFile(this.lblFotoUsuario);
                }
            } else {
                setNoFile(this.lblFotoUsuario);
            }
        } catch (Exception ex) {
            JLabel label = new JLabel("Se ha presentado un error, intente nuevamente");
            label.setFont(new Font("consolas", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(this, label, "Alerta de error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(frmUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param lblFoto
     */
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
        lblFotoUsuario = new javax.swing.JLabel();
        btnCapturarFoto = new javax.swing.JButton();
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
        lblLoggin = new javax.swing.JLabel();
        lblActivo = new javax.swing.JLabel();
        cmbYn_activo = new javax.swing.JComboBox();
        lblTipo_usuario = new javax.swing.JLabel();
        cmbTipo_usuario = new javax.swing.JComboBox();
        txtLoggin = new javax.swing.JTextField();
        btnCambiarClave = new javax.swing.JButton();

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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "AGREGAR USUARIO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTitulo_foto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTitulo_foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo_foto.setText("Foto");

        lblFotoUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFotoUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblFotoUsuario.setPreferredSize(new java.awt.Dimension(128, 128));

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
                    .addComponent(btnCapturarFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFotoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTitulo_foto, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(lblTitulo_foto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFotoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCapturarFoto)
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
                setGuardarUsuario(evt);
            }
        });

        lblDireccion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblDireccion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDireccion.setText("Dirección");

        txtFecha_nacimiento.setDateFormatString("yyyy-MM-dd");
        txtFecha_nacimiento.setMaxSelectableDate(new java.util.Date(253370786466000L));

        lblLoggin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLoggin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoggin.setText("Usuario");

        lblActivo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblActivo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblActivo.setText("Activo");

        cmbYn_activo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblTipo_usuario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTipo_usuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTipo_usuario.setText("Perfil");

        cmbTipo_usuario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtLoggin.setEditable(false);

        btnCambiarClave.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCambiarClave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/change_password.png"))); // NOI18N
        btnCambiarClave.setText("Cambiar Clave");
        btnCambiarClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCambiarClave(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lblDireccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblActivo)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addGap(79, 79, 79)
                                            .addComponent(lblGenero))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblMovil, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblEmail, javax.swing.GroupLayout.Alignment.TRAILING)))))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbYn_activo, 0, 236, Short.MAX_VALUE)
                                    .addComponent(txtMovil)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(rbtMasculino)
                                        .addGap(12, 12, 12)
                                        .addComponent(rbtFemenino))
                                    .addComponent(txtEmail))))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(lblFecha_nacimiento)
                                        .addGap(10, 10, 10)
                                        .addComponent(txtFecha_nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(lblBarrio)
                                        .addGap(10, 10, 10)
                                        .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblLoggin)
                                            .addComponent(lblFijo))
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtFijo, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtLoggin, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addComponent(lblTipo_usuario)
                                        .addGap(10, 10, 10)
                                        .addComponent(cmbTipo_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addComponent(btnGuardar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnCambiarClave))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTipo_documento)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblPrimer_nombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbTipo_documento, 0, 236, Short.MAX_VALUE)
                                    .addComponent(txtPrimer_apellido))
                                .addGap(50, 50, 50)
                                .addComponent(lblDocumento))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtPrimer_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblSegundo_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblSegundo_apellido))))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSegundo_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSegundo_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
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
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblEmail))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblLoggin)
                        .addComponent(txtLoggin, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblActivo)
                            .addComponent(cmbYn_activo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTipo_usuario)
                            .addComponent(cmbTipo_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(40, 40, 40)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCambiarClave, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
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

    public JLabel getLblFotoUsuario() {
        return lblFotoUsuario;
    }

    public void setLblFotoUsuario(JLabel lblFotoUsuario) {
        this.lblFotoUsuario = lblFotoUsuario;
    }

    /**
     * @tutorial Method Description: llena la información capturada del
     * ----------------------------- formulario en clienteDto
     * @author Eminson Mendoza ~~ emimaster16@gmail.com
     * @date 08/07/2016
     */
    protected void setLlenarUsuarioDto() {
        ComboDto cmbTipoDocumento = (ComboDto) this.cmbTipo_documento.getSelectedItem();
        ComboDto cmbTipoUsuario = (ComboDto) this.cmbTipo_usuario.getSelectedItem();
        ComboDto cmbYnActivo = (ComboDto) this.cmbYn_activo.getSelectedItem();

        this.usuarioDto.getPersonaDto().setPrimerNombre(this.txtPrimer_nombre.getText());
        this.usuarioDto.getPersonaDto().setSegundoNombre(this.txtSegundo_nombre.getText());
        this.usuarioDto.getPersonaDto().setPrimerApellido(this.txtPrimer_apellido.getText());
        this.usuarioDto.getPersonaDto().setSegundoApellido(this.txtSegundo_apellido.getText());

        if (!Util.getVacio(cmbTipoDocumento.getCodigo())) {
            this.usuarioDto.getPersonaDto().setTipoIdentificacion(Short.parseShort(cmbTipoDocumento.getCodigo()));
        } else {
            this.usuarioDto.getPersonaDto().setTipoIdentificacion(Short.parseShort("0"));
        }
        if (!Util.getVacio(cmbTipoUsuario.getCodigo())) {
            this.usuarioDto.setTipoUsuario(Short.parseShort(cmbTipoUsuario.getCodigo()));
        } else {
            this.usuarioDto.setTipoUsuario(Short.parseShort("0"));
        }
        if (!Util.getVacio(cmbYnActivo.getCodigo())) {
            this.usuarioDto.setYnActivo(Short.parseShort(cmbYnActivo.getCodigo()));
        } else {
            this.usuarioDto.setYnActivo(Short.parseShort("0"));
        }

        this.usuarioDto.getPersonaDto().setNumeroIdentificacion(this.txtDocumento.getText());
        if (this.rbtFemenino.isSelected()) {
            this.usuarioDto.getPersonaDto().setGenero(EGenero.FEMENIMO.getId());
        }
        if (this.rbtMasculino.isSelected()) {
            this.usuarioDto.getPersonaDto().setGenero(EGenero.MASCULINO.getId());
        }
        if (this.txtFecha_nacimiento.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaNacimiento = sdf.format(this.txtFecha_nacimiento.getDate().getTime());
            this.usuarioDto.getPersonaDto().setFechaNacimiento(fechaNacimiento);
        } else {
            this.usuarioDto.getPersonaDto().setFechaNacimiento(null);
        }
        this.usuarioDto.getPersonaDto().setDireccion(this.txtDireccion.getText());
        this.usuarioDto.getPersonaDto().setBarrio(this.txtBarrio.getText());
        this.usuarioDto.getPersonaDto().setMovil(this.txtMovil.getText());
        this.usuarioDto.getPersonaDto().setTelefono(this.txtFijo.getText());
        this.usuarioDto.getPersonaDto().setEmail(this.txtEmail.getText());
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
    private void setGuardarUsuario(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setGuardarUsuario
        try {
            setLlenarUsuarioDto();
            List<String> listMessage = this.operacion.setValidarDatosUsuario(this.usuarioDto);
            if (listMessage.size() < 1) {
                try {
                    if (this.usuarioDto.getPersonaDto().getFotoPerfil() == null) {
                        this.usuarioDto.getPersonaDto().setFotoPerfil(this.usuarioDto.getPersonaDto().getNumeroIdentificacion() + ".JPG");
                    }
                    String password = this.operacion.setGuardarDatosUsuario(this.usuarioDto);
                    JLabel label;
                    if (!Util.getVacio(password)) {
                        label = new JLabel("<html>El usuario <b>" + this.usuarioDto.getPersonaDto().getNombreCompleto().toUpperCase() + "</b> se registró correctamente,<br>Usuario: <b>" + this.usuarioDto.getPersonaDto().getNumeroIdentificacion() + "</b><br>La clave del usuario es: <b>" + password + "</b></html>");
                        label.setFont(new Font("consolas", Font.PLAIN, 14));
                        JOptionPane.showMessageDialog(this, label, "Alerta de información", JOptionPane.INFORMATION_MESSAGE);
                        this.setVisible(false);
                    } else {
                        label = new JLabel("<html>Los datos para el usuario <b>" + this.usuarioDto.getPersonaDto().getNombreCompleto().toUpperCase() + "</b> se guardaron correctamente</html>");
                        label.setFont(new Font("consolas", Font.PLAIN, 14));
                        JOptionPane.showMessageDialog(this, label, "Alerta de información", JOptionPane.INFORMATION_MESSAGE);
                        this.setVisible(false);
                    }
                    if (this.frmBusca != null) {
                        this.frmBusca.setConsultarTableUsuarios();
                    }
                } catch (SQLException | HeadlessException ex) {
                    JLabel label = new JLabel("Se ha presentado un error para registrar el pago del paquete, intente nuevamente");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, label, "Alerta de error", JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(frmUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JLabel label = new JLabel("<html>Verífique la siguiente lista de campos obligatorios:\n<ol>" + Joiner.on("\n").join(listMessage) + "</ol></html>");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_setGuardarUsuario

    private void setCloseIframeCliente(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_setCloseIframeCliente
        this.padre.setUsuarioView(null);
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
        setLlenarUsuarioDto();
        if (!Util.getVacio(this.usuarioDto.getPersonaDto().getNumeroIdentificacion())) {
            try {
                String idPersona = "";
                if (this.usuarioDto.getPersonaDto().getId() != null) {
                    idPersona = this.usuarioDto.getPersonaDto().getId().toString();
                }
                boolean correcto = this.operacion.setValidaDocumentoCliene(idPersona, this.usuarioDto.getPersonaDto().getNumeroIdentificacion());
                if (correcto) {
                    this.txtLoggin.setText(this.usuarioDto.getPersonaDto().getNumeroIdentificacion());
                } else {
                    JLabel label = new JLabel("<html>El usuario con número de documento: <b>" + this.usuarioDto.getPersonaDto().getNumeroIdentificacion() + "</b> ya se encuentra registrado</html>");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, label, "Error de ingreso de datos", JOptionPane.WARNING_MESSAGE);
                    this.txtDocumento.setText(null);

                }
            } catch (SQLException ex) {
                Logger.getLogger(frmUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_setValidarNumeroDocumento

    private void setValidaEmail(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_setValidaEmail

        if (!Util.getVacio(this.txtEmail.getText()) && !setValidaEmail(this.txtEmail.getText().toLowerCase())) {
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
    private void setCapturarFotoPerfil(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setCapturarFotoPerfil
        try {
            setLlenarUsuarioDto();
            List<String> listMessage;
            listMessage = this.operacion.setValidarDatosUsuario(this.usuarioDto);
            if (listMessage.size() < 1) {
                WebcamViewer webCam = new WebcamViewer();
                webCam.setUsuarioDto(this.usuarioDto);
                webCam.setFrmUsuario(this);
                SwingUtilities.invokeLater(webCam);
            } else {
                JLabel label = new JLabel("<html>Verífique la siguiente lista de campos obligatorios:\n<ol><li>Debe ingresar los datos del cliente <br>para realizar el proceso de captura de la foto</li>" + Joiner.on("\n").join(listMessage) + "</ol></html>");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_setCapturarFotoPerfil

    private void setCambiarClave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setCambiarClave
        try {
            setLlenarUsuarioDto();
            List<String> listMessage;
            listMessage = this.operacion.setValidarDatosUsuario(this.usuarioDto);
            if (listMessage.size() < 1) {
                this.operacion.setGuardarDatosUsuario(this.usuarioDto);
                String password = String.valueOf(Util.setRandom(1000, 9999));
                if (this.operacion.setCambiarPassword(this.usuarioDto, password)) {
                    JLabel label = new JLabel("<html>Los datos para el usuario <b>" + this.usuarioDto.getPersonaDto().getNombreCompleto().toUpperCase() + "</b> se guardaron correctamente,<br>Usuario: <b>" + this.usuarioDto.getPersonaDto().getNumeroIdentificacion() + "</b><br>La clave del usuario es: <b>" + password + "</b></html>");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, label, "Alerta de información", JOptionPane.INFORMATION_MESSAGE);
                    this.setVisible(false);
                    if (this.frmBusca != null) {
                        this.frmBusca.setConsultarTableUsuarios();
                    }
                }
            } else {
                JLabel label = new JLabel("<html>Verífique la siguiente lista de campos obligatorios:\n<ol>" + Joiner.on("\n").join(listMessage) + "</ol></html>");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de verificación de datos", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(frmUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_setCambiarClave
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
    private javax.swing.JButton btnCambiarClave;
    private javax.swing.JButton btnCapturarFoto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox cmbTipo_documento;
    private javax.swing.JComboBox cmbTipo_usuario;
    private javax.swing.JComboBox cmbYn_activo;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblActivo;
    private javax.swing.JLabel lblBarrio;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFecha_nacimiento;
    private javax.swing.JLabel lblFijo;
    private javax.swing.JLabel lblFotoUsuario;
    private javax.swing.JLabel lblGenero;
    private javax.swing.JLabel lblLoggin;
    private javax.swing.JLabel lblMovil;
    private javax.swing.JLabel lblPrimer_nombre;
    private javax.swing.JLabel lblSegundo_apellido;
    private javax.swing.JLabel lblSegundo_nombre;
    private javax.swing.JLabel lblTipo_documento;
    private javax.swing.JLabel lblTipo_usuario;
    private javax.swing.JLabel lblTitulo_foto;
    private javax.swing.JRadioButton rbtFemenino;
    private javax.swing.ButtonGroup rbtGenero;
    private javax.swing.JRadioButton rbtMasculino;
    private javax.swing.JTextField txtBarrio;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JFormattedTextField txtEmail;
    private com.toedter.calendar.JDateChooser txtFecha_nacimiento;
    private javax.swing.JTextField txtFijo;
    private javax.swing.JTextField txtLoggin;
    private javax.swing.JTextField txtMovil;
    private javax.swing.JTextField txtPrimer_apellido;
    private javax.swing.JTextField txtPrimer_nombre;
    private javax.swing.JTextField txtSegundo_apellido;
    private javax.swing.JTextField txtSegundo_nombre;
    // End of variables declaration//GEN-END:variables
}
