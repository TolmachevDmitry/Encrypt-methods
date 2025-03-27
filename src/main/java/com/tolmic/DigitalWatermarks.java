package com.tolmic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import static com.tolmic.utils.BitsArrayOperationUtils.getBitSequence;

public final class DigitalWatermarks {

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

    private static final double q = 0.5;
    private static final int c = 3;
    private static final int r = 10;

    private static final String INITIAL_IMAGE_PATH = (new File("").getAbsolutePath()) + "\\src\\main\\resources\\imgaes\\image.jpg";
    private static final String IMAGE_WITH_INFORMATION_PATH = (new File("").getAbsolutePath()) + "\\src\\main\\resources\\imgaes\\informationImage.jpg";

    // Let it be x-dimension
    private static Comparator<Point> comp = (a, b) -> {
        int h = 2 * c;

        if (a.y != b.y || Math.abs(a.x - b.x) > h) {
            if (a.x != b.x || Math.abs(a.y - b.y) > h) {
                if (a.x <= b.x) {
                    return -1;
                } else if (a.x > b.x) {
                    return 1;
                }
            }
        }

        return 0;
    };

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

    private static Point calculatePoint(PseudorandomNumbers ps, Set<Point> createdPoints, int w, int h) {
        Point p = new Point((int) (w * ps.run()), (int) (h * ps.run()));

        // && !checkAllBounds(p.x, w, p.y, h)
        while (createdPoints.contains(p) || !checkAllBounds(p.x, w, p.y, h)) {
            p = new Point((int) (w * ps.run()), (int) (h * ps.run()));
        }

        return p;
    }

    private static boolean checkSizeSuitable(int bound, int n) {
        return n * Math.pow(2 * c + 1, 2) <= Math.pow(bound, 2);
    }

    public static Point[] diagonall(BufferedImage image, int n) {
        int bound = Math.min(image.getWidth(), image.getHeight());

        if (!checkSizeSuitable(bound, n)) {
            return new Point[0];
        }

        int dW = 1, dH = 1;
        int x = 4, y = 4;

        Point[] points = new Point[n];
        points[0] = new Point(x, y);

        int h = 2 * c + 1;
        for (int i = 0; i < n - 1; i++) {
            if (i % 6 == 0) {
                x += h;
            } else if (i % 6 == 1) {
                y += dH * h;
                dH++;
            } else if (i % 6 == 2) {
                x -= dW * h;
                dW++;
            } else if (i % 6 == 3) {
                y += h;
            } else if (i % 6 == 4) {
                x += dW * h;
                dW++;
            } else {
                y -= dH * h;
                dH++;
            }

            points[i + 1] = new Point(x, y);
        }

        return points;
    }

    private static Point[] getPoints(double key, int n, int w, int h) {
        PseudorandomNumbers ps = new PseudorandomNumbers(key);
        Set<Point> createdPoints = new TreeSet<>(comp);

        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            Point p = calculatePoint(ps, createdPoints, w, h);

            if (n % 10 == 0) {
                System.out.println(i + "-ая точка");
            }

            points[i] = p;
            createdPoints.add(p);
        }

        return points;
    }

    public static void putInformation(double key, int[] bits) throws IOException {
        BufferedImage image = ImageIO.read(new File(INITIAL_IMAGE_PATH));

        int width = image.getWidth(), height = image.getHeight();

        int n = bits.length;

        Point[] points = diagonall(image, r * n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < r; j++) {
                Point p = points[i * r + j];

                if ((p.x + " <> " + p.y).equals("1285 <> 1285")) {
                    int a = 10;
                }
                System.out.println((p.x + " <> " + p.y));
                RGB rgb = new RGB(image.getRGB(p.x, p.y));

                int L = (int) (0.299 * rgb.R + 0.587 * rgb.G + 0.114 * rgb.B);
                rgb.B = (int) (rgb.B + (2 * bits[i] - 1) * L * q);
                
                image.setRGB(p.x, p.y, rgb.toRGB());
            }
        }

        recordImage(image);
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
        int b = getB(image, p);

        return (1 / c) * (countHorizNeighborhood(image, p) + countVerticNeighborhood(image, p) - 2 * b);
    }

    public static int[] getInformation(double key, int n) throws IOException {
        BufferedImage image = ImageIO.read(new File(IMAGE_WITH_INFORMATION_PATH));

        int width = image.getWidth();
        int height = image.getHeight();

        Point[] points = getPoints(key, n, width, height);

        int countBits = n / r;

        int[] bits = new int[countBits];

        for (int i = 0; i < countBits; i += r) {
            for (int j = 0; j < r; j++) {
                Point p = points[i + r];

                int b = image.getRGB(p.x, p.y);
                double estB = getBEstimate(image, p);


            }
        }

        return bits;
    }

    public static void main(String[] args) throws IOException {
        String messege = "Привет, Ваня ! fdsgdfgdfglfshjdijgkjdhgijdsgfigjsoifjoiufghaishfohfofsj;foisadjf;iosjf;wijfpajfdsaifjoerhgfrtjhglofj eij pfdispofdjp fdpgijpfdjgp p s ijfdpogjfd p djpifdpifdpjgifdjppgjpfdjgpifdjgp ijp ijpdgjpfdig jpfdijgpifjdgpidhgpfhpdfijgpidj0hfgp[fsidjgjg;ds08jp08h978y]";

        double key = 35;

        int[] bits = getBitSequence(messege.getBytes());

        String initialBits = Arrays.toString(bits);

        putInformation(key, bits);

        // String finalBits = Arrays.toString(getInformation(key, 10));

        // if (finalBits.equals(initialBits)) {
        //     System.out.println("Success !");
        // } else {
        //     System.out.println("Fail !");
        // }

        // Set<Point> set = new TreeSet<>(comp);

        // set.add(new Point(0, 0));
        // System.out.println(set.contains(new Point(1,1)));
        // System.out.println(set.contains(new Point(0, 4)));
        // System.out.println(set.contains(new Point(0, 1)));
        // System.out.println(set.contains(new Point(4, 0)));
    }

}
