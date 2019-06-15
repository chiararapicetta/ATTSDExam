package attsd.exam.spring.project.controllers.webdriver.pages;


import java.math.BigInteger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditPage extends AbstractPage {

	private WebElement name;
	private WebElement averagePrice;
	@FindBy(name = "btn_submit")
	private WebElement submit;

	public EditPage(WebDriver driver) {
		super(driver);
	}

	public static EditPage to(WebDriver driver) {
		get(driver, "new");
		return PageFactory.initElements(driver, EditPage.class);
	}

	public static EditPage to(WebDriver driver, BigInteger restaurantId) {
		get(driver, "edit?id=" + restaurantId);
		return PageFactory.initElements(driver, EditPage.class);
	}

	public <T> T submitForm(Class<T> resultPage, String name, int averagePrice) {
		this.name.clear();
		this.name.sendKeys(name);
		this.averagePrice.clear();
		this.averagePrice.sendKeys("" + averagePrice);
		this.submit.click();
		return PageFactory.initElements(driver, resultPage);
	}
}