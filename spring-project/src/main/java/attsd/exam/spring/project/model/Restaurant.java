package attsd.exam.spring.project.model;


import java.math.BigInteger;

import org.springframework.data.annotation.Id;

public class Restaurant {
	
	@Id
	private BigInteger id;
	private String name;
	private int averagePrice;
	
	public Restaurant() {
		
	}
	
	public Restaurant(BigInteger id, String name, int averagePrice) {
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
	
	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", averagePrice=" + averagePrice + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + averagePrice;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaurant other = (Restaurant) obj;
		if (averagePrice != other.averagePrice)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	public void setName(String name2) {
		this.name = name2;
	}

	public void setAveragePrice(int avgPrice) {
		this.averagePrice = avgPrice;		
	}

	public void setId(BigInteger id) {
		this.id = id;
	}


	

}
