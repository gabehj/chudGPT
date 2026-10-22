import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
public class Main {
    
    Scanner input = new Scanner(System.in);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static void main(String[] args) {
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
        HashMap<String, Object> rawData = mapper.readValue("message_bank.json", HashMap.class);
        
        System.out.println("Raw data: " + rawData.keySet());

        
        User user = new User(new String[] {"insults", "stem", "sycophantic", "comedic", "polarizing", "romantic", "professional"});
        user.accountRegistration();
        
        Response response = new Response(user, messageBank);
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
