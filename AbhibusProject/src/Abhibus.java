import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class Abhibus {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		/* Launching the site - abhibus.com */
		driver.get("https://www.abhibus.com/");

		/* Defining Actions so that we can use it to move to Abhibus logo */

		Actions act = new Actions(driver);

		/* Taking the xpath of Logo in a Webelemnt */
		WebElement Logo = driver.findElement(By.xpath("//img[contains(@title, 'abhibus')]"));

		/* Moving to Abhibus logo */
		act.moveToElement(Logo).perform();

		/* Taking the title of Abhibus logo in a string variable */
		String Tooltiptext = Logo.getAttribute("title");

		/*
		 * Validating the tooltip using Assert - if no error that means the tooltip is
		 * validated
		 */
		Assert.assertEquals(Tooltiptext, "abhibus.com - India's Fastest Online bus ticket booking site");

		/* Here we want to enter Pune in "Leavingfrom" field */
		WebElement Leavingfrom = driver.findElement(By.id("source"));
		Leavingfrom.sendKeys("Pun");
		Thread.sleep(2000L);
		Leavingfrom.sendKeys(Keys.ENTER);
		String source = Leavingfrom.getAttribute("value");

		/* Here we want to enter Hyderabad in "Goingto" field */
		WebElement Goingto = driver.findElement(By.id("destination"));
		Goingto.sendKeys("hyd");
		Thread.sleep(2000L);
		Goingto.sendKeys(Keys.ENTER);
		String destination = Goingto.getAttribute("value");

		/*
		 * Now we want to click on "Date of Journey" field and select the date 5th
		 * March, 2019
		 */

		driver.findElement(By.cssSelector("input[id='datepicker1']")).click();

		while (!driver.findElement(By.cssSelector("[class='ui-datepicker-month']")).getText().contains("March")) {
			driver.findElement(By.xpath(
					"//div[@class='ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-right']/a/span"))
					.click();
		}

		List<WebElement> dates = driver.findElements(By.cssSelector(".ui-state-default"));
		int count = dates.size();

		for (int i = 0; i < count; i++) {
			String datetext = dates.get(i).getText();
			if (datetext.equalsIgnoreCase("5")) {
				dates.get(i).click();
				break;
			}
		}

		/*
		 * Now we want to click on "Date of Return" field and select the date 7th March,
		 * 2019
		 */

		driver.findElement(By.cssSelector("input[id='datepicker2']")).click();

		List<WebElement> returndate = driver.findElements(By.cssSelector(".ui-state-default"));
		int count1 = returndate.size();

		for (int r = 0; r < count1; r++) {
			String datetext = returndate.get(r).getText();
			if (datetext.equalsIgnoreCase("7")) {
				returndate.get(r).click();
				break;
			}
		}

		/* Now we want to click on Search button */

		driver.findElement(By.xpath("//a[@title='Search Buses']")).click();

		/* Now we want to click on "SLEEPER" in Filters section */

		driver.findElement(By.xpath("//input[@title='SLEEPER']")).click();

		/*
		 * Now we want to click on Select seat on the first travels available in the
		 * list - for this we will use absolute xpath as id can change in future
		 */

		driver.findElement(
				By.xpath("/html[1]/body[1]/div[3]/div[7]/div[2]/div[1]/div[3]/div[1]/div[1]/div[3]/div[2]/a[1]"))
				.click();

		/* Now i want to select the seat E2 */

		driver.findElement(By.id("O2-3ZZ")).click();

		/* Now get the total fare for the seat selected and store it in a variable */

		String Fare1 = driver.findElement(By.id("totalfare")).getText();
		
		Double sourcefare = Double.parseDouble(Fare1);

		/* Select a boarding point */

		Select bpoint = new Select(driver.findElement(By.id("boardingpoint_id1")));
		bpoint.selectByIndex(1);

		/* Now click on Book Return button */

		driver.findElement(By.xpath("//input[@id='btnEnable1']")).click();

		Thread.sleep(20000L);

		/* Click on Select Seat button */
		driver.findElement(By.xpath("//a[@id='498747885']//span[@class='book'][contains(text(),'Select Seat')]"))
				.click();

		/* Select a certain seat */
		driver.findElement(By.xpath("//a[@id='O2-8ZZ']")).click();

		String Fare2 = driver.findElement(By.xpath("(//span[@id='totalfare'])[2]")).getText();
		Double destinationfare = Double.parseDouble(Fare2);

		/* Now select a boarding point */

		Select bpointreturn = new Select(driver.findElement(By.xpath("//select[@id='boardingpoint_id2']")));
		bpointreturn.selectByIndex(1);

		/* Print the boarding point address */

		String hydboardingpoint = driver.findElement(By.xpath("//div[@id='bpDesc1498747885']")).getText();
		System.out.println(hydboardingpoint);

		/* Now click on "Continue to payment" button */

		driver.findElement(By.xpath("(//input[@value='Continue to Payment '])[2]")).click();

		/* Validate source station and boarding point */
		String sourcedestination = driver
				.findElement(By.xpath("//div[@class='col2 sidebar']//div[3]//div[1]//h3[1]//strong[1]")).getText();

		if (sourcedestination.contains(source)) {
			if (driver.findElement(By.xpath("//p[contains(text(),'Boarding Point:')]")).getText().contains("Wakad")) {
				System.out.println("Source station and boarding points are validated");
			} else {
				System.out.println("Source station and boarding points are not validated");
			}
		}

		/* Validate destination station and boarding point */
		if (sourcedestination.contains(destination)) {
			if (driver.findElement(By.xpath("//p[contains(text(),'boarding at')]")).getText().contains("Afzalgunj")) {
				System.out.println("Destination station and boarding points are validated");
			} else {
				System.out.println("Destination station and boarding points are not validated");
			}
		}

		/* Now we will validate the fare */
		Double Finalfare = sourcefare + destinationfare;
		DecimalFormat df = new DecimalFormat("###.##");
		String journeyfare = df.format(Finalfare).trim();

		String Tfare = driver.findElement(By.xpath("//p[@id='NetAmountmsg']")).getText();
		String Totalfare = Tfare.trim().replace(",", "");
		/* String Totalfare = df.format(ff).trim(); */

		if (journeyfare.contentEquals(Totalfare)) {
			System.out.println("Fare is validated");
		} else {
			System.out.println("Fare is not validated");
		}
		driver.quit();
	}

}
