# Escape The Maze



Thank you for trying out Escape the maze. 

This ReadMe contains useful information on how to set the game up and how to play.

While it is a multiplayer game, the game server currently defaults to a localhost connection. Feel free to edit the sockets such that you may host this server on the web.

##Contents
1. Overview
1. Set Up
2. Compiling The Code
3. How To Play
4. Hacking Your Character Stats

###Overview
Escape the maze is a multiplayer maze escape game. Players connected to the server vote for the actions that they want the protagonist to carry out. The server will collect all the votes and carry out the most popular decision.

Prior to running any of the clients, ensure that the server is already running. Clients executed before the server is run will not connect to the server. Ensure that only one server is running at any point of time.

The mazeHardware.c client was built to be played on Raspberry Pi and Beaglebone. Feel free to check out the code or run it locally on your computer if you know how libusb operates. A seperate readme.txt document has been created for using the hardware client. Also check out the communications protocol to learn more about the game. 

###Set Up
If you do not wish to compile the code, the latest version of the executable .jar files are available on [Google Drive](https://drive.google.com/open?id=0B3duucJ6dyimZDJpLUQ2YlozWU0)

Simply execute the MazeServer.jar file to start the server and execute any of the client executables to begin playing. There are two client versions, a GUI client made in Java and a command line client made in C. Feel free to open multiple clients locally to simulate the multiplayer aspect of the game. 


###Compiling The code
Import the packages into eclipse and follow this [guide](http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.jdt.doc.user%2Ftasks%2Ftasks-37.htm) to create a runnable .jar file.

###How To Play

#####World Mode
The server will prompt players to vote at every voting cycle. Vote using WASD movement controls.

![GitHub Logo](/screenshots/World.png)

#####Battle Mode
In battle mode, use ASD to vote for scissors, paper, or stone.

![GitHub Logo](/screenshots/Battle.png)

