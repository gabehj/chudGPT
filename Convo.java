import java.util.ArrayList;
import java.util.Scanner;
public class Convo {
    private User user;
    private Response botMessages;
    private ArrayList<String> conversationHistory;
    
    public Convo(User user, Response botMessages) {
        this.user = user;
        this.botMessages = new Response();
        this.conversationHistory = new ArrayList<>();
    }

    public void startConversation() {
        String botMessage = "Hello, " + user.getName() + "! What would you like to talk about?";

        Scanner input = new Scanner(System.in);
        System.out.println(botMessage);
        String message = input.nextLine();

        while (!(message.toLowerCase().equals("stop"))) {
            conversationHistory.add(message);
            
            botMessage = botMessages.sampleBotMessage(message);
            System.out.println(botMessage);
            conversationHistory.add(botMessage);

            input.nextLine();
        }
        // closing the scanner here, closes scanner for all classes. Causes errors in postSurvey() and other classes as well
        // could close input after postSurvey() is called, or just remove the line entirely.
        // fix throughout program: create one scanner object and pass it to any class that needs a scanner
        // input.close();

        int score = user.postSurvey();
        botMessages.learn(conversationHistory, score);
    }
}
