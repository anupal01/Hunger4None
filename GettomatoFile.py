import os
from pathlib import Path
import time
import csv

dirs = {}
lst = []

path='C:/Users/maniv/Downloads/tomato'
subdirs = [x[0] for x in os.walk(path)]
file = open('tomato.csv', 'w',newline='')
with file:
    # identifying header
    header = ['PlantName', 'Disease', 'SetType','FileName','Path']
    writer = csv.DictWriter(file, fieldnames = header)
    writer.writeheader()
    for dir in subdirs:
        for a in Path(dir).iterdir():
            if a.is_file():
                if '.7z' not in str(a):
                    arr = str(a).split('\\')
                    itmcnt = len(arr)
                    dirs['PlantName'] = 'Tomato'
                    dirs['Disease'] = arr[itmcnt-2]
                    dirs['SetType'] = ''
                    dirs['FileName'] = a.name
                    dirs['Path'] = str(a)
                    writer.writerow(dirs)
                    #time.sleep(1)
    print('completed')


