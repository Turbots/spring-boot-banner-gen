package be.ordina.cloudfoundry.banner;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.color.ColorSpace;

/**
 * Computes the CIE94 distance between two COLORS.
 * <p>
 * See https://github.com/michael-simons/dfx-mosaic/blob/public/src/main/java/de/dailyfratze/mosaic/images/CIE94ColorDistance.java)
 * </p>
 *
 * @author Michael Simons
 */
@Component
public class CIE94ColorDistanceCalculator {

    public double getColorDistance(final Color color1, final Color color2) {
        float[] lab1 = toLab(color1);
        float[] lab2 = toLab(color2);

        // Make it more readable
        double L1 = lab1[0];
        double a1 = lab1[1];
        double b1 = lab1[2];
        double L2 = lab2[0];
        double a2 = lab2[1];
        double b2 = lab2[2];

        // CIE94 coefficients for graphic arts
        double kL = 1;
        double K1 = 0.045;
        double K2 = 0.015;

        // Weighting factors
        double sl = 1.0;
        double kc = 1.0;
        double kh = 1.0;

        // See http://en.wikipedia.org/wiki/Color_difference#CIE94
        double c1 = Math.sqrt(a1 * a1 + b1 * b1);
        double deltaC = c1 - Math.sqrt(a2 * a2 + b2 * b2);
        double deltaA = a1 - a2;
        double deltaB = b1 - b2;
        double deltaH = Math.sqrt(Math.max(0.0, deltaA * deltaA + deltaB * deltaB - deltaC * deltaC));

        return Math.sqrt(Math.max(0.0, Math.pow((L1 - L2) / (kL * sl), 2) + Math.pow(deltaC / (kc * (1 + K1 * c1)), 2) + Math.pow(deltaH / (kh * (1 + K2 * c1)), 2.0)));
    }

    private float[] toLab(Color color) {
        float[] xyz = color.getColorComponents(ColorSpace.getInstance(ColorSpace.CS_CIEXYZ), null);

        return xyzToLab(xyz);
    }

    private float[] xyzToLab(float[] colorValue) {
        double l = f(colorValue[1]);
        double L = 116.0 * l - 16.0;
        double a = 500.0 * (f(colorValue[0]) - l);
        double b = 200.0 * (l - f(colorValue[2]));
        return new float[]{(float) L, (float) a, (float) b};
    }

    private double f(float t) {
        if (t > 216.0 / 24389.0) {
            return Math.cbrt(t);
        } else {
            return (1.0 / 3.0) * Math.pow(29.0 / 6.0, 2) * t + (4.0 / 29.0);
        }
    }
}
