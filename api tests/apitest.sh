#!/bin/bash

# =========================================
# Image Processing Service API Test Script
# =========================================
# Run with:
#   chmod +x test_api.sh
#   ./test_api.sh
# =========================================

BASE_URL="http://localhost:8080"
IMG_PATH="$1"
CSRF_TOKEN=""

if [ -z "$IMG_PATH" ]; then
  echo "❌ Usage: $0 <path_to_image_file>"
  exit 1
fi

if [ ! -f "$IMG_PATH" ]; then
  echo "❌ File not found: $IMG_PATH"
  exit 1
fi

#adding user
curl --location 'localhost:8080/api/register' \
--header 'Content-Type: application/json' \
--header 'Cookie: XSRF-TOKEN=fc10b000-c6c6-489f-bae7-15e2c5f5bb90' \
--data '{
	"username": "bartek",
    "password": "33242fgdgfhytdjyujyutdt"
}'
#get CSRF token
curl --location 'localhost:8080/api/csrf' \
--header 'Authorization: Basic YmFydGVrOjI=' \
--header 'Cookie: XSRF-TOKEN=fc10b000-c6c6-489f-bae7-15e2c5f5bb90'
#adding image to database
curl --location 'localhost:8080/api/images' \
--header 'X-XSRF-TOKEN: 4mo1l3wiKCgKOv4GhwJWhWDnj34MkCy3bDOtaHRP4usP1X-NhAkEpx4SGBgnWchlsS9ivVmBohxt9RuaXQbIWhd6hN5tt0a9' \
--header 'Authorization: Basic YmFydGVrOjI=' \
--header 'Cookie: XSRF-TOKEN=fc10b000-c6c6-489f-bae7-15e2c5f5bb90' \
--form 'file=@"///wsl.localhost/Ubuntu/home/bbbda/Images/bestimage.jpg"'

#GET image from transformed table
curl --location 'localhost:8080/api/images/transformed/4' \
--header 'X-XSRF-TOKEN: 8KWaLkzFff99kNtmNPsgDn71fHVkQae1FhA5PFQeijvExZtQlJaoG3-kH8lQ8utTBtYUN0eRUU1ddcGYISQND2Z86Vj39agy' \
--header 'Authorization: Basic YmFydGVrOjI=' \
--header 'Cookie: XSRF-TOKEN=fc10b000-c6c6-489f-bae7-15e2c5f5bb90'

#transforming and copying image
curl --location --request PATCH 'localhost:8080/api/images/originals/1' \
--header 'X-XSRF-TOKEN: 4mo1l3wiKCgKOv4GhwJWhWDnj34MkCy3bDOtaHRP4usP1X-NhAkEpx4SGBgnWchlsS9ivVmBohxt9RuaXQbIWhd6hN5tt0a9' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YmFydGVrOjI=' \
--header 'Cookie: XSRF-TOKEN=fc10b000-c6c6-489f-bae7-15e2c5f5bb90' \
--data '{
  "resize": { "width": 800, "height": 600 },
  "rotate": 90,
  "format": "png",
  "filters": { "grayscale": false, "sepia": true },
  "saved": true,
  "copied": true
}'

