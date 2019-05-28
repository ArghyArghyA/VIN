package vin;

import java.io.Closeable;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VIN2 implements Closeable
{
	private static WebDriver driver = new HtmlUnitDriver();;
	private VIN2()
	{
	}

	public void close() throws IOException
	{
		driver.quit();
		driver = null;
	}
	
	public static String getRandomVin(FROM from)
	{
		driver.get(from.URL);
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement target = wait.until(ExpectedConditions.presenceOfElementLocated(from.VIN));
//		WebElement target = driver.findElement(from.VIN);
		if (from.Source.equalsIgnoreCase("text"))
			return target.getText();
		else
			return target.getAttribute(from.Source);
	}
	
	public enum FROM
	{
		VINGENERATOR("https://www.vingenerator.org/", By.name("vin"), "value"),
		RANDOMVIN("http://www.randomvin.com/", By.cssSelector("span#Result>h2"), "text");
		
		public String URL;
		public By VIN;
		public String Source;
		private FROM(String uRL, By vIN, String source)
		{
			URL = uRL;
			VIN = vIN;
			Source = source;
		}
	}

}
