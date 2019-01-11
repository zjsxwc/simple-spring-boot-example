package hello;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

import com.mysql.jdbc.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public Test test;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/db-test")
    public String dbTest(@RequestParam(value = "name", defaultValue = "World") String name, HttpServletRequest request,HttpServletResponse response) {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.33.77:3306/gfyh";
        String username = "root";
        String password = "root";

        Connection conn;
        PreparedStatement pstmt;
        String sql = "select * from account limit 20";
        String output = "";
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
            pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            output = "";
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    output += rs.getString(i) + "\t";
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                        output += "\t";
                    }
                }
                output += "\n";
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        response.addHeader("Foook", "baaar");

        test.say(output + request.getMethod());
        return counter.incrementAndGet() + ">>" + output;
    }
}