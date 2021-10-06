import java.sql.*;

public class DatabaseController {
    private final String dbURL;
    private final String dbUsername;
    private final String dbPassword;
    private String query;

    public DatabaseController(String dbURL, String dbUsername, String dbPassword) {
        this.dbURL = dbURL;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public void addNewUser(int id) {
        query = "insert into user "
                + " (id, active, email, first_name, loginname, middle_name, password, path_to_avatar_image, surname)"
                + " values ( ?, 1, 'testMail@gmail.com', 'testName', 'testLogin', 'testMiddlename', 'qwe123123',"
                + " 'testPath.com', 'TestSurname')";

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addNewAddress(int id) {
        query = "insert into address "
                + " (id, address_nickname, city_name, postal_code, region_name, street, street_additional, user_id)"
                + " values ( ?, 'testAddress', 'testCity', '20200', 'testRegion', 'testStreet', 'testAdditional', null)";

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addNewPaymentCard(int id) {
        query = "insert into paymentcard"
                + " (id, card_code, card_nick_name, card_number, expiration_date, owner_name, user_id)"
                + " values ( ?, '312', 'testCardNickname', '343914624684393', '2022-12-20',  'testOwner', null)";

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int selectUserIdByLogin(String login) {
        query = "SELECT id FROM user WHERE user.loginname=?";
        int result = -1;

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            rs.next();
            result = rs.getInt("id");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public void deleteById(String entity, int id) {
        query = "DELETE FROM ? WHERE id=?;";

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, entity);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean exists(String entity, int id) {
        query = "SELECT COUNT(1) FROM %s WHERE id=?;";
        query = String.format(query, entity);
        boolean result = false;

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) == 1)
                result = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
