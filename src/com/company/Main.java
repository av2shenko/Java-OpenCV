package com.company;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat colorImage = Imgcodecs.imread("text.jpg");
        Mat grayImage = new Mat(colorImage.rows(), colorImage.cols(), CvType.CV_8UC1);
        Mat resultLibraryGaussianBlur = new Mat(colorImage.rows(), colorImage.cols(), CvType.CV_8UC1);

        double sigma = 6.0;
        int width = 3;
        int height = 3;
        Imgproc.cvtColor(colorImage, grayImage, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(grayImage, resultLibraryGaussianBlur, new Size(width, height), sigma);
        Imgcodecs.imwrite("text.bmp", resultLibraryGaussianBlur);

        double[] kernel = {0.5, 0.75, 0.5, 0.75, 1, 0.75, 0.5, 0.75, 0.5};
        new GaussianBlur().calculateWeightMatrix();

        Mat resultMyGaussianBlur = new GaussianBlur().GaussianBlurring(kernel, width, height,  grayImage);
        Mat res = new GaussianBlur().GaussianB(kernel, width, height,  grayImage);
//        HighGui.imshow("Color Image", colorImage);
//        HighGui.imshow("Library GaussianBlur", resultLibraryGaussianBlur);
//        HighGui.imshow("My GaussianBlur", resultMyGaussianBlur);
//        HighGui.imshow("test", res);
        HighGui.waitKey(0);
    }
}
