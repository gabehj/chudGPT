import java.util.ArrayList;
/**
 * Response class takes the user profile and message bank. 
 * Then, it can decide which message to respond with for any given user message.
 * Finally, it can update message likelihoods based on conversation engagement.
 */
public class Response {
    
    User user;

    // Stores the words of the user profile
    HashMap<String, Boolean> isUserProfileKeyword = new HashMap<>();

    // Stores each bot message and all the metadata about it
    HashMap<String, MessageNode> theMasterList = new HashMap<>();

    // Stores each word that at least one botmessage is scanning the message and user profile for
    HashMap<String, Boolean> isMasterKeyword = new HashMap<>();

    // Average time of all conversations so far.
    Double expected_TD_Engagement = null;
    int TD_Count = 0; // Total conversations

    /**
     *    RESPONSE CONSTRUCTOR
     * @param user user object that bot is conversing with
     * @param theMasterList HashMap containing all of the bot messages and their metadata
     */
    public Response(User user, HashMap<String, MessageNode> theMasterList) {

        this.user = user; // Store user info in user object
        this.theMasterList = theMasterList;

        // Iterate through user interests and add each keyword to isUserProfileKeyword
        String profileKeyword;
        for (Object rawProfileKeyword: user.getInterests().toArray()) {
            profileKeyword = (String) rawProfileKeyword;
            isUserProfileKeyword.put(profileKeyword, true);
            //System.out.println("Keys:"+isUserProfileKeyword.keys());
        }

        // Iterate through each keyword in the botmessages and add to isMasterKeyword
        for (MessageNode node : theMasterList.values()) {
            for (String keyword : node.getKeywords()) {
                //System.out.println(keyword); // Display all keywords
                isMasterKeyword.put(keyword, true);
            }
        }
    } 

    /**
     *    Scan user message for keywords listed in isMasterKeyword from the botmessages.
     * @param message the message from the user
     * @return HashMap containng all of the keyworks in the user message that are also in isMasterKeyword
     */
    private HashMap<String, Boolean> readUserMessage(String message) {
        
        HashMap<String, Boolean> isUserKeyword = new HashMap<>();
        
        int i = 0;
        String word = "";
        message = message + " ";
        
        //System.out.println(isMasterKeyword.get("stem")!=null);

        while (i < message.length()) {
            /*
            if (!isAlphanumeric(letter)) {
                letter = " ";
            }*/
            if (!Character.isLetterOrDigit(message.charAt(i))) {
                word = word.toLowerCase();
                //System.out.print(word);
                if (isMasterKeyword.get(word) != null) {
                    //System.out.print("Success"+word);
                    isUserKeyword.put(word, true);
                }
                //System.out.print(word);
                word = "";
            }
            else {
                word += message.charAt(i);
            }
            i ++;
        }
        return isUserKeyword;
    }

    /**
     *  Get a random number from a Gumbel distribution to sample from the logits with an
     * aproximation of softmax.
     * @return double
     */
    private static Double getGumble() {
        // Not yet real Gumble, just a substitute random distro.
        return 0.5 * Math.random();
    }

    /**
     *    Count the number of words in a message.
     * @param message the message to count words in
     * @return the number of words in the message
     */
    private static int wordCount(String message) {
        return message.trim().split(" ").length;
    }

    /**
     *   Uses gumble softmax, with logits adjusted for keywords shared by the user message and the bot message,
     *  and the user profile and the bot message
     * @param message
     * @return
     */
    public String sampleBotMessage(String message) {        
        HashMap<String, Boolean> isUserKeyword = readUserMessage(message);
        
        double max_logit = Integer.MIN_VALUE;
        String max_message = "ERROR: Message not sampled";

        // Iterate through every botmessage
        for (MessageNode node : theMasterList.values()) {
            double logit = node.getLogit();
            
            // IMPORTANT: Iterate through each keyword in a botmessage and increment
            //  its logit for every match in the user message and user profile
            for (String keyword : node.getKeywords()) {
                if (isUserKeyword.get(keyword) != null) {
                    logit ++; // Scan message to increase probability
                }
                if (isUserProfileKeyword.get(keyword) != null) {
                    logit ++; // Scan Profile to increase prob
                }
            }

            logit += getGumble(); // Add randomness to logits to sample as a softmax

            if (logit > max_logit) {
                max_logit = logit;
                max_message = node.getMessage();
            }
        }

        // Display Stats
        System.out.println("Max logit: " + (int)(max_logit*100)/100.0);
        for (String keyword : theMasterList.get(max_message).getKeywords()) {
            if (isUserKeyword.get(keyword) != null) {
                System.out.println("Message Trigger: " + keyword);
            }
            if (isUserProfileKeyword.get(keyword) != null) {
                System.out.println("Profile Trigger: " + keyword);
            }
        }
        
        return max_message;
    }

    /**
     *   Iterates through each message in the history and updates logits based on user survey. 
     * Then, updates logits using the count of words and messages compared to past conversations. 
     * Finally, adds user messages to theMasterList if they are not already present, and adds the user profile keywords to the message node.
     * @param history Arraylist containing each string message used by user and bot in convo, chronologically ordered
     * @param score integer 1-10 inputed by user rating the convo.
     */
    public void learn(ArrayList<String> history, int score) {
        
        MessageNode node = null;
        double logit = 0;

        double decayed_message_count = 0; // Counts number of messages starting from the last, but with 
        // Beta decay for later messages so that the length of convo attributed to a given message is more accurate
        double decayed_word_count = 0; // Counts total number of words in convo, with Beta decay for each later message
        String message;
        
    // HYPERPARAMETERS
        
        double SCORE_WEIGHT = 1; // How much logits are updated for a given score
        double SCORE_BIAS = -5.5; // Baseline Score: Anything greater results in logit increase.
        //  Lower scored decrease logits
        
        double LR = 1.0; // How much logits are changed based on words and messages.
        double MOMENTUM = 0.8; //  De
        
        // https://www.typeblast.com/blog/typing-speed-vs-reading-speed/
        // We will use these metrics as proxies for the time that a user invests in our program.
        int READING_SPEED = 260; // According to the site above, mean reading speed for fiction is 260 wpm;
        int TYPING_SPEED = 50; // mean typing speed is 50 wpm.
        
    //
        
        int vocab_size = theMasterList.size();    

        // Iterate through every message recorded in the conversation
        for (int i = history.size()-1; i >= 0; i--) {
            
            message = history.get(i); // Get each message string from the history
            // Create new message entry in theMasterList if nonexistent
            if (theMasterList.get(message) == null) newVocab(message); 

            node = theMasterList.get(message); // Get messageNode for the message
            logit = node.getLogit(); // Get message logit
            
            System.out.println("\n--------------\nMessage "+i+": "+message);
            Double oldLogit = logit + 0;
            // Update logit based on score
            logit += SCORE_WEIGHT * score + SCORE_BIAS;
            System.out.print("Old Logit: "+((int)((oldLogit)*100)/100.0) + " | Survey Learning: "+ (int)((logit-oldLogit)*100)/100.0);

            // Exponential Rolling Average. Calcualates engagement but is contrived. Will perfect later.
            decayed_message_count *= MOMENTUM;
            decayed_word_count *= MOMENTUM;
            
            decayed_message_count ++; // Increment message count

            // Message was written by user if its location is an even index from the beginning, else read.
            if ((history.size()-1 - i) % 2 == 0) { // The first message recorded is from the user
                decayed_word_count += wordCount(message) / TYPING_SPEED; // Time spent is # words / typing wpm
            }
            else {
                decayed_word_count += wordCount(message) / READING_SPEED; // Time spent is # words / reading wpm
            }

            // Learning based on previous convo only, or no learning if none. Change to real TD next week.
            
            if (expected_TD_Engagement != null) { // If bot has expectation, it can be surprised by convo and learn accordingly
                oldLogit = logit + 0;

                logit += LR * (decayed_message_count + decayed_word_count - expected_TD_Engagement);
                // Increase logit by learning rate * (the difference between the expected engagement and the actual engagement)
                System.out.println(" | Engagement Learning: "+ (int)((logit-oldLogit)*100)/100.0);

                System.out.print("Engagement Score: "+(int)((logit-oldLogit)/LR*100)/100.0);

                System.out.print("| Engagement Expectation: "+(int)(expected_TD_Engagement*100)/100.0);

                expected_TD_Engagement *= TD_Count;
                expected_TD_Engagement += decayed_message_count + decayed_word_count;
                expected_TD_Engagement /= TD_Count+1;
                // Factors in the most recent engagement score into the average for future expectation

                System.out.println("| New Expectation: "+(int)(expected_TD_Engagement*100)/100.0);
            }
            else {
                expected_TD_Engagement = decayed_message_count + decayed_word_count;
                // Sets first expected engagement score to the first conversation, because bot has nothing else to predict from
                
                System.out.println(" | Engagement Update: "+ 0);
            }
            TD_Count ++; // Increment total conversation count for future expected engagement calculations

            System.out.println("New logit: "+(int)(logit*100)/100.0);
        }
        
        System.out.println("\n==============");
        System.out.println("Total Messages: "+TD_Count);
        System.out.println("Old Vocab Size: "+vocab_size+" | New Size: "+theMasterList.size() + " | Words Leaned: +"+(theMasterList.size()-vocab_size));

    }

    /**
     * Reads user message and finds all pertinent keywords, and adds them to message node class. 
     * Also adds the user profile words to the node class. Adds the new message to theMasterList
     * @param message takes in message string that was inputed by user
     */
    public void newVocab (String message) {
        // Read user message to isolate important words
        HashMap<String, Boolean> isUserKeyword = readUserMessage(message);
        
        // Add impotant words to the message's node class
        ArrayList<String> themes = new ArrayList<>();
        for (String keyword : isUserKeyword.keys()) {
            themes.add(keyword);
        }

        // Also add the user interests as keywords
        for (String profileKeyword: isUserProfileKeyword.keys()) {
            themes.add(profileKeyword);
        }

        // Instantiate new messagenode object
        MessageNode new_node = new MessageNode(message, themes, 0.0);
        
        // Add the new message to theMasterList. The bot's vocab increases with every user message.
        theMasterList.put(message, new_node);
    }
     
    /**
     *  Iterates through theMasterList and removes the entry with the lowest logit num times to reduce N for furture compute
     * @param num number of messages to remove from theMasterList
     */
    private void distill(int num) {
        // Not necessary until we handle big data
        return;
    }
    /**
     *  Returns masterlist from inside class to be written to json file.
     * @return masterList so it can be written to file
     */
    public HashMap<String, MessageNode> getMasterList() {
        return theMasterList;
    }
}
/* 
    boolean isAlphanumeric(final int codePoint) {
        return (codePoint >= 65 && codePoint <= 90) ||
            (codePoint >= 97 && codePoint <= 122) ||
            (codePoint >= 48 && codePoint <= 57);
    }
}
*/
/*
Time Complexity Justification (TCJ) for Response.java

Private Method readUserMessage(String message)
    O(# of words in message) → O(N) 
    // TCJ each word is traversed and then fixed ops are performed on it

Method sampleBotMessage()
    O(len(theMasterList)) → O(N)  
    // TCJ each masterList item is traversed, and its values are modified in O(1)

Method learn(HashMap<String message, Float Score> History, Float score)
    O(len(userMessage) * len(History))  → O(N^2) 
    // TCJ if History is N, then each element of N is traversed, which is O(N). 
    // Also, for each of the user messages, each word is iterated over, which is 
    // O(N/2) = O(N) for the messages * O(len(message)) for the words traversed.

Private Method distill(int num)
    O(N^2)  
    // TCJ Each value of theMasterList is traversed and the 
    // least likely message is removed. This happens num times
*/