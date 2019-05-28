package vin;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;

/**
 * @author Alpha Romeo
 *
 */
public class VIN implements Closeable {

	private VIN() {
	}

	private static int[] vinDigitPositionMultiplier = { 8, 7, 6, 5, 4, 3, 2, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static HashMap<String, Integer> vinDigitValues;
	private static WebDriver driver;

	static {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
	}

	private static Pair<String, String> getRandomVinStart() {
		String key, value, line = null;
		try {
			int targetLine = new Random().nextInt(62177) + 1; // generates random number between 1 and 62177
			BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/VinPrefixes.txt"));
			for (int i = 0; i < targetLine + 1; i++) {
				line = reader.readLine();
			}
			reader.close();

			key = line.trim().split(" ")[0].trim();
			value = line.trim().split(" ")[1].trim();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return Pair.of(key, value);
	}

	private static String getRandomVinChar() {
		if (vinDigitValues==null)
		{
			vinDigitValues = new HashMap<String, Integer>();
			vinDigitValues.put("A", 1);
			vinDigitValues.put("B", 2);
			vinDigitValues.put("C", 3);
			vinDigitValues.put("D", 4);
			vinDigitValues.put("E", 5);
			vinDigitValues.put("F", 6);
			vinDigitValues.put("G", 7);
			vinDigitValues.put("H", 8);
			vinDigitValues.put("J", 1);
			vinDigitValues.put("K", 2);
			vinDigitValues.put("L", 3);
			vinDigitValues.put("M", 4);
			vinDigitValues.put("N", 5);
			vinDigitValues.put("P", 7);
			vinDigitValues.put("R", 9);
			vinDigitValues.put("S", 2);
			vinDigitValues.put("T", 3);
			vinDigitValues.put("U", 4);
			vinDigitValues.put("V", 5);
			vinDigitValues.put("W", 6);
			vinDigitValues.put("X", 7);
			vinDigitValues.put("Y", 8);
			vinDigitValues.put("Z", 9);
			vinDigitValues.put("1", 1);
			vinDigitValues.put("2", 2);
			vinDigitValues.put("3", 3);
			vinDigitValues.put("4", 4);
			vinDigitValues.put("5", 5);
			vinDigitValues.put("6", 6);
			vinDigitValues.put("7", 7);
			vinDigitValues.put("8", 8);
			vinDigitValues.put("9", 9);
			vinDigitValues.put("0", 0);
		}
		return (String) vinDigitValues.keySet().toArray()[new Random().nextInt(vinDigitValues.keySet().size())];
	}

	private static String getCheckSumChar(String vin) {
		int checkSumTotal = 0;

		if (vin.length() != 16)
			return null;
		for (int i = 0; i < vin.length(); i++) {
			if (vinDigitValues.get(Character.toString(vin.charAt(i))) != null)
				checkSumTotal = checkSumTotal
						+ vinDigitValues.get(Character.toString(vin.charAt(i))) * vinDigitPositionMultiplier[i];
			else
				return null;
		}
		return (checkSumTotal % 11 == 10) ? "X" : Integer.toString(checkSumTotal % 11);
	}

	public static String getRandomVin() {
		Pair<String, String> vinFirst = getRandomVinStart();
		String vin = vinFirst.getKey() + getRandomVinChar() + vinFirst.getValue();

		for (int i = 0; i < 7; i++) {
			vin = vin + getRandomVinChar();
		}
		String check = getCheckSumChar(vin);
		return vin.substring(0, 8) + check + vin.substring(8);
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

	public void close() throws IOException {
		if (driver != null) {
			driver.quit();
			driver = null;
		}

	}
}
