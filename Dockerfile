# Use the official image as a parent image.
FROM openjdk:11

# Set the working directory.
WORKDIR /usr/

# Copy the rest of your app's source code from your host to your image filesystem.
COPY . .

# Add metadata to the image to describe which port the container is listening on at runtime.
EXPOSE 8088

# Run the specified command within the container.
CMD [ "./mvnw", "spring-boot:run"]
