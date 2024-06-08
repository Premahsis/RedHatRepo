# RedHatRepo

Execution Process:
Step-1 : Run FileStoreServerApplication.java class inside Server Package
Step-2 : Run FileClint5.java class inside client package    

**Rest End Point URL** :
Store File                              : http://localhost:8080/store/add          POST Method
List files in the store                 : http://localhost:8080/files              GET Method
Remove a file                           : http://localhost:8080/files/file1.txt    DELETE Method
Update contents of a file in the store  : http://localhost:8080/files/file1.txt    POST Method
Word count                              : http://localhost:8080/wc                 GET Method
store freq-words                        : http://localhost:8080/freq-words         GET Method

For Storing File:
Steps:
We can Postman, having POST Method below is the End Point URL for the same
http://localhost:8080/store/add
We need to create some txt files and upload by selecting form-data
key: file
value: select file names from any locations (For now i have taken from stores Folder) that you created 
e.g file3.txt , file4.txt and select type as file
and send request(http://localhost:8080/store/add). so file get upload into uploads folder


Created Client class using JAVA Client:

Where we need to pass command as below ::
store ls  :     **Respose**: store ls: ["file1.txt","file2.txt","please update"]
store rm  :    {filename}  e.g store rm file1.txt
store update {filename}  :   e.g should update contents of a given file in the server or create a new if it is absent 
store wc  :   e.g **Response**: 8
store freq-words  :   Should return the 10 most frequent words in all the files combined
  
