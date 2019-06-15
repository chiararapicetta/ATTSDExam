package attsd.exam.spring.project.controllers.webdriver.pages;

import java.math.BigInteger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends AbstractPage {

	@FindBy(id = "restaurant_table")
	private WebElement restaurantTable;

	public HomePage(WebDriver driver) {
		super(driver);
	}

	public static HomePage to(WebDriver driver) {
		get(driver, "");
		return PageFactory.initElements(driver, HomePage.class);
	}

	public static HomePage toDelete(WebDriver driver, BigInteger restaurantId) {
		get(driver, "delete?id=" + restaurantId);
		return PageFactory.initElements(driver, HomePage.class);
	}

	public static HomePage toLogout(WebDriver driver) {
		get(driver, "logout");
		return PageFactory.initElements(driver, HomePage.class);
	}
	
	public static HomePage toReset(WebDriver driver) {
		get(driver, "reset");
		return PageFactory.initElements(driver, HomePage.class);
	}

	public String getRestaurantTableAsString() {
		return restaurantTable.getText();
	}
}