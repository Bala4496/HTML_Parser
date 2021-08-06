package ua.bala;

import ua.bala.parser.HtmlParserUtil;

public class Main {

    public static void main(String[] args) {

        String rootUrl = "https://www.aboutyou.de/c/maenner/bekleidung-20290";
        System.out.println("Started parsing: " + rootUrl);

        HtmlParserUtil htmlParser = HtmlParserUtil.getInstance();

        htmlParser.parse(rootUrl);
    }
}
