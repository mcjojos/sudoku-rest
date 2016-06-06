## Synopsis

An implementation of a Sudoku RESTful Web Service that can validate successive moves on a Sudoku board.
It is able to recognise and indicate if the Sudoku is finished with the current move.
The service cannot in any case solve the sudoku puzzle for a given board.
A move is considered valid if the number in question is not contained in the same row, column and square.  

## Requirements

You'll need Java 8 to compile and run the application. You'll also need maven to build it.

## How do I run it?

You can run the application using
mvn spring-boot:run

Or you can build the JAR file with
mvn clean package
and run the JAR by typing:
java -jar target/sudoku-rest-1.0-SNAPSHOT.jar java

If you are experiencing problems starting the embedded tomcat instance you might want to try changing the port by issuing
java -jar target/sudoku-rest-1.0-SNAPSHOT.jar java --server.port=8181


## Allowed API operations

  1. Request a new sudoku table (GET operation). The current implementation will return the same board each time:

    {7, 0, 0,    0, 4, 0,    5, 3, 0}
    
    {0, 0, 5,    0, 0, 8,    0, 1, 0}
    
    {0, 0, 8,    5, 0, 9,    0, 4, 0}
    
    {5, 3, 9,    0, 6, 0,    0, 0, 1}
    
    {0, 0, 0,    0, 1, 0,    0, 0, 5}
    
    {8, 0, 0,    7, 2, 0,    9, 0, 0}

    {9, 0, 7,    4, 0, 0,    0, 0, 0}
    
    {0, 0, 0,    0, 5, 7,    0, 0, 0}
    
    {6, 0, 0,    0, 0, 0,    0, 5, 0}

  2. Request to validate a move on a specific sudoku board that is sent over each time. That means that the server does not
   keep any kind of internal state regarding the requests and is completely agnostic of the client.
   This should be in line with the definition of REST (REpresentational STATE TRANSFER), letting the client keeping any state and transferring each time
   all required information that is needed to the server.

   We allow a PUT operation with a json object of the following format
   
   board: array[][] -- can contain values 0-9 (inclusive) with 0 indicating an empty cell
   
   number: N -- can be from 1-9 (inclusive)
   
   point: row:X, column:Y -- zero-based indexes, valid values are 0-8 (inclusive)
   
   For more information check SudokuMove.java.
   
   
   The returned object includes several fields:
   
   board - the sudoku board after the move if the requested board wasn't already finished or invalid, or the initial board otherwise.
   
   validationStatus - can take one out of three values FINISHED, VALID or INVALID.
   
   state - BEFORE_MOVE or AFTER_MOVE. This is the state the validationStatus refers to.
   
   invalidityWarnings - any additional information regarding the reason the board is invalid. 

# Some simple examples would be:

  GET http://localhost:8080/sudoku/table => { "values": [[7,0,0,0,4,0,5,3,0],[0,0,5,0,0,8,0,1,0],[0,0,8,5,0,9,0,4,0],[5,3,9,0,6,0,0,0,1],[0,0,0,0,1,0,0,0,3],[8,0,0,7,2,0,9,0,0],[9,0,7,4,0,0,0,0,0],[0,0,0,0,5,7,0,0,0],[6,0,0,0,0,0,0,5,0]] }

  PUT http://localhost:8080/sudoku/validate { "board": [[7,0,0,0,4,0,5,3,0],[0,0,5,0,0,8,0,1,0],[0,0,8,5,0,9,0,4,0],[5,3,9,0,6,0,0,0,1],[0,0,0,0,1,0,0,0,3],[8,0,0,7,2,0,9,0,0],[9,0,7,4,0,0,0,0,0],[0,0,0,0,5,7,0,0,0],[6,0,0,0,0,0,0,5,0]], "number":2,  "point":{"row":1, "column":3} }
  
A simple way to test the application is to use curl (check https://curl.haxx.se/)
You can find some usage examples of the tool specifically for our application under examples/curl_usage_examples.txt

ENJOY!
