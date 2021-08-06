package ua.bala.output;

import ua.bala.model.Product;

import java.util.List;

public interface Printer {

    String path = "";
    String fileName = "productsOutput";

    void extract(List<Product> products);
}
