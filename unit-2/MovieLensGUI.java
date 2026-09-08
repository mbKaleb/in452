import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//  MovieLensGUI - the presentation layer
public class MovieLensGUI {

    private JFrame frame;
    private JTextArea resultArea;
    private MovieLensDB db;

    //Entry point
    public static void main(String[] args) {
        new MovieLensGUI();
    }

    public MovieLensGUI() {
        frame = new JFrame("MovieLens Database Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Left side five label/button rows
        JPanel buttonPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 0));

        JLabel connectLabel = new JLabel("Connect");
        JButton connectButton = new JButton("Connect to MovieLens DB");
        JLabel countLabel = new JLabel("Count");
        JButton countButton = new JButton("Movie Count");
        JLabel titlesLabel = new JLabel("Titles");
        JButton titlesButton = new JButton("Get Movie Titles");
        JLabel topRatedLabel = new JLabel("Top Rated");
        JButton topRatedButton = new JButton("Get Top Rated Movies");
        JLabel genresLabel = new JLabel("Genres");
        JButton genresButton = new JButton("Get Movie Genres");

        buttonPanel.add(connectLabel);
        buttonPanel.add(connectButton);
        buttonPanel.add(countLabel);
        buttonPanel.add(countButton);
        buttonPanel.add(titlesLabel);
        buttonPanel.add(titlesButton);
        buttonPanel.add(topRatedLabel);
        buttonPanel.add(topRatedButton);
        buttonPanel.add(genresLabel);
        buttonPanel.add(genresButton);

        // Right side scrollable results pane
        resultArea = new JTextArea("Connect to the database to begin.");
        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resultArea.setMargin(new java.awt.Insets(8, 8, 8, 8));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(12, 0, 12, 12),
                BorderFactory.createTitledBorder("Results")));

        connectButton.addActionListener(e -> {
            try {
                db = new MovieLensDB();
                // Run a trivial query so a bad connection fails here,
                // not on a later button press.
                String test = db.getMovieCount();
                if (test.startsWith("SQL Error")) {
                    showResult(test);
                } else {
                    showResult("Connected to MovieLens Database");
                }
            } catch (Exception ex) {
                showResult("Connection Failed: " + ex.getMessage());
            }
        });

        countButton.addActionListener(e -> {
            if (notConnected()) return;
            showResult("The movie count is: " + db.getMovieCount());
        });

        titlesButton.addActionListener(e -> {
            if (notConnected()) return;
            showResult(db.getMovieTitles());
        });

        topRatedButton.addActionListener(e -> {
            if (notConnected()) return;
            showResult(db.getTopRatedMovies());
        });

        genresButton.addActionListener(e -> {
            if (notConnected()) return;
            showResult(db.getMovieGenres());
        });

        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setSize(860, 460);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean notConnected() {
        if (db == null) {
            showResult("Please connect to the database first.");
            return true;
        }
        return false;
    }

    //Shows a query result in the results pane from the top
    private void showResult(String text) {
        resultArea.setText(text);
        resultArea.setCaretPosition(0);
    }
}