import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/*
 * Program to make each image pixel a mean of its neighbouring 
 * pixels using sequential programming.
 * @author Lukhanyo Vena
 */

public class MeanFilterSerial {

    /*
     * Computes the mean of the pixels given a window size and
     * updates the current pixel with the mean.
     */
    protected static void compute(int width, int height, int window, BufferedImage myImage){
        if(window%2!=0 && window>=3){   
            int EntryAndLoop =(window-1)/2;
            for(int X_index=0;X_index<width-window;X_index++){
                for(int Y_index=EntryAndLoop;Y_index<height;Y_index++){
                    // get Mean of pixels
                    double red =0, green =0, blue=0;
                    for(int column = X_index;column<X_index+window;column++){
                        for (int mi = -EntryAndLoop; mi <= EntryAndLoop; mi++) {
                            int ColumnIndex = Math.min(Math.max(mi + Y_index, 0),height - 1);
                            int pixel = myImage.getRGB(column,ColumnIndex);

                            //Averaging 
                            red += (float)((pixel & 0x00ff0000) >> 16)/ (window*window);
                            green += (float)((pixel & 0x0000ff00) >>  8)/ (window*window);
                            blue += (float)((pixel & 0x000000ff) >>  0)/ (window*window);
                        }
                        
                    }
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
        BufferedImage img = null;
        String inputImage = "images/";
        String outputImage = "images/";
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
        
        //Computing mean of pixels
        long start = System.currentTimeMillis();
        MeanFilterSerial.compute(imgWidth, imgHeight, windowWidth, img);
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
