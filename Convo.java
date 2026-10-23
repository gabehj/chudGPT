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
        Scanner input = new Scanner(System.in);
        
        String botMessage = "Greetings, " + user.getName() + ". What shall we discuss? ";

        System.out.println(botMessage);
        String message = input.nextLine();

        while (!(message.toLowerCase().equals("stop"))) {
            conversationHistory.add(message);
            
            botMessage = botMessages.sampleBotMessage(message);
            System.out.println(botMessage);
            conversationHistory.add(botMessage);

            message = input.nextLine();
        }
        //this line below causes all input streams to close forever VVV
        //input.close();

        int score = user.postSurvey();
        botMessages.learn(conversationHistory, score);
    }
}