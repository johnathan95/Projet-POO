import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
//Johnathan NADARAJAH - 3A UFA - ESIEA 2016/2017
public class Client implements Serializable {
	
	private int currentID;
	private int id; // numero identifiant du client
	
	private String lastname; // nom de famille du client
	private String firstname; // prenom du client
	private String address; // address du client
	
	public LinkedList<Seat> seats;
	
	/**
	 * Constructeur de la classe client
	 * @param lastname
	 * @param firstname
	 * @param address
	 */
	public Client(String lastname, String firstname, String address){
		this.lastname = lastname;
		this.firstname = firstname;
		this.address = address;
		this.seats = new LinkedList<Seat>();
	}
	
	/**
	 * Fonction qui retourne l'identifiant du client
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Fonction qui retourne le nom de famille du client
	 * @return lastname
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * Fonction qui retourne le prénom du client
	 * @return firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Fonction qui retourne l'adresse du client
	 * @return address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Fonction qui retourne une chaine de caractère permettant l'affichage du prenom et du nom du client
	 */
	public String toString() {
		return this.firstname + " " + this.lastname;
	}
	
	/**
	 * Fonction qui retourne une chaine de caractère permettant l'affichage du prenom, du nom, 
	 * de l'adresse et l'identifiant du client
	 * @return currentID, firstname, lastname, address
	 */
	public String getFullString() {
		return "Client n°" + this.currentID + " : " + this.firstname + " " + this.lastname + " (" + this.address + ")";
	}
	
	/**
	 * Fonction qui retourne la liste seats
	 * @return seats
	 */
	public List<Seat> getSeats(){
		return seats;
	}
	/**
	 * Méthode permettant d'ajouter le siege dans la liste seats
	 * @param s
	 */
	public void addSeat(Seat s) {
		seats.add(s);
	}
	
	/**
	 * Méthode permettant d'enlever un siege de la liste seats
	 * @param s
	 * @return seats.remove(s)
	 */
	public boolean removeSeat(Seat s) {
		
		for(Seat se : seats) {
			if(se.getCol() == s.getCol() && se.getRow() == s.getRow()){
				s = se;
			}
		}
		return seats.remove(s);
	}
	
	/**
	 * Fonction qui retourne le prix la place
	 * @return price
	 */
	public double getReservationCost() {
		
		double price = 0;
		
		for(Seat s : seats){
			price += s.getType().getPrice();
		}
		return price;
	}
	
	/**
	 * Fonction qui
	 * @return buff.toString()
	 */
	public String getExplictedCost() {
		
		if(seats.size() == 0){
			return "No reservation made for this client.\n";
		}
		
		StringBuffer buff = new StringBuffer();
		
		for(Seat s : seats){
			buff.append(s.toString() + " (" + s.getType().getPrice() + "e)\n");
		}
		
		buff.append("Total : " + this.getReservationCost() + "e\n");
		
		return buff.toString();
	}
	
	/**
	 * Méthode qui fixe la valeur id à la variable currentID après la déserialisation
	 * @param id
	 */
	public void setCurrentId(int id){
		this.currentID = id;
	}
}
