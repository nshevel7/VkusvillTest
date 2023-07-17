import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VTest {

    WebDriver driver;

    @BeforeEach
    public void setupDriver() {
        System.setProperty("webdriver.chrome.driver", "/Users/nsh/Code/webdrivers/chromedriver");
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void VkusvillTest() {
        driver.get("https://vkusvill.ru/goods/");
        VkusvillPage vkusvillPage = new VkusvillPage(driver);
        vkusvillPage.selectIceCream();
        List<Double> priceAverages = vkusvillPage.parsePrices();
        List<Double> sortedAverages = new ArrayList<>(priceAverages);
        Collections.sort(sortedAverages);
        Assertions.assertEquals(priceAverages, sortedAverages, "Цена в среднем не возрастает.");
    }

    @AfterEach
    public void closeDriver() {
        driver.quit();
    }

}
