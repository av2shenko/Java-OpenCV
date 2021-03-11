package com.company;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Binarization {
    public int[] bins = new int[266]; //массив содерж. знач. гистограммы

    public void SetBins(BufferedImage bufferedImage) {
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                Color color = new Color(bufferedImage.getRGB(i, j));
                ++bins[color.getRed()]; //для цветного ((color.getRed())+color.getGreen()+color.getBlue())/3)
                //System.out.println(color);
            }
        }
    }

    public BufferedImage Otsu(BufferedImage bufferedImage, Mat grayImageMat) throws IOException {
        SetBins(bufferedImage);
        BufferedImage binarizationOtsu = new BufferedImage(grayImageMat.width(), grayImageMat.height(), BufferedImage.TYPE_INT_ARGB);
        int sum = grayImageMat.width() * grayImageMat.height();
        int count1 = 0;
        int count2 = sum;
        int thresholdOtsu = 0; //порог который ищем
        double max_s = 0; //max дисперсия
        long smI1 = 0; //сум яркость
        long smI2 = 0;
        for (int t = 0; t < 256; t++)
            smI2 += (long) bins[t] * t;
        double mu1, mu2; //мат ожидания

        for (int t = 0; t < 256; t++) {

            int newbin = bins[t];

            count1 += newbin;
            count2 -= newbin;

            double w1 = (double) count1 / sum; //вероятность попадания пикселя
            double w2 = 1.0 - w1;

            smI1 += (long) newbin * t;
            smI2 -= (long) newbin * t;

            mu1 = smI1 / (count1 == 0 ? 1 : count1);
            mu2 = smI2 / (count2 == 0 ? 1 : count2);

            double d = mu1 - mu2;
            double sigma = w1 * w2 * d * d;

            if (sigma > max_s) {
                max_s = sigma;
                thresholdOtsu = t;
            }
        }
        System.out.println("thresholdOtsu: " + thresholdOtsu);
        for (int i = 0; i < grayImageMat.rows(); i++) {
            for (int j = 0; j < grayImageMat.cols(); j++) {
                Color color = new Color(bufferedImage.getRGB(i, j));
                int pixel = color.getRed(); //для цветного ((color.getRed())+color.getGreen()+color.getBlue())/3)
                binarizationOtsu.setRGB(i, j, pixel < thresholdOtsu ? Color.black.getRGB() : Color.white.getRGB());
            }
        }
        File file = new File("binarizationOtsu.png");
        ImageIO.write(binarizationOtsu, "PNG", file);
        Mat otsu = Imgcodecs.imread("binarizationOtsu.png");
        HighGui.imshow("binarizationOtsu", otsu);
        return binarizationOtsu;
    }

    public BufferedImage BradleyRoot(BufferedImage bufferedImage, Mat grayImageMat) throws IOException {
        double t = 0.15;
        int ab = 8;

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage binarizationBradley = new BufferedImage(grayImageMat.width(), grayImageMat.height(), BufferedImage.TYPE_INT_ARGB);

        int Sxy = 0;
        int[][] b = new int[width][height];
        int[][] integralImg = new int[width][height];
        int s = width / (ab); //== 0 ? 1 : ab


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = new Color(bufferedImage.getRGB(i, j));
                b[i][j] = (int) color.getRed(); //для цветного ((color.getRed())+color.getGreen()+color.getBlue())/3)
                Sxy += b[i][j];

                if (i == 0) {
                    integralImg[i][j] = Sxy;
                } else {
                    integralImg[i][j] = integralImg[i - 1][j] + Sxy;
                }
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int x1 = i - s / 2;
                if (x1 < 1)
                    x1 = 1;

                int x2 = i + s / 2;
                if (x2 >= width)
                    x2 = width - 1;

                int y1 = j - s / 2;
                if (y1 < 1)
                    y1 = 1;

                int y2 = j + s / 2;
                if (y2 >= height)
                    y2 = height - 1;

                int count = (x2 - x1) * (y2 - y1);
                Sxy = integralImg[x2][y2] - integralImg[x2][y1 - 1] - integralImg[x1 - 1][y2] + integralImg[x1 - 1][y1 - 1];
                if (b[i][j] * count <= Sxy * (100 - t) / 100)
                    binarizationBradley.setRGB(i, j, Color.black.getRGB());
                else
                    binarizationBradley.setRGB(i, j, Color.white.getRGB());
            }
        }
        File file = new File("binarizationBradley.png");
        ImageIO.write(binarizationBradley, "PNG", file);
        Mat bradley = Imgcodecs.imread("binarizationBradley.png");
        HighGui.imshow("binarizationBradley", bradley);
        return binarizationBradley;
    }
}
