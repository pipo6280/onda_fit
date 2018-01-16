/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gimnasio.views;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import static com.digitalpersona.onetouch.processing.DPFPTemplateStatus.TEMPLATE_STATUS_FAILED;
import static com.digitalpersona.onetouch.processing.DPFPTemplateStatus.TEMPLATE_STATUS_READY;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.gimnasio.controller.Operaciones;
import com.gimnasio.model.ClienteDto;
import com.gimnasio.model.HiloBusqueda;
import com.gimnasio.model.UsuarioDto;
import java.awt.Image;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.gimnasio.util.Util;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javazoom.jl.player.Player;

/**
 *
 * @author user
 */
public class frmHuella extends javax.swing.JDialog {

    private DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();
    private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();
    private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();

    private static String TEMPLATE_PROPERTY = "template";
    private DPFPFeatureSet featuresverificacion;
    private DPFPFeatureSet featuresinscripcion;
    private List<DPFPTemplate> listTemplates;

    private List<ClienteDto> listClientesHuellas;
    private List<List<ClienteDto>> listClientes;

    private DPFPTemplate template;
    private Operaciones operacion;

    private BufferedInputStream usuarioIncorrectoCache;
    private BufferedInputStream registradoCache;
    private FileInputStream usuarioIncorrecto;
    private Player playUsuarioIncorrecto;
    private FileInputStream registrado;
    private Player playRegistrado;

    private List<HiloBusqueda> listHilos;
    private boolean cambia = false;
    private UsuarioDto usuarioDto;
    private ClienteDto clienteDto;
    private int cantidadHuellas;
    private short tipoProceso;
    private frmClientesIngresos frmClienteIngreso;
    private frmClientes frmCliente;

    private String rutaHuellas = "huellas/";
    private String extension = ".fpt";

    private UsuarioDto usuarioSessionDto;

    /**
     *
     * @param operacion
     * @param parent
     * @param modal
     * @param cliDto
     * @param tipoProceso
     * @param frmCliente
     */
    public frmHuella(Operaciones operacion, javax.swing.JFrame parent, boolean modal, ClienteDto cliDto, short tipoProceso, frmClientes frmCliente) {
        super(parent, modal);
        initComponents();
        this.cambia = false;
        this.operacion = operacion;
        try {
            this.listClientesHuellas = this.operacion.getClienteHuellasDatos(null);
            this.listTemplates = new ArrayList();
            FileInputStream stream;
            File fileHuella;
            for (ClienteDto dto : this.listClientesHuellas) {
                if (dto.getPersonaDto().getNumeroIdentificacion() != null) {
                    fileHuella = new File(this.rutaHuellas + dto.getPersonaDto().getNumeroIdentificacion() + this.extension);
                    if (fileHuella.exists()) {
                        try {
                            stream = new FileInputStream(fileHuella);
                            byte[] data = new byte[stream.available()];
                            stream.read(data);
                            stream.close();
                            DPFPTemplate t = DPFPGlobal.getTemplateFactory().createTemplate();
                            t.deserialize(data);
                            setTemplate(t);
                            dto.getPersonaDto().setTemplateHuella(getTemplate());
                        } catch (IOException | IllegalArgumentException ex) {
                            try {
                                throw ex;
                            } catch (IOException ex1) {
                                Logger.getLogger(frmHuella.class.getName()).log(Level.SEVERE, null, ex1);
                            }
                        }
                    }
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        if (this.listClientesHuellas.size() > 0) {
            this.listClientes = (List<List<ClienteDto>>) Util.getDivideArray(this.listClientesHuellas, Util.CANTIDAD_DIVIDE_ARRAY);
        }

        this.frmCliente = frmCliente;
        this.cantidadHuellas = 0;
        Util.setCentrarJFrame(null, this);
        this.setResizable(false);
        this.lblIndiceDerecho.setText(null);

        this.tipoProceso = tipoProceso;
        this.clienteDto = cliDto;

        if (this.clienteDto.getId() != null) {
            this.lblCodigo.setText(this.clienteDto.getId().toString());
        } else {
            int id = this.listClientesHuellas.size() + 1;
            this.lblCodigo.setText(String.valueOf(id));
        }
        this.lblEstudiante.setText(this.clienteDto.getPersonaDto().getNombreCompleto());
        switch (tipoProceso) {
            case 1: {
                this.btnGuardar.setEnabled(false);
            }
            break;
            case 2: {
                this.lblEstudiante.setText(null);
                this.btnGuardar.setVisible(false);
            }
            break;
            default: {
                this.lblEstudiante.setText(null);
                this.btnGuardar.setVisible(false);
                this.btnCancelar.setText("Cerrar");
            }
            break;
        }
    }

    /**
     *
     * @param operacion
     * @param parent
     * @param modal
     * @param tipoProceso
     * @param frmClienteIngreso
     */
    public frmHuella(Operaciones operacion, javax.swing.JFrame parent, boolean modal, short tipoProceso, frmClientesIngresos frmClienteIngreso) {
        super(parent, modal);
        initComponents();
        this.cambia = false;

        this.operacion = operacion;
        this.tipoProceso = tipoProceso;
        this.clienteDto = new ClienteDto();
        try {
            this.listTemplates = new ArrayList();
            this.listClientesHuellas = this.operacion.getClienteHuellasDatos(null);
            FileInputStream stream;
            File fileHuella;
            for (ClienteDto dto : this.listClientesHuellas) {
                if (dto.getPersonaDto().getNumeroIdentificacion() != null) {
                    fileHuella = new File(this.rutaHuellas + dto.getPersonaDto().getNumeroIdentificacion() + this.extension);
                    if (fileHuella.exists()) {
                        try {
                            stream = new FileInputStream(fileHuella);
                            byte[] data = new byte[stream.available()];
                            stream.read(data);
                            stream.close();
                            DPFPTemplate t = DPFPGlobal.getTemplateFactory().createTemplate();
                            t.deserialize(data);
                            setTemplate(t);
                            dto.getPersonaDto().setTemplateHuella(getTemplate());
                        } catch (IOException | IllegalArgumentException ex) {
                            try {
                                throw ex;
                            } catch (IOException ex1) {
                                Logger.getLogger(frmHuella.class.getName()).log(Level.SEVERE, null, ex1);
                            }
                        }
                    }
                }
            }
            if (this.listClientesHuellas.size() > 0) {
                this.listClientes = (List<List<ClienteDto>>) Util.getDivideArray(this.listClientesHuellas, Util.CANTIDAD_DIVIDE_ARRAY);
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        this.frmClienteIngreso = frmClienteIngreso;
        this.cantidadHuellas = 0;
        Util.setCentrarJFrame(null, this);
        this.setResizable(false);
        this.lblIndiceDerecho.setText(null);
        if (this.clienteDto.getId() != null) {
            this.lblCodigo.setText(this.clienteDto.getId().toString());
        }
        this.lblEstudiante.setText(this.clienteDto.getPersonaDto().getNombreCompleto());
        switch (tipoProceso) {
            case 1: {
                this.btnGuardar.setEnabled(false);
            }
            break;
            case 2: {
                this.lblCodigo.setText(null);
                this.lblEstudiante.setText(null);
                this.btnGuardar.setVisible(false);
            }
            break;
            default: {
                this.lblEstudiante.setText(null);
                this.btnGuardar.setVisible(false);
                this.btnCancelar.setText("Cerrar");
            }
            break;
        }
    }

    /**
     *
     * @param template
     */
    public void setTemplate(DPFPTemplate template) {
        DPFPTemplate old = this.template;
        this.template = template;
        firePropertyChange(TEMPLATE_PROPERTY, old, template);
    }

    /**
     *
     * @param sample
     * @return
     */
    public Image CrearImagenHuella(DPFPSample sample) {
        return DPFPGlobal.getSampleConversionFactory().createImage(sample);
    }

    public void stop() {
        if (Lector.isStarted()) {
            Lector.stopCapture();
        }
    }

    public void start() {
        if (!this.Lector.isStarted()) {
            Lector.startCapture();
        }
        setEnviarTexto("Utilizando el lector de huella dactilar");
    }

    protected void setIniciar() {
        Lector.addDataListener(new DPFPDataAdapter() {
            @Override
            public void dataAcquired(final DPFPDataEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setEnviarTexto("La huella digital ha sido capturada");
                        setProcesarCaptura(e.getSample());
                    }
                });
            }
        });

        Lector.addReaderStatusListener(new DPFPReaderStatusAdapter() {
            @Override
            public void readerConnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setEnviarTexto("El sensor de huella digital está activado");
                    }
                });
            }

            @Override
            public void readerDisconnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setEnviarTexto("El sensor de huella digital está desactivado");
                    }
                });
            }
        });

        Lector.addSensorListener(new DPFPSensorAdapter() {
            @Override
            public void fingerTouched(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // setEnviarTexto("El dedo ha sido colocado sobre el lector de huella");
                    }
                });
            }

            @Override
            public void fingerGone(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // setEnviarTexto("El dedo ha sido quitado del lector de huella");
                    }
                });
            }
        });

        Lector.addErrorListener(new DPFPErrorAdapter() {
            public void errorReader(final DPFPErrorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        setEnviarTexto("Error: " + e.getError());
                    }
                });
            }
        });
    }

    public void setEnviarTexto(String message) {
        if (message != null) {
            this.txtVisor.setText(this.txtVisor.getText() + "\n" + message);
        }
    }

    public DPFPFeatureSet setExtraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            this.btnCancelar.setEnabled(true);
            return null;
        }
    }

    public void setProcesarCaptura(DPFPSample sample) {
        boolean sinErrores = true;
        this.listHilos = null;
        this.listHilos = null;

        featuresinscripcion = setExtraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
        featuresverificacion = setExtraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
        if (featuresinscripcion != null) {
            try {
                this.btnCancelar.setEnabled(true);
                Reclutador.addFeatures(featuresinscripcion);
                Image image = CrearImagenHuella(sample);
                Util.setDibujarHuella(image, this.lblIndiceDerecho, this);
            } catch (DPFPImageQualityException ex) {
                setEnviarTexto("Las huellas no coinciden, vuelva a intentarlo");
                JLabel label = new JLabel("Las huellas no coinciden, vuelva a intentarlo");
                label.setFont(new Font("consolas", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, label, "Alerta de error", JOptionPane.ERROR_MESSAGE);

                this.Reclutador.clear();
                stop();
                this.btnCancelar.setEnabled(true);
                sinErrores = false;
                System.err.println("Error: " + ex.getMessage());
            } finally {
                this.cantidadHuellas++;
                switch (Reclutador.getTemplateStatus()) {
                    case TEMPLATE_STATUS_READY: {
                        stop();
                        setTemplate(Reclutador.getTemplate());
                    }
                    break;
                    case TEMPLATE_STATUS_FAILED: {
                        Reclutador.clear();
                        stop();
                        setTemplate(null);
                        start();
                    }
                    break;
                }
            }
        }
        switch (this.tipoProceso) {
            case 1: {
                if (this.cantidadHuellas == 4) {
                    setEnviarTexto("Huella capturada, clic en Asignar");
                    JLabel label = new JLabel("Huella capturada, clic en Asignar");
                    label.setFont(new Font("consolas", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, label, "Mensaje de alerta", JOptionPane.INFORMATION_MESSAGE);
                    this.btnGuardar.setEnabled(true);
                }
            }
            break;
            case 2: {
                if (sinErrores) {
                    this.setIdentifyCliente();
                }
            }
            break;
        }
        System.gc();
    }

    public void setIdentifyCliente() {
        try {
            try {
                stop();
                this.cambia = false;
                this.listHilos = new ArrayList();
                this.btnCancelar.setEnabled(false);
                for (List<ClienteDto> lis : this.listClientes) {
                    HiloBusqueda h = new HiloBusqueda(this, this.operacion, this.frmClienteIngreso);
                    h.setFeaturesverificacion(featuresverificacion);
                    h.setUsuarioSessionDto(usuarioSessionDto);
                    h.setLblEstudiante(this.lblEstudiante);
                    h.setLblCodigo(this.lblCodigo);
                    h.setListHilos(this.listHilos);
                    h.setIngresaAsistencia(true);
                    h.setListPlantillas(lis);
                    this.listHilos.add(h);
                }
                for (HiloBusqueda h : this.listHilos) {
                    h.start();
                }
            } catch (Exception e) {
                this.btnCancelar.setEnabled(true);
                setEnviarTexto("Error al verificar los datos de la huella.");
                System.err.println("Error al verificar los datos de la huella.");
            } finally {
                Reclutador.clear();
                stop();
            }
        } catch (Exception ex) {
            this.btnCancelar.setEnabled(true);
            System.err.println(ex.getMessage() + "error al registrar");
            Logger.getLogger(frmHuella.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DPFPTemplate getTemplate() {
        return template;
    }

    public boolean isCambia() {
        return cambia;
    }

    public void setCambia(boolean cambia) {
        this.cambia = cambia;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblIndiceDerecho = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblEstudiante = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtVisor = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                getCerrarPanel(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                getCerrarPanel(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                getAbrirPanel(evt);
            }
        });

        lblIndiceDerecho.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIndiceDerecho.setText("Huella dactilar");
        lblIndiceDerecho.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/add.png"))); // NOI18N
        btnGuardar.setText("Asignar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAsignarHuella(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/gimnasio/files/cancel.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCancelarCapturaHuella(evt);
            }
        });

        jLabel1.setText("Código:");

        lblCodigo.setText("Código del cliente");

        jLabel3.setText("Cliente:");

        lblEstudiante.setText("Nombre del cliente");
        lblEstudiante.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jScrollPane1.setRequestFocusEnabled(false);
        jScrollPane1.setVerifyInputWhenFocusTarget(false);

        txtVisor.setEditable(false);
        txtVisor.setColumns(20);
        txtVisor.setFont(new java.awt.Font("Consolas", 0, 10)); // NOI18N
        txtVisor.setRows(5);
        txtVisor.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtVisor.setFocusable(false);
        jScrollPane1.setViewportView(txtVisor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnCancelar))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(lblIndiceDerecho, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblEstudiante))
                .addGap(11, 11, 11)
                .addComponent(lblIndiceDerecho, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setCancelarCapturaHuella(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setCancelarCapturaHuella
        Reclutador.clear();
        stop();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dispose();
    }//GEN-LAST:event_setCancelarCapturaHuella

    private void getAbrirPanel(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_getAbrirPanel
        setIniciar();
        start();
    }//GEN-LAST:event_getAbrirPanel

    private void getCerrarPanel(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_getCerrarPanel
        Reclutador.clear();
        stop();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dispose();
    }//GEN-LAST:event_getCerrarPanel

    private void setAsignarHuella(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setAsignarHuella
        boolean reemplaza = true;
        File file = new File(this.rutaHuellas + this.clienteDto.getPersonaDto().getNumeroIdentificacion() + this.extension);
        if (file.exists()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    String.format("La huella para el cliente \"%1$s\" realmente existe.\nDesea reemplazarla?", this.clienteDto.getPersonaDto().getNombreCompleto()),
                    "Información de captura de huella",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.NO_OPTION) {
                reemplaza = false;
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                reemplaza = false;
            }
        }
        if (reemplaza) {
            try {
                FileOutputStream stream;
                stream = new FileOutputStream(file);
                stream.write(this.template.serialize());
                stream.close();
                ImageIcon imagen = new ImageIcon(getClass().getResource("/com/gimnasio/files/finger-print-2.png"));
                this.frmCliente.getLblHuellaDactilar().setIcon(imagen);
                this.frmCliente.getLblHuellaDactilar().repaint();
                this.clienteDto.getPersonaDto().setHuellaDactilar(this.template.serialize());
                // this.template = null;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(frmHuella.class
                        .getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(frmHuella.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        Reclutador.clear();
        this.dispose();
        this.btnGuardar.setEnabled(false);
        this.cantidadHuellas = 0;
        Reclutador.clear();
        stop();
        this.dispose();

        Reclutador.clear();
        stop();

    }//GEN-LAST:event_setAsignarHuella

    public Operaciones getOperacion() {
        return operacion;
    }

    public void setOperacion(Operaciones operacion) {
        this.operacion = operacion;
    }

    public UsuarioDto getUsuarioDto() {
        return usuarioDto;
    }

    public void setUsuarioDto(UsuarioDto usuarioDto) {
        this.usuarioDto = usuarioDto;
    }

    public short getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(short tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public UsuarioDto getUsuarioSessionDto() {
        return usuarioSessionDto;
    }

    public void setUsuarioSessionDto(UsuarioDto usuarioSessionDto) {
        this.usuarioSessionDto = usuarioSessionDto;
    }

    public JTextArea getTxtVisor() {
        return txtVisor;
    }

    public void setTxtVisor(JTextArea txtVisor) {
        this.txtVisor = txtVisor;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void setBtnCancelar(JButton btnCancelar) {
        this.btnCancelar = btnCancelar;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(JButton btnGuardar) {
        this.btnGuardar = btnGuardar;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblEstudiante;
    private javax.swing.JLabel lblIndiceDerecho;
    private javax.swing.JTextArea txtVisor;
    // End of variables declaration//GEN-END:variables
}
