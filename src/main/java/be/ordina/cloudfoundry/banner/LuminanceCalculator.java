package be.ordina.cloudfoundry.banner;

import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class LuminanceCalculator {

    private static final double RED_WEIGHT = 0.2126d;
    private static final double GREEN_WEIGHT = 0.7152d;
    private static final double BLUE_WEIGHT = 0.0722d;

    public int calculateLuminance(final Color color, final boolean dark) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        double luminanceDouble;

        if (dark) {
            luminanceDouble = (RED_WEIGHT * (255.0d - red)) + (GREEN_WEIGHT * (255.0d - green)) + (BLUE_WEIGHT * (255.0d - blue));
        } else {
            luminanceDouble = (RED_WEIGHT * red) + (GREEN_WEIGHT * green) + (BLUE_WEIGHT * blue);
        }

        return (int) Math.ceil((luminanceDouble / 255.0d) * 100);
    }
}
