package Demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/best_pr_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "123";
    
    private static final Logger logger = LogManager.getLogger(StudentServlet.class);

    public StudentServlet() {
        super();
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("PostgreSQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load PostgreSQL JDBC Driver", e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Received GET request");
        String action = request.getPathInfo();
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "/read":
                readStudent(request, response);
                break;
            default:
                logger.warn("Invalid action for GET request: {}", action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action for GET request");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Received POST request");
        String action = request.getPathInfo();
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "/create":
                createStudent(request, response);
                break;
            case "/update":
                updateStudent(request, response);
                break;
            case "/delete":
                deleteStudent(request, response);
                break;
            default:
                logger.warn("Invalid action for POST request: {}", action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action for POST request");
        }
    }

    private void createStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Entering createStudent method");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        logger.info("Attempting to create student with name: {}", name);

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO student (name) VALUES (?) RETURNING id";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, name);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    logger.info("Student created successfully with ID: {}", id);
                    out.println("<html><body>");
                    out.println("<h2>Student created successfully</h2>");
                    out.println("<p>ID: " + id + "</p>");
                    out.println("<p>Name: " + name + "</p>");
                    out.println("<a href='index.html'>Back to main page</a>");
                    out.println("</body></html>");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating student: {}", e.getMessage(), e);
            out.println("<html><body>");
            out.println("<h2>Error creating student</h2>");
            out.println("<p>Error message: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to main page</a>");
            out.println("</body></html>");
        }
        logger.debug("Exiting createStudent method");
    }

    private void readStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Entering readStudent method");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String idStr = request.getParameter("id");
        logger.info("Attempting to read student with ID: {}", idStr);

        try {
            int id = Integer.parseInt(idStr);
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT name FROM student WHERE id = ?";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setInt(1, id);
                    ResultSet rs = pst.executeQuery();
                    out.println("<html><body>");
                    if (rs.next()) {
                        String name = rs.getString("name");
                        logger.info("Student found: ID={}, Name={}", id, name);
                        out.println("<h2>Student found</h2>");
                        out.println("<p>ID: " + id + "</p>");
                        out.println("<p>Name: " + name + "</p>");
                    } else {
                        logger.warn("No student found with ID: {}", id);
                        out.println("<h2>No student found with ID: " + id + "</h2>");
                    }
                    out.println("<a href='index.html'>Back to main page</a>");
                    out.println("</body></html>");
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid ID format: {}", idStr, e);
            out.println("<html><body>");
            out.println("<h2>Invalid ID format</h2>");
            out.println("<a href='index.html'>Back to main page</a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            logger.error("Database error while reading student: {}", e.getMessage(), e);
            out.println("<html><body>");
            out.println("<h2>Database error</h2>");
            out.println("<p>Error message: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to main page</a>");
            out.println("</body></html>");
        }
        logger.debug("Exiting readStudent method");
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Entering updateStudent method");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        logger.info("Attempting to update student with ID: {}, New name: {}", idStr, name);

        try {
            int id = Integer.parseInt(idStr);
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "UPDATE student SET name = ? WHERE id = ?";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setString(1, name);
                    pst.setInt(2, id);
                    int rowsAffected = pst.executeUpdate();
                    out.println("<html><body>");
                    if (rowsAffected > 0) {
                        logger.info("Student updated successfully: ID={}, New Name={}", id, name);
                        out.println("<h2>Student updated successfully</h2>");
                        out.println("<p>ID: " + id + "</p>");
                        out.println("<p>New Name: " + name + "</p>");
                    } else {
                        logger.warn("No student found with ID: {}", id);
                        out.println("<h2>No student found with ID: " + id + "</h2>");
                    }
                    out.println("<a href='index.html'>Back to main page</a>");
                    out.println("</body></html>");
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid ID format: {}", idStr, e);
            out.println("<html><body>");
            out.println("<h2>Invalid ID format</h2>");
            out.println("<a href='index.html'>Back to main page</a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            logger.error("Database error while updating student: {}", e.getMessage(), e);
            out.println("<html><body>");
            out.println("<h2>Database error</h2>");
            out.println("<p>Error message: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to main page</a>");
            out.println("</body></html>");
        }
        logger.debug("Exiting updateStudent method");
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Entering deleteStudent method");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String idStr = request.getParameter("id");
        logger.info("Attempting to delete student with ID: {}", idStr);

        try {
            int id = Integer.parseInt(idStr);
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "DELETE FROM student WHERE id = ?";
                try (PreparedStatement pst = con.prepareStatement(sql)) {
                    pst.setInt(1, id);
                    int rowsAffected = pst.executeUpdate();
                    out.println("<html><body>");
                    if (rowsAffected > 0) {
                        logger.info("Student deleted successfully: ID={}", id);
                        out.println("<h2>Student deleted successfully</h2>");
                        out.println("<p>Deleted student with ID: " + id + "</p>");
                    } else {
                        logger.warn("No student found with ID: {}", id);
                        out.println("<h2>No student found with ID: " + id + "</h2>");
                    }
                    out.println("<a href='index.html'>Back to main page</a>");
                    out.println("</body></html>");
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid ID format: {}", idStr, e);
            out.println("<html><body>");
            out.println("<h2>Invalid ID format</h2>");
            out.println("<a href='index.html'>Back to main page</a>");
            out.println("</body></html>");
        } catch (SQLException e) {
            logger.error("Database error while deleting student: {}", e.getMessage(), e);
            out.println("<html><body>");
            out.println("<h2>Database error</h2>");
            out.println("<p>Error message: " + e.getMessage() + "</p>");
            out.println("<a href='index.html'>Back to main page</a>");
            out.println("</body></html>");
        }
        logger.debug("Exiting deleteStudent method");
    }
}