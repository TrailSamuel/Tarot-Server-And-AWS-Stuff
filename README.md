# Tarot Card Reader
TCP client-server application that provides three-card tarot readings (Past, Present, Future) using Rider-Waite tarot cards.

## Requirements
- JDK 8 or higher

## Setup

**Compile:**
```bash
javac TarotCardServer.java TarotCardClient.java
```

**Start server:**
```bash
java TarotCardServer <port>
# Example: java TarotCardServer 8080
```

**Run client:**
```bash
java TarotCardClient <hostname> <port>
# Example: java TarotCardClient localhost 8080
```

## Output
```
=======================================
           TAROT CARD READING          
=======================================

Past:    The Fool
Present: The Star
Future:  Wheel of Fortune

Server IP: 192.168.1.100
=======================================
```

## How It Works
- Server listens for connections and spawns a thread per client
- Each client receives 3 unique random cards plus server IP
- Client displays the reading and disconnects

Stop server with Ctrl+C
