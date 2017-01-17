import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
//Johnathan NADARAJAH - 3A UFA - ESIEA 2016/2017
public class Theater extends JPanel {

	private Seat[][] tabSeat;

	private String filename;
	private String msgReservationError = "/!\\This space is not valid for reservation ! /!\\";
	private String msgCancelationError = "/!\\This space is not valid for cancelation ! /!\\";
	private String msgFileError = "/!\\Error file not found ! /!\\";

	private int nbRow;
	private int nbCol;

	private int rectSize = 40;
	private int currentRow = -1;
	private int currentCol = -1;

	private SeatType placeType;

	/**
	 * Constructeur de la classe Theater qui initialise le tableau 2D tabSeat
	 * @param nbRow
	 * @param nbCol
	 */
	public Theater(int nbRow, int nbCol) {
		
		this.nbRow = nbRow;
		this.nbCol = nbCol;

		tabSeat = new Seat[nbRow][nbCol]; // initialisation du tableau 2D de siege

		//Remplissage du tableau 2D tabSeat
		for (int i = 0; i < nbRow; i++){
			for (int j = 0; j < nbCol; j++){
				tabSeat[i][j] = new Seat(i, j, SeatType.FIRST_CATEGORY, false);
			}
		}
	}

	/**
	 * Constructeur de la classe Theater qui initialise le tableau 2D tabSeat à partir d'un fichier .CSV
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public Theater(String filename) throws FileNotFoundException {

		List<String> values = new ArrayList<String>();

		String symbole = new String();
		int limit = 2;
		File csv = new File(filename);

		if (!csv.exists()){
			throw new FileNotFoundException(msgFileError);
		}

		Scanner scan = new Scanner(new FileInputStream(csv));
		scan.useDelimiter(scan.delimiter() + "|;+");

		while (scan.hasNext()){

			String next = scan.next();

			if(!next.isEmpty()){
				values.add(next);
			}
		}

		tabSeat = new Seat[Integer.parseInt(values.get(0))][Integer.parseInt(values.get(1))];

		for (int i = 0; i<Integer.parseInt(values.get(0)); i++){
			for (int j = 0; j<Integer.parseInt(values.get(1)); j++){

				if (limit< values.size()){
					tabSeat[i][j] = new Seat(i, j, SeatType.getSeatTypeFromSymbole(values.get(limit)), SeatType.isBooked(values.get(limit++)));
				}
			}
		}
	}

	/**
	 * Fonction qui retourne le nombre de ligne du tableau 2D tabSeat
	 * @return nbRow
	 */
	public int getNbRow() {
		return tabSeat.length;
	}

	/**
	 * Fonction qui retourne le nombre de colonne du tableau 2D tabSeat
	 * @return tabSeat[0].length
	 */
	public int getNbCol() {
		return tabSeat[0].length;
	}

	/**
	 * Fonction qui retourne le siege dans le tableau 2D tabSeat
	 * @param row
	 * @param col
	 * @return tabSeat[row][col]
	 */
	public Seat getSeat(int row, int col) {
		return tabSeat[row][col];
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public SeatType getType(int row, int col){
		return tabSeat[row][col].getType();
	}
	/**
	 * Fonction permettant d'afficher la disposition du theatre v1
	 */
	public String toString() {
		
		StringBuffer buff = new StringBuffer();

		for (int i = 0; i<tabSeat.length; i++) {

			buff.append((char) ('A' + i));

			for (int j = 0; j<tabSeat[0].length; j++) {

				placeType = tabSeat[i][j].getType();

				buff.append(" " + (tabSeat[i][j].isBooked() ? placeType.getSymbole().toUpperCase() : placeType.getSymbole()));
			}
			buff.append("\n");
		}

		buff.append("  ");

		for (int k = 0; k<tabSeat[0].length; k++){
			buff.append((int) (0 + k) + " ");
		}

		return buff.toString();
	}
	
	/**
	 * Methode pour réaliser une reservation de siege
	 * cherche à marquer le siège de la case d’indice (row, col) comme réservé (setBooked)
	 * si le siège était déjà réservé elle renvoie une exception de type InvalidActionException.
	 * @param row
	 * @param col
	 * @return return reservationController(row, col, true)
	 * @throws InvalidActionException
	 * @throws IOException
	 */
	public boolean makeReservation(int row, int col) throws InvalidActionException, IOException {
		return reservationController(row, col, true);
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @return reservationController(row, col, false)
	 * @throws InvalidActionException
	 * @throws IOException
	 */
	public boolean cancelReservation(int row, int col) throws InvalidActionException, IOException {
		return reservationController(row, col, false);
	}
	
	/**
	 * Methode pour gérer une annulation ou une création d'une réservation
	 * @param row
	 * @param col
	 * @param isBooking
	 * @return
	 * @throws InvalidActionException
	 * @throws IOException
	 */
	private boolean reservationController(int row, int col, boolean isBooking) throws InvalidActionException, IOException {

		if (row >= tabSeat.length || col >= tabSeat[0].length){
			throw new InvalidActionException(msgReservationError);
		}
		
		if(tabSeat[row][col].getType() == SeatType.OBSTACLE || tabSeat[row][col].getType() == SeatType.SCENE){
			throw new InvalidActionException("This isn't a seat !");
		}

		if ((isBooking && tabSeat[row][col].isBooked()) || (!isBooking && !tabSeat[row][col].isBooked())){
			throw new InvalidActionException(isBooking ? msgCancelationError : "This seat is already free !");
		}

		tabSeat[row][col].setBooked(isBooking);
		save();

		return true;
	}
	
	/**
	 * Fonction qui retourne le nombre de place réserver 
	 * @return reservedSeats
	 */
	public int getNbBookedSeats() {
		int reservedSeats = 0;

		for(int i=0; i<tabSeat.length; i++){
			for(int j=0; j<tabSeat[0].length; j++){
				if(tabSeat[i][j].isBooked() && tabSeat[i][j].getType() != SeatType.OBSTACLE && tabSeat[i][j].getType() != SeatType.SCENE)
					reservedSeats++;
			}
		}
		return reservedSeats;
	}
	
	/**
	 * Fonction qui retourne le nombre de la place restants
	 * @return freeSeats
	 */
	public int getNbFreeSeats() {

		int freeSeats = 0;

		for(int i=0; i<tabSeat.length; i++)
			for(int j=0; j<tabSeat[0].length; j++)
				if(!tabSeat[i][j].isBooked()
						&& tabSeat[i][j].getType() != SeatType.OBSTACLE
						&& tabSeat[i][j].getType() != SeatType.SCENE)
					freeSeats++;

		return freeSeats;
	}
		
	/**
	 * Méthode isBooked qui indique la place comme étant réserver
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isBooked(int row, int col){
		return tabSeat[row][col].isBooked();
	}
	
	/**
	 * Méthode qui écrit dans un fichier d’extension ".bak" : 
	 * theatre1.csv.bak si le fichier de départ était theatre1.csv ou theatre.bak 
	 * si filename est null car on a utilisé le premier constructeur)
	 */
	public void save() throws IOException{

		FileWriter fwriter = new FileWriter(filename != null ? filename+".bak": "theater.bak" );

		try{
			fwriter.write(tabSeat.length + ";" + tabSeat[0].length + ";;;\n");

			for(int i = 0; i<tabSeat.length; i++){
				for(int j = 0; j<tabSeat[0].length; j++){
					fwriter.write(tabSeat[i][j].isBooked() ? tabSeat[i][j].getType().getSymbole().toUpperCase() + ";": tabSeat[i][j].getType().getSymbole() + ";");
				}
			}
		}catch(IOException iex){
			System.err.println(iex.getMessage());
			System.exit(-1);
		}
		fwriter.close();
	}

	@Override
	public int getWidth() {
		return 640;
	}

	@Override
	public int getHeight() {
		return 480;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}

	@Override
	public void paint(Graphics g) {
		ImageIcon seatImg = new ImageIcon("img/free_seat.png");
		ImageIcon reservedImg = new ImageIcon("img/reserved_seat.png");
		super.paint(g);

		g.setColor(new Color(221, 221, 221));
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0; i < tabSeat.length; i++) {
			for (int j = 0; j < tabSeat[0].length; j++) {
				switch (tabSeat[i][j].getType()) {
					case OBSTACLE:
							g.setColor(Color.GRAY);
						break;
					case SCENE:
							g.setColor(Color.YELLOW);
						break;
					default:
							g.setColor(tabSeat[i][j].isBooked() ? Color.RED : Color.GREEN);
						break;
				}

				g.fillRect((j * rectSize), (i * rectSize), rectSize, rectSize);

				if ((tabSeat[i][j].getType() != SeatType.OBSTACLE) && (tabSeat[i][j].getType() != SeatType.SCENE)) {
					if (!tabSeat[i][j].isBooked()){
						seatImg.paintIcon(this, g,(j*rectSize),(i*rectSize));
					}else{
						reservedImg.paintIcon(this, g,(j*rectSize), 4+(i*rectSize));
					}
					
					g.setColor(Color.BLACK);
					/* Loading a new font (14pt, bold) */
					g.setFont(new Font("default", Font.BOLD, 14));
					/* Getting the category String */
					String category = tabSeat[i][j].getType().toString().toUpperCase();
					/* Calculating the String dimension in pixels */
					Rectangle2D stringDim = g.getFontMetrics().getStringBounds(category, g);
					/* Drawing the string centered within the rectangle and centered from the string itself */
					g.drawString(category, j * rectSize + rectSize / 2 - (int) stringDim.getWidth() / 2, i * rectSize + rectSize / 2 + (int) stringDim.getHeight() / 2);
				}

				g.setColor(Color.BLACK);
				g.drawRect((j*rectSize),(i*rectSize), rectSize, rectSize);

			}
		}

		if((currentCol < tabSeat.length) && (currentRow >= 0 )){
			if((currentCol < tabSeat[0].length) && (currentRow >= 0)){
				g.setColor(Color.RED);
				g.drawRect(currentCol*rectSize, currentRow*rectSize, rectSize, rectSize);
			}
		}
	}

	public int getRectSize() {
		return rectSize;
	}

	public void setSelectedRow(int row){
		currentRow = row;
	}

	public void setSelectedCol(int col){
		currentCol = col;
	}

	public int getSelectedRow(){
		return currentRow;
	}

	public int getSelectedCol(){
		return currentCol;
	}
	/*	
	 //Fonction permettant d'afficher la disposition du theatre v1
	  public String toString(){
		StringBuffer buf = new StringBuffer();

		for(int i = 0; i<this.tabSeat.length; i++){

			buf.append((char) ('A' + i)).append(" ");

			for(int j = 0; j<this.tabSeat[i].length;j++){

				if(tabSeat[i][j].isBooked()){
					buf.append("A ");
				}
				else{
					buf.append("a ");
				}
			}

			buf.append("\n");

			if(i == this.tabSeat.length-1){
				buf.append("  ");
				for(int j = 0; j<this.tabSeat[0].length;j++){
					buf.append(j + " ");
				}
			}
		}

		System.out.println(buf.toString());
		System.out.println("\t");

		return null;
	}*/
	/*
	// 1ere version de la methode cancelReservation
	public void cancelReservation(int row, int col) throws InvalidActionException{

		if(col <0 || col >= this.getNbCol() || row < 0 || row >= this.getNbRow()){
			throw new InvalidActionException(msgCancelationError);
		}
		else{
			System.out.println(this.tabSeat[row][col].isBooked());
			if(this.tabSeat[row][col].isBooked()){
				this.tabSeat[row][col].setBooked(false);
				System.out.println("OK");
			}
			else{
				System.out.println("OK1");
				throw new InvalidActionException(msgCancelationError);
			}
		}
	}*/
	/*
	// 1ere version de la methode makeReservation
	public void makeReservation(int row, int col) throws InvalidActionException, IOException{

		if(col <0 || col >= this.getNbCol() || row < 0 || row >= this.getNbRow()){
			throw new InvalidActionException(msgReservationError);
		}
		else{
			if(this.tabSeat[row][col].isBooked()){
				throw new InvalidActionException(msgReservationError);
			}
			else{
				this.tabSeat[row][col].setBooked(true);
			}
		}
		save();
	}*/
}
