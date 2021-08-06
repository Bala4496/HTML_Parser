package ua.bala.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ua.bala.parser.HtmlParserUtil;
import ua.bala.model.Product;

import java.io.*;
import java.util.Comparator;
import java.util.List;

public class JsonPrinter implements Printer{

    private static final String path = "";
    private static final String fileName = "productsOutput";

    public void extract(List<Product> products){

        if (products.isEmpty()){
            System.out.println("Product list is empty!!!");
            return;
        }
        products.sort(Comparator.comparing(Product::getID));

        System.out.println("Start ProductList's printing to JSON");
        try (final Writer writer = new FileWriter(path + fileName + ".json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(products, writer);
            System.out.println("ProductList printed to JSON");
            System.out.printf("Amount of triggered HTTP requests: %s%nAmount of extracted products: %s%n",
                                HtmlParserUtil.getHttpRequestsCounter(), Product.getProductsCounter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
