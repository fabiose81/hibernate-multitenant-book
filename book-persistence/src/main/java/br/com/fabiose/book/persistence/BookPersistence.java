/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fabiose.book.persistence;

/**
 *
 * @author fabioestrela
 */

import br.com.fabiose.book.model.BookModel;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BookPersistence {
    
    public BookModel add(BookModel bookModel, Session session) throws Exception{
        Transaction t = null;
        try {
            t = session.beginTransaction();
            session.save(bookModel);
            t.commit();
            return bookModel;
        } catch (Exception e) {
            t.rollback();
            throw new Exception(e.getMessage());
        } 
    }
}
