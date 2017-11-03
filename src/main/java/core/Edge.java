package core;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

public class Edge {
	
	static String username = "alex";
	static String password = "password";

	public static void main(String[] args) throws InterruptedException {
		Logger.getLogger("").setLevel(Level.OFF);
		String url = "http://alex.academy/exe/login/index.html";
		if (!System.getProperty("os.name").contains("WINDOWS")) throw new IllegalArgumentException("Edge is not available only on Mac");
		System.setProperty("webdriver.edge.driver", "./resources/webdrivers/pc/MicrosoftWebDriver.exe");
		WebDriver driver = new EdgeDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		driver.get(url);

		String result = null;
		driver.findElement(By.id("id_useranme")).sendKeys(username);
		driver.findElement(By.id("id_password")).sendKeys(password);
		driver.findElement(By.id("id_login_button")).submit();
		Thread.sleep(200);
		if (!driver.getTitle().equals("welcome")) {
			result = "Login failed: " + driver.findElement(By.id("ErrorLineEx")).getText();
		} else 	result = "Login success";

			System.out.println(result);

			driver.quit();
		}
}