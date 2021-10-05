import java.sql.*;

public class Database {
    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;
    public static String sqlQuery;

    public Database(String dbURL, String dbUsername, String dbPassword) {
        try {
            connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewUser(int id) {
        try {
            sqlQuery = "insert into user "
                    + " (id, active, email, first_name, loginname, middle_name, password, path_to_avatar_image, surname)"
                    + " values ( %d, 1, 'testMail@gmail.com', 'testName', 'testLogin', 'testMiddlename', 'qwe123123',"
                    + " 'testPath.com', 'TestSurname')";
            statement.executeUpdate(String.format(sqlQuery, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewAddress(int id) {
        try {
            sqlQuery = "insert into address "
                    + " (id, address_nickname, city_name, postal_code, region_name, street, street_additional, user_id)"
                    + " values ( %d, 'testAddress', 'testCity', '20200', 'testRegion', 'testStreet', 'testAdditional', null)";
            statement.executeUpdate(String.format(sqlQuery, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewPaymentCard(int id) {
        try {
            sqlQuery = "insert into paymentcard"
                    + " (id, card_code, card_nick_name, card_number, expiration_date, owner_name, user_id)"
                    + " values ( %d, '312', 'testCardNickname', '343914624684393', '2022-12-20',  'testOwner', null)";
            statement.executeUpdate(String.format(sqlQuery, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int selectUserIdByLogin(String login) {
        try {
            sqlQuery = "SELECT id FROM user WHERE user.loginname='%s'";
            resultSet = statement.executeQuery(String.format(sqlQuery, login));
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void deleteById(String entity, int id) {
        try {
            sqlQuery = "DELETE FROM %s WHERE id=%d";
            statement.executeUpdate(String.format(sqlQuery, entity, id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String entity, int id) {
        boolean result = false;

        try {
            sqlQuery = "SELECT COUNT(1)"
                    + " FROM %s"
                    + " WHERE id = %d;";

            resultSet = statement.executeQuery(String.format(sqlQuery, entity, id));

            resultSet.next();
            if (resultSet.getInt(1) == 1)
                result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
