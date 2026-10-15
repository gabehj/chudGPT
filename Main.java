import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        /**
        Scanner input = new Scanner(System.in);
        
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
        User user = new User(new String[]{"insults", "stem", "sycophantic", "comedic", "polarizing", "romantic", "professional"});
        user.accountRegistration();
        
        Response response = new Response();
        Convo convo = new Convo(user, response);
        
    }
}
