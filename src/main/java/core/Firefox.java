package core;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

public class Firefox {

	static String username = "alex";
	static String password = "password";

	public static void main(String[] args) throws InterruptedException {
		Logger.getLogger("").setLevel(Level.OFF);
		String url = "http://alex.academy/exe/login/index.html";

		String driverPath = "";
		if (System.getProperty("os.name").toUpperCase().contains("MAC"))
			driverPath = "./resources/webdrivers/mac/geckodriver.sh";
		else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
			driverPath = "./resources/webdrivers/pc/geckodriver.exe";
		else
			throw new IllegalArgumentException("Unknown OS");
		System.setProperty("webdriver.gecko.driver", driverPath);
		WebDriver driver = new FirefoxDriver();

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
