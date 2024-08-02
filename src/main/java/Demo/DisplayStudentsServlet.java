package Demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/displayStudents")
public class DisplayStudentsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/best_pr_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "123";

    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("PostgreSQL JDBC Driver not found", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        List<Student> students = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT id, name FROM student ORDER BY id";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    students.add(new Student(id, name));
                }
            }
        } catch (SQLException e) {
            out.println("<html><body>");
            out.println("<h2>Database error</h2>");
            out.println("<p>Error message: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to main page</a>");
            out.println("</body></html>");
            e.printStackTrace();
            return;
        }

        out.println("<html><head><title>Student Details</title></head><body>");
        out.println("<h2>Student Details</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Name</th></tr>");
        for (Student student : students) {
            out.println("<tr>");
            out.println("<td>" + student.getId() + "</td>");
            out.println("<td>" + student.getName() + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("<br><a href='index.html'>Back to CRUD Operations</a>");
        out.println("</body></html>");
    }
}