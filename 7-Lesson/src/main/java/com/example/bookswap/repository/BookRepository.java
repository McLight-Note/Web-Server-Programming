package com.example.bookswap.repository;

import com.example.bookswap.model.Book;
import com.example.bookswap.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByDeletedFalse();

    @Query("SELECT b FROM Book b WHERE b.deleted = false " +
           "AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
           "AND (:status IS NULL OR b.status = :status) " +
           "AND (:ownerId IS NULL OR b.owner.id = :ownerId)")
    List<Book> searchBooks(@Param("title") String title,
                           @Param("author") String author,
                           @Param("status") BookStatus status,
                           @Param("ownerId") Long ownerId);

    List<Book> findByOwnerIdAndDeletedFalse(Long ownerId);

    List<Book> findByStatusAndDeletedFalse(BookStatus status);
}

