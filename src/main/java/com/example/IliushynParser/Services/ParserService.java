package com.example.IliushynParser.Services;

import com.example.IliushynParser.Models.ProductData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParserService {

    private WebDriver driver;

    public void EstablishConnection(WebDriver driver) {
        this.driver = driver;
    }

    public void DisconnectBrowserConnection(WebDriver connectDriver) {
        connectDriver.quit();
    }

    private void GoToSite(String url) {
        driver.get(url);
    }

    private void Search(String query) throws InterruptedException {
        WebElement searchInput = driver.findElement(By.cssSelector("input[name='search']"));
        WebElement searchButton = driver.findElement(By.cssSelector("button[class='button button_color_green button_size_medium search-form__submit ng-star-inserted']"));
        searchInput.sendKeys(query);
        searchButton.click();
        Thread.sleep(1000);
    }

    private int GetPageCount() {
        List<WebElement> pageElements = driver.findElements(By.className("pagination__item"));
        return pageElements.isEmpty() ? 0 : Integer.parseInt(pageElements.get(pageElements.size() - 1).getText());
    }

    private List<ProductData> ParsePage(String query) {
        List<ProductData> products = new ArrayList<>();
        Elements productElements = Jsoup.parse(driver.getPageSource()).select("div.goods-tile");
        for (Element productElement : productElements) {
            ProductData productData = new ProductData();
            productData.SetSearch(query);
            productData.SetInternalNumber(productElement.select("div.g-id.display-none").text());
            productData.SetDescription(productElement.select("a.goods-tile__heading").text());
            String price = productElement.select("span.goods-tile__price-value").text();
            productData.SetPrice(price.isEmpty()? "На сайті не вказано ціну!" : price);
            productData.SetProductLink(productElement.select("a.goods-tile__heading").attr("href"));
            productData.SetAvailability(productElement.select("div.goods-tile__availability").text());
            products.add(productData);
        }
        return products;
    }

    public List<ProductData> Parse(String query) throws InterruptedException {
        List<ProductData> products = new ArrayList<>();
        GoToSite("https://rozetka.com.ua/");
        Search(query);
        int pageCount = GetPageCount();
        if (pageCount > 0) {
            products.addAll(ParsePage(query));
            for (int i = 2; i <= pageCount; i++) {
                WebElement nextPageButton = driver.findElement(By.cssSelector("a.pagination__direction--forward"));
                nextPageButton.click();
                Thread.sleep(1000);
                products.addAll(ParsePage(query));
            }
        } else {
            ProductData productData = new ProductData();
            productData.SetSearch(query);
            productData.SetDescription("За Вашим запитом нічого не знайдено");
            products.add(productData);
        }
        return products;
    }
}
