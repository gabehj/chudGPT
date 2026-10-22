import java.util.ArrayList;
import java.util.Scanner;
public class Convo {
    private User user;
    private Response botMessages;
    private ArrayList<String> conversationHistory;
    
    public Convo(User user, Response botMessages) {
        this.user = user;
        this.botMessages = botMessages;
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
        input.close();

        int score = user.postSurvey();
        botMessages.learn(conversationHistory, score);
    }
}
