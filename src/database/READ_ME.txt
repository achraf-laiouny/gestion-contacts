********** assuming you're working with Intellij IDEA community Edition ************
" also assuming you pulled the project using Git "

1) first you need to download and install XAMPP

2) secondly you need to add the MySQL Connector/J (JDBC driver) .jar

    Step 1: Download the JAR

        - Go to: https://dev.mysql.com/downloads/connector/j/
        - Download the platform-independent .zip
        - Extract it → You’ll find mysql-connector-j-8.x.x.jar inside the folder

    Step 2: Add JAR to IntelliJ Project

        - Go to File > Project Structure (or press Ctrl+Alt+Shift+S)
        - Select Modules > Dependencies
        - Click the + icon > JARs or directories
        - Select the downloaded .jar file
        - Click OK → Make sure it appears in the list with scope Compile

3) thirdly run xampp Apache & MySql

4) fourthly import database

    Step 1 : Go to localhost on browser

        - Go to: phpMyAdmin
        - Create database under the name "gestion_contacts"
        - Select it
        - Click on Import & select the .sql file in src/database/contact.sql
        - Execute

    Step 2  : Got to Contacts.java

        - Click RUN

*****************************************************************************************