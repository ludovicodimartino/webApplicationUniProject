# WACAR
The user selects a car, and based on its type (e.g. off-road, supercar), the web app suggests some circuits where he can do laps as a driver during one of the available time slots.

## Run the application
The application is set up to run on Docker. To run it follow these steps:

- Check that the control characters for the script [container_health_check.sh](src/main/database/container_health_check.sh) are set to _LF_.
- Run the command:
    ```
    docker compose up
    ```

The web application should now be accessible at [http://localhost:8081/wacar](http://localhost:8081/wacar). 
