import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Scanner;
/*
 * Program to make each image pixel a mean of its neighbouring 
 * pixels using sequential programming.
 * @author Lukhanyo Vena
 */

public class MeanFilterSerial {
    
    public static void main(String[] args) {
        Scanner rd = new Scanner(System.in);
        System.out.println("Type:");
        String inputImage = "../images/"+rd.next();
        String outputImage = "../images/"+rd.next();
        int windowWidth = rd.nextInt();
        
        // if(args.length>0){
        //     inputImage = args[0];
        //     outputImage = args[1];
        // }
        // else{
        //     System.exit(0);
        // }
        // System.out.println(inputImage);
        
    }
}
