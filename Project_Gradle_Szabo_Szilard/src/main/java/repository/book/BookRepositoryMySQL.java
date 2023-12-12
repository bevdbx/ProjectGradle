package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }


        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        Optional<Book> book = Optional.empty();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                book = Optional.of(getBookFromResultSet(resultSet));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return book;
    }

    /**
     *
     * How to reproduce a sql injection attack on insert statement
     *
     *
     * 1) Uncomment the lines below and comment out the PreparedStatement part
     * 2) For the Insert Statement DROP TABLE SQL Injection attack to succeed we will need multi query support to be added to our connection
     * Add to JDBConnectionWrapper the following flag after the DB_URL + schema concatenation: + "?allowMultiQueries=true"
     * 3) book.setAuthor("', '', null); DROP TABLE book; -- "); // this will delete the table book
     * 3*) book.setAuthor("', '', null); SET FOREIGN_KEY_CHECKS = 0; SET GROUP_CONCAT_MAX_LEN=32768; SET @tables = NULL; SELECT GROUP_CONCAT('`', table_name, '`') INTO @tables FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()); SELECT IFNULL(@tables,'dummy') INTO @tables; SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables); PREPARE stmt FROM @tables; EXECUTE stmt; DEALLOCATE PREPARE stmt; SET FOREIGN_KEY_CHECKS = 1; --"); // this will delete all tables. You are not required to know the table name anymore.
     * 4) Run the program. You will get an exception on findAll() method because the test_library.book table does not exist anymore
     */


    // ALWAYS use PreparedStatement when USER INPUT DATA is present
    // DON'T CONCATENATE Strings!

    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO book (author, title, publishedDate, quantity) VALUES (?, ?, ?, ?)";

        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, book.getAuthor());
                preparedStatement.setString(2, book.getTitle());

                // Handle null case for publishedDate
                if (book.getPublishedDate() != null) {
                    java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(book.getPublishedDate().atStartOfDay());
                    preparedStatement.setTimestamp(3, timestamp);
                } else {
                    preparedStatement.setNull(3, Types.TIMESTAMP);
                }

                preparedStatement.setInt(4, book.getQuantity());

                int rowsInserted = preparedStatement.executeUpdate();
                return rowsInserted == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteBook(Long id) {
        String sql = "DELETE FROM book WHERE id = ?;";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateBook(Book book) {
        try {
            String sql = "UPDATE book SET quantity = ?, author = ?, title = ?, publishedDate = ? where id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, book.getQuantity());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getTitle());
            preparedStatement.setDate(4, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setLong(5, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .setQuantity(resultSet.getInt("quantity"))
                .build();
    }
}