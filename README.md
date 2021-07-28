# Price Tracker
A CLI program developed in Java 11 that fetch products from popular ecommerce like Amazon and notify the user if a certain price criteria is match.

## Tech / Tools
* Java 11 HttpClient
* [Jsoup](https://jsoup.org/) for HTML parsing
* [Slf4j](http://www.slf4j.org/) and [Logback](http://logback.qos.ch/) for logging
* [JUnit](https://junit.org/junit5/) for unit tests
* [Lombok](https://projectlombok.org/) for boilerplate code generation

## How to run
The app expects the CLI arguments `product` and `price` to work correctly.

The `product` represents the search criteria that will be used by the product fetchers.

The `price` represents the value that will be used to notify the user. If a product is found costing equal or less the provided price, the user will receive a notification.

**Examples**

* ```java -jar PriceTraker.jar --price 600 --product "SDD 1TB" ```
* CLI arguments can also be configured directly in most IDEs 


## Cool things I've done 
* The product fetchers were implemented using the [TemplateMethod](https://en.wikipedia.org/wiki/Template_method_pattern).