package pages.test;

import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.MainConverterPage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import pages.ConverterPage;
import pages.NewsPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CurrencyConverterTest {
    private WebDriver driver;

    @Before
    @Parameters({"browser", "hub", "url"})
    public void setUp(String browser, String hub, String url) throws MalformedURLException {
        if (browser.toLowerCase().equals("chrome"))
            driver = new RemoteWebDriver(new URL(hub), DesiredCapabilities.chrome());
        else if (browser.toLowerCase().equals("firefox"))
            driver = new RemoteWebDriver(new URL(hub), DesiredCapabilities.firefox());
        else
            throw new NotImplementedException();
        driver.manage().window().maximize();
        driver.get(url);
    }
    @Test
    public void getTitleOfPage() {
        driver = new FirefoxDriver();
        driver.get("http://go.mail.ru/");
        new MainConverterPage(driver).search_form_currency_converter();
        String title = driver.getTitle();
        String request = "курс валют";
        assertTrue(request.equals(title.substring(0,request.length())));
        driver.quit();
    }

    @Test
    public void clickChangeConverterTest() {
        driver = new FirefoxDriver();
        driver.get("http://go.mail.ru/");
        ConverterPage converterPage = new ConverterPage(driver);
        MainConverterPage mainConverterPage = new MainConverterPage(driver);
        mainConverterPage.search_form_currency_converter();
        String firstCurrency = converterPage.getFirstCurrency().substring(0,4);
        String secondCurrency = converterPage.getSecondCurrency().substring(0,4);
        converterPage.clickChangeConverter();
        String newFirstCurrency = converterPage.getFirstCurrency().substring(0,4);
        String newSecondCurrency = converterPage.getSecondCurrency().substring(0,4);
        assertTrue((firstCurrency.equals(newSecondCurrency))&(secondCurrency.equals(newFirstCurrency)));
        driver.quit();
    }

    public String getNewWindow(final Set<String> oldWindowsSet) {
        String newWindow = (new WebDriverWait(driver, 15))
                .until(new ExpectedCondition<String>() {
                    public String apply(WebDriver driver) {
                        Set<String> newWindowsSet = driver.getWindowHandles();
                        newWindowsSet.removeAll(oldWindowsSet);
                        return newWindowsSet.size() > 0 ?
                                newWindowsSet.iterator().next() : null;
                    }
                }
                );
        return newWindow;
    }

    @Test
    public void clickLinkNewsPageTest() {
        driver = new FirefoxDriver();
        driver.get("http://go.mail.ru/");
            ConverterPage converterPage = new ConverterPage(driver);
            MainConverterPage mainConverterPage = new MainConverterPage(driver);
            NewsPage newsPage = new NewsPage(driver);
        mainConverterPage.search_form_currency_converter();
            //String originalWindow = driver.getWindowHandle();
            final Set<String> oldWindowsSet = driver.getWindowHandles();
            converterPage.clickButtonDynamics();
            String newWindow = getNewWindow(oldWindowsSet);
            driver.switchTo().window(newWindow);
            String title = driver.getTitle();
        String request = "Новости Mail.Ru: Курсы валют";
        assertTrue(request.equals(title.substring(0,request.length())));
        driver.quit();
    }

    @Test
    public void CheckMathTest() {
        driver = new FirefoxDriver();
        driver.get("http://go.mail.ru/");
        ConverterPage converterPage = new ConverterPage(driver);
        new MainConverterPage(driver).search_form_currency_converter();
        int number = 1;
        String newNumberStr = converterPage.getRub();
        newNumberStr =  newNumberStr.replace(",",".");
        double newNumber = Double.parseDouble(newNumberStr);
        double currency = newNumber/number;
        converterPage.enterText();
        int number2 = 2;
        String oldNumberStr = converterPage.getRub();
        oldNumberStr =  oldNumberStr.replace(",",".");
        double oldNumber = Double.parseDouble(oldNumberStr);
        double newCurrency = oldNumber/number2;
        assertTrue(currency == newCurrency);
        driver.quit();
    }

    @Test
    public void choiceOfOtherCurrencyTest() {
        driver = new FirefoxDriver();
        driver.get("http://go.mail.ru/");
        ConverterPage converterPage = new ConverterPage(driver);
        new MainConverterPage(driver).search_form_currency_converter();
        String beforeStr = converterPage.getFirstCurrency();
        converterPage.clickCurrencyName();
        String afterStr = converterPage.getFirstCurrency();
        assertFalse(beforeStr.equals(afterStr));
        driver.quit();
    }

    @Test
    public void clickCbrRuTest() {
        driver = new FirefoxDriver();
        driver.get("http://go.mail.ru/");
        ConverterPage converterPage = new ConverterPage(driver);
        new MainConverterPage(driver).search_form_currency_converter();
        Set<String> oldWindowsSet = driver.getWindowHandles();
        converterPage.clickLinkOfCurrencyRate();
        String newWindow = getNewWindow(oldWindowsSet);
        driver.switchTo().window(newWindow);
        String url = driver.getCurrentUrl();
        String request = "http://www.cbr.ru";
        assertTrue(request.equals(url.substring(0,request.length())));
        driver.quit();
    }

    @Test
    public void RUinUAHTest() {
        driver = new FirefoxDriver();
        driver.get("http://go.mail.ru/");
        ConverterPage converterPage = new ConverterPage(driver);
        new MainConverterPage(driver).search_form_currency_converter_with_other_request("курс валют рубль гривна");
        String firstStr = "российский рубль";
        String secondStr = "украинской гривны";
        String firstStrWeb = converterPage.getFirstCurrency();
        String secondStrWeb = converterPage.getSecondCurrency();
        assertTrue((firstStr.equals(firstStrWeb))&(secondStr.equals(secondStrWeb)));
        driver.quit();
    }

    @Test
    public void CurrencyRateDollarTest() {
        driver = new FirefoxDriver();
        driver.get("http://go.mail.ru/");
        ConverterPage converterPage = new ConverterPage(driver);
        new MainConverterPage(driver).search_form_currency_converter_with_other_request("курс валют доллар");
        String firstStr = "доллар сша";
        String secondStr = "российского рубля";
        String firstStrWeb = converterPage.getFirstCurrency();
        String secondStrWeb = converterPage.getSecondCurrency();
        assertTrue((firstStr.equals(firstStrWeb))&(secondStr.equals(secondStrWeb)));
        driver.quit();
    }
}