/*
 * Copyright (c) 2018.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package gui;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTableModel extends AbstractTableModel {

    private String[] tableHeader = {"nazwa pliku", "rozmiar", "data modyfikacji"};

    private List<File> fileList;

    public FileTableModel(){
        this(new ArrayList<>());
    }

    public FileTableModel(List<File> files) {
        fileList = files;
    }

    public void setFilesList(List<File> filesList){
        this.fileList = filesList;
    }

    @Override
    public String getColumnName(int i) {
        return tableHeader[i];
    }

    @Override
    public int getRowCount() {
        return fileList.size();
    }

    @Override
    public int getColumnCount() {
        return tableHeader.length;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        File file = fileList.get(i);

        switch (i1){
            case 0:
                return file.getName();
            case 1:
                long fileSize = file.length()/1024;
                if (fileSize < 1024)
                    return String.format("%.1f KB", (float) fileSize);
                else
                    return String.format("%.1f MB", (float)(fileSize/1024f));
            case 2:
                return String.format("%tc", file.lastModified());
        }
        return null;
    }
}
