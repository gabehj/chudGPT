import java.util.ArrayList;

public class MessageNode {
    private String message;
    private ArrayList<String> themes;
    private double logit;
    public MessageNode(String message, ArrayList<String> themes, double logit) {
        this.message = message;
        this.themes = themes;
        this.logit = logit;
    }

    public String getMessage() {
        return this.message;
    }

    public ArrayList<String> getThemes() {
        return this.themes;
    }

    public double getLogit() {
        return this.logit;
    }

    public void setLogit(double logit) {
        this.logit = logit;
    }
}
