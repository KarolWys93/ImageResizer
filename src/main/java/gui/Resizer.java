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

    private static final String DIRECTORY_NAME = "resized";
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
        startConvert(directoryPath, outputFileType, scale, null);
    }


    public void startConvert(Path directoryPath, String outputFileType, int scale, String name){

        directoryPath = Paths.get(directoryPath.toString(), DIRECTORY_NAME);
        new File(directoryPath.toString()).mkdirs();


        int cpu = Runtime.getRuntime().availableProcessors();
        System.out.println("Processors: " + cpu);
        ExecutorService exec = Executors.newFixedThreadPool(cpu);

        int fileCounter = 1;
        for (File file:filesSet) {
            Path finalDirectoryPath = directoryPath;

            String fileName = null;
            if (name != null)
                fileName = String.format("%d%s", fileCounter++, name);

            String finalFileName = fileName;
            exec.submit(() -> {
                boolean success = resize(file, finalDirectoryPath, outputFileType, scale, finalFileName);
                if (progressListener != null) {
                    SwingUtilities.invokeLater(() -> progressListener.convertComplete(file, success));
                }
            });
        }

        exec.shutdown();

    }

    private boolean resize(File file, Path directoryPath, String outputFileType, int scale, String name){

        try {
            float scaleFactor = scale/100f;
            BufferedImage originalImage = ImageIO.read(file);
            int newWidth = (int)(scaleFactor*originalImage.getWidth());
            int newHeight = (int)(scaleFactor*originalImage.getHeight());

            File outputFile;
            if (name != null) {
                outputFile = Paths.get(directoryPath.toString(), name + "." + outputFileType).toFile();
            }
            else {
                outputFile = Paths.get(directoryPath.toString(), changeFileExtension(file.getName(), outputFileType)).toFile();
            }

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

    private String changeFileExtension(String fileName, String type){

        String[] nameParts = fileName.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < nameParts.length-1; i++) {
            stringBuilder.append(nameParts[i]).append(".");
        }
        stringBuilder.append(type);
        return stringBuilder.toString();
    }

    public interface ConvertProgressListener{
        void convertComplete(File convertedFile, boolean success);
    }

}
