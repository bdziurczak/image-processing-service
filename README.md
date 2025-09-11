### Image processing service
This project involves creating a backend system for an image processing service .
### Docs
OpenAPI description is available at the following url for json format: http://server:port/v3/api-docs
html user friendly format is available at: http://localhost:8080/swagger-ui/index.html
### Running project
App is dockerized, and it's run on default using docker compose

Run this command inside the project directory to start the service:
```bash 
docker compose -f docker-compose.yml up -p imageprocessingservice \
up -d --build 
```

I use BuildKit to speed up building app
https://docs.docker.com/build/buildkit/

There's  initialization script that creates a database and adds some initial data in db/init/init.sql
[DB init will be automated in next version of an app]

### Examples of using service with a Basic Auth authentication
#### Add image to db
```
curl -X POST 'http://localhost:8080/api/images' \
  --header 'Content-Type: multipart/form-data' \
  --form file=@/path/to/image.jpeg;type=image/jpeg \
  --user 'username:secret'
```
#### Get image
```
curl -X GET 'http://localhost:8080/api/images/1' \
  --user 'username:secret'
```
#### Transform image
```
curl -X POST 'http://localhost:8080/api/images/1/transform' \
  --header 'Content-Type: application/json' \
  --data '{
  "resize": {
    "width": 800,
    "height": 600
  },
  "rotate": 90,
  "format": "png",
  "filters": {
    "grayscale": true,
    "sepia": false
  },
  "saved": false
}' \
  --user 'username:secret'
```
