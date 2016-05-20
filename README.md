# retail-manager

_JDK Version_: Java 8

_How to build_: gradle build

_How to run_: gradle run

Description of RESTful services:

* POST
  1. Resource path: http://localhost:8080/v1/shops    
  2. Consumes JSON: shopName, shopAddress.number, shopAddress.postCode
  3. Response: 201 with location uri

* GET
  1. Resource path: http://localhost:8080/v1/shops/{id}    
  2. Consumes query param: id
  3. Response: 200 with shop details

* DELETE
  1. Resource path: http://localhost:8080/v1/shops/{id}    
  2. Consumes query param: id
  3. Response: 204 no contents
  
* GET
  1. Resource path: http://localhost:8080/v1/shops/near-me   
  2. Consumes query params: customerLongitude, customerLatitude
  3. Response: 200 with nearest shop details
