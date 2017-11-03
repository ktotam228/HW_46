package core;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Facebook {
	static Cipher cipher;
	
	private static String myMac() throws IOException{
		String mac_address;
		String cmd_mac = "ifconfig en0";
		String cmd_win = "cmd /C for /f \"usebackq tokens=1\" %a in (`getmac ^| findstr Device`) do echo %a";

		if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_win).getInputStream()).useDelimiter("\\A").next()
					.split(" ")[1];
		} else {
			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_mac).getInputStream()).useDelimiter("\\A").next()
					.split(" ")[4];
		}
		mac_address = mac_address.toLowerCase().replaceAll("-", ":");
		return mac_address;
	}

	public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		String encryptedText = Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
		return encryptedText;
	}

	public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
		return decryptedText;
	}
	
	
	

	public static void main(String[] args) throws Exception {
		
		WebDriver driver;
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);
		String browser = null;
		String driverPath = "";
		browser = "Safari";  // Chrome  Firefox HtmlUnit
		
//		if ((browser == "Firefox") && (System.getProperty("os.name").toUpperCase().contains("MAC"))) driverPath = "./resources/webdrivers/mac/geckodriver.sh";
//		else if ((browser == "Firefox") && (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))) driverPath = "./resources/webdrivers/pc/geckodriver.exe";
//		
//		else if ((browser == "Chrome") && (System.getProperty("os.name").toUpperCase().contains("MAC"))) driverPath = "./resources/webdrivers/mac/chromedriver";
//		else if ((browser == "Chrome") && (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))) driverPath = "./resources/webdrivers/pc/geckodriver.exe";
//		
//		else throw new IllegalArgumentException("Unknown OS");
			
		switch (browser) {
		
		case "HtmlUnit":
			driver = new HtmlUnitDriver();
			((HtmlUnitDriver) driver).setJavascriptEnabled(true);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			break;
		
		case "Safari":
			driver = new HtmlUnitDriver();
			((HtmlUnitDriver) driver).setJavascriptEnabled(true);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			break;

		case "Firefox":
			System.setProperty("webdriver.gecko.driver", driverPath);
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
//			driver.manage().window().maximize();
			break;

		case "Chrome":
			System.setProperty("webdriver.chrome.driver", driverPath);
			System.setProperty("webdriver.chrome.silentOutput", "true");
			ChromeOptions option = new ChromeOptions();
			option.addArguments("disable-infobars"); 
			option.addArguments("--disable-notifications");
			if (System.getProperty("os.name").toUpperCase().contains("MAC"))
				option.addArguments("-start-fullscreen");
			else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
				option.addArguments("--start-maximized");
			else throw new IllegalArgumentException("Unknown OS");
			driver = new ChromeDriver(option);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			break;
			
		default: throw new IllegalArgumentException("Unknown Broweser");
		}
	
		String url = "http://facebook.com/";
		String email_address = "gnvl61@mail.com";
	    String password = "6R8DCYBCzo/e37pg3p/tCg==";
		
		
		//cipher = Cipher.getInstance("AES");
		//String password = "6R8DCYBCzo/e37pg3p/tCg==";
		String key = myMac();
		key = key.replaceAll("-", ":"); // c8:2a:14:4d:07:7b =>
										// c8:2a:14:4d:07:7b
		SecretKeySpec sk = new SecretKeySpec(Arrays.copyOf(key.getBytes("UTF-8"), 16), "AES");
		String decryptedpassword = decrypt(password, sk);
		driver.get(url);

		Thread.sleep(1000); // Pause in milleseconds (1000 – 1 sec)
		String title = driver.getTitle();
		String copyright = driver.findElement(By.xpath("//*[@id=\'pageFooter\']/div[3]/div/span")).getText();	
		driver.findElement(By.id("email")).sendKeys(email_address);
		driver.findElement(By.id("pass")).sendKeys(decryptedpassword);
        driver.findElement(By.id("u_0_2")).click();
        
        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id='u_0_b']/div[1]/div[1]/div/a/span/span")).click();

        Thread.sleep(1000);
        String friends = driver.findElement(By.xpath("//div[2]/ul/li[3]/a/span[1]")).getText();
        
        Thread.sleep(1000);
        driver.findElement(By.id("userNavigationLabel")).click();
        driver.findElement(By.xpath("//ul/li[14]/a/span/span")).click();
      
        Thread.sleep(1000);
		driver.quit();
        
		System.out.println("Browser is: " + browser);
        System.out.println("Title of the page: " + title);
        System.out.println("Copyright: " + copyright);
        System.out.println("You have " + friends + " friends");
	}
}