package com.jmc.libsystem.Information;

public class BookLoan {
    private String bookName;
    private String borrowedDate;
    private String returnDate;
    private String status;

    public BookLoan(String bookName, String borrowedDate, String returnDate, String status) {
        this.bookName = bookName;
        this.borrowedDate = borrowedDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
