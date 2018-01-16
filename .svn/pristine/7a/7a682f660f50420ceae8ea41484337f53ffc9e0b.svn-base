package com.gimnasio.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author Miguel Carmona
 */
public class Text {

    private static Properties texts = null;
    final static private String name = "es_lang.properties";

    /**
     * @param nombre
     * @return
     * @throws Exception
     */
    public static String getText(String nombre) throws Exception {
        if (texts == null) {
            texts = new Properties();
            String ruta = new File(".").getAbsolutePath().substring(0, new File(".").getAbsolutePath().length() - 1) + "src/";
            texts.load(new FileInputStream(ruta + "Archivos/" + name));
        }
        String retorna = texts.getProperty(nombre).trim();
        return retorna;
    }

}
