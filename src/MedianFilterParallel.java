import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/*
 * Program to make each image pixel a mean of its neighbouring 
 * pixels using parallel programming.
 * @author Lukhanyo Vena
 */
public class MedianFilterParallel extends RecursiveAction{

    private int Width;
    private int Height;
    private int Window;
    private BufferedImage BufferedImg;
    private int Offset_X;
    private int Offset_Y;
    
    protected static int Area_THRESHOLD =3480*2160;

    public MedianFilterParallel(int offset_X,int width, int offset_Y,int height, int window, BufferedImage image){
        BufferedImg = image;
        Width = width;
        Height = height;
        Window = window;
        Offset_Y= offset_Y;
        Offset_X= offset_X;

    }
    protected void compute(){
        if(Window%2!=0 && Window>=3){
            int area = Width*Height;
            if(area<Area_THRESHOLD){
                computeDirectly();
            }

            else{
                int splitX = (int)Math.floor((double)Width/2);  //half the width
                int splitY = (int)Math.floor((double)Height/2); // and half the height
                MeanFilterParallel left = new MeanFilterParallel(0,splitX,0,splitY,Window, BufferedImg);
                MeanFilterParallel right =  new MeanFilterParallel(splitX,Width-splitX,splitY,Height-splitY,Window, BufferedImg);
                left.fork(); //give first half to new threas
		    	right.compute(); //do second half in this thread
                left.join();

            }
            
        }
        else{
            //exit gracefully
           System.exit(0);}
    }

    protected void computeDirectly(){
        int EntryAndLoop =(Window-1)/2;
        int numPixels = Window*Window; 
        for(int X_index=Offset_X;X_index<(Width-Window)+Offset_X;X_index++){
            for(int Y_index=EntryAndLoop+Offset_Y;Y_index<Height+Offset_Y;Y_index++){
                // get Mean of pixels
                double red =0, green =0, blue=0;

                //keeps the RGB of all pixels in the window
                ArrayList<Integer> pixels = new ArrayList<>();
                
                for(int column = X_index;column<X_index+Window;column++){
                    for (int mi = -EntryAndLoop; mi <= EntryAndLoop; mi++) {
                        int ColumnIndex = Math.min(Math.max(mi + Y_index, 0),Height - 1);
                        pixels.add(BufferedImg.getRGB(column,ColumnIndex));
                    }
                }
                Collections.sort(pixels);
                int pixel = pixels.get(pixels.size()/2); //get median pixel
                red += ((pixel & 0x00ff0000) >> 16);
                green += ((pixel & 0x0000ff00) >> 8);
                blue += ((pixel & 0x000000ff) >>  0);
                
                //Update pixel's rgb
                int dpixel = (0xff000000) |(((int)red) << 16) |(((int)green) << 8) |(((int)blue) << 0);
                BufferedImg.setRGB(X_index, Y_index, dpixel);
            }
        } 
    }

    public static void main(String[] args) {
        BufferedImage img = null;
        String inputImage = "../images/";
        String outputImage = "../images/";
        int windowWidth = 0;
        int imgWidth = 0;
        int imgHeight =0;
        StopWatch timer = new StopWatch();
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
        //update image by change each pixel using Fork/Join
        MedianFilterParallel mnParallel = new MedianFilterParallel(0,imgWidth, 0,imgHeight,windowWidth,img);
        ForkJoinPool pool = new ForkJoinPool();
        timer.start();
        pool.invoke(mnParallel);
        timer.stop();

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