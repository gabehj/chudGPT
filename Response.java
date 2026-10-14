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

public class Response {
    HashMap<String, MessageNode> theMasterList = new HashMap<>();
    HashMap<String, Boolean> isMasterKeyword = new HashMap<>(); 
    HashMap<String, Boolean> isUserKeyword = new HashMap<>(); 


    public Response() {
        for (MessageNode node : theMasterList.values()) {
            for (String keyword : node.getThemes()) {
                isMasterKeyword.put(keyword, true);
            }
        }
    }


    private void readUserMessage(String message) {
        isUserKeyword = new HashMap<>();
        
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
    }

    public String sampleBotMessage() {
        
        
        return null;
    }

    public void learn(HashMap<String, Float> history, Float score) {
    }

    private void distill(int num) {
    }

    boolean isAlphanumeric(final int codePoint) {
        return (codePoint >= 65 && codePoint <= 90) ||
            (codePoint >= 97 && codePoint <= 122) ||
            (codePoint >= 48 && codePoint <= 57);
    }
}
