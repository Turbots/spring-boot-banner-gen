package be.ordina.cloudfoundry.banner;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;

@Component
public class ImageResizer {

    private static final double ASPECT_RATIO = 0.5d;

    public BufferedImage resizeImage(final BufferedImage image, int width) {
        int newWidth;
        double resizeRatio;
        if (image.getWidth() > width) {
            resizeRatio = (double) width / (double) image.getWidth();
            newWidth = width;
        } else {
            resizeRatio = 1.0d;
            newWidth = image.getWidth();
        }

        int height = (int) (Math.ceil(resizeRatio * ASPECT_RATIO * (double) image.getHeight()));
        Image newImage = image.getScaledInstance(newWidth, height, Image.SCALE_DEFAULT);

        BufferedImage resizedImage = new BufferedImage(newImage.getWidth(null), newImage.getHeight(null), BufferedImage.TYPE_INT_RGB);

        resizedImage.getGraphics().drawImage(newImage, 0, 0, null);
        return resizedImage;
    }
}
