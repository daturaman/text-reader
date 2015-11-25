# Welcome to text-reader!
It is required that Java 1.8 and Maven are installed with the “JAVA_HOME” and “PATH” environment variables configured.

1) Extract text-reader-master.zip.
2) CD to the extracted folder in command prompt/shell
3) Type and enter  mvn compile package to compile and generate an executable jar.
4) Type and enter the following command to run the program. One or more fully qualified file paths must be passed as space separated arguments: 
                  java -jar target/text-file-reader-0.0.1-SNAPSHOT-jar-with-dependencies.jar “/path/to/file” …

You should see an output similar to below for each file:
=================
Statistics for C:/bdlog.txt
Line count is: 3248
Word count is: 20416
Average word length is: 8.3
Most common letter is: i

