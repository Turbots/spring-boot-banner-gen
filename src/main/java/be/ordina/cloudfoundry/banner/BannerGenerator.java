package be.ordina.cloudfoundry.banner;

import be.ordina.cloudfoundry.util.MaxSizeHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class BannerGenerator {

    private Map<Color, Color> nearestColors = new MaxSizeHashMap<>(10000);

    public static Map<Color, String> ANSI_COLORS = new HashMap<>();

    static {
        ANSI_COLORS.put(new Color(0, 0, 0), "BLACK");
        ANSI_COLORS.put(new Color(170, 0, 0), "RED");
        ANSI_COLORS.put(new Color(0, 170, 0), "GREEN");
        ANSI_COLORS.put(new Color(170, 85, 0), "YELLOW");
        ANSI_COLORS.put(new Color(0, 0, 170), "BLUE");
        ANSI_COLORS.put(new Color(170, 0, 170), "MAGENTA");
        ANSI_COLORS.put(new Color(0, 170, 170), "CYAN");
        ANSI_COLORS.put(new Color(170, 170, 170), "WHITE");

        ANSI_COLORS.put(new Color(85, 85, 85), "BRIGHT_BLACK");
        ANSI_COLORS.put(new Color(255, 85, 85), "BRIGHT_RED");
        ANSI_COLORS.put(new Color(85, 255, 85), "BRIGHT_GREEN");
        ANSI_COLORS.put(new Color(255, 255, 85), "BRIGHT_YELLOW");
        ANSI_COLORS.put(new Color(85, 85, 255), "BRIGHT_BLUE");
        ANSI_COLORS.put(new Color(255, 85, 255), "BRIGHT_MAGENTA");
        ANSI_COLORS.put(new Color(85, 255, 255), "BRIGHT_CYAN");
        ANSI_COLORS.put(new Color(255, 255, 255), "BRIGHT_WHITE");
    }

    @Autowired
    private CIE94ColorDistanceCalculator colorDistanceCalculator;

    @Autowired
    private LuminanceCalculator luminanceCalculator;

    @Autowired
    private ImageResizer imageResizer;

    public Banner generateBanner(final MultipartFile file, final boolean dark) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());

            image = imageResizer.resizeImage(image, 115);

            return imageToBanner(image, dark);
        } catch (IOException e) {
            log.error("Could not read image", e);
            return new Banner(new Color[0][0], new char[0][0], dark);
        }
    }

    private Banner imageToBanner(final BufferedImage image, final boolean dark) {
        if (image == null) {
            log.error("Empty image");
            return new Banner(new Color[0][0], new char[0][0], dark);
        }
        int width = image.getWidth();
        int height = image.getHeight();

        Color[][] colors = new Color[height][];
        char[][] characters = new char[height][];

        for (int i = 0; i < height; i++) {
            colors[i] = new Color[width];
            characters[i] = new char[width];
            for (int j = 0; j < width; j++) {
                Color color = new Color(image.getRGB(j, i), false);
                colors[i][j] = calculateNearestColor(color);
                characters[i][j] = calculateCharacterByColor(color, dark);
            }
        }

        return new Banner(colors, characters, dark);
    }

    private Color calculateNearestColor(final Color color) {
        Color nearestColor = nearestColors.get(color);
        if (nearestColor == null) {
            double minimumDistance = Double.MAX_VALUE;
            for (Color ansiColor : ANSI_COLORS.keySet()) {
                double distance = colorDistanceCalculator.getColorDistance(color, ansiColor);
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    nearestColor = ansiColor;
                }
            }
            nearestColors.put(color, nearestColor);
        }
        return nearestColor;
    }

    private char calculateCharacterByColor(final Color color, final boolean dark) {
        int luminance = luminanceCalculator.calculateLuminance(color, dark);

        if (luminance >= 90) {
            return ' ';
        } else if (luminance >= 80) {
            return '.';
        } else if (luminance >= 70) {
            return '*';
        } else if (luminance >= 60) {
            return ':';
        } else if (luminance >= 50) {
            return 'o';
        } else if (luminance >= 40) {
            return '&';
        } else if (luminance >= 30) {
            return '8';
        } else if (luminance >= 20) {
            return '#';
        } else {
            return '@';
        }
    }
}
