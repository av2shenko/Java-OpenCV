package com.company;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GaussianBlur {

    private double average;
    private double count;
    private double sigma;

    public Mat GaussianBlurring(Mat grayImage, Mat kernel, double sigma) {
        Mat resultImage = new Mat(grayImage.rows(), grayImage.cols(), CvType.CV_64FC1);

        Point anchor = new Point(-1, -1);
        double delta = 0.0;
        int ddepth = -1;
        Core.multiply(kernel, new Scalar(1 / sigma), kernel);
        Imgproc.filter2D(grayImage, resultImage, ddepth, kernel, anchor, delta, Core.BORDER_DEFAULT);
        return resultImage;
    }

    public void Sigma(Mat kernel) {
    }

    public void Sigma(double[][] kernel) {
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                average += kernel[i][j];
                count++;
            }
        }
        average /= count;
        for (int i = 0; i < kernel.length; i++) {
            for (int j = 0; j < kernel.length; j++) {
                kernel[i][j] = pow((kernel[i][j] - average), 2);
                sigma += kernel[i][j];
            }
        }
        sigma = sqrt(sigma / count);
        System.out.println("average = " + average);
        System.out.println("sigma = " + sigma);
    }
}
