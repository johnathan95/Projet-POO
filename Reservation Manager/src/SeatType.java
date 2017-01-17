//Johnathan NADARAJAH - 3A UFA - ESIEA 2016/2017
public enum SeatType
{
	SCENE("S",-1.0), OBSTACLE("X",-1.0),FIRST_CATEGORY("a", 125.0),SECOND_CATEGORY("b", 80.0),THIRD_CATEGORY("c",50.0),FOURTH_CATEGORY("d",20.0), FIFTH_CATEGORY("e",10.0);

	private String symbole;
	private double price;
	
	/**
	 * Constructeur de l'énumération SeatType
	 * @param symbole
	 * @param price
	 */
	SeatType (String symbole, double price)
	{
		this.symbole = symbole;
		this.price = price;
	}
	
	/**
	 * Fonction toString qui retourne le symbole associe au siege
	 */
	public String toString()
	{
		return symbole;
	}
	
	/**
	 * Fonction qui permet de récuperer le symbole
	 * @return symbole
	 */
	public String getSymbole()
	{
		return symbole;
	}
	
	/**
	 * Fonction qui retourne le prix du type de siege
	 * @return price
	 */
	public double getPrice()
	{
		return price;
	}
	
	/**
	 * Fonction qui renvoie l’instance associée au symbole passée en paramètre
	 * (en ignorant la casse c’est-à-dire si le symbole est en majuscule ou minuscule)
	 * ou null si le symbole ne correspond à aucune instance.
	 * @param symbole
	 * @return
	 */
	public static SeatType getSeatTypeFromSymbole(String symbole)
	{
		switch(symbole.toUpperCase())
		{
			case "A":
				return SeatType.FIRST_CATEGORY;
			case "B":
				return SeatType.SECOND_CATEGORY;
			case "C":
				return SeatType.THIRD_CATEGORY;
			case "D":
				return SeatType.FOURTH_CATEGORY;
			case "E":
				return SeatType.FIFTH_CATEGORY;
			case "S":
				return SeatType.SCENE;
			case "X":
				return SeatType.OBSTACLE;
			default: 
				return null;
		}
	}
	
	/**
	 * Fonction qui permet reservation pour le type de siege choisi
	 * La fonction retourne un booleen : 
	 * 1 si l'opération c'est correctement, sinon 0
	 * @param symbole
	 * @return Character.isUpperCase(symbole.charAt(0))
	 */
	public static boolean isBooked(String symbole) {
		return Character.isUpperCase(symbole.charAt(0));
	}
}
