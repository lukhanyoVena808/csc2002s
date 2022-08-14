# Parallelism Programming using the Fork/Join Library
## 2D Median Filter for Image Smoothing

### Aim    
The aim of the experiment is to implement two parallel filters for smoothing RGB colour
Images using the Java Fork-Join library, thereafter benchmark the programs to determine situations where parallelization would be optimal.

### Method

All images used to test can be found in the src/images folder. All input images used are "jpg" file.

#### Terminal
In both windows and linux, the java files can be complile in the main folder:

```console
javac src/<FileName>.java
```

To test the program:

```console
java -cp src/ <FileName> <inputImageName> <outputImageName> <windowSize>
```

example:
```console
javac src/MeanFilterSerial.java
```
```console
java -cp src/ MeanFilterSerial apple appleOut 3
```
#### Makefile for Linux

Compile makefile:
```console
make
```
Each java has its own run function in the makefile
- mean: runs the MeanFilterSerial file
- median: runs the MedianFilterSerial file
- meanp: runs the MeanFilterParallel file
- medianp: runs the MedianFilterParallel file

if you were to want to run the MeanFilterSerial:
```console
make mean in=<inputImageName> out=<outputImageName> size=<windowSize>
```
Example:
```console
make mean in=apple out=appleOut size=3
```
