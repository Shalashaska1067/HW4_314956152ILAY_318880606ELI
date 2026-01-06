public class Client {
	private String name;
	private String id;
	private int sunglasses = 0;
	private int belt = 0;
	private int scarf = 0;

	public Client(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public void update(int itemCode, int qnt) {
		if (itemCode == 1) sunglasses += qnt;
		else if (itemCode == 2) belt += qnt;
		else if (itemCode == 3) scarf += qnt;
	}

	public String getName() { return name; }
	public String getId() { return id; }
}