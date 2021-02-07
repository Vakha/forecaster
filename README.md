# Forecaster

Kotlin + Spring Boot web application. JVM layer on top python script `pymodel/predict.py`.


Endpoints:
1. Forecasting sales for date
    
    `POST /forecast/sales` 
    Example:
    Request
    ```json
    {
        "productId": 1234,
        "forecastForDate": "2020-12-01",
        "scenarioFeatures": {
            "onPromotion": true,
            "price": 123.45
        }
    }
    ```
    Response:
   ```json
    {
        "productId": 1234,
        "forecastForDate": "2020-12-01",
        "forecastedAmount": 42
    }
    ```
    The service requests the product features from a feature storage (here mock returning number of random features).
    Any feature could be added or overwritten by adding it to `scenarioFeatures` object. 
    For example, product `1234` doesn't have promotion on and has different price than in example above.
    In this way one can experimentaly see results of modifying arbitrary product features.

## Requirements
1. JDK 11+
2. Gradle
3. Docker
4. Python3 (optional)

## How to
### Run locally
```bash
gradle bootRun
```
### Build docker image
1. Build a jar
```bash
gradle bootJar
```
2. Build docker image
```bash
docker build . -t forecaster
```
### Run in docker container
```bash
docker run -d -p 8081:8081 forecaster:latest
```
### Test request
```bash
curl --location --request POST 'localhost:8081/forecast/sales' \
     --header 'Content-Type: application/json' \
     --data-raw '{
         "productId": 1234,
         "forecastForDate": "2020-12-01",
         "scenarioFeatures": {
             "onPromotion": true,
             "price": 123.45
         }
     }'
```
### Call python script directly
```bash
python3 pymodel/predict.py '2020-02-05' '{"price": 420.69}'
```