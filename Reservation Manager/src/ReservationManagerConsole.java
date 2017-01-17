import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
//Johnathan NADARAJAH - 3A UFA - ESIEA 2016/2017
public class ReservationManagerConsole {

	private Scanner scan = new Scanner(System.in);
	private Theater theater;
	private LinkedList<Client> clients;

	/**
	 * Constructeur vide de la classe ReservationManagerConsole
	 */
	/*public ReservationManagerConsole(){

	}*/

	/**
	 * Constructeur de la classe ReservationManagerConsole
	 * @throws NumberFormatException
	 * @throws InvalidActionException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ReservationManagerConsole() throws NumberFormatException, InvalidActionException, IOException, ClassNotFoundException {

		System.out.println("Welcome to the Reservation Manager");

		File dir = new File("./");
		File[] theaterFile = dir.listFiles(new FilenameFilter() {
			public boolean accept (File dir, String filename) {
				return filename.endsWith(".bak");
			}
		});

		theater = new Theater(theaterFile.length > 0 ? theaterFile[0].getAbsolutePath() : ConfigDetails.THEATER_FILE);

		File clientFile = new File(ConfigDetails.CLIENTS_FILE);
		clients = clientFile.exists() ? Serializer.<LinkedList<Client>>loadFromFile(ConfigDetails.CLIENTS_FILE) : new LinkedList<Client>();

		int i = 0;
		
		for(Client c : clients){
			c.setCurrentId(i++);
		}
		
		while(true){     

			System.out.println("What do you want to do? (h for help)");	    
			String inputString = scan.next();

			switch(inputString){

				case "h": System.out.println("h: Print this help");
						  System.out.println("st: Show Theater");
						  System.out.println("mr: Make a Reservation");
						  System.out.println("cr: Cancel a Reservation");
						  System.out.println("sr: Show Reservation");
						  System.out.println("lc: List of clients");
						  System.out.println("ac: Add new client");
						  System.out.println("rc: Remove a client");
						  System.out.println("q: Quit");
				break;
				case "st":
						System.out.println(theater + "\n");
					break;
				case "mr": 
						makeReservation();
					break;
				case "cr":
						cancelReservation();
					break;
				case "sr":
						showReservation();
					break;
				case "lc":
						listClients();
					break;
				case "ac":
						addClient();
					break;
				case "rc":
						removeClient();
					break;
				case "q": System.out.println("Bye Bye"); 
						  System.exit(0);
				break;
				default : System.out.println("unknow command");
			}
		}
	}

	public static void main(String[] args)  {
		try {
			ReservationManagerConsole rmc = new ReservationManagerConsole();
		} catch (NumberFormatException | ClassNotFoundException | InvalidActionException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode permettant d'afficher les reservations
	 * @throws InvalidActionException
	 */
	private void showReservation() throws InvalidActionException {

		System.out.println("Client list : ");

		if(!listDetailledClients()){
			System.exit(0);
		}

		System.out.println("Please enter the id of the wanted client or -1 to cancel :");

		Client selectedClient = selectClient();

		if(selectedClient != null)
		{
			System.out.println(selectedClient.toString() + " has reserved seats number :");
			System.out.println(selectedClient.getExplictedCost());	
		}
	}

	/**
	 * Méthode qui appel la méthode de la classe Theater, permettant faire une réservation
	 * @throws NumberFormatException
	 * @throws InvalidActionException
	 * @throws IOException
	 */
	private void makeReservation() throws NumberFormatException, InvalidActionException, IOException {
		if(theater.getNbFreeSeats() > 0)
			reservationController(true);
		else
			System.out.println("HOUSE FULL");
	}

	/**
	 * Méthode qui appel la méthode de la classe Theater, permettant d'annuler une réservation
	 * @throws NumberFormatException
	 * @throws InvalidActionException
	 * @throws IOException
	 */
	private void cancelReservation() throws NumberFormatException, InvalidActionException, IOException {
		if(theater.getNbBookedSeats() > 0){
			reservationController(false);
		}else{
			System.out.println("No reservation have been made yet.");
		}
	}
	
	/**
	 * Méthode qui appel la méthode de la classe Theater, permettant faire une réservation
	 * @param isBooking
	 * @throws InvalidActionException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private void reservationController(boolean isBooking) throws NumberFormatException, InvalidActionException, IOException {

		System.out.println("Client list : ");

		if(!listDetailledClients()){
			System.exit(0);
		}
		System.out.println("Please enter the id of the wanted client or -1 to cancel :");

		Client selectedClient = selectClient();

		if(selectedClient != null)
		{
			String rowLetter = new String();
			String numberLine = new String();

			System.out.println(theater + "\n");

			System.out.println("Please enter row letter: ");
			rowLetter = (scan.next()).toUpperCase();

			System.out.println("Please enter column number : ");
			numberLine = scan.next();

			if(isBooking)
			{
				int numberLineInt = Integer.parseInt(numberLine);
				int rowLetterInt = rowLetter.charAt(0)-65;

				if(theater.makeReservation(rowLetterInt, numberLineInt)){
					selectedClient.addSeat(theater.getSeat(rowLetterInt, numberLineInt));
				}
			}
			else
			{
				if(selectedClient.removeSeat(theater.getSeat(rowLetter.charAt(0) - 65, Integer.parseInt(numberLine)))){
					theater.cancelReservation(rowLetter.charAt(0)-65, Integer.parseInt(numberLine));
				}
			}		

			Serializer.saveToFile(ConfigDetails.CLIENTS_FILE, clients);
			System.out.println(theater + "\n");
		}
	}

	/**
	 * Méthode permettant d'afficher la liste des clients
	 */
	private void listClients() {

		System.out.print("Client list : [");

		for(Client c : clients)
			if(c != clients.getLast()){
				System.out.print(c.toString() + ", ");
			}
			else{
				System.out.print(c.toString());
			}
		System.out.println("]");
	}

	/**
	 * Fonction qui retourne un booleen pour permettre l'affichage de la liste détaillés des clients 
	 * @return true, false
	 */
	private boolean listDetailledClients() {
		if(clients.size() > 0)
		{
			for(Client c : clients){
				System.out.println(c.getFullString());
			}
			return true;
		}
		else{
			System.out.println("No clients");
		}
		return false;
	}

	/**
	 * Méthode permettant l'ajout d'un client
	 * @throws IOException
	 */
	public void addClient() throws IOException {

		String firstname = new String();
		String lastname = new String();
		String address = new String();

		System.out.print("Lastname : ");
		lastname = scan.next();

		System.out.print("Firstname : ");
		firstname = scan.next();

		System.out.print("Address : ");
		address = scan.next();

		int nextId = clients.size() > 0 ? clients.getLast().getId() + 1 : 0;

		System.out.println("Choose client type :");
		System.out.println("1 - Client");
		System.out.println("2 - VIP");
		System.out.println("3 - Group");

		switch(scan.nextInt())
		{
			case 1 :
				clients.add(new Client(lastname,firstname,address));
				break;
			case 2 :
				clients.add(new ClientVIP(lastname,firstname,address));
				break;
			case 3 :
				clients.add(new ClientGroup(lastname,firstname,address));
				break;
		}

		clients.getLast().setCurrentId(nextId);

		Serializer.saveToFile(ConfigDetails.CLIENTS_FILE, clients);
	}

	/**
	 * Méthode permettant de sélectionner un client
	 * @return id, selectedClient
	 * @throws InvalidActionException
	 */
	public Client selectClient() throws InvalidActionException {

		Client selectedClient = null;

		try {
				int id = scan.nextInt();
	
				for(Client c : clients){
					if(c.getId() == id) {
						selectedClient = c;
						break;
					}
				}
				if(selectedClient == null && id != -1){
					throw new InvalidActionException("Invalid selection");
				}
	
				return id != -1 ? selectedClient : null;
		}
		catch(RuntimeException e) {

			scan.nextLine();
			throw new InvalidActionException("This is not a valid number !");

		}
	}

	/**
	 * Méthode permettant de retirer un client
	 * @throws InvalidActionException
	 * @throws IOException
	 */
	public void removeClient() throws InvalidActionException, IOException {

		if(!listDetailledClients()){
			System.exit(0);
		}
		
		System.out.println("Please enter the id of the client to be removed or -1 to cancel :");
		Client selectedClient = selectClient();

		if(selectedClient != null)
		{
			clients.remove((Client) selectedClient);

			for (Seat s : selectedClient.getSeats()) {
				theater.cancelReservation(s.getCol(), s.getRow());
			}

			System.out.println(selectedClient.toString() + " was removed with success.");
			Serializer.saveToFile(ConfigDetails.CLIENTS_FILE, clients);
		}
	}
}


