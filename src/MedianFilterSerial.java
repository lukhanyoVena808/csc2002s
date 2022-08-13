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
        int numPixels = window*window;
        int EntryAndLoop =(window-1)/2;
        
        if(window%2!=0 && window>=3){   
            for(int X_index=0;X_index<width-window;X_index++){
                for(int Y_index=EntryAndLoop;Y_index<height;Y_index++){

                    //keeps the RGB of all pixels in the window
                    ArrayList<Integer> Reds = new ArrayList<>();
                    ArrayList<Integer> Blues = new ArrayList<>();
                    ArrayList<Integer> Greens = new ArrayList<>();
                  
                    for(int column = X_index;column<X_index+window;column++){
                        for (int mi = -EntryAndLoop; mi <= EntryAndLoop; mi++) {
                            int ColumnIndex = Math.min(Math.max(mi + Y_index, 0),height-1);
                            int pix = myImage.getRGB(column,ColumnIndex);
                            Reds.add((pix & 0x00ff0000) >> 16);
                            Greens.add((pix & 0x0000ff00) >> 8);
                            Blues.add((pix & 0x000000ff) >> 0);
                        }  
                    }
                    Collections.sort(Reds);
                    Collections.sort(Blues);
                    Collections.sort(Greens);
                    
                    // Update pixel's rgb
                    int dpixel = (0xff000000) | ((Reds.get(numPixels/2)) << 16) 
                                            | ((Greens.get(numPixels/2)) << 8) 
                                            | ((Blues.get(numPixels/2)) << 0);

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
        String inputImage = "src/images/";
        String outputImage = "src/outputs/";
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
            
            img = new BufferedImage(imgWidth,imgHeight,BufferedImage.TYPE_INT_RGB);
            img = ImageIO.read(mSource);
            
        }catch(IOException e){
            System.out.println("Error");
            e.printStackTrace();
        }
        //update image by changing each pixel
        long start = System.currentTimeMillis();
        MedianFilterSerial.compute(imgWidth, imgHeight, windowWidth, img);
        System.out.println("Execution time: "+((double)(System.currentTimeMillis()-start)/1000)+", for window: "+windowWidth+" and image: "+imgWidth+" x "+imgHeight);

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
