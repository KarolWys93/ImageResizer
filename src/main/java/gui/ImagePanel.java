/*
 * Copyright (c) 2018.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package gui;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JPanel {
    private ExecutorService exec;
    private BufferedImage image;
    private ImageIcon loadingGif;
    private boolean loading = false;

    public ImagePanel(File imageFile) {
        loadingGif = new ImageIcon(ImagePanel.class.getResource("/images/loading.gif"));
        exec = Executors.newSingleThreadExecutor();
        setImage(imageFile);
    }

    public void setImage(File imageFile) {
        if (imageFile != null) {
            loading = true;
            image = null;
            System.gc();
            exec.execute(() -> {
                        try {
                            image = ImageIO.read(imageFile);
                            SwingUtilities.invokeLater(() -> {
                                loading = false;
                                this.repaint();
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int frameSize = this.getWidth() > this.getHeight() ? this.getHeight() : this.getWidth();
        if (image!= null) {
            int imageSize = image.getWidth() < image.getHeight() ? image.getHeight() : image.getWidth();
            float scale = (float)frameSize/(float)imageSize;

            int imageWidth = (int)(image.getWidth()*scale);
            int imageHeight = (int)(image.getHeight()*scale);
            int imageX = (this.getWidth()/2) - (imageWidth/2);
            int imageY = (this.getHeight()/2) - (imageHeight/2);

            g.drawImage(image, imageX, imageY, imageWidth, imageHeight, this);
        } else if(loading) {
            int imageWidth = loadingGif.getImage().getWidth(null);
            int imageHeight = loadingGif.getImage().getHeight(null);
            int imageX = (this.getWidth()/2) - (imageWidth/2);
            int imageY = (this.getHeight()/2) - (imageHeight/2);
            g.drawImage(loadingGif.getImage(), imageX, imageY, imageWidth, imageHeight, this);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(0,0, frameSize, frameSize);
        }
    }
}
