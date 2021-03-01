package com.company;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class GaussianBlur {

    private double average;
    private double count;
    private double sigma;
    private Mat kernel;
    private Mat resultImage;
    private int blurRadius = 1;
    private double[][] weightArr;

    private double[] CoppyArray(double[] sourceArray) {
        double[] coppyArray = new double[sourceArray.length];
        System.arraycopy(sourceArray, 0, coppyArray, 0, sourceArray.length);
        return coppyArray;
    }

    private void Sigma(double[] doubleKernel) {
        double[] copyKernel = CoppyArray(doubleKernel);
        for (int i = 0; i < copyKernel.length; i++) {
            average += copyKernel[i];
            count++;
        }
        average /= count;
        for (int i = 0; i < copyKernel.length; i++) {
            copyKernel[i] = pow((copyKernel[i] - average), 2);
            sigma += copyKernel[i];
        }
        sigma = sqrt(sigma / count);
        System.out.println("sigma = " + sigma);
    }

    private void KernelTransformation(double[] doubleKernel, int width, int height) {
        double[] copyKernel = CoppyArray(doubleKernel);
        Sigma(copyKernel);
        for (int i = 0; i < copyKernel.length; i++) {
            copyKernel[i] *= sigma;
        }
        kernel = new Mat(width, height, CvType.CV_64FC1);
//        Core.multiply(kernel, new Scalar(1 / sigma), kernel);
        kernel.put(0, 0, copyKernel);
        System.out.println(kernel.dump());
        System.out.println(kernel);
    }

    private double GaussianFunctions(int x, int y) {
        double convolution = (1 / (2 * Math.PI * sigma * sigma)) * Math.pow(Math.E, ((-(x * x + y * y)) / ((2 * sigma) * (2 * sigma))));
        return convolution;
    }

    public void calculateWeightMatrix() {
        weightArr = new double[blurRadius * 2 + 1][blurRadius * 2 + 1];
        for (int i = 0; i < blurRadius * 2 + 1; i++) {
            for (int j = 0; j < blurRadius * 2 + 1; j++) {

                weightArr[i][j] = GaussianFunctions(j - blurRadius, blurRadius - i);
                System.out.print(weightArr[i][j] + "\t");
            }
            System.out.println();
        }

    }


    public Mat GaussianB(double[] doubleKernel, int width, int height, Mat grayImage) {

        double[] copyKernel = CoppyArray(doubleKernel);

        double data[][] = new double[grayImage.width()][grayImage.height()];
        double[] temp;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                temp = grayImage.get(i, j);
                data[i][j]=temp[0];
            }

        }
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                System.out.println("i " + data[i][j]);
            }
        }
        KernelTransformation(copyKernel, width, height);

        resultImage = new Mat(grayImage.rows(), grayImage.cols(), CvType.CV_8UC1);


//        Core.multiply(kernel, new Scalar(1 / sigma), kernel);

        System.out.println(resultImage);
        return resultImage;
    }

    public Mat GaussianBlurring(double[] doubleKernel, int width, int height, Mat grayImage) {

        double[] copyKernel = CoppyArray(doubleKernel);

        KernelTransformation(copyKernel, width, height);

        resultImage = new Mat(grayImage.rows(), grayImage.cols(), CvType.CV_8UC1);

        Point anchor = new Point(-1, -1);
        double delta = 0.0;
        int ddepth = -1;
//        Core.multiply(kernel, new Scalar(1 / sigma), kernel);
        Imgproc.filter2D(grayImage, resultImage, ddepth, kernel, anchor, delta, Core.BORDER_DEFAULT);
        return resultImage;
    }

}
