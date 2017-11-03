package core;

import org.openqa.selenium.*;
import org.openqa.selenium.safari.SafariDriver;
import java.math.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.regex.*;

public class Safari {

	static String username = "alex";
	static String password = "password";
	
	public static void main(String[] args) throws InterruptedException {
		Logger.getLogger("").setLevel(Level.OFF);
		String url = "http://alex.academy/exe/login/index.html";

		if (!System.getProperty("os.name").contains("Mac")) 
			throw new IllegalArgumentException("Safari is available only on Mac");

		WebDriver driver = new SafariDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		driver.get(url);

		String result = null;
		driver.findElement(By.id("id_useranme")).sendKeys(username);
		driver.findElement(By.id("id_password")).sendKeys(password);
		
		WebElement submit = driver.findElement(By.id("id_login_button"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
		
		Thread.sleep(200);
		if (!driver.getTitle().equals("welcome")) {
			result = "Login failed: " + driver.findElement(By.id("ErrorLineEx")).getText();
		} else result = "Login success";

		System.out.println(result);

		driver.quit();
	}
}