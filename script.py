import time
import os

ls = ["MeanFilterSerial","MeanFilterParallel","MedianFilterSerial","MedianFilterParallel"]
img = ["apple","deer","light","scary","super"]
windows =[5,9,15,21,35,47,55,63]

os.system("javac src/*.java")
counter = 0
for file in os.listdir("src/images/"):
    file = file.strip(".jpg")
    for filter in ls:
        print(filter)
        for i in range(5):
            os.system(f"java -cp src/ {filter} {file} {counter} {3}")
            counter += 1
            time.sleep(1)

os.system("rm src/outputs/*.jpg")
time.sleep(10)

counter =0
for pic in img:
    for filter in ls:
        for window in windows:
            for i in range(2):
                os.system(f"java -cp src/ {filter} {pic} {counter} {window}")
                counter += 1
                time.sleep(1)

os.system("rm src/outputs/*.jpg")         
os.system("rm src/*.class")
