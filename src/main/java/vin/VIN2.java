package vin;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;

public class VIN2 implements Closeable {
	static
	{
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
	}
	private static WebDriver driver;

	private VIN2() {
	}

	public void close() throws IOException {
		driver.quit();
		driver = null;
	}

	public static String getRandomVin(final FROM from) {
		if (driver==null)
		{
			if (from.URL.contains("randomvin"))
				driver = new HtmlUnitDriver(true);
			else
				driver = new HtmlUnitDriver();
		}
		driver.get(from.URL);
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
		wait.pollingEvery(10, TimeUnit.MILLISECONDS);
		wait.withTimeout(3, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);

		return wait.until(new Function<WebDriver, String>() {
			public String apply(WebDriver input) {
				String text;
				if (from.Source.equalsIgnoreCase("text"))
					text = driver.findElement(from.VIN).getText();
				else
					text = driver.findElement(from.VIN).getAttribute(from.Source);
				if (text.length() == 17)
					return text;
				else
					return null;
			}
		});
	}

	public enum FROM {
		VINGENERATOR("https://www.vingenerator.org/", By.name("vin"), "value"),
		RANDOMVIN("http://www.randomvin.com/", By.cssSelector("span#Result>h2"), "text");

		public String URL;
		public By VIN;
		public String Source;

		private FROM(String uRL, By vIN, String source) {
			URL = uRL;
			VIN = vIN;
			Source = source;
		}
	}

}
