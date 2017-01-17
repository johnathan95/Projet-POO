//Johnathan NADARAJAH - 3A UFA - ESIEA 2016/2017
public class ClientGroup extends Client {
	/**
	 * Constructeur de la classe ClientGroup permettant de créer un groupe de client
	 * @param lastname
	 * @param firstname
	 * @param address
	 */
	public ClientGroup(String lastname, String firstname, String address) {
		super(lastname, firstname, address);
	}
	
	@Override
	public String toString() {
		return super.toString() + " \"Group\"";
	}

	@Override
	public double getReservationCost() {
		double price = 0;
		
		for(Seat s : seats){
			price += s.getType().getPrice();
		}
		return price-(price*getPromotionByQuantity(seats.size()));
	}
	
	@Override
	public String getExplictedCost() {
		if(seats.size() == 0){
			return "No reservation made for this client.\n";
		}
		
		StringBuffer buff = new StringBuffer();
		
		for(Seat s : seats){
			buff.append(s.toString()+" (" + s.getType().getPrice()+ "e =>\n");
		}
		
		if(getPromotionByQuantity(seats.size()) != 0){
			buff.append(" -"+ getPromotionByQuantity(seats.size())*100+ "%\n");
		}
		
		buff.append("Total : " + this.getReservationCost() + "e\n");
		
		return buff.toString();
	}
	
	private double getPromotionByQuantity(int nbSeats) {
		return nbSeats >= 5 && nbSeats <= 10 ? 0.1 : nbSeats > 10 ? 0.2 : 0;
	}
}
