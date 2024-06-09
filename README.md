# RedHatRepo

Execution Process:
Step-1 : Run FileStoreServerApplication.java class inside Server Package
Step-2 : Run FileClint.java class inside client package    

**Rest End Point URL** :
Store File                              : http://localhost:8080/store/add          POST Method
List files in the store                 : http://localhost:8080/files              GET Method
Remove a file                           : http://localhost:8080/files/file1.txt    DELETE Method
Update contents of a file in the store  : http://localhost:8080/files/file1.txt    POST Method
Word count                              : http://localhost:8080/wc                 GET Method
store freq-words                        : http://localhost:8080/freq-words         GET Method

Created Client class using JAVA Client:

Where we need to pass command as below ::
store add :     give name which is there in stores folder e.g. file3.txt file4.txt "store add file3.txt file4.txt"
                **Steps**:
                Run FileClient.java File
                In Console type "store add" command
                then enter one file name e.g. file3.txt
                then enter another file name e.g. file4.txt

                Then will response: 
                Response status code: 201 CREATED
                Response body: File added successfully
                
store ls  :     **Respose**: store ls: ["file1.txt","file2.txt","please update"]
                **Steps**
                Run FileClient.java File
                In console type "store ls" command
Updated
                Response:
                store ls ["file1.txt","file2.txt"]
                
store rm  :    {filename}  e.g store rm file1.txt
                **Steps**
                Run FileClient.java class
                In console type "store rm" command
                then enter file name which want to be delete e.g. file4.txt

                Respose:
                File deleted successfully
                
store update {filename}  :   e.g should update contents of a given file in the server or create a new if it is absent
                             **Steps**
                             Run FileClient.java class
                             In console type "store update" command
                             then give file name which file want to update e.g. file3.txt (Currently some msg hard coded content is there)
                             
                             Response:  
                             File file3.txt updated successfully.
                             
store wc  :     e.g. **Response**: 8
                **steps**
                Run FileClient.java class
                In console type "store wc" command

                Response:
                store wc 14
                
store freq-words  :   Should return the 10 most frequent words in all the files combined
                      **Steps**
                      Run FileClient.java class
                      In console type "store freq-words" command
                      
                      Response:
                      store freq-words[["the","this","is","hello","pradhan.","updated","premashis","new","update","content"]]
                      
Note: Keep FileStoreServerApplication.java class always keep running don't stop it but FileClient.java class needs to be run multiple time based on each action
===========================================================================================
Using Postman::::

We can do all operation to verify on server side.

For Storing File:
Steps:
We can Postman, having POST Method below is the End Point URL for the same
http://localhost:8080/store/add
We need to create some txt files and upload by selecting form-data
key: file
value: select file names from any locations (For now i have taken from stores Folder) that you created
e.g file3.txt , file4.txt and select type as file
and send request(http://localhost:8080/store/add). so file get upload into uploads folder
  
