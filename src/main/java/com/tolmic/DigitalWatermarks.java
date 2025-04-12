package com.tolmic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import com.tolmic.pseudorandom.PseudorandomNumbers;
import java.awt.image.BufferedImage;

import static com.tolmic.utils.BitsArrayOperationUtils.convertToString;
import static com.tolmic.utils.BitsArrayOperationUtils.getBitSequence;

public final class DigitalWatermarks {

    //#region local variables
    private static final double q = 10;
    private static final int c = 3;
    private static final int r = 100;

    private static final String INITIAL_IMAGE_PATH = (new File("").getAbsolutePath()) + "\\src\\main\\resources\\imgaes\\image.jpg";
    private static final String IMAGE_WITH_INFORMATION_PATH = (new File("").getAbsolutePath()) + "\\src\\main\\resources\\imgaes\\informationImage.jpg";
    //#endregion

    //#region classes for intermediate counters
    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class RGB {
        int R;
        int G;
        int B;

        public RGB(int rgb) {
            this.R = (rgb >> 16) & 0xff;
            this.G = (rgb >> 8) & 0xff;
            this.B = rgb & 0xff;
        }

        public int toRGB() {
            return (R << 16) | (G << 8) | B;
        }
    }
    //#endregion

    private static void recordImage(BufferedImage image) throws IOException {
        File outputFile = new File(IMAGE_WITH_INFORMATION_PATH);
        ImageIO.write(image, "jpg", outputFile);
    }

    private static boolean checkBounds(int d, int bound) {
        return d - c >= 0 && d + c < bound;
    }

    private static boolean checkAllBounds(int x, int xBound, int y, int yBound) {
        return checkBounds(x, xBound) && checkBounds(y, yBound);
    }


    private static List<Point> getPointSeries(int w, int h) {
        List<Point> points = new ArrayList<>();

        int step = 2 * c + 1;

        for (int y = c; y < h - c - 1; y += step) {
            for (int x = c; x < w - c - 1; x += step) {
                points.add(new Point(x, y));
            }
        }

        return points;
    }

    private static Point[] getPoints(double key, int n, int w, int h) {
        PseudorandomNumbers ps = new PseudorandomNumbers(key);

        List<Point> pointSeries = getPointSeries(w, h);

        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int ind = (int) (pointSeries.size() * ps.run());
            Point p = pointSeries.remove(ind);

            points[i] = p;
        }
        

        return points;
    }

    private static void changeBlueComponent(RGB rgb, int bit) {
        int L = (int) (0.299 * rgb.R + 0.587 * rgb.G + 0.114 * rgb.B);
        rgb.B = (int) (rgb.B + (2 * bit - 1) * L * q);
    }

    private static void putBits(Point[] points, int[] bits, BufferedImage image) throws IOException {
        int n = bits.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < r * c; j++) {
                Point p = points[i * r * c + j];

                RGB rgb = new RGB(image.getRGB(p.x, p.y));

                changeBlueComponent(rgb, bits[i]);
                
                image.setRGB(p.x, p.y, rgb.toRGB());
            }
        }
    }

    private static int getB(BufferedImage image, int x, int y) {
        return new RGB(image.getRGB(x, y)).B;
    }

    private static int getB(BufferedImage image, Point p) {
        return getB(image, p.x, p.y);
    }

    private static double countHorizNeighborhood(BufferedImage image, Point p) {
        double sum = 0;

        for (int x = p.x - c; x <= p.x + c; x++) {
            sum += getB(image, x, p.y);
        }

        return sum;
    }

    private static double countVerticNeighborhood(BufferedImage image, Point p) {
        double sum = 0;

        for (int y = p.y - c; y <= p.y + c; y++) {
            sum += getB(image, p.x, y);
        }

        return sum;
    }

    private static double getBEstimate(BufferedImage image, Point p) {
        double b = getB(image, p);

        double horiz = countHorizNeighborhood(image, p);
        double vertic = countVerticNeighborhood(image, p);

        double sum = horiz + vertic - 2 * b;

        return sum / (4.0 * c);
    }

    public static void putInformation(double key, int[] bits) throws IOException {
        BufferedImage image = ImageIO.read(new File(INITIAL_IMAGE_PATH));

        int width = image.getWidth(), height = image.getHeight();
        int n = bits.length;

        Point[] points = getPoints(key, n * r * c, width, height);

        putBits(points, bits, image);

        recordImage(image);
    }

    public static int[] getInformation(double key, int n) throws IOException {
        BufferedImage image = ImageIO.read(new File(IMAGE_WITH_INFORMATION_PATH));

        int width = image.getWidth();
        int height = image.getHeight();

        Point[] points = getPoints(key, n * r * c, width, height);

        int[] bits = new int[n];

        for (int i = 0; i < n; i++) {
            double sumE = 0;

            for (int j = 0; j < r * c; j++) {
                Point p = points[i * r * c + j];

                int b = getB(image, p.x, p.y);
                double estB = getBEstimate(image, p);

                sumE += estB - b;
            }

            sumE /= c * r;
            bits[i] = sumE < 0 ? 0 : 1;
        }

        return bits;
    }

    public static void main(String[] args) throws IOException {
        String messege = "Hello, Vanya !";

        double key = 12;

        int[] bits = getBitSequence(messege.getBytes());

        String initialBits = convertToString(bits);
        System.out.println("init: " + initialBits);

        putInformation(key, bits);

        int[] decrypted = getInformation(key, bits.length);

        System.out.println("Decr: " + convertToString(decrypted));
    }

}
