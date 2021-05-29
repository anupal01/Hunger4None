import os
from shutil import copyfile
from pathlib import Path
import time

opPath = r"D:\dataset"

#set the parameter
root = r"D:\dataset1"
x=""
subdirs = [x[0] for x in os.walk(root)]
for dirs in subdirs:
    for a in Path(dirs).iterdir():
        if a.is_file():
            arr = str(a).split('\\')            
            itmcnt = len(arr)            
            DiseaseName = arr[itmcnt-2].replace("_",'').replace(" ",'')
            VeggiName = (arr[itmcnt-3]).replace("_leaf",'')
            n = (VeggiName + " " + DiseaseName)
            print(n)
            destinationFilePath = opPath+"\\"+VeggiName+"\\"+DiseaseName.lower()+"\\"
            if not os.path.exists(destinationFilePath):
                os.makedirs(destinationFilePath)
            print(n)
            print(str(a.name))
            sourceFilePath = str(a)
            print(destinationFilePath)
            print(sourceFilePath)
            copyfile(sourceFilePath, destinationFilePath+"\\"+str(a.name))
            #exit()
print('completed')