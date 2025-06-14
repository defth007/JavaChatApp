FROM openjdk:23-slim

WORKDIR /app

COPY . .

# Compile the server Java file
RUN javac Server.java

EXPOSE 1234

# Start the server when the container runs
CMD ["java", "Server"]