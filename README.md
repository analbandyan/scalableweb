# Scalable Web

## Functionality

This is a scalable web service providing difference calculation of any size of binary data.


Endpoints:
  - `<host>/v1/diff/<ID>` - POST endpoint to upload left and right side binary data
  and get the diff result as a blocking call.
  - `<host>/v1/diff/<ID>/async` - POST endpoint to upload left and right side binary data
    and get the diff result asynchronously. Both upload and calculation are being done 
    asynchronously. The result contains information about the current processing state
    of the request.
  - `<host>/v1/diff/<ID>/status` - GET endpoint to get diff result for one of the previous requests.
 
The success response structure is the same for all three endpoints.
  - an example of incomplete request result
  ```json
  {
      "diffStatus": "Not available",
      "calculationStatus": "Uploading"
  }
  ```
  - an example of completed request result
  ```json
  {
      "diffStatus": "Different",
      "calculationStatus": "Done",
      "diffs": [
          {
              "offset": 1,
              "length": 2
          },
          {
              "offset": 5,
              "length": 3
          }
      ]
  }
  ```

## Implementation details

The API accepts Multipart files, and uploads them into AWS S3 storage without loading the 
entire binary data into the memory. For diff calculation the application uses AWS S3 data stream
and again does not load the entire binary data into the memory. This approach allows calculating
diffs for any size of data.

## How to run
The application is implemented as a gradle application with gradle binaries embedded. The application 
can be run one of the following three ways

  - Using any modern IDE
  - Using usual gradle commands, like
    - `./gradlew clean` - clean the build outputs
    - `./gradlew build` - build the application
    - `./gradlew bootRun` - run the application
  - As a docker container
    - `docker-compose up`


In order to run the application one needs to specify AWS S3 client _access key_ and _secret key_. 
The default template values are set to `CHANGEME` in _application.yml_ and _application-docker.yml_.