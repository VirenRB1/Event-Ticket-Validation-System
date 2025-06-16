
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        TicketCollection.processFile("tickets_1000.txt");
        TicketCollection.processCheckIns("checkins_1000.txt");
    }

}
