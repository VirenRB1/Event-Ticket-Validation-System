
/**
 * TicketCollection
 */
import java.io.*;
import java.util.*;

public class TicketCollection {
    final static int MAX_BUCKET = 1009;
    // The Hashtable of buckets with each bucket having its own head node
    final static Bucket[] ticketTable = new Bucket[MAX_BUCKET];

    // This method processes a file with all the tickets that can be purchased and
    // adds them in a hashtable as a sorted linkedlist
    public static void processFile(String filename) {
        try {
            File myFile = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(myFile));
            String line = reader.readLine();
            while (line != null) {
                String[] numbers = line.split(",");

                String serialNum = numbers[0];
                int sectionNum = Integer.parseInt(numbers[1]);
                int rowNum = Integer.parseInt(numbers[2]);
                int seatNum = Integer.parseInt(numbers[3]);

                Ticket newTicket = new Ticket(serialNum, sectionNum, rowNum, seatNum); // The ticket to be added

                // The first five number of the serial number that will be hashed
                String hashSerial = serialNum.substring(0, 5);
                // The unique last 5 number of each ticket
                int uniqIdentifier = Integer.parseInt(serialNum.substring(5, serialNum.length()));

                // Stores the position in the hashtable for each ticket
                int hashBucket = hashFunction(hashSerial);

                // if the bucket is null initial it and add the new ticket as the head
                if (ticketTable[hashBucket] == null) {
                    ticketTable[hashBucket] = new Bucket();
                    ticketTable[hashBucket].head = new Node(newTicket, null);
                } // else insert it in the existing sorted linked list using its unique identifier
                  // number
                else {
                    boolean inserted = false;
                    Node curr = ticketTable[hashBucket].head;
                    Node prev = curr;

                    while (curr != null && !inserted) {
                        String currSerial = curr.ticket.getSerialNumber();
                        int currIdentifier = Integer.parseInt(currSerial.substring(5, currSerial.length()));
                        // if the unique number for the ticket to be added is less than the current
                        // node's ticket unique number then inserted the ticket to be added before the
                        // current node
                        if (uniqIdentifier <= currIdentifier) {
                            if (curr == ticketTable[hashBucket].head) {
                                Node newNode = new Node(newTicket, ticketTable[hashBucket].head);
                                ticketTable[hashBucket].head = newNode;
                            } else {
                                prev.next = new Node(newTicket, curr);
                            }
                            inserted = true;
                        }
                        prev = curr;
                        curr = curr.next;
                    }
                    // if the unique number for the ticket to be added is the largest then add it to
                    // the end
                    if (!inserted) {
                        prev.next = new Node(newTicket, null);
                    }
                }
                // Move to the next line in the file and repeat the process for all tickets
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("No File To Process");
        }

    }

    // This method checks all the spectators tickets by searching them in the
    // hashtable. it grants access to those with genuine and unused tickets and
    // denies access to those with used or invalid tickets
    public static void processCheckIns(String filename) {
        try {
            File checkIns = new File(filename);
            Scanner scanner = new Scanner(checkIns);
            int validScans = 0, invalidScans = 0, deniedScans = 0;
            while (scanner.hasNext()) {
                boolean found = false; // ticket found yet or not
                boolean inTable = true; // is the ticket in the hashtable? we will assume it exists in the hashtable

                String serialNum = scanner.next();
                // String that will be hashed and will give us the correct bucket in the
                // hashtable to check
                String hashString = serialNum.substring(0, 5);
                int subSerial = Integer.parseInt(hashString);
                // unique last 5 digit number for all the tickets
                int identifier = Integer.parseInt(serialNum.substring(5, 10));
                // Bucket number to check in the array
                int hashBucket = hashFunction(hashString);

                if (ticketTable[hashBucket] != null) {
                    Node curr = ticketTable[hashBucket].head;

                    while (curr != null && !found && inTable) {
                        // the first 5 number to be mapped into the bucket index in the array
                        int currSerial = Integer.parseInt(curr.ticket.getSerialNumber().substring(0, 5));
                        // unique last five number for each ticket
                        int currIdentifier = Integer.parseInt(curr.ticket.getSerialNumber().substring(5, 10));
                        // if the serial number matches then the ticket is found
                        if (currIdentifier == identifier && subSerial == currSerial) {
                            found = true;
                            // if the ticket has not been used before grant access else deny the access
                            if (!(curr.ticket.isInUse())) {
                                curr.ticket.scannedSeat();
                                System.out.println("Scan success. Access granted.");
                                validScans += 1;
                            } else {
                                System.out.println("Ticket already used,access denied.");
                                deniedScans += 1;
                            }
                        } // if the current ticket's identity number is less than the ticket to be
                          // checked's identity number, move to the next ticket in the linked list and if
                          // the ticket is not found deny access
                        else if (currIdentifier <= identifier) {
                            curr = curr.next;
                            if (curr == null && !found) {
                                invalidScans++;
                                System.out.println("Ticket was invalid");
                            }
                        } // if the current ticket's identity number is more than the ticket to be
                          // checked's identity number then it means the ticket is not in the linkedlist
                          // as the linkedlist is sorted
                        else {
                            inTable = false;
                            invalidScans += 1;
                            System.out.println("Ticket was invalid");
                        }

                    }
                } // if the bucket was null initially then the ticket is invalid
                else {
                    System.out.println("Ticket was invalid");
                    invalidScans += 1;
                }
            }
            System.out.printf("\nInvalid Scans: %d \nValid Scans: %d \nDenied Scans:%d", invalidScans, validScans,
                    deniedScans);
            scanner.close();
        } catch (IOException ioe) {
            System.out.println("No such file exists");
        }

    }

    // This method takes in a string, converts it into an int and maps it into an
    // index within a specific bucket within the hashtable
    private static int hashFunction(String hash) {
        int num = Integer.parseInt(hash);
        return num % MAX_BUCKET;

    }

    // The bucket class which stores the linked list
    private static class Bucket {
        public Node head;

        public Bucket() {
            head = null;
        }

    }

    // The node class which stores a ticket and the next node
    private static class Node {
        public Ticket ticket;
        public Node next;

        public Node(Ticket ticket, Node next) {
            this.ticket = ticket;
            this.next = next;
        }
    }

}