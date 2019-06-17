package attsd.exam.spring.project.model;

import java.math.BigInteger;

public class RestaurantDTO {
	
	private BigInteger id;
	private String name;
	private int averagePrice;

	public RestaurantDTO(BigInteger id, String name, int averagePrice) {
		this.id = id;
		this.name = name;
		this.averagePrice = averagePrice;
	}

	public BigInteger getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAveragePrice() {
		return averagePrice;
	}
	
}
