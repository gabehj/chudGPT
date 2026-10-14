import java.util.Scanner;
public class DataVal {
    private Scanner scanner;
    public DataVal() {
        this.scanner = new Scanner(System.in);
    }
 
    public String selectFromOptions(String message, String[] options) {
        while(true){
            System.out.println(message);
            for(int i = 0; i < options.length; i++){
                System.out.println(options[i]);
            }
            String input = scanner.nextLine();
            input = input.trim();
            for(int i = 0; i < options.length; i++){
                if(options[i].equalsIgnoreCase(input)){
                    return options[i];
                }
            }
            System.out.println("Please type an option from the list of options.");
        }
    }
 
    public int selectFromRange(String message, int min, int max) {
        int inputInt;
        while(true){
            System.out.println(message);
            String input = scanner.nextLine();
            input = input.trim();
            try{
                inputInt = Integer.parseInt(input);
                if(inputInt >= min && inputInt <= max){
                    return inputInt;
                }
                System.out.println("Please type a number in the range");
            } catch(NumberFormatException e){
                System.out.println("Please input a number.");
            }
        }
    }
}
