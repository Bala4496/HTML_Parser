package ua.bala.model;

import lombok.*;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Product {

    private static final AtomicLong productsCounter = new AtomicLong(0);

    private Long ID;
    private Long articleID;
    private String name;
    private String brand;
    private String price;
    private Set<String> colors;
    private String url;

    public Product(Long articleID, String name, String brand, Set<String> colors, String price, String url) {
        this.articleID = articleID;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.colors = colors;
        this.url = url;
    }

    public void setID(){
        productsCounter.getAndIncrement();
        this.ID = productsCounter.get();
    }

    public static AtomicLong getProductsCounter() {
        return productsCounter;
    }

    @Override
    public String toString() {
        return String.format("%d\t%d\t%s\t%s\t%s\t%s\t%s\n", ID, articleID, name, brand, price, colors, url);
    }
}
