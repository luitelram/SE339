# SE339
Created by Team Feest: Ram Luitel, Jeremiah Brusegaard, Faizul Jasmi, Khoa Bui
Feest App
Project Development
	To run, we need to run server first, and then we run client side.

Server

	Server is coded in NodeJS, so we need NodeJS and Git to run the server code. The links to download below. Download and install if they are not installed in your computer.

NodeJS: https://nodejs.org/en/download/
Git: https://git-scm.com/downloads

After installing, it’s ready to run the server. Follow these steps:
Go to folder contains “server folder” (Server folder is available on our git. Refer above git link)
Right click on server folder, and choose Git Bash Here and then command window pop up
Run server by typing command “node server_filename.js”. In our case, it is “node server.js”
Now, server is ready.

Client
	Client which is Android platform is written in Java, so you can read the code by using any text editors which can open java file (Notepad++, Eclipse, Notepad). However, our recommendation software for Android is Android Studio. It provides in depth tools to debug and analyze the code with provided libraries in the software. 

Download link: https://developer.android.com/studio/install.html

To deploy our client, for the best result, we recommend to use any devices runs Android. Android emulator does not support for google map, so the map will not be showed on emulator.

Before setup application:
	
	We need to change server ip address according to the computer that we use to boot up the server file to create communication between client and server. 
Open our project in Android Studio,click to expand ‘app’ folder on navigation bar.
Click to expand ‘res’ folder
Under ‘res’ folder, click to expand ‘values’ folder
Open ‘strings.xml’ file, you will see a string variable named ‘server_ip_address”. You can enter your ip address here

*Note: For MessagingActivity (Chat feature), due to variable scope issue when using socketio, you need to change the server ip address for this activity.

Under ‘res’ folder on navigation bar, expand ‘java’ folder
Expand ‘edu.faizuljiastate.mapwithsidemenu’ package
Open MessagingActivity file, enter your ip address in socket connection part.

Setup application:

There are two way to set up the application:

Using Android Studio: 
Connect Android device to computer
Open project with Android Studio
Click ‘Run app’ on tool bar (a small green triangle symbol) (Shortcut key: Shift-F10). ‘Select Deployment Target’ window pop up
Choose your device 
Click Ok

Using setup file (.apk file):
You can find apk file in project folder
Open folder which contains project folder
Open ‘app’ folder
Open ‘build’ folder
Open ‘outputs’ folder
You will find ‘app-debug.apk’ file, transfer the file to your Android device and then set up
