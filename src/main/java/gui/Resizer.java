/*
 * Copyright (c) 2018.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package gui;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Resizer {

    private ConvertProgressListener progressListener;
    private HashSet<File> filesSet = new HashSet<>();

    public void setProgressListener(ConvertProgressListener progressListener){
        this.progressListener = progressListener;
    }

    public void addFilesToConvert(List<File> files){
        filesSet.addAll(files);
    }

    public void removeFiles(List<File> files){
        filesSet.removeAll(files);
    }

    public List<File> getFiles(){
        return new ArrayList<>(filesSet);
    }


    public void startConvert(Path directoryPath, String outputFileType, int scale){

    }


    public interface ConvertProgressListener{
        void convertComplete(File convertedFile, boolean success);
    }

}
