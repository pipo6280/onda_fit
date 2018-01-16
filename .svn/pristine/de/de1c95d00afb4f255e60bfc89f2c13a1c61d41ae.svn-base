/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gimnasio.model;

import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author user
 */
public class ComboModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList lista;
    private ComboDto objeto;

    public ComboModel() {
        this.lista = new ArrayList();
        this.objeto = new ComboDto(null, null);
    }

    public ArrayList getLista() {
        return lista;
    }

    public void setLista(ArrayList lista) {
        this.lista = lista;
    }

    @Override
    public int getSize() {
        return this.lista.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getElementAt(int index) {
        return this.lista.get(index);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.objeto = (ComboDto) anItem;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getSelectedItem() {
        return this.objeto;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
