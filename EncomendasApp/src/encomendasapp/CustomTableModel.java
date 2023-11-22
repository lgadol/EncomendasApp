package encomendasapp;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

class CustomTableModel extends DefaultTableModel {
    private int editableRow = -1;

    public CustomTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return row == editableRow;
    }

    public void setEditableRow(int row) {
        this.editableRow = row;
    }
}
