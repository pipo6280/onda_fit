package com.gimnasio.util;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.gimnasio.model.Conexion;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Conexion Educativa
 */
public class Util {

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final char[] CONSTS_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final String urlServidor = "";
    public static final int CANTIDAD_DIVIDE_ARRAY = 20;

    public static void setShowMessage(JInternalFrame frm, String message, String alert, int typeMessage) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("consolas", Font.PLAIN, 14));
        JOptionPane.showMessageDialog(frm, label, alert, typeMessage);
    }

    public static void setShowMessage(JFrame frm, String message, String alert, int typeMessage) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("consolas", Font.PLAIN, 14));
        JOptionPane.showMessageDialog(frm, label, alert, typeMessage);
    }

    public static void setFondoJFrame(JFrame jf, Color color) {
        jf.getContentPane().setBackground(color);
    }

    public static void setCentrarJFrame(JFrame jf, JDialog jd) {
        if (jf != null) {
            jf.setLocationRelativeTo(null);
        } else {
            jd.setLocationRelativeTo(null);
        }
    }

    public static void setFuncionesJFrame(JFrame jf, boolean puedeMaximizar) {
        jf.setResizable(puedeMaximizar);
    }

    public DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            return null;
        }
    }

    public static void setDibujarHuella(Image image, JLabel lblHuella, JFrame jf) {
        lblHuella.setIcon(new ImageIcon(
                image.getScaledInstance(lblHuella.getWidth(), lblHuella.getHeight(), Image.SCALE_DEFAULT)));
        jf.repaint();
    }

    public static void setPintarFotoPerfil(Image image, JLabel lblFoto) {
        lblFoto.setIcon(new ImageIcon(
                image.getScaledInstance(128, 128, Image.SCALE_DEFAULT)));
        lblFoto.repaint();
    }

    public static void setDibujarHuella(Image image, JLabel lblHuella, JDialog jf) {
        lblHuella.setIcon(new ImageIcon(
                image.getScaledInstance(lblHuella.getWidth(), lblHuella.getHeight(), Image.SCALE_DEFAULT)));
        lblHuella.repaint();
        //jf.repaint();
    }

    public static void setTemplate(DPFPTemplate template, DPFPTemplate padre) {
        DPFPTemplate old = padre;
        padre = template;
        //firePropertyChange(TEMPLATE_PROPERTY, old, template);
    }

    public static String getEncriptarMD5(String stringAEncriptar) {
        try {
            MessageDigest msgd = MessageDigest.getInstance("MD5");
            byte[] bytes = msgd.digest(stringAEncriptar.getBytes());
            StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                int bajo = (int) (bytes[i] & 0x0f);
                int alto = (int) ((bytes[i] & 0xf0) >> 4);
                strbCadenaMD5.append(CONSTS_HEX[alto]);
                strbCadenaMD5.append(CONSTS_HEX[bajo]);
            }
            return strbCadenaMD5.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean getVacio(String dato) {
        boolean vacio = false;
        if (dato == null) {
            vacio = true;
        } else if (dato.trim().length() < 1) {
            vacio = true;
        }
        return vacio;
    }

    /**
     *
     * @param arr
     * @param size
     * @return
     */
    public static List<?> getDivideArray(List<?> arr, int size) {
        List<List<?>> lista = new ArrayList();
        double val = arr.size() < size ? 1 : Double.parseDouble(arr.size() + "") / size;
        int cantidad = (int) Math.ceil(val);
        for (int i = 0; i < cantidad; i++) {
            List tempo = new ArrayList();
            int hasta = ((i + 1) * size) == 1 ? 1 : ((i + 1) * size);
            hasta = hasta > arr.size() ? arr.size() : hasta;
            tempo.addAll(arr.subList((i * size), hasta));
            lista.add(tempo);
        }
        return lista;
    }

    public static BufferedReader getLeerArchivo(String url) throws Exception {
        File archivo = new File(url);
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        //fr.close();
        //archivo                 = null;
        return br;
    }

    public static String getQuitaNULL(String val) {
        if (val == null) {
            val = "";
        }
        return val;
    }

    /**
     * Metodo que convierte un documento a una cadena de texto
     *
     * @param url
     * @return
     */
    public static String getContents(String url) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(url));
            StringBuilder builder = new StringBuilder();
            String line;

            // For every line in the file, append it to the string builder
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i != 0) {
                    builder.append("\n" + URLEncoder.encode(line));
                } else {
                    builder.append(URLEncoder.encode(line));
                    i++;
                }
            }

            reader.close();
            return builder.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    /**
     *
     * @param email
     * @return
     */
    public static boolean setValidateEmail(String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    /**
     *
     * @param ruta
     * @param parametros
     */
    public static void generarReportes(String ruta, Map parametros) {
        try {
            parametros.put("RUTA_IMAGENES", "reports/");
            Conexion conexion = new Conexion();
            File fileReport = new File("reports/" + ruta);
            JasperReport load = JasperCompileManager.compileReport(fileReport.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(load, parametros, conexion.getConexion());
            JasperViewer view = new JasperViewer(print, false);
            view.setVisible(true);
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    /**
     * 
     * @param min
     * @param max
     * @return 
     */
    public static int setRandom(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
