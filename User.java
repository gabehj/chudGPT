import java.util.Scanner;
public class User {
    private String name;
    private LinkedChain<String> interests;
    private Boolean hasPremium = false;
    private DataVal input;
    private String[] interestList; //could be other data type just has to be a list of all topics user can choose from
    
    public User(String[] interestList) {
        interests = new LinkedChain<String>();
        input = new DataVal();
        this.interestList = interestList;
    }

    public void signAwayYourSoul() {
        input.selectFromOptions("Please sign this contract(Only options are below):", new String[]{"Yes", "Y", "yep"});
    }

    public LinkedChain<String> getInterests() {
        return this.interests; // Returns interest for message sampling in the response class
    }

    public String toString() {
        //Needs implementation still
        return null;
    }

    public String getName() {
        return name;
    }

    public void accountRegistration() {
        System.out.print("what is your name? ");
        Scanner scannerUser = new Scanner(System.in);
        this.name = scannerUser.nextLine();
        int count = input.selectFromRange("How many interests do you want to list? (1-" + interestList.length + ")", 1, interestList.length);
        int i = 0;
        while (i < count) {
            String choice = input.selectFromOptions("Pick interest #" + (i + 1) + ":", interestList);
            if (interests.contains(choice)) {
                System.out.println("You already listed that one.");
            } else {
                interests.addToEnd(choice);
                i++;
            }
        }
    }

    
    public int postSurvey() {
         return input.selectFromRange("How do you rate this conversation on a scale 1 to 10", 1, 10);
    }
}
