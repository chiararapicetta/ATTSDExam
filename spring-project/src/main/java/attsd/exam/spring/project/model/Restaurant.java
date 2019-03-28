package attsd.exam.spring.project.model;

public class Restaurant {
	
	private long id;
	private String name;
	private int averagePrice;
	
	public Restaurant(long id, String name, int averagePrice) {
		this.id = id;
		this.name = name;
		this.averagePrice = averagePrice;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAveragePrice() {
		return averagePrice;
	}
	
	@Override
	public String toString() {
		return "Restaurant [id = " + id + ", name = " + name + ", average price = " + averagePrice + "]";
	}
	

}
