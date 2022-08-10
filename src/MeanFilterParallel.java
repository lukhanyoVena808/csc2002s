import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/*
 * Program to make each image pixel a mean of its neighbouring 
 * pixels using parallel programming.
 * @author Lukhanyo Vena
 */
public class MeanFilterParallel extends RecursiveAction{

    private int Width;
    private int Height;
    private int Window;
    private BufferedImage BufferedImg;
    private int Offset_X;
    private int Offset_Y;
    private int EntryAndLoop;
    private int numPixels;
    
    protected static int Area_THRESHOLD = 3480*2160;

    public MeanFilterParallel(int offset_X,int width, int offset_Y,int height, int window, BufferedImage image){
        BufferedImg = image;
        Width = width;
        Height = height;
        Window = window;
        Offset_Y= offset_Y;
        Offset_X= offset_X;
        EntryAndLoop =(Window-1)/2;
        numPixels = Window*Window;

    }

    /*
     * Invokes the Fork/Join image area over threshold
     */
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

    /*
     * Updates each pixel which the average of the pixels in the window     *
     */
    protected void computeDirectly(){ 
        for(int X_index=Offset_X;X_index<(Width-Window)+Offset_X;X_index++){
            for(int Y_index=EntryAndLoop+Offset_Y;Y_index<Height+Offset_Y;Y_index++){
                // get Mean of pixels
                double red =0, green =0, blue=0;
                for(int column = X_index;column<X_index+Window;column++){
                    for (int mi = -EntryAndLoop; mi <= EntryAndLoop; mi++) {
                        int ColumnIndex = Math.min(Math.max(mi + Y_index, 0),Offset_Y+Height - 1);
                        int pixel = BufferedImg.getRGB(column,ColumnIndex);

                         // summing
                         red += (pixel & 0x00ff0000) >> 16;
                         green += (pixel & 0x0000ff00) >> 8;
                         blue += (pixel & 0x000000ff) >> 0;
                    }
                }
                // Update pixel's rgb
                int dpixel = (0xff000000) | (((int) red/ (Window * Window)) << 16) 
                                        | (((int) green/ (Window * Window)) << 8) 
                                        | (((int) blue/ (Window * Window)) << 0);
                BufferedImg.setRGB(X_index, Y_index, dpixel);
            }
        } 
    }

    public static void main(String[] args) {
        BufferedImage img = null;
        String inputImage = "src/images/";
        String outputImage = "src/images/";
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
        //update image by changing each pixel using Fork/Join
        MeanFilterParallel mnParallel = new MeanFilterParallel(0,imgWidth, 0,imgHeight,windowWidth,img);
        ForkJoinPool pool = new ForkJoinPool();
        long start = System.currentTimeMillis();
        pool.invoke(mnParallel);
        System.out.println("Execution time: "+((double)(System.currentTimeMillis()-start)/1000)+", for window: "+windowWidth+" and image: "+imgWidth+" x "+imgHeight);

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