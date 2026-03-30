package com.restapi.clients;

import com.restapi.pojo.Book;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BookDemo {

    public static void main(String[] args) {
        Book book = new Book();
        book.setIsbn("978-0134685991");
        book.setAisle("A1");

        log.info("Book Details:");
        log.info("ISBN: {}", book.getIsbn());
        log.info("Aisle: {}", book.getAisle());
        log.info("Book Object: {}", book);

        Book book2 = new Book();
        book2.setIsbn("978-0321356680");
        book2.setAisle("B2");

        log.info("Second Book: {}", book2);
        log.info("Are books equal? {}", book.equals(book2));
        log.info("Book1 hashCode: {}", book.hashCode());
        log.info("Book2 hashCode: {}", book2.hashCode());
    }

}
