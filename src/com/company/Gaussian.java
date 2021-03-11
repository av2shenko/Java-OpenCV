package com.company;

import org.opencv.core.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Gaussian {
    private double mu;
    private double count;
    private double sigma;
    private Mat resultImage;

    private double[][] CoppyArray(double[][] sourceArray) {
        double[][] coppyArray = new double[sourceArray.length][sourceArray.length];
        for (int i = 0; i < coppyArray.length; i++) {
            for (int j = 0; j < coppyArray.length; j++) {
                coppyArray[i][j] = sourceArray[i][j];
            }
        } //System.arraycopy(sourceArray, 0, coppyArray, 0, sourceArray.length);
        return coppyArray;
    }
    private double[][] MatConversionArray(Mat mat) {
        double[] temp;
        double[][] array = new double[mat.rows()][mat.cols()];
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                temp = mat.get(i, j);
                array[i][j] = temp[0];
            }
        }
        return array;
    }
    private void Sigma(double[][] doubleKernel) {
        double[][] copyKernel = CoppyArray(doubleKernel);
        for (int i = 0; i < copyKernel.length; i++) {
            for (int j = 0; j < copyKernel.length; j++) {
                mu += copyKernel[i][j];
                count++;
            }
        }
        mu /= count;
        for (int i = 0; i < copyKernel.length; i++) {
            for (int j = 0; j < copyKernel.length; j++) {
                copyKernel[i][j] = pow((copyKernel[i][j] - mu), 2);
                sigma += copyKernel[i][j];
            }
        }
        sigma = sqrt(sigma / count);
        System.out.println("mu = " + mu);
        System.out.println("sigma = " + sigma);
    }
    private double[][] KernelTransformation(double[][] doubleKernel) {
        double[][] copyKernel = CoppyArray(doubleKernel);
        Sigma(copyKernel);
        for (int i = 0; i < copyKernel.length; i++) {
            for (int j = 0; j < copyKernel.length; j++) {
                copyKernel[i][j] *= sigma;
            }
        }
        return copyKernel;
    }
    public Mat Blure(double[][] doubleKernel, Mat grayImage) {
        double[][] copyKernel = CoppyArray(doubleKernel);
        double[][] gaussianKernel = KernelTransformation(copyKernel);
        resultImage = new Mat(grayImage.rows(), grayImage.cols(), CvType.CV_8UC1);
        int grayImageRows = grayImage.rows();
        int grayImageCols = grayImage.cols();
        int kernelRows = gaussianKernel.length;
        int kernelCols = gaussianKernel[0].length;
        double[][] dataImage = MatConversionArray(grayImage);

        for (int i = 1; i < grayImageRows - kernelRows + 1; i++) {
            for (int j = 1; j < grayImageCols - kernelCols + 1; j++) {
                double temp = 0;
                for (int k = 1; k < kernelRows; k++) {
                    for (int l = 1; l < kernelCols; l++) {
                        temp += dataImage[i + k - 1][j + l - 1] * gaussianKernel[k][l];
                    }
                }
                if (temp < 0) temp = 0;
                if (temp > 255) temp = 255;
                resultImage.put(i, j, temp);
            }
        }
        return resultImage;
    }
    public void Print(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println(" ");
        }
    }
    public void Print(Mat mat) {
        System.out.println(mat.dump());
    }
}

//        Mat colorImage = Imgcodecs.imread("text.jpg");
//        Mat grayImage = new Mat(colorImage.rows(), colorImage.cols(), CvType.CV_8UC1);
//        Mat resultLibraryGaussianBlur = new Mat(colorImage.rows(), colorImage.cols(), CvType.CV_8UC1);
//        Imgproc.cvtColor(colorImage, grayImage, Imgproc.COLOR_RGB2GRAY);
//        Imgproc.GaussianBlur(grayImage, resultLibraryGaussianBlur, new Size(3, 3), 6.0);
//        Imgcodecs.imwrite("text.bmp", resultLibraryGaussianBlur);
//        HighGui.imshow("GaussianBlurLibrary(0)", resultLibraryGaussianBlur);
//
//        HighGui.imshow("gray", grayImage);
//        double[][] kernel = new double[][]{{0.5, 0.75, 0.5}, {0.75, 1, 0.75}, {0.5, 0.75, 0.5}};
//        Mat result = new Gaussian().Blure(kernel, grayImage);
//        HighGui.imshow("GaussianBlur(2)", result);