import os
from pathlib import Path
import time
import csv
import re

dirs = {}
lst = []

path='C:/Users/maniv/Downloads/multicorp'
subdirs = [x[0] for x in os.walk(path)]
file = open('multicorp.csv', 'w',newline='')
with file:
    # identifying header
    header = ['PlantName', 'Disease', 'SetType','FileName','Path']
    writer = csv.DictWriter(file, fieldnames = header)
    writer.writeheader()
    for dir in subdirs:
        for a in Path(dir).iterdir():
               if a.is_file():
                    if '.rar' not in str(a):
                        arr = str(a).split('\\')
                        itmcnt = len(arr)
                        spliting = arr[itmcnt - 2]
                        nonsplit = arr[-1]
                        if '___' in spliting:
                            splitvalue = spliting.split('___')
                            dirs['PlantName'] = splitvalue[0].replace(',', '')
                            dirs['Disease'] = splitvalue[1]
                        else:
                            separating = (re.findall('[A-Z][^A-Z]*', nonsplit))
                            dirs['PlantName'] = separating[0]
                            seperatingdisease = separating[1:]
                            l = ''.join(seperatingdisease).replace('.JPG', '')
                            dirs['Disease'] = ''.join(i for i in l if not i.isdigit())
                        dirs['SetType'] = ''
                        dirs['FileName'] = a.name
                        dirs['Path'] = str(a)
                        print(dirs)
                    ##    time.sleep(2)
                        ##print(dirs)
                        writer.writerow(dirs)
                        # time.sleep(1)
    print('completed')