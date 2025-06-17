# Java Chat App

A simple real-time chat server written in Java using sockets and threads.

## Features
- Real time group chat using Java sockets
- GUI built with Java Swing and custom fonts/backgrounds
- 'Username: message' display
- ngrok-compatible: connect across the internet

## How to Run

### 1. Start the server
- Run Server.java in IntelliJ
- To use ngrok, run this in your terminal:
```bash
ngrok tcp 1234
```
- Copy the tcp:// address it gives you

### 2. Start the client(s)
- Run ChatClientGUI.java in IntelliJ or open the Minechat app
- Enter your username and the ngrok URL when prompted
- Start chatting!


