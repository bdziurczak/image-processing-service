### Image processing service
This project involves creating a backend system for an image processing service (as title says).


There's /web and /api endpoints for a browser and api interface.

I focus on delivering api (for now)

There's  initialization script that creates a database and adds some initial data in db/init/init.sql

App is dockerized, and it's run on default using docker compose.

Run this command inside the project directory to start the service:
```bash 
docker compose -f docker-compose.yml up -p imageprocessingservice \
up -d --build 
```
