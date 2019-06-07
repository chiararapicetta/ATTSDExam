package attsd.exam.spring.project.controllers.webdriver.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage extends AbstractPage{

	private WebElement email;
	private WebElement password;
	private WebElement username;
	@FindBy(id = "submit")
	private WebElement submit;
	

	
	
	public SignUpPage(WebDriver driver) {
		super(driver);
	}
	

	public static SignUpPage to(WebDriver driver) {
		get(driver, "/signup");
		return PageFactory.initElements(driver, SignUpPage.class);
	}

	public <T> T submitForm(Class<T> resultPage, String email, String password, String username) {
		this.email.clear();
		this.email.sendKeys(email);
		this.password.clear();
		this.password.sendKeys(password);
		this.username.clear();
		this.username.sendKeys(username);
		this.submit.click();
		return PageFactory.initElements(driver, resultPage);
	}
	
}