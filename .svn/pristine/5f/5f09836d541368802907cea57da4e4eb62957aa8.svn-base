/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gimnasio.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author user
 */
public class TablaModelo implements TableModel {

    private LinkedList list;
    private LinkedList data;
    private String[] headTable;
    private int[] widthColumna;

    public TablaModelo(String[] headTable) {
        this.list = new LinkedList();
        this.data = new LinkedList();
        this.headTable = headTable;
    }

    public LinkedList getList() {
        return list;
    }

    public void setList(LinkedList list) {
        this.list = list;
    }

    public LinkedList getData() {
        return data;
    }

    public void setData(LinkedList data) {
        this.data = data;
    }

    public String[] getHeadTable() {
        return headTable;
    }

    public void setHeadTable(String[] headTable) {
        this.headTable = headTable;
    }

    public int[] getWidthColumna() {
        return widthColumna;
    }

    public void setWidthColumna(int[] widthColumna) {
        this.widthColumna = widthColumna;
    }

    private void setOrganizaLista(TableModelEvent evento) {
        int i;
        // Bucle para todos los suscriptores en la lista, se llama al metodo
        // tableChanged() de los mismos, pasándole el evento.
        for (i = 0; i < this.list.size(); i++) {
            ((TableModelListener) this.list.get(i)).tableChanged(evento);
        }
    }

    @Override
    public int getRowCount() {
        return this.data.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        return this.headTable.length;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.headTable[columnIndex];
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String dato = "";
        try {
            if (this.data.size() > 0) {
                TablaDto tabla = (TablaDto) this.data.get(rowIndex);
                Class[] types = new Class[]{};
                Method metodo = tabla.getClass().getMethod("getDato" + (columnIndex + 1), types);
                dato = (String) metodo.invoke(tabla, new Object[0]);
            }
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TablaModelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TablaModelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TablaModelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TablaModelo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TablaModelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dato;
    }

    public void setAgregar(TablaDto nuevo) {
        this.data.add(nuevo);
        //this.list.add(nuevo);
        // Avisa a los suscriptores creando un TableModelEvent...
        TableModelEvent evento;
        evento = new TableModelEvent(this, this.getRowCount() - 1, this.getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        this.setOrganizaLista(evento);
    }

    public void setBorrarTodo() {
        for (int fila = 0; fila < this.getRowCount(); fila++) {
            // Se borra la fila 
            this.data.remove(fila);
            // Y se avisa a los suscriptores, creando un TableModelEvent...
            TableModelEvent evento = new TableModelEvent(this, fila, fila, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
            // ... y pasándoselo a los suscriptores
            setOrganizaLista(evento);
        }

    }

    public void setBorrarFila(int fila) {
        this.data.remove(fila);
        // Y se avisa a los suscriptores, creando un TableModelEvent...
        TableModelEvent evento = new TableModelEvent(this, fila, fila, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        // ... y pasándoselo a los suscriptores
        setOrganizaLista(evento);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.list.add(l);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.list.remove(l);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
