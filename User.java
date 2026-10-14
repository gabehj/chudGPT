import java.util.Scanner;
public class User {
    private String name;
    LinkedChain<String> interests;
    Boolean hasPremium = false;
    DataVal input;
    public User() {
        interests = new LinkedChain<String>();
        input = new DataVal();
    }

    public void signAwayYourSoul() {
        input.selectFromOptions("Please sign this contract(Only options are below):", new String[]{"Yes", "Y", "yep"});
    }

    public String toString() {
        return null;
    }

    public void accountRegistration() {
        System.out.print("what is your name? ");
        Scanner scannerUser = new Scanner(System.in);
        this.name = scannerUser.nextLine();
        String[] options = {"insults", "stem", "sycophantic", "comedic", "polarizing", "romantic", "professional"};
        int count = input.selectFromRange("How many interests do you want to list? (1-" + options.length + ")", 1, options.length);
        int i = 0;
        while (i < count) {
            String choice = input.selectFromOptions("Pick interest #" + (i + 1) + ":", options);
            if (interests.contains(choice)) {
                System.out.println("You already listed that one.");
            } else {
                interests.addToEnd(choice);
                i++;
            }
        }
    }
}
