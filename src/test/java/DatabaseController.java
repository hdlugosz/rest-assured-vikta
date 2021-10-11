import Entity.Address;
import Entity.PaymentCard;
import Entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DatabaseController {
    private static final Logger logger = LogManager.getLogger(DatabaseController.class);

    private final String dbURL;
    private final String dbUsername;
    private final String dbPassword;
    private String query;

    public DatabaseController(String dbURL, String dbUsername, String dbPassword) {
        this.dbURL = dbURL;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    public void addNewUser(User user) {
        query = "INSERT INTO user "
                + " (id, active, email, first_name, loginname, middle_name, password, path_to_avatar_image, surname)"
                + " VALUES ( ?, 1, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, user.getId());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLoginName());
            ps.setString(5, user.getMiddleName());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getPathToAvatarImage());
            ps.setString(8, user.getSurname());

            ps.executeUpdate();

            logger.debug("created user: " + user.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addNewAddress(Address address) {
        query = "INSERT INTO address "
                + " (id, address_nickname, city_name, postal_code, region_name, street, street_additional, user_id)"
                + " VALUES ( ?, ?, ?, ?, ?, ?, ?, null)";

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, address.getId());
            ps.setString(2, address.getAddressNickname());
            ps.setString(3, address.getCityName());
            ps.setString(4, address.getPostalCode());
            ps.setString(5, address.getRegionName());
            ps.setString(6, address.getStreet());
            ps.setString(7, address.getStreetAdditional());

            ps.executeUpdate();

            logger.debug("created address: " + address.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addNewPaymentCard(PaymentCard paymentCard) {
        query = "INSERT INTO paymentcard"
                + " (id, card_code, card_nick_name, card_number, expiration_date, owner_name, user_id)"
                + " VALUES ( ?, ?, ?, ?, ?, ?, null)";

        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, paymentCard.getId());
            ps.setString(2, paymentCard.getCardCode());
            ps.setString(3, paymentCard.getCardNickName());
            ps.setString(4, paymentCard.getCardNumber());
            ps.setString(5, paymentCard.getExpirationDate());
            ps.setString(6, paymentCard.getOwnerName());

            ps.executeUpdate();

            logger.debug("created payment card: " + paymentCard.getId());
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

    public void deleteUserById(int id) {
        deleteById("DELETE FROM user WHERE id=?;", "user", id);
    }

    public void deleteAddressById(int id) {
        deleteById("DELETE FROM address WHERE id=?;", "address", id);
    }

    public void deletePaymentCardById(int id) {
        deleteById("DELETE FROM paymentcard WHERE id=?;", "payment card", id);
    }

    private void deleteById(String query, String entity, int id) {
        try (Connection con = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();

            logger.debug("deleted " + entity + ": " + id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean existsUser(int id) {
        return exists("SELECT COUNT(1) FROM user WHERE id=?;", id);
    }

    public boolean existsAddress(int id) {
        return exists("SELECT COUNT(1) FROM address WHERE id=?;", id);
    }

    public boolean existsPaymentCard(int id) {
        return exists("SELECT COUNT(1) FROM paymentcard WHERE id=?;", id);
    }

    private boolean exists(String query, int id) {
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
