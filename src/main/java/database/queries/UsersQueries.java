package database.queries;

import database.entities.UsersEntity;
import helpers.DatabaseHelper;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testng.Assert.fail;

public class UsersQueries {

    public static final UsersQueries INSTANCE = new UsersQueries();
    private final String SELECT_SQL = "SELECT %s FROM users";
    private final String INSERT_SQL = "INSERT INTO users";

    public void addUser(User user) {
        try (Connection connect = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connect.prepareStatement(INSERT_SQL + " (name, email, gender, status) VALUES(?, ?, ?, ?)")) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getGender());
            preparedStatement.setString(4, user.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            fail("Error: " + e.getMessage());
        }
    }

    public UsersEntity getUserByName(String userName) {
        UsersEntity user = new UsersEntity();
        try (Connection connect =  DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connect.prepareStatement(String.format(SELECT_SQL, "*") +
                     " WHERE name = ?")) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setGender(resultSet.getString("gender"));
                user.setStatus(resultSet.getString("status"));
            } else return null;
        } catch (SQLException e) {
            fail("Error: " + e.getMessage());
        }
        return user;
    }
}
