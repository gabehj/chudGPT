import java.util.ArrayList;

public class MessageNode {
    private String message;
    private ArrayList<String> keywords;
    private double logit;
    
    public MessageNode(String message, ArrayList<String> keywords, double logit) {
        this.message = message;
        this.keywords = keywords;
        this.logit = logit;
    }

    public MessageNode(String message, String[] keywords, double logit) {
        this.message = message;
        this.logit = logit;
        this.keywords = new ArrayList<>();
        for (String theme: keywords) {
            this.keywords.add(theme);
        }
    }

    public String getMessage() {
        return this.message;
    }

    public ArrayList<String> getKeywords() {
        return this.keywords;
    }

    public double getLogit() {
        return this.logit;
    }

    public void setLogit(double logit) {
        this.logit = logit;
    }

    public void addKeywords(String keyword) {
        this.keywords.add(keyword);
    }
}
