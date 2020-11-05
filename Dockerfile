# Use the official image as a parent image.
FROM maven:3-openjdk-11

# Set the working directory.
WORKDIR /usr/

# Copy the rest of your app's source code from your host to your image filesystem.
COPY . .

# Add metadata to the image to describe which port the container is listening on at runtime.
EXPOSE 8080

# Run the specified command within the container.
CMD [ "./mvnw", "spring-boot:run"]
