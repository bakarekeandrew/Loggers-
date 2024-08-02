//package Demo;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.*;
//
//@WebServlet("/submitForm")  // Annotation to map the servlet to the URL pattern /submitForm
//public class NewClass extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    public NewClass() {
//        super();
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // Extract form parameters
//        String idStr = request.getParameter("id");
//
//        // Convert idStr to integer
//        int id = 0;
//        try {
//            id = Integer.parseInt(idStr);
//        } catch (NumberFormatException e) {
//            response.getWriter().write("Invalid ID format.<br>");
//            return;
//        }
//
//        // Database connection and query
//        Connection con = null;
//        PreparedStatement pst = null;
//        ResultSet rs = null;
//
//        try {
//            String url = "jdbc:postgresql://localhost:5432/best_pr_db";
//            String username = "postgres";
//            String password = "123";
//
//            Class.forName("org.postgresql.Driver");
//            con = DriverManager.getConnection(url, username, password);
//
//            pst = con.prepareStatement("SELECT name FROM student WHERE id = ?");
//            pst.setInt(1, id);
//            rs = pst.executeQuery();
//
//            if (rs.next()) {
//                String retrievedName = rs.getString("name");
//                response.getWriter().print("<h1>Student name is " + retrievedName + " and id is " + id + "</h1>");
//            } else {
//                response.getWriter().print("<h1>No student found with id " + id + "</h1>");
//            }
//        } catch (ClassNotFoundException e) {
//            response.getWriter().write("Database driver not found.<br>");
//            e.printStackTrace();
//        } catch (SQLException e) {
//            response.getWriter().write("Database error occurred.<br>");
//            e.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (pst != null) pst.close();
//                if (con != null) con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}

























