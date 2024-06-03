import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

class ValidationBox {
    LazyParser parser;
    ArrayList<Validator> validators;
    Set<StatusType> statuses; 

    ValidationBox(LazyParser newParser, ArrayList<Validator> newValidators) {
        parser = newParser;
        validators = newValidators; 
        statuses = new HashSet<StatusType>();
    }

    // can examine value of ValidationBox().statuses afterward
    void validate() {
        Set<StatusType> tempStatuses = new HashSet<StatusType>();
        for (int i = 0; i < validators.size(); i++) {
            tempStatuses = validators.get(i).validate(parser);
            System.out.println(tempStatuses);
            statuses.addAll(tempStatuses);
        }
    }
}