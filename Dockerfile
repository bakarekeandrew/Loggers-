# Use an official Tomcat runtime as a parent image
FROM tomcat:10.1-jdk17

# Set the working directory in the container
WORKDIR /usr/local/tomcat/webapps

# Remove the default Tomcat applications

# Copy the WAR file into the container
COPY build/TodayApp.war ./TodayApp.war

# Install the PostgreSQL JDBC driver
RUN wget https://jdbc.postgresql.org/download/postgresql-42.6.0.jar -O /usr/local/tomcat/lib/postgresql-42.6.0.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run Tomcat when the container launches
CMD ["catalina.sh", "run"]