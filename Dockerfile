# Use the official image as a parent image.
FROM maven:3-openjdk-11

# Set the working directory.
WORKDIR /usr/

# Copy the rest of your app's source code from your host to your image filesystem.
COPY target/*.jar SteamLeaderboards-API.jar

# Add metadata to the image to describe which port the container is listening on at runtime.
EXPOSE 8080

# Run the specified command within the container.
CMD [ "java", "-jar", "SteamLeaderboards-API.jar", "--spring.datasource.url=jdbc:mysql://mysql:3306/SteamAPI", "--spring.datasource.username=root", "--spring.datasource.password=Aman7123", "--com.aaronrenner.apikey=E7F6470D0BAFE99CED3362CB2DB5F25B", "--com.aaronrenner.tokenkey=heismyrock"]
