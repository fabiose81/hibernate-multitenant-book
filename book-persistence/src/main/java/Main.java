/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fabioestrela
 */
import br.com.fabiose.book.model.BookModel;
import br.com.fabiose.book.persistence.BookPersistence;
import br.com.fabiose.book.utils.HibernateUtil;
import java.util.List;
import java.util.Scanner;
import org.hibernate.*;

public class Main {

    public static void main(String[] args) {
        Scanner scInput = new Scanner(System.in);

        String title;
        String isbn;
        String description;
        char schema;

        do {
            System.out.print("Enter Book's title: ");
            title = scInput.nextLine();
        } while (title.trim().equalsIgnoreCase(""));

        do {
            System.out.print("Enter Book's description: ");
            description = scInput.nextLine();
        } while (description.trim().equalsIgnoreCase(""));

        do {
            System.out.print("Enter Book's isbn: ");
            isbn = scInput.nextLine();
        } while (isbn.trim().equalsIgnoreCase(""));

        do {
            System.out.print("Enter Database's schema [1] or [2]: ");
            schema = scInput.nextLine().charAt(0);
        } while ((schema != '1') && (schema != '2'));

        BookModel bookModel = new BookModel();
        bookModel.setName(title);
        bookModel.setDescription(description);
        bookModel.setIsbn(isbn);
        
        SessionFactory factory = HibernateUtil.factory();
        Session session = factory.withOptions().tenantIdentifier(new StringBuilder("db").append(schema).toString()).openSession();
        
        BookPersistence bookPersistence = new BookPersistence();
        
        try{
            bookModel = bookPersistence.add(bookModel, session);
            System.out.println("Book id: "+bookModel.getId()+" inserted!");
            session.close();
        }catch(Exception e){
           System.out.println(e.getMessage());
        }
    }

}
