FROM openjdk:23-slim

WORKDIR /app

COPY . .

# Compile all Java source files in src/
RUN javac src/*.java

EXPOSE 1234

# Run the server class
CMD ["java", "-cp", "src", "Server"]