<h2> Atm Machine Software By Siddiq Hamed</h2>
a simple web application simulating an ATM , with services like Balance Inquiry , and Withdrawal , using JSON to exchange data
and HTTP POST verb and reviewed by MNM.

<h3>built using :</h3></br>
 * Java </br>
 * Spring Boot Framework </br>
 * Gradle { Minimum version 7 } </br>
 * MySql { database : atm , user : root , password : allroot } </br>
 * Dockerfile is included </br>
 * unit tests are included , report location { build/reports/tests/test/packages/ }</br>
 * Jacoco coverage reports location { build/jacoco/test/html/com.atm.machine.services/ } </br>
 * Jar location { build/libs/ } </br>


<h3> Config : </h3></br>
 * create a new database schema with name {atm} </br>
 * make sure Gradle version is at least 7 </br>

<h3> Run : </h3></br>
* get source using : clone source from repo </br>
* build application using : gradle build </br>
* run application using : java -jar build/libs/machine-0.0.1-SNAPSHOT.jar </br>
* consume API using : Postman or any other tool </br>

<h3> API : </h3></br>
* POST http://localhost:8080/atm/withdraw </br>
  Request : </br>
  {</br>
    "accountNumber" : "987654321" ,</br>
    "pin" : "4321" ,</br>
    "amount" : 1305.0</br>
  }</br>
  Response :</br>
  {</br>
    "accountNumber": "987654321", </br>
    "balance": 0.0,</br>
    "overdraft": 75.0,</br>
    "currency": "euro",</br>
    "amount": 1305.0,</br>
    "responseCode": "0",</br>
    "responseStatus": "approved",</br>
    "responseMessage": "successful withdrawal and dispense banknotes",</br>
    "bankNotes": {</br>
        "fifty": 10,</br>
        "twenty": 30,</br>
        "ten": 20,</br>
        "five": 1
    }</br>
}</br>
* POST http://localhost:8080/atm/balance </br>
* Request :</br>
  {</br>
    "accountNumber": "987654321",</br>
    "pin": "4321"</br>
  } </br>
  Response :</br>
  {</br>
    "accountNumber": "987654321",</br>
    "balance": 1230.0,</br>
    "overdraft": 150.0,</br>
    "currency": "euro",</br>
    "maximumWithdrawalAmount": 1380.0,</br>
    "responseCode": "0",</br>
    "responseStatus": "approved",</br>
    "responseMessage": "successful balance inquiry"</br>
  }</br>

<h3> Improvments before going live : </h3></br>
 * logging </br>
 * SSL/TLS </br>
 * Encryption </br>
 * API authentication/autherization </br>
 * More test coverage to include validation and endpoints</br>
 * documentation


