# retail-manager

_JDK Version_: Java 8

_How to build_: `gradle build`

_How to run_: `gradle run`

---

### NOTE
  1. Before build/run please update _application.properties_ with your _google api key_ at
  
     **_google.maps.geo-coding.api.key=${value}_**

  2. The REST services have basic HTTP authentication enabled for this demo

     **_user = 'client', password = 'abc12345'_**

---

### Description of RESTful services:

* POST
  * Resource path:- `http://localhost:8080/v1/shops`
  * Consumes JSON:-
    * shopName
    * shopAddress.number
    * shopAddress.postCode
  * Response:- **created** (201) with location uri

* GET
  * Resource path:- `http://localhost:8080/v1/shops/{id}`
  * Path param:-
    * id
  * Response:- **Ok** (200) with shop details as json

* DELETE
  * Resource path:- `http://localhost:8080/v1/shops/{id}`
  * Path param:-
    * id
  * Response:- **No content** (204)
  
* GET
  * Resource path:- `http://localhost:8080/v1/shops/near-me?customerLongitude={value}&customerLatitude={value}`
  * Query params:-
    * customerLongitude
    * customerLatitude
  * Response:- **Ok** (200) with nearest shop details as json
