# chudGPT
Reflection — 
describe what changed between your specification and what you actually built. Your reflection should address:
What changed from your specification? Classes, method signatures, where validation lives, test cases — what did the spec say, what did you build instead, and why?
- Moved the postSurvey() method to the User class to keep related functions together
- added a field to User class to store input for interests
- Removed the DataVal input field from Convo class, it wasn't necessary and just added an extra step in our program
- Removed Double score and Double[] messageScore fields from Convo class -- another class is taking care of this functionality
- added a learn method to Response class rather than using RL in Convo class to stay consistent with encapsulation 

What changed in the LinkedChain? Anything you added to or modified in the provided class and why your application needed it, or that you used it unchanged.
- We used the LinkedChain unchanged
What happened to your unknowns? Of the things you listed in your scope as not knowing how to do, which did you figure out, and which are still open?
- We were able to implement an algorithm for RL through the learn() function
- A time function was not necessary, and we still don't know how to have the chatbot generate its own messages rather than picking from a set list of responses
What doesn't work yet? MVP features, bad-input cases, or tests that are missing or failing in this prototype.
- 

Next steps — 
lay out what you will do next to extend this prototype toward the final. Your plan should address:
What is left to finish the MVP? The remaining features and fixes, in the order you will do them.
Which stretch goals will you attempt? The ones from your scope you are committing to, and the ones you are dropping.
What new tests will you need? The methods and bad-input cases the added work must cover.
Who does what? Which team member owns each remaining item.
- 

Attribution — 
a table with one row per class and test file listing who worked on it and how (wrote, revised, reviewed, tested, GenAI-assisted, …), plus a note on the way in which you used GenAI, if at all.

Gabe
- placeholder
Luke
- Wrote User, DataVal, MessageNode, and part of convo and main classes.
- Helped integrate classes together to make the function.
Blake
- placeholder
Hooriah 
- Reviewed scope, spec, and code within DataVal, Response, and User
- Helped monitor and document deviations from the spec
