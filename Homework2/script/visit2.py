import requests
import socket
import sys

min = int(sys.argv[1]) #The site ID to start visiting with this job.
max = int(sys.argv[2]) #The site ID to stop visiting with this job.

sites = []
#read the input file, and only get the sites that this host should visit.
in_file = open("/home/ippei/cs5123/hw2/top-1m.csv", "r")
for i, line in enumerate(in_file):
   if i >= min and i < max:
      sites.append(line)
   if i > max:
      break
in_file.close()

#The output file name will be "all_beginID_endID.dat"
out_file_name = "all_" + sys.argv[1] + "_"+ sys.argv[2] + ".dat"
out_file = open(out_file_name,'w+')
out_file.write('')
out_file.close()

#In case the exception does not catch network, try with different protocol.
protocols = ['http://', 'https://']

#go through my list of sites.
for i, site in enumerate(sites):
   site_id = site.split(",")[0].strip()
   site_name = site.split(",")[1].strip()
   success = False
   page_content = None
   url = '-1'
   for protocol in protocols:
      try:
         url = protocol + site_name
         resp = requests.get(url, timeout=10, headers={'User-Agent':'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36 OPR/48.0.2685.35'});
         if resp.status_code == 200:
            success = True
            page_content = resp.content
            break
      except requests.exceptions.ConnectTimeout:
         break
      except requests.exceptions.ReadTimeout:
         break
      except requests.exceptions.Timeout:
         break
      except:
         #try next protocol.
         one = 1
   
   page_size = -1
   page_title = 'UNKNOWN'
   if success:
      page_size = len(page_content)
      #Get the begin and end index of the page title.
      title_begin = page_content.lower().find('<title')
      title_end = page_content.lower().find('</title>')
      if title_end < 0:
         title_end = page_content.lower().find('<\/title>')
      if (title_begin > 0 and title_end > 0 and title_begin < title_end):
         for index in range(title_begin, title_end):
            if(page_content[index] == '>'):
               title_begin = index + 1
      if(title_begin > 0 and title_end > 0 and title_begin < title_end):
         page_title = page_content[title_begin : title_end]
      page_title = page_title.replace('\r\n','\n').replace('\n',' ').replace(":","-")
   #Write the results to the file.
   #id::site::url::size::title
   out_line = site_id + "::" + site_name + "::" + url + "::" + str(page_size) + "::" + page_title + '\n'
   out_file = open(out_file_name,'a+')
   out_file.write(out_line)
   out_file.close()
   msg = out_line + "("+ str(i) +" of "+ str(len(sites)) +")"
   print(msg)
