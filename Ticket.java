public class Ticket {
	private String serialNumber; // A 10-digit string.
	private int section; 		 // max 3 digit section
	private int row; 			 // max 2 digit row number
	private int seat; 			 // make 2 digit seat number
	private boolean inUse; 		 // indicating whether the ticket is already used.

	public Ticket(String serialNumber, 
				  int section, 
				  int row, 
				  int seat) {
		this.serialNumber = serialNumber;
		this.section = section;
		this.row = row;
		this.seat = seat;
		this.inUse = false;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public int getSection() {
		return this.section;
	}

	public int getRow() {
		return this.row;
	}

	public int getSeat() {
		return this.seat;
	}

	public boolean isInUse() {
		return this.inUse;
	}

	public void scannedSeat() {
		this.inUse = true;
	}

	public void freeSeat() {
		this.inUse = false;
	}
}