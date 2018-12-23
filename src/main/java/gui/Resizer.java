/*
 * Copyright (c) 2018.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        directoryPath = Paths.get(directoryPath.toString(), "resized");
        new File(directoryPath.toString()).mkdirs();


        int cpu = Runtime.getRuntime().availableProcessors();
        System.out.println(cpu);
        ExecutorService exec = Executors.newFixedThreadPool(cpu);


        List<Runnable> tasksList = new ArrayList<>();
        for (File file:filesSet) {
            Path finalDirectoryPath = directoryPath;
            tasksList.add(() -> {
                boolean success = resize(file, finalDirectoryPath, outputFileType, scale);
                if (progressListener!= null){
                    SwingUtilities.invokeLater(() -> progressListener.convertComplete(file, success));
                }
            });
        }

        for (Runnable task:tasksList) {
            exec.submit(task);
        }

    }

    private boolean resize(File file, Path directoryPath, String outputFileType, int scale){

        try {
            float scaleFactor = scale/100f;
            BufferedImage originalImage = ImageIO.read(file);
            int newWidth = (int)(scaleFactor*originalImage.getWidth());
            int newHeight = (int)(scaleFactor*originalImage.getHeight());

            File outputFile = Paths.get(directoryPath.toString(), file.getName()).toFile();

            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            ImageIO.write(resizedImage, outputFileType, outputFile);
            System.gc();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public interface ConvertProgressListener{
        void convertComplete(File convertedFile, boolean success);
    }

}
