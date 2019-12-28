# -*- encoding: utf-8 -*-
import json
import csv

class csvcreator:

    def __init__(self):
        self.numimg = 0
        self.tag_list = []
        self.read_hashtags()
        self.numtag = len(self.tag_list)
        self.csvlist = [[]for _ in range(1000)] 
        self.csvlist[0] = self.csvlist[0] + ['URL'] + self.tag_list
        self.concat_hashtags()
        self.create_csvfile()

    def read_hashtags(self):
        f= open("hashtags.txt","r")
        f1 = f.readlines()
        
        for x in f1:
            self.tag_list += list(dict.fromkeys(x.split(' #')))
            del self.tag_list[0] 
        

    def concat_hashtags(self):
        for i in range(self.numtag):
            with open(r'C:\Users\q\Desktop\Flow-Camp\insta_crawler_ver2\instagram-crawler-master\outputs\out' + str(i), 'rt', encoding='UTF8') as f:
                json_data = json.load(f)
                for j in range (len(json_data)):
                    json_dict = json_data[j]
                    if 'hashtags' in json_dict.keys() and len(json_dict['img_urls'])>0:
                        self.numimg = self.numimg + 1
                        url = json_dict['img_urls'][0]
                        self.csvlist[self.numimg].append(url)
                        hashtaglist = json_dict['hashtags']
                        for tag in self.tag_list:
                            if tag in hashtaglist:
                                self.csvlist[self.numimg].append(1)
                            else:
                                self.csvlist[self.numimg].append(0)
                    else:
                        continue

    def create_csvfile(self):
        with open('img_hashtag.csv', 'w',  encoding='UTF-8', newline='') as csvfile:
            writer = csv.writer(csvfile)
            for row in self.csvlist:
                writer.writerow(row)


csvcreator()