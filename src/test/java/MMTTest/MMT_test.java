package MMTTest;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MMT_test {
	WebDriver driver;
	Actions act;
	String ActualchangeHotelName;
	String iteneraryRoomName;
	String addedActivity1;

	@BeforeTest
	public void startup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.makemytrip.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@Test(priority = 1)
	public void add_tour_package_test() throws InterruptedException {
		driver.findElement(By.xpath("(//*[@class='chNavText darkGreyText'])[4]")).click();
		driver.findElement(By.id("fromCity")).click();

		List<WebElement> fromCity = driver
				.findElements(By.xpath("//*/li[@class='font14 blackText appendBottom6 autoSuggestValue']"));

		for (WebElement e : fromCity) {
			if (e.getText().equalsIgnoreCase("Bangalore")) {
				e.click();
				break;
			}
		}
		driver.findElement(By.id("toCity")).click();
		driver.findElement(By.xpath("//*[@class='dest-search-container']/input")).sendKeys("Singapore");
		Thread.sleep(3000);
		WebElement clickOn = driver.findElement(By.xpath("(//div[@class='dest-city-container'])[1]"));
		act = new Actions(driver);
		act.click(clickOn).build().perform();
		driver.findElement(By.xpath("(//*[@class='dateInnerPara'])[31]")).click();
		driver.findElement(By.xpath("//*[contains(text(),'APPLY')]")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[normalize-space()='Filters']")).click();
		driver.findElement(By.xpath("//*[@data-cy='submit']")).click();
		Thread.sleep(8000);
		driver.findElement(By.xpath("//*[@class='skipBtn']")).click();
		Thread.sleep(5000);
		// driver.findElement(By.xpath("//*[@class='close closeIcon']")).click();

		driver.findElement(By.xpath("(//*[@class='packageName tooltipPackageName'])[1]")).click();
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> values = handles.iterator();
		while (values.hasNext()) {
			String parentWindow = values.next();
			driver.switchTo().window(parentWindow);
			String childWindow = values.next();
			driver.switchTo().window(childWindow);
		}
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@class='skipBtn']")).click();
		WebElement changeRoom = driver.findElement(By.xpath("(//*[@id='change'])[4]"));
		Thread.sleep(2000);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// js.executeScript("arguments[0].scrollIntoView(true);", changeRoom);
		// Thread.sleep(3000);
		js.executeScript("window.scrollTo(0,150)");
		act.click(changeRoom).build().perform();
		Thread.sleep(8000);
		driver.findElement(By.xpath("(//*[@class='primaryBtn fill selectBtn'])[1]")).click();
		Thread.sleep(3000);
		ActualchangeHotelName = driver
				.findElement(By.xpath("//*[text()='Citadines Connect City Centre Holiday Selection']")).getText();
		System.out.println(ActualchangeHotelName);
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//*[@class='updatePackageBtnText font10'])[1]")).click();
		String changeHotelName = driver
				.findElement(By.xpath("//*[text()='Citadines Connect City Centre Holiday Selection']")).getText();
		Assert.assertEquals(ActualchangeHotelName, changeHotelName);
	}

	@Test(priority = 2)
	public void add_an_activity_test() throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.id("chooseAndAddBtn")).click();

		addedActivity1 = driver.findElement(By.xpath("(//*[@class='activityHeading'])[1]")).getText();

		Thread.sleep(5000);

		driver.findElement(By.xpath("(//*[@class='primaryBtn fill selectBtn'])[1]")).click();

		driver.findElement(By.xpath("(//*[@class='updatePackageBtnWrapper btn'])[1]")).click();
		Thread.sleep(5000);
		iteneraryRoomName = driver
				.findElement(By.xpath("//*[text()='Citadines Connect City Centre Holiday Selection']")).getText();
		Assert.assertEquals(iteneraryRoomName, ActualchangeHotelName);
	}

	@Test(priority = 3)
	public void validate_hotel_change_activity_test() throws Exception {
		Thread.sleep(3000);

		driver.findElement(By.xpath("//ul[@id='initeraryNav']//li[3]")).click();
		String hotelDetails = driver
				.findElement(By.xpath("//*[text()='Citadines Connect City Centre Holiday Selection']")).getText();
		Assert.assertEquals(iteneraryRoomName, hotelDetails);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//ul[@id='initeraryNav']//li[5]")).click();
		String activityAdded = driver.findElement(By.xpath("(//*[@class='activity-row-details-title'])[1]")).getText();
		Assert.assertEquals(addedActivity1, activityAdded);
	}
	@AfterTest
	public void tearDown() {
		driver.close();
	}
}
