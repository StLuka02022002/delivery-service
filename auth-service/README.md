# Auth service

After service is started you can find API description here:
http://localhost:8083/auth/swagger-ui/index.html

To create user use this request to auth service
```bash
curl -X 'POST' \
  'http://localhost:8083/auth/user/signup' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "some_user",
  "password": "some_pass"
}'
```

After that you can get a token
```bash
curl -X 'POST' \
  'http://localhost:8083/auth/token/generate' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "some_user",
  "password": "some_pass"
}'
```

