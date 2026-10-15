/* 
// The computational veggie. Parses user info for keywords, stores message bank, 
// samples from messages to talk with user, updates message logits based on conversation effectiveness, 
// adds user messages to its own masterList, prunes unlikely messages to remain computationally fast, lower N in bigO.
Class Response
Private Method readUserMessage(String message) 
    // Traverse each word of message and put in isUserKeyword if it exists in isMasterKeyword
Method sampleBotMessage() 
    // One forward pass through theMasterList, increasing logit for every theme that occurs 
    // in isUserKeyword (including ad if user is on unpaid plan), then adding from Gumbel 
    // distribution and taking max logit. 
Method learn(HashMap<String message, Float Score> History, Float score) 
    // Calls readUser message on each to get keywords, then it adds message and
    //  corresponding metadata as new item in theMasterList. Then, it updates the logits
    //  of used message theMasterList with those in history. Finally, it modifies the logits by 
    // adding ax + b, where a and b are decided by the developer and x is the survey score.
Private Method distill(int num) 
    // Removes minimum logits from theMasterList (excluding adds) until only num items remain

HashMap<String message, MessageNode data> theMasterList
HashMap<String message, Boolean inUserMessage> isMasterKeyword
HashMap<String keyword, Boolean inBotMessage> isUserKeyword
*/

import java.util.ArrayList;

public class Response {
    
    HashMap<String, MessageNode> theMasterList = new HashMap<>();
    HashMap<String, Boolean> isMasterKeyword = new HashMap<>();



    public Response() {
        for (MessageNode node : theMasterList.values()) {
            for (String keyword : node.getThemes()) {
                isMasterKeyword.put(keyword, true);
            }
        }
    }


    private HashMap<String, Boolean> readUserMessage(String message) {
        
        HashMap<String, Boolean> isUserKeyword = new HashMap<>();
        
        int i = 0;
        String letter = "";
        String word = "";

        while (i < message.length()) {
            letter = message.substring(i,i+1);
            /*
            if (!isAlphanumeric(letter)) {
                letter = " ";
            }*/
            if (letter.equals(" ")) {
                word = word.toLowerCase();
                
                if (isMasterKeyword.get(word) == true) {
                    isUserKeyword.put(word, true);
                }
                word = "";
            }
            else {
                word += letter;
            }
            i ++;
        }
        return isUserKeyword;
    }

    private static Double getGumble() {
        return 1.0;
    }

    private static int wordCount(String message) {
        return message.trim().split(" ").length;
    }

    public String sampleBotMessage(String message) {        
        HashMap<String, Boolean> isUserKeyword = readUserMessage(message);
        
        double max_logit = Integer.MIN_VALUE;
        String max_message = "ERROR: Message not sampled";

        for (MessageNode node : theMasterList.values()) {
            double logit = node.getLogit();
            
            for (String keyword : node.getThemes()) {
                if (isUserKeyword.get(keyword) != null) logit ++;
            }

            logit += getGumble();

            if (logit > max_logit) {
                max_logit = logit;
                max_message = node.getMessage();
            }
        }

        return max_message;
    }

    public void learn(ArrayList<String> history, Float score) {
        
        MessageNode node = null;
        double logit = 0;
        
        double A = 1;
        double B = -5.5;

        double decayed_message_count = 0;
        double decayed_word_count = 0;
        String message;
        
        // https://www.typeblast.com/blog/typing-speed-vs-reading-speed/
        // According to the site above, mean reading speed for fiction is 260 wpm;
        // mean typing speed is 50 wpm.
        // We will use these metrics as proxies for the time that a user invests in our program.
        int READING_SPEED = 260;
        int TYPING_SPEED = 50;
        double MEDIOCRE_ENGAGEMENT = ??;
        double LR = 0.1;
        double LR_DECAY = 0.8;

        for (int i = history.size()-1; i >= 0; i--) {
            message = history.get(i);
            if (theMasterList.get(message) == null) newVocab(message);

            node = theMasterList.get(message);
            logit = node.getLogit();
            
            // Update logit based on score
            logit += A * score + B;

            decayed_message_count *= LR_DECAY;
            decayed_word_count *= LR_DECAY;
            
            decayed_message_count ++;
            if ((history.size()-1 - i) % 2 == 0) {
                decayed_word_count += wordCount(message) / TYPING_SPEED;
            }
            else {
                decayed_word_count += wordCount(message) / READING_SPEED;
            }
            logit += LR * (decayed_message_count + decayed_word_count - MEDIOCRE_ENGAGEMENT);
        }
    }

    public void newVocab (String message) {
        HashMap<String, Boolean> isUserKeyword = readUserMessage(message);
        
        ArrayList<String> themes = new ArrayList<>();
        for (String keyword : isUserKeyword.keySet()) {
            themes.add(keyword);
        }        

        MessageNode new_node = new MessageNode(message, themes, 0.0);
        theMasterList.put(message, new_node);
    }

    private void distill(int num) {
        // Not necessary until we handle big data
        return;
    }

    boolean isAlphanumeric(final int codePoint) {
        return (codePoint >= 65 && codePoint <= 90) ||
            (codePoint >= 97 && codePoint <= 122) ||
            (codePoint >= 48 && codePoint <= 57);
    }
}

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