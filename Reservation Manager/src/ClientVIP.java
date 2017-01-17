//Johnathan NADARAJAH - 3A UFA - ESIEA 2016/2017
public class ClientVIP extends Client {

	/**
	 * Constructeur de la classe ClientVIP permettant de créer un client de type VIP
	 * @param lastname
	 * @param firstname
	 * @param address
	 */
	public ClientVIP(String lastname, String firstname, String address) {
		super(lastname, firstname, address);
	}

	@Override
	public String toString() {
		return super.toString() + " \"VIP\"";
	}
	
	@Override
	public double getReservationCost() {
		double price = 0;
		
		for(Seat s : seats){
			price += getReducedPrice(s.getType());
		}
		return price;
	}
	
	@Override
	public String getExplictedCost() {
		if(seats.size() == 0){
			return "No reservation made for this client.\n";
		}
		StringBuffer buff = new StringBuffer();
		
		for(Seat s : seats)
		{
			buff.append(s.toString()+" ("+s.getType().getPrice()+"e -"+getPromotionBySeatType(s.getType())*100+"% => "+getReducedPrice(s.getType())+ "e )\n");
		}
			
		buff.append("Total : " + this.getReservationCost() + "e\n");
		
		return buff.toString();
	}
	
	private double getPromotionBySeatType(SeatType type) {
		return type == SeatType.FIRST_CATEGORY ? 0.3 : type == SeatType.SECOND_CATEGORY ? 0.2 : type == SeatType.THIRD_CATEGORY ? 0.1 : 0;
	}
	
	private double getReducedPrice(SeatType type) {
		return type.getPrice()-(type.getPrice()*getPromotionBySeatType(type));
	}
}
