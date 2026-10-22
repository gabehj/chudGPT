package Practice;
// Example

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.HashMap;

public class test {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        try{
            HashMap<String, Object> data = MAPPER.readValue(new File("test.json"), HashMap.class);
            System.out.print(data.keySet());
        }
        catch (RuntimeException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
