import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/*
 * Program to make each image pixel a median of its neighbouring 
 * pixels using parallel programming.
 * @author Lukhanyo Vena
 */

public class MedianFilterSerial {

    /*
     * Computes the median of the pixels given a window size and
     * updates the current pixel with the median.
     */
    protected static void compute(int width, int height, int window, BufferedImage myImage){
        if(window%2!=0 && window>=3){   
            int EntryAndLoop =(window-1)/2;
            for(int X_index=0;X_index<width-window;X_index++){
                for(int Y_index=EntryAndLoop;Y_index<height;Y_index++){
                    // get Median of pixels
                    double red =0, green =0, blue=0;

                    //keeps the RGB of all pixels in the window
                    ArrayList<Integer> pixels = new ArrayList<>();

                    for(int column = X_index;column<X_index+window;column++){
                        for (int mi = -EntryAndLoop; mi <= EntryAndLoop; mi++) {
                            int ColumnIndex = Math.min(Math.max(mi + Y_index, 0),height - 1);
                            pixels.add(myImage.getRGB(column,ColumnIndex));
                        }  
                    }
                    Collections.sort(pixels);
                    int pixel = pixels.get(pixels.size()/2); //get median pixel
                    red += ((pixel & 0x00ff0000) >> 16);
                    green += ((pixel & 0x0000ff00) >> 8);
                    blue += ((pixel & 0x000000ff) >>  0);

                    //Update pixel's rgb
                    int dpixel = (0xff000000) |(((int)red) << 16) |(((int)green) << 8) |(((int)blue) << 0);
                    myImage.setRGB(X_index, Y_index, dpixel);
                }
            }
        }
        else{
             //exit gracefully
            System.exit(0);}
    }

    public static void main(String[] args) {
        BufferedImage img=null;
        String inputImage = "../images/";
        String outputImage = "../images/";
        int windowWidth = 0;
        int imgWidth = 0;
        int imgHeight =0;
        
        if(args.length>0){
            inputImage = inputImage+args[0];
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
            
            img = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_RGB);
            img = ImageIO.read(mSource);
            
        }catch(IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }
        //update image by change each pixel
        MedianFilterSerial.compute(imgWidth, imgHeight, windowWidth, img);

        //Write to Output Image
        try{
            File myDestination = new File(outputImage);
            ImageIO.write(img,"jpg",myDestination);
        }catch (IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
