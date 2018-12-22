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
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private BufferedImage image;

    public ImagePanel(File imageFile) {
        setImage(imageFile);
    }

    public void setImage(File imageFile){
        if (imageFile != null) {
            try {
                image = ImageIO.read(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.repaint();
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
        }else {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(0,0, frameSize, frameSize);
        }
    }
}
