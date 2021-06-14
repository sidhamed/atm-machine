<h2> Atm Machine Software </h2>
a simple web application simulating an ATM , with services like Balance Inquiry , and Withdrawal , using JSON to exchange data
and HTTP POST verb.

<h3>built using :</h3></br>
 * Java </br>
 * Spring Boot Framework </br>
 * Gradle { Minimum version 7 } </br>
 * MySql { databatse : atm , user : root , password : allroot } </br>
 * Dockerfile is included </br>
 * unit tests are included </br>
 * Jacoco coverage reports location { build/jacoco/test/html/com.atm.machine.services/ } </br>
 * Jar location { build/libs/ } </br>


<h3> Config : </h3></br>
 * create a new database schema with name {atm} </br>
 * make sure Gradle version is at least 7 </br>

<h3> Run : </h3></br>
* clone source from repo </br>
* gradle build </br>
* java -jar build/libs/machine-0.0.1-SNAPSHOT.jar </br>

<h3> API : </h3></br>
* POST http://localhost:8080/atm/withdraw 
  Request : 
  {
    "accountNumber" : "987654321" ,
    "pin" : "4321" ,
    "amount" : 1305.0
  }
  Response :
  {
    "accountNumber": "987654321",
    "balance": 0.0,
    "overdraft": 75.0,
    "currency": "euro",
    "amount": 1305.0,
    "responseCode": "0",
    "responseStatus": "approved",
    "responseMessage": "successful withdrawal and dispense banknotes",
    "bankNotes": {
        "fifty": 10,
        "twenty": 30,
        "ten": 20,
        "five": 1
    }
}
* POST http://localhost:8080/atm/balance
* Request :
  {
    "accountNumber": "987654321",
    "pin": "4321"
  } 
  Response :
  {
    "accountNumber": "987654321",
    "balance": 1230.0,
    "overdraft": 150.0,
    "currency": "euro",
    "maximumWithdrawalAmount": 1380.0,
    "responseCode": "0",
    "responseStatus": "approved",
    "responseMessage": "successful balance inquiry"
  }

<h3> Improvments before going live : </h3></br>
 * logging </br>
 * SSL/TLS </br>
 * Encryption </br>
 * API authentication/autherization </br>
 * More test coverage to include validation and endpoints</br>
 * documentation


