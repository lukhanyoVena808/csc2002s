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
        long start = System.currentTimeMillis();
        System.out.println(start);
        for (int i=0;i<1000000;i++){}
        System.out.println(System.currentTimeMillis()-start);        
    }
}