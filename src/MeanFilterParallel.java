import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/*
 * Program to make each image pixel a mean of its neighbouring 
 * pixels using parallel programming.
 * @author Lukhanyo Vena
 */

public class MeanFilterParallel {

    public static void main(String[] args) {
        BufferedImage img = null;
        String inputImage = "../images/";
        String outputImage = "../images/";
        int windowWidth = 0;
        int imgWidth = 0;
        int imgHeight =0;
        
        if(args.length>0){
            inputImage = inputImage+args[0]+".jpg";
            outputImage = outputImage+args[1]+".jpg";
            windowWidth = Integer.parseInt(args[2]);
        }
        else{
            //exit gracefully
            System.exit(0);
        }
        
        //Read input Image
        try{
            File mSource = new File(inputImage);
            BufferedImage getImgInfo = ImageIO.read(mSource);
            imgWidth=  getImgInfo.getWidth();
            imgHeight= getImgInfo.getHeight();
            img = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_ARGB);
            img = ImageIO.read(mSource);
            
        }catch(IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }
        //update image by change each pixel
        MeanFilterSerial.compute(imgWidth, imgHeight, windowWidth, img);

        //Write to Output Image
        try{
            File myDestFile = new File(outputImage);
            ImageIO.write(img,"jpg",myDestFile);
        }catch (IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }       
    }
}