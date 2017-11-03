package core;

import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.*;
import java.util.concurrent.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class HtmlUnit {

	public static String decrypt(String encryptedText, SecretKey secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher;
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
		return decryptedText;
	}
	
	public static void main(String[] args) throws InterruptedException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		

		String mac_address;
		String cmd_mac = "ifconfig en0";
		String cmd_win = "cmd /C for /f \"usebackq tokens=1\" %a in (`getmac ^| findstr Device`) do echo %a";
		if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_win).getInputStream()).useDelimiter("\\A").next().split(" ")[1].toLowerCase();
		} else {
			mac_address = new Scanner(Runtime.getRuntime().exec(cmd_mac).getInputStream()).useDelimiter("\\A").next().split(" ")[4];
		}
		
		String username = "alex";
		// String password = "password";
		
		String password = decrypt("UefMcE7rOkNfxEi4myffKw==", new SecretKeySpec(Arrays.copyOf(mac_address.getBytes("UTF-8"), 16), "AES"));

		Thread.sleep(200);
		Logger.getLogger("").setLevel(Level.OFF);
		String url = "http://alex.academy/exe/login/index.html";

		WebDriver driver = new HtmlUnitDriver();
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get(url);

		String result = null;
		driver.findElement(By.id("id_useranme")).sendKeys(username);
		driver.findElement(By.id("id_password")).sendKeys(password);
		driver.findElement(By.id("id_login_button")).submit();
		Thread.sleep(200);

		if (!driver.getTitle().equals("welcome")) {
			result = "Login failed: " + driver.findElement(By.id("ErrorLineEx")).getText();}
		else result = "Login success";

		System.out.println(result);
		driver.quit();
	}
}
