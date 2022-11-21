package ru.cs.vsu.berezin_y_a;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Sector {
    public static void drawSector(
            final GraphicsContext graphicsContext,
            final int x_center,
            final int y_center,
            final int R,
            final double firstAngle,
            final double secondAngle,
            final Color color1,
            final Color color2) throws Exception {

        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        if (checkAngles(firstAngle, secondAngle)) {
            drawPartOfSectorNew(
                    pixelWriter,
                    x_center,
                    y_center,
                    R,
                    firstAngle,
                    secondAngle,
                    color1,
                    color2
            );
        } else {
            throw new Exception(" Углы больше 2 * pi");
        }
    }

    private static int getQuarterPoint(double x, double y, int x_center, int y_center) {
        if (x >= x_center && y < y_center) {
            return 1;
        } else {
            if (x < x_center && y <= y_center) {
                return 2;
            } else {
                if (x <= x_center && y > y_center) {
                    return 3;
                }
            }
        }
        return 4;
    }

    private static void drawPartOfSectorNew(
            final PixelWriter pixelWriter,
            final int x_center,
            final int y_center,
            final int R,
            final double firstAngle,
            final double secondAngle,
            final Color color1,
            final Color color2) {

        final int x_firstPoint = (int) (x_center + R * Math.cos(firstAngle));
        final int y_firstPoint = (int) (y_center - R * Math.sin(firstAngle));
        final int x_secondPoint = (int) (x_center + R * Math.cos(secondAngle));
        final int y_secondPoint = (int) (y_center - R * Math.sin(secondAngle));

        List<Integer> filledQuarters = new ArrayList<>();

        int quarterFirstPoint = getQuarterPoint(x_firstPoint, y_firstPoint, x_center, y_center);
        int quarterSecondPoint = getQuarterPoint(x_secondPoint, y_secondPoint, x_center, y_center);

        filledQuarters.add(quarterFirstPoint);
        filledQuarters.add(quarterSecondPoint);

        for (int i = 1; i < 5; i++) {
            if (!filledQuarters.contains(i)) {
                if (quarterFirstPoint < quarterSecondPoint) {
                    if (i > quarterFirstPoint && i < quarterSecondPoint) {
                        filledQuarters.add(i);
                    }
                } else {
                    if (i < quarterFirstPoint || i > quarterSecondPoint) {
                        filledQuarters.add(i);
                    }
                }
            }
        }

        interpolationSector(pixelWriter, x_center, y_center, R, x_firstPoint, y_firstPoint,
                x_secondPoint, y_secondPoint, filledQuarters, color1, color2);
    }

    private static boolean checkAngles(double firstAngle, double secondAngle) {
        return firstAngle <= 2 * Math.PI && secondAngle <= 2 * Math.PI;
    }

    private static double distanceBetweenPoints(int x, int y, int x_center, int y_center) {
        return (x - x_center) * (x - x_center) + (y - y_center) * (y - y_center);
    }

    private static void interpolationSector(
            final PixelWriter pixelWriter,
            final int x_center,
            final int y_center,
            final int R,
            final int x_firstPoint,
            final int y_firstPoint,
            final int x_secondPoint,
            final int y_secondPoint,
            final List<Integer> filledQuarters,
            final Color color1,
            final Color color2) {

        double red, green, blue;
        int xStart = x_center - R;
        int yStart = y_center - R;
        int quarterFirstPoint = getQuarterPoint(x_firstPoint, y_firstPoint, x_center, y_center);
        int quarterSecondPoint = getQuarterPoint(x_secondPoint, y_secondPoint, x_center, y_center);
        double part;
        double D1, D2;

        /*
        Определяется так. Предположим, у нас есть 3 точки: А(х1,у1), B(х2,у2), С(х3,у3).
        Через точки А и B проведена прямая. И нам надо определить, как расположена точка С относительно прямой АB.
        Для этого вычисляем значение:

        D = (х3 - х1) * (у2 - у1) - (у3 - у1) * (х2 - х1)
        - Если D = 0 - значит, точка С лежит на прямой АБ.
        - Если D < 0 - значит, точка С лежит слева от прямой.
        - Если D > 0 - значит, точка C лежит справа от прямой.
        */

        for (int row = 0; row < 2 * R; row++) {
            xStart++;

            for (int col = 0; col < 2 * R; col++) {
                yStart++;

                if (filledQuarters.contains(getQuarterPoint(xStart, yStart, x_center, y_center))) {
                    double tmp = distanceBetweenPoints(xStart, yStart, x_center, y_center);

                    D1 = (xStart - x_center) * (y_firstPoint - y_center) -
                            (yStart - y_center) * (x_firstPoint - x_center);

                    D2 = (xStart - x_center) * (y_secondPoint - y_center) -
                            (yStart - y_center) * (x_secondPoint - x_center);

                    if (tmp < R * R) {
                        if (quarterSecondPoint - quarterFirstPoint <= 1
                                && quarterSecondPoint - quarterFirstPoint >= 0
                                || quarterFirstPoint == 4 && quarterSecondPoint == 1) {
                            if (D1 > 0 && D2 < 0) {
                                part = Math.sqrt(tmp);

                                red = (color1.getRed() + (color2.getRed() - color1.getRed()) * part / R);
                                green = (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * part / R);
                                blue = (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * part / R);

                                pixelWriter.setColor(xStart, yStart, new Color(red, green, blue, 1));
                            }
                        } else {
                            if (D1 > 0 || D2 < 0) {
                                part = Math.sqrt(tmp);

                                red = (color1.getRed() + (color2.getRed() - color1.getRed()) * part / R);
                                green = (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * part / R);
                                blue = (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * part / R);

                                pixelWriter.setColor(xStart, yStart, new Color(red, green, blue, 1));
                            }
                        }
                    }

                }
            }

            yStart = y_center - R;
        }
    }
}
