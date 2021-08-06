package ua.bala.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.bala.model.Product;
import ua.bala.output.JsonPrinter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class HtmlParserUtil {

    private static HtmlParserUtil htmlParser;
    private static final AtomicInteger httpRequestsCounter = new AtomicInteger(0);

    public static HtmlParserUtil getInstance() {
        return htmlParser == null ? new HtmlParserUtil() : htmlParser;
    }
    public static AtomicInteger getHttpRequestsCounter() {
        return httpRequestsCounter;
    }

    public void parse(String url) {
        try {
            Document page = getPage(url);
            parsePage(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document getPage(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        httpRequestsCounter.getAndIncrement();
        return document;
    }

    private void parsePage(Document page) {
        Elements productElements = page.select("a.dgBQdu");
        Set<Element> elementList = new HashSet<>();
        elementList.addAll(productElements);
        parseElements(elementList);
    }

    private void parseElements(Set<Element> productElements) {

        List<Product> productList = new CopyOnWriteArrayList<>();
        AtomicInteger parseCount = new AtomicInteger();

        ExecutorService service = Executors.newCachedThreadPool();
        for (Element element: productElements)
            service.execute(() -> {

                Long articleID = Long.parseLong(element.attr("id"));
                String name = "NOT FOUND";
                String brand = "NOT FOUND";
                String price = "NOT FOUND";
                Set<String> colors = new HashSet<>();
                String url = "https://www.aboutyou.de" + element.attr("href");
                Document innerPage;

                Product[] products = new Product[2];
                for (int i = 0; i < 2; i++) {
                    try {
                        innerPage = getPage(url);
                        Element innerElement = innerPage.selectFirst("[data-test-id='BuyBox']");
                        name = innerElement.selectFirst("div.dZjUXd").text();
                        brand = innerElement.selectFirst("[data-test-id='BrandLogo']").attr("alt");
                        colors = new HashSet<>(innerElement.select("span.jlvxcb-1").eachText());
                        price = innerElement.selectFirst("div.dWWxvw > span").text().replace("ab ", "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    products[i] = new Product(articleID, name, brand, colors, price, url);
                }

                if (!products[0].equals(products[1])){ // Double-checking results
                    System.out.println("Products aren't equals: Product passed");
                }

                products[0].setID();
                addProduct(products[0], productList);
                parseCount.getAndIncrement();
                if (parseCount.get() >= productElements.size()) {
                    new JsonPrinter().extract(productList);
                }
            });
        service.shutdown();
    }

    private synchronized void addProduct(Product product, List<Product> productList){
        System.out.println("Product " + product.getID() + " parsed");
        System.out.print(product);
        productList.add(product);
        System.out.printf("Product %d added to list\n%n", product.getID());
    }
}
