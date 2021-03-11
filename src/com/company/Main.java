package com.company;

import org.opencv.core.*;
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

        Mat sourceImage = Imgcodecs.imread("sourceImage1.jpg");
        Mat grayImageMat = new Mat(sourceImage.rows(), sourceImage.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(sourceImage, grayImageMat, Imgproc.COLOR_RGB2GRAY);
        HighGui.imshow("sourceImage", sourceImage);

        Mat binarizationLibraryOtsu = new Mat();
        int threshold = (int) Imgproc.threshold(grayImageMat, binarizationLibraryOtsu, 0, 255, Imgproc.THRESH_OTSU | Imgproc.THRESH_BINARY);
        System.out.println("thresholdLibraryOtsu: " + threshold);
        HighGui.imshow("binarizationLibraryOtsu", binarizationLibraryOtsu);

        //Imgproc.GaussianBlur(grayImageMat, grayImageMat, new Size(3, 3), 6.0);

        Imgcodecs.imwrite("grayImageMat.jpg", grayImageMat);
        String path = "";
        BufferedImage bufferedImage = ImageIO.read(new File(path));
        BufferedImage grayBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                int rgb = bufferedImage.getRGB(i, j);
                grayBufferedImage.setRGB(i, j, rgb);
                //System.out.println(rgb);
            }
        }
        new Binarization().Otsu(grayBufferedImage, grayImageMat);

        new Binarization().BradleyRoot(grayBufferedImage, grayImageMat);

        HighGui.waitKey(0);
    }
}
