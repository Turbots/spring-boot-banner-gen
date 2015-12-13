package be.ordina.cloudfoundry.banner;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Banner {

    private Color[][] colors;
    private char[][] characters;
    private boolean dark;

    public Banner(final Color[][] colors, final char[][] characters, final boolean dark) {
        if (colors.length != characters.length) {
            throw new IllegalStateException("color and char arrays should match");
        }
        this.colors = colors;
        this.characters = characters;
        this.dark = dark;
    }

    public String getAnsi() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < colors.length; i++) {
            Color[] colorRow = colors[i];
            for (int j = 0; j < colorRow.length; j++) {
                Color pixelColor = colorRow[j];
                stringBuilder.append("${AnsiColor.").append(BannerGenerator.ANSI_COLORS.get(pixelColor)).append("}").append(characters[i][j]);
            }
            stringBuilder.append("\r\n");
        }

        return stringBuilder.toString();
    }

    public String getHtml() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < colors.length; i++) {
            Color[] colorRow = colors[i];
            for (int j = 0; j < colorRow.length; j++) {
                Color pixelColor = colorRow[j];
                stringBuilder.append("<span style='color:").append(String.format("#%02x%02x%02x", pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue())).append("'>").append(replaceSpace(characters[i][j])).append("</span>");
            }
            stringBuilder.append("<br>");
        }

        return stringBuilder.toString();
    }

    private String replaceSpace(char c) {
        if (c == ' ') {
            return "&nbsp;";
        } else {
            return new String(new char[]{c});
        }
    }
}
