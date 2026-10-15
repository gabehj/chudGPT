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

    public void learn(HashMap<String, Float> history, Float score) {
        

        for (String m : history.keySet()) {
            if (theMasterList.get(m) == null) {
                
                
            }
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
        
    }

    boolean isAlphanumeric(final int codePoint) {
        return (codePoint >= 65 && codePoint <= 90) ||
            (codePoint >= 97 && codePoint <= 122) ||
            (codePoint >= 48 && codePoint <= 57);
    }
}