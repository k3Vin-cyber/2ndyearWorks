class Book {
    String title;
    String author;
    int pages;

    public Book(String t, String a, int p) {
        title = t;
        author = a;
        pages = p;
    }

    public void display() {
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Pages: " + pages);
        System.out.println("-------------------------");
    }
}

public class Main {
    public static void main(String[] args) {
        Book b1 = new Book("1984", "George Orwell", 328);
        Book b2 = new Book("The Hobbit", "J.R.R. Tolkien", 310);
        Book b3 = new Book("To Kill a Mockingbird", "Harper Lee", 281);
        Book b4 = new Book("Pride and Prejudice", "Jane Austen", 432);
        Book b5 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 180);

        b1.display();
        b2.display();
        b3.display();
        b4.display();
        b5.display();
    }
}