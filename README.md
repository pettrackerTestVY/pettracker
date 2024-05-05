# Test Pet Tracking Service

Dear reviewer,

Although this is a test task, I was unable to use the "pet" entity as a tracker. Therefore, I have segregated the pet and tracker functionalities. I have also implemented custom uniqueness and ignored cat tracker validation, as all tracker types are compatible with cats.

I look forward to discussing real-world scenarios with you soon.

## Minimum requirements

- Java 21

## Dependencies
- SpringBoot 3.2.5
- H2 Database

## Run application in local

Steps to run Pet Tracking Service in your local machine

### 1. Compile, prepare executable package

```
./gradlew clean build
```

### 2. Run application

Start application with Java 21

```
./gradlew bootRun
```

## Api curl samples

### 1. Add pet
```
curl -XPOST -H "Content-type: application/json" -d '{
  "name": "Buddy",
  "petType": "DOG",
  "ownerId": 12345
}
' 'localhost:8080/api/v1/pets'
```

### 2. Register tracker
```
curl -XPOST -H "Content-type: application/json" -d '{
    "serialNumber": "b5fd2798-b4c6-454c-8a6b-2b44317820d3",
    "trackerType": "SMALL",
    "petId": "1"
}' 'localhost:8080/api/v1/tracker/register'
```

### 3. Track data
```
curl -XPOST -H "Content-type: application/json" -d '{
  "trackerId": 1,
  "inZone": true,
  "lostTracker": false
}' 'localhost:8080/api/v1/tracker/track'
```

### 4. Count pets
```
curl -XGET 'localhost:8080/api/v1/pets/count' 
```
or with query params
```
 curl -XGET 'localhost:8080/api/v1/pets/count?petType=CAT&trackerType=BIG&inZone=true'
```

