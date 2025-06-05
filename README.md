# Java Chat App

A simple real-time chat server written in Java using sockets and threads.

## Features
- Multi-client chat support
- Real-time message broadcasting
- Clean code with basic error handling
- Ready to expand (GUI, usernames, private chat, etc.)

## How to Run

### 1. Compile the code

```bash
javac Server.java
javac ChatClientGUI.java
```
### 2. Start the server
```bash
java Server
```

### 3. Start the client(s)
```bash
java ChatClientGUI
```
Each GUI window represents a different user connected to the same server.

