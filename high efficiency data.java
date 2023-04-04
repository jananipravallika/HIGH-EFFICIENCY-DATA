import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/u_register")
@MultipartConfig(maxFileSize = 16177215)    //
public class u_register extends HttpServlet {

    private String dbURL = "jdbc:mysql://localhost:3306/data_booster";
    private String dbUser = "root";
    private String dbPass = "root";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException , ClassNotFoundException {
       response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String userid = request.getParameter("userid");
        String pass = request.getParameter("pass");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");        
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String pincode = request.getParameter("pincode");
        
         
        InputStream inputStream = null; // input stream of the upload file
         
        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("pic");
        if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
             
            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }
         
        Connection conn = null; // connection to the database
        String message = null;  // message will be sent back to client
         
        try {
            // connects to the database
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
 
            // constructs SQL statement
            
            String query1="select * from user  where username='"+userid+"' or email='"+email+"' or mobile='"+mobile+"' ";
	    Statement st1=conn.createStatement();
	    ResultSet rs1=st1.executeQuery(query1);
            
            if(!rs1.next()){
            String sql = "insert into user"
        + " (username,password,email,mobile,dob,gender,address,pincode,status,image) "
                    + "values(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userid);
            statement.setString(2, pass);
            statement.setString(3, email);
            statement.setString(4, mobile);
            statement.setString(7, address);
            statement.setString(5, dob);
            statement.setString(6, gender);
            statement.setString(8, pincode);
            statement.setString(9, "waiting");
            
             
            if (inputStream != null) {
                // fetches input stream of the upload file for the blob column
                statement.setBlob(10, inputStream);
            }
             
 
            // sends the statement to the database server
            int row = statement.executeUpdate();
            if (row == 1) {
                
                response.sendRedirect("u_register.html?Registration_done_success");
                
            }
            }
            else
            {
               response.sendRedirect("index.html?Registration_failed");
            }
        } catch (SQLException ex) {
            
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                // closes the database connection
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}