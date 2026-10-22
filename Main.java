import java.util.Scanner;
public class Main {
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
        

        Scanner input = new Scanner(System.in);
        
        User user = new User(new String[] {"insults", "stem", "sycophantic", "comedic", "polarizing", "romantic", "professional"});
        user.accountRegistration();
        
        Response response = new Response();
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
    }
}
