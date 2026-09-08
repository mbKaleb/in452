import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieLensDB {

    private String dbURL;

    public MovieLensDB() {
        dbURL = "jdbc:sqlserver://localhost:1433;databaseName=IN452;"
              + "user=sa;password=Ke3p#Local-Dev42;"
              + "encrypt=true;trustServerCertificate=true";
    }

    public MovieLensDB(String PConnectionString) {
        dbURL = PConnectionString;
    }

    public String getMovieCount() {
        String result = "";
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM movies")) {
            if (rs.next()) {
                result = String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            result = "SQL Error: " + e.getMessage();
        }
        return result;
    }

    public String getMovieTitles() {
        StringBuilder result = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT TOP 100 title FROM movies")) {
            while (rs.next()) {
                result.append(rs.getString("title")).append("\n");
            }
        } catch (SQLException e) {
            return "SQL Error: " + e.getMessage();
        }
        return result.toString();
    }

    public String getTopRatedMovies() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT TOP 20 m.title, AVG(CAST(r.rating AS float)) AS avgRating "
                + "FROM movies m JOIN ratings r ON m.movieId = r.movieId "
                + "GROUP BY m.title "
                + "HAVING AVG(CAST(r.rating AS float)) > 4.0 "
                + "ORDER BY avgRating DESC";
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                result.append(rs.getString("title"))
                      .append(" - Rating: ")
                      .append(String.format("%.2f", rs.getDouble("avgRating")))
                      .append("\n");
            }
        } catch (SQLException e) {
            return "SQL Error: " + e.getMessage();
        }
        return result.toString();
    }

    public String getMovieGenres() {
        StringBuilder result = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT genres FROM movies")) {
            while (rs.next()) {
                result.append(rs.getString("genres")).append("\n");
            }
        } catch (SQLException e) {
            return "SQL Error: " + e.getMessage();
        }
        return result.toString();
    }
}
