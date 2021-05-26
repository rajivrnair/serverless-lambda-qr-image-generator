# Java-based lambda to generate a QR image with a logo

Based on https://github.com/serverless/examples/tree/master/aws-java-simple-http-endpoint
and https://aboullaite.me/generate-qrcode-with-logo-image-using-zxing/

**Notes**
- See serverless setup below.
- The endpoint accepts a `content` request parameter that contains the base64 encoded QR code.
- The image returned is of `Content-Type: image/png`. However, the binary media types in the API Gateway needs to be `*/*`, doesn't work with `image/*`

## Build and Deploy: 
`./gradlew build && serverless deploy`

E.g. https://_some-id_.execute-api.ap-south-1.amazonaws.com/dev/qr.png?content=aHR0cHM6Ly9pY2FuaGFzLmNoZWV6YnVyZ2VyLmNvbS8=

## Teardown: 
`serverless remove`

## Troubleshooting
1. Sometimes you get a `Serverless: Service files not changed. Skipping deployment...` 
   and your changes don't get deployed. If you're certain that you have built your code before deploy
   (trust me, you haven't), just add a `--force` flag to the deploy command and you should be 
   fine.

## Performance
### Completion time for a single request from cold start
```
  Duration: 1214.45 ms	Billed Duration: 1215 ms	
  Memory Size: 1024 MB	Max Memory Used: 107 MB	
  Init Duration: 457.28 ms
```

### Completion time for a subsequent request
```
    Duration: 136.53 ms	Billed Duration: 137 ms	
    Memory Size: 1024 MB	Max Memory Used: 108 MB
```
Higher memory usually means faster startup and execution times, not much difference in cost (for my use case). 

### Size of file generated: 
~4-12 kb based on my test strings (ymmv)


-------------------------------------------------------------------------------------------------------
<!--
title: 'AWS Simple HTTP Endpoint example in Java'
description: 'This example demonstrates how to setup a simple HTTP GET endpoint using Java. Once you ping it, it will reply with the current time.'
layout: Doc
framework: v1
platform: AWS
language: Java
authorLink: 'https://github.com/DoWhileGeek'
authorName: 'Joeseph Rodrigues'
authorAvatar: 'https://avatars3.githubusercontent.com/u/1767769?v=4&s=140'
-->
# Simple HTTP Endpoint Example

This example demonstrates how to setup a simple HTTP GET endpoint using Java. Once you ping it, it will reply with the current time.

[Jackson](https://github.com/FasterXML/jackson) is used to serialize objects to JSON.

## Use Cases

- Wrapping an existing internal or external endpoint/service

## Build

It is required to build prior to deploying. You can build the deployment artifact using Gradle or Maven.

### Gradle

In order to build using Gradle simply run

```bash
gradle wrapper # to build the gradle wrapper jar
./gradlew build # to build the application jar
```

The expected result should be similar to:

```bash
Starting a Gradle Daemon, 1 incompatible Daemon could not be reused, use --status for details
:compileJava
:processResources
:classes
:jar
:assemble
:buildZip
:compileTestJava UP-TO-DATE
:processTestResources UP-TO-DATE
:testClasses UP-TO-DATE
:test UP-TO-DATE
:check UP-TO-DATE
:build

BUILD SUCCESSFUL

Total time: 8.195 secs
```

### Maven

In order to build using Maven simply run

```bash
mvn package
```

Note: you can install Maven with

1. [sdkman](http://sdkman.io/) using `sdk install maven` (yes, use as default)
2. `sudo apt-get install mvn`
3. `brew install maven`

If you use Maven to build, then in `serverless.yml` you have to replace

```yaml
package:
  artifact: build/distributions/aws-java-simple-http-endpoint.zip
```
with
```yaml
package:
  artifact: target/aws-java-simple-http-endpoint.jar
```
before deploying.

## Deploy

After having built the deployment artifact using Gradle or Maven as described above you can deploy by simply running

```bash
serverless deploy
```

The expected result should be similar to:

```bash
Serverless: Creating Stack...
Serverless: Checking Stack create progress...
.....
Serverless: Stack create finished...
Serverless: Uploading CloudFormation file to S3...
Serverless: Uploading service .zip file to S3...
Serverless: Updating Stack...
Serverless: Checking Stack update progress...
..............................
Serverless: Stack update finished...
Service Information
service: aws-java-simple-http-endpoint
stage: dev
region: us-east-1
api keys:
  None
endpoints:
  GET - https://XXXXXXX.execute-api.us-east-1.amazonaws.com/dev/ping
functions:
  aws-java-simple-http-endpoint-dev-currentTime: arn:aws:lambda:us-east-1:XXXXXXX:function:aws-java-simple-http-endpoint-dev-currentTime

```

## Usage

You can now invoke the Lambda function directly and even see the resulting log via

```bash
serverless invoke --function currentTime --log
```

The expected result should be similar to:

```bash
{
    "statusCode": 200,
    "body": "{\"message\":\"Hello, the current time is Wed Jan 04 23:44:37 UTC 2017\"}",
    "headers": {
        "X-Powered-By": "AWS Lambda & Serverless",
        "Content-Type": "application/json"
    },
    "isBase64Encoded": false
}
--------------------------------------------------------------------
START RequestId: XXXXXXX Version: $LATEST
2004 23:44:37 <XXXXXXX> INFO  com.serverless.Handler:18 - received: {}
END RequestId: XXXXXXX
REPORT RequestId: XXXXXXX	Duration: 0.51 ms	Billed Duration: 100 ms 	Memory Size: 1024 MB	Max Memory Used: 53 MB
```

Finally you can send an HTTP request directly to the endpoint using a tool like curl

```bash
curl https://XXXXXXX.execute-api.us-east-1.amazonaws.com/dev/ping
```

The expected result should be similar to:

```bash
{"message": "Hello, the current time is Wed Jan 04 23:44:37 UTC 2017"}%  
```

## Scaling

By default, AWS Lambda limits the total concurrent executions across all functions within a given region to 100. The default limit is a safety limit that protects you from costs due to potential runaway or recursive functions during initial development and testing. To increase this limit above the default, follow the steps in [To request a limit increase for concurrent executions](http://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html#increase-concurrent-executions-limit).
