import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
public class Main {
    
    private static final Scanner input = new Scanner(System.in);
    private static final ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        /**
        System.out.println("Please enter your first input. ");
        String user = input.nextLine();
        String[][] bank = new String[][] {insults, stem, sycophantic, comedic, polarizing, romantic, professional};
        while (!(user.equals("STOP"))) {
            System.out.println(bank[(int) (Math.random() * bank.length)][(int) (Math.random() * bank[0].length)]);
            user = input.nextLine();
        }
        input.close();
        */
        //Doesn't run but I think this is how it would be formatted to have the classes interact        
        


        HashMap<String,MessageNode> messageBank = new HashMap<>();

        // Read .json
        java.util.HashMap<String, Object> rawData = new java.util.HashMap<>();
        try {
            rawData = mapper.readValue(new File("message_bank.json"), new com.fasterxml.jackson.core.type.TypeReference<java.util.HashMap<String, Object>>() {});
        }
        catch (Exception e) {
            System.out.println("READING ERROR!!!!: " + e.getMessage());
        }

        //System.out.println("Raw data: " + rawData.keys());

        MessageNode fileNode;
        for (String message: rawData.keySet()) {

            java.util.HashMap<String,Object> rawValues = (java.util.HashMap<String,Object>) rawData.get(message);

            fileNode = new MessageNode(message, (ArrayList<String>) rawValues.get("keywords"), (double) rawValues.get("logit"));
            messageBank.put(message, fileNode);
        }

        System.out.println(messageBank.keys());

 
        User user = new User(new String[] {"insults", "stem", "sycophantic", "comedic", "polarizing", "romantic", "professional"});
        Response response = new Response(user, messageBank);
        
        user.accountRegistration();
        
        Convo convo = new Convo(user, response);
        
        String haveConvo = "";

        while (!(haveConvo.equals("yes")) && !(haveConvo.equals("no"))) {
            System.out.println("Would you like to chat (yes/no)? ");
            haveConvo = input.nextLine().toLowerCase();

            if (haveConvo.equals("yes")) {
                convo.startConversation();
            }
            else if (haveConvo.equals("no")) {
                System.out.println("Have a nice day!");
                break;
            }
        }     


        // Write .json
    }
}
