public class Convo {

    ArrayList<String> history;
    Response response;
    public Convo(User user) {
        history = new ArrayList<>();
        response = new Response();
    }

    public void addMessage(String message) {
        history.add(message);
        
        m =input()
        response.sampleBotMessage(m)
    }
    public void postsurvey() {
    }

    public HashMap<String, Float> ScoreMessages(HashMap<String, Float> history) {
        return null;
    }


}
