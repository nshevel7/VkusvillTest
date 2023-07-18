import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class VkusvillPage {
    private final WebDriver driver;
    private final String iceCreamXpath = "//span[contains(text(),'Мороженое') and contains(@class,'LinkCol')]";
    private final String teaAndCoffeeXpath = "//span[contains(text(),'Чай и кофе') and contains(@class,'LinkCol')]";
    private final String catalogSorterButtonXpath = "//span[@class='js-catalog-sorter-title']";
    private final String sortPriceButtonXpath = "//div[contains(text(),'По возрастанию цены')]";
    private final String pricesXpath = "//span[@class='Price__value' and not(ancestor::div[contains(@class, 'preloaded hidden')]) and not (ancestor::span[contains(@class, 'last')])]";
    private final String nextPageButtonXpath = "//span[text()='Вперёд']";
    private final String loadingSpinner = "//span[@class='ui-spinner__svg']";
    private WebElement iceCreamButton;
    private WebElement teaAndCoffeeButton;
    private WebElement catalogSorterButton;
    private WebElement sortPriceButton;
    private WebElement nextPageButton;
    private List<Double> pageAverages = new ArrayList<>();
    private WebDriverWait wait;

    public VkusvillPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void selectIceCream() {
        iceCreamButton = driver.findElement(By.xpath(iceCreamXpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", iceCreamButton);
        iceCreamButton.click();
    }

    public void selectTeaAndCoffee() {
        teaAndCoffeeButton = driver.findElement(By.xpath(teaAndCoffeeXpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", teaAndCoffeeButton);
        teaAndCoffeeButton.click();
    }

    public void sortPrices() {
        catalogSorterButton = wait.until(driver -> driver.findElement(By.xpath(catalogSorterButtonXpath)));
        catalogSorterButton.click();
        sortPriceButton = driver.findElement(By.xpath(sortPriceButtonXpath));
        sortPriceButton.click();
    }

    public void clickNextPage() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loadingSpinner)));
        nextPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(nextPageButtonXpath)));
        nextPageButton.click();
    }
    public List<Double> parsePrices() {
        parsePricesFromFirstPage();
        clickNextPage();

        int pageLimit = 10;
        for (int i = 0; i < pageLimit; ++i) {
            parsePricesFromOnePage();
            if (driver.findElements(By.xpath(nextPageButtonXpath)).size() == 0) {
                return pageAverages;
            }
            clickNextPage();
        }
        return null;
    }

    public void parsePricesFromFirstPage() {
        List<WebElement> prices = driver.findElements(By.xpath(pricesXpath));
        wait.until(ExpectedConditions.stalenessOf(prices.get(0)));
        prices = wait.until(driver -> driver.findElements(By.xpath(pricesXpath)));
        double pageAverage = prices.stream().map(WebElement::getText).mapToInt(Integer::parseInt).average().getAsDouble();
        pageAverages.add(pageAverage);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", prices.get(prices.size()-1));
    }

    public void parsePricesFromOnePage() {
        List<WebElement> prices = wait.until(driver -> driver.findElements(By.xpath(pricesXpath)));
        double pageAverage = prices.stream().map(WebElement::getText)
                                            .map(s -> s.replace(" ", ""))
                                            .mapToInt(Integer::parseInt).average().getAsDouble();
        pageAverages.add(pageAverage);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", prices.get(prices.size()-1));
    }

}
