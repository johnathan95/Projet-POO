import java.io.Serializable;
//Johnathan NADARAJAH - 3A UFA - ESIEA 2016/2017
public class Seat implements Serializable {
	
	private int row,col;
	private SeatType type;
	private boolean isBooked;

	public Seat(int row, int col, SeatType type, boolean isBooked) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.isBooked = isBooked;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}
	
	public boolean isBooked() {
		return isBooked;
	}
	
	public void setType(SeatType type) {
		this.type = type;
	}
	
	public SeatType getType() {
		return type;
	}
	
	/**
	 * Fonction toString qui permet de convertir les nombres 0,1,2,... en caracteres A, B, C, ...
	 * Pour cela, on utilise l'artithmetique des caracteres ('A'+1 => 'B')
	 */
	public String toString() {
		char rowLetter = (char) ('A' + row);
		return "" + rowLetter + col;
	}
}
