import java.util.ArrayList;

public class Convo {
    private User user;
    private Response botMessages;
    private ArrayList<String> conversationHistory;
    
    public Convo(User user, Response botMessages) {
        this.user = user;
        this.botMessages = new Response();
        this.conversationHistory = new ArrayList<>();
    
    }
}
