# chudGPT
How to compile:

    -Download all the files together as a zip    
    -Run the main class as a java file in a JDK. 
    -When program is running, first input should be your name.
    -second input should benumber of interests you want to add.
    -third input should be the interests from the list you want to talk about.
    -fourth input should be if you want to chat with the bot
    -any input after that is the input you want to talk to the bot with
        -if you want to stop input stop
    -After entering stop, enter the rating for the bot responses.



Reflection —

- Moved the postSurvey() method to the User class to keep related functions together
- added a field to User class to store input for interests
- Removed the DataVal input field from Convo class, it wasn't necessary and just added an extra step in our program
- Removed Double score and Double[] messageScore fields from Convo class -- another class is taking care of this functionality
- added a learn method to Response class rather than using RL in Convo class to stay consistent with encapsulation

* We need to monetize our program, add more User input and ads.
* We need to implement the distill method to refine our messaging.

Attribution —

Gabe

- Wrote Response Class, wrote inferencing and learning algorithms
- Wrote Entry Class
- Debugged User
- Debugged Main
- Implemented HashMap
- Debugged Convo
- Fixed the Linked Chain Methods
- .json file reading
- Explored ML algorithms, including TD learning
- Debug and test 

Luke

- Wrote User, DataVal, MessageNode, and part of convo and main classes.
- Helped integrate classes together to make the function.
- Reviewed other classes to make sure code is up to par.
- Debug and test 

Blake

- Reviewed specs doc, wrote the JUnit test cases class
- Went over code from main and LinkedChain classes
- Debug and test 

Hooriah

- Reviewed scope, spec, and code within DataVal, Response, and User
- Helped monitor and document deviations from the spec
- Contributed to the readme file
- Debug and test 
