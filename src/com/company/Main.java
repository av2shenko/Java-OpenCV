package com.company;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {

    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat colorImage = Imgcodecs.imread("text.jpg");
        Mat grayImage1 = new Mat(colorImage.rows(), colorImage.cols(), CvType.CV_64FC1);

        double sigma = 6.0;
        Imgproc.cvtColor(colorImage, grayImage1, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(grayImage1, grayImage1, new Size(3, 3), sigma);
        Imgcodecs.imwrite("text.bmp", grayImage1);

        Mat kernel = new Mat(3, 3, CvType.CV_64FC1);
        kernel.put(0, 0, new double[]{0.5, 0.75, 0.5, 0.75, 1, 0.75, 0.5, 0.75, 0.5});

        Mat grayImage2 = new Mat(colorImage.rows(), colorImage.cols(), CvType.CV_64FC1);
        Imgproc.cvtColor(colorImage, grayImage2, Imgproc.COLOR_RGB2GRAY);
        Mat resultImage = new GaussianBlur().GaussianBlurring(grayImage2, kernel, sigma);
        HighGui.imshow("Color Image", colorImage);
        HighGui.imshow("OpenCV Image", grayImage1);
        HighGui.imshow("My Gaussian Image", resultImage);

        HighGui.waitKey(0);
    }
}
