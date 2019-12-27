import os
import sys
import json
class instagram_crawler:
    
    def __init__(self):
        self.result = 0
        self.tag_list = []
        self.img_tag_dict = {}
        self.read_hashtags()
        self.download_files()
#        self.concat_files()
        
    def read_hashtags(self):
        f= open("hashtags.txt","r")
        f1 = f.readlines()
        
        for x in f1:
            self.tag_list += list(dict.fromkeys(x.split(' #')))
            del self.tag_list[0]
           


    def download_files(self):
        i = 4
        os.system("python crawler.py hashtag -t " + self.tag_list[i] + " -n 100 -o ./outputs/out" + str(i) + " --fetch_hashtags")


    


        
def main():
    instagram_crawler()

        


main()
