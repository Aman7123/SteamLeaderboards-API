# Use the official image as a parent image.
FROM maven:3-openjdk-11

# Set the working directory.
WORKDIR /usr/

# Copy the rest of your app's source code from your host to your image filesystem.
COPY target/*.jar SteamLeaderboards-API.jar

# Add metadata to the image to describe which port the container is listening on at runtime.
EXPOSE 8080

# Run the specified command within the container.
CMD [ "java", \
        "-jar", \
        "SteamLeaderboards-API.jar", \
        "--spring.datasource.url=jdbc:mysql://{URL-ADDRESS}:{PORT}/{DATABASE-NAME}", \
        "--spring.datasource.username=ADMIN", \
        "--spring.datasource.password=PASSWORD", \
        "--com.aaronrenner.apikey=API_KEY_SECRET", \
        "--com.aaronrenner.tokenkey=TOKEN_KEY_SECRET"]