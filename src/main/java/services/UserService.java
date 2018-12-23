package services;

import connection.ConnectionPool;
import connection.TransactionHandler;
import constants.MessageConstants;
import constants.Parameters;
import constants.QueriesDB;
import dao.daofactory.DaoFactory;
import dao.interfacesdao.UserDAO;
import entities.User;
import exceptions.DAOException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: This class describes actions on the user object.
 * This class contains methods that implement work with transaction support.
 * <p>
 * Created by Yaroslav Bodyak on 11.12.2018.
 */
public class UserService {

    private final static Logger logger = Logger.getLogger(UserService.class);
    private volatile static UserService instance;
    private DaoFactory mySqlFactory;
    private UserDAO userDAO;

    private UserService() {
        mySqlFactory = DaoFactory.getDaoFactory(DaoFactory.MYSQL);
        userDAO = mySqlFactory.getUserDao();
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return - an instance of the class.
     */
    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    return instance = new UserService();
                }
            }
        }
        return instance;
    }

    /**
     * This method checks if the user's login and password are correct. This method implements work with transaction support.
     *
     * @param login    - incoming user's login.
     * @param password - incoming user's password.
     * @return - boolean value of the condition if the user is authorized or not.
     * @throws SQLException
     */
    public boolean checkUserAuthorization(String login, String password) throws SQLException {
        final boolean[] isAuthorized = new boolean[1];
        TransactionHandler.runInTransaction(connection ->
                isAuthorized[0] = userDAO.isAuthorized(login, password, connection)
        );
        return isAuthorized[0];
    }

    /**
     * This method receives user object. This method implements work with transaction support.
     *
     * @param login - entered login.
     * @return - User object.
     */
    public User getUserByLogin(String login) throws SQLException {
        final User[] user = new User[1];
        TransactionHandler.runInTransaction(connection ->
                user[0] = userDAO.getByLogin(login, connection)
        );
        return user[0];
    }

    /**
     * This method updates user object. This method implements work with transaction support.
     *
     * @param user - an user which fields will be updated.
     * @throws SQLException
     */
    public void updateUser(User user) throws SQLException {
        TransactionHandler.runInTransaction(connection ->
                userDAO.update(user, connection)
        );
    }

    /**
     * This method checks the uniqueness of the user. This method implements work with transaction support.
     *
     * @param user - an user object with fields will be checked.
     * @return - boolean value of the condition.
     * @throws SQLException
     */
    public boolean isUniqueUser(User user) throws SQLException {
        final boolean[] isUnique = new boolean[1];
        TransactionHandler.runInTransaction(connection ->
                isUnique[0] = userDAO.checkUniqueUser(user.getLogin(), connection)
        );
        return isUnique[0];
    }

    /**
     * This method registers new user of application. This method implements work with transaction support.
     *
     * @param user - a new user which will be registered.
     * @throws SQLException
     */
    public void registerUser(User user) throws SQLException {
        TransactionHandler.runInTransaction(connection ->
                userDAO.add(user, connection)
        );
    }

    /**
     * An additional accessory method that provides work with some attributes of the object of http session.
     * This method sets user's parameters to the session.
     *
     * @param session - an object of the current session.
     */
    public void setAttributeToSession(User user, HttpSession session) {
        session.setAttribute(Parameters.USER, user);
        session.setAttribute(Parameters.USER_TYPE, String.valueOf(user.getUserType()));
    }
    /**
     * An additional overloaded accessory method that provides work with some attributes of the object of http session.
     * This method sets user's names parameters to the session.
     *
     * @param session - an object of the current session.
     */
    public void setAttributeToSession(List<String> userList, User user, HttpSession session) {
        session.setAttribute(Parameters.USER, user);
        session.setAttribute(Parameters.USER_TYPE, String.valueOf(user.getUserType()));
        session.setAttribute(Parameters.USER_LIST, userList);
    }

    /**
     * This method receives all client names from database. This method implements work with transaction support.
     *
     * @return - a list of activity names from the database.
     * @throws SQLException
     */
    public List<String> getAllClientNames() throws SQLException {
        final List<String>[] clientNameList = new List[1];
        TransactionHandler.runInTransaction(connection ->
                clientNameList[0] = getClientNames(connection)
        );
        return clientNameList[0];
    }

    /**
     * This method reads and returns information from all records (rows) of a database table.
     *
     * @param connection - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return - list of all entities from a database table.
     */
    public List<String> getAllNames(Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();
        List<String> userNames ;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_ALL_USERS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(userDAO.createUser(resultSet, new User()));
            }
            userNames = usersArrayGetNames(users);
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectionPool.closeResultSet(resultSet);
            ConnectionPool.closeStatement(statement);
        }
        return userNames;
    }

    /**
     * This method reads and returns clients names from database table.
     *
     * @param connection - the current connection to a database. Transmitted from the service module to provide transactions.
     * @return - list of all entities from a database table.
     */
    public List<String> getClientNames(Connection connection) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<User> clients = new ArrayList<>();
        List<String> userNames ;
        try {
            statement = connection.prepareStatement(QueriesDB.GET_ALL_CLIENTS);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                clients.add(userDAO.createUser(resultSet, new User()));
            }
            userNames = usersArrayGetNames(clients);
        } catch (SQLException e) {
            logger.error(MessageConstants.EXECUTE_QUERY_ERROR, e);
            throw new DAOException(MessageConstants.EXECUTE_QUERY_ERROR, e);
        } finally {
            ConnectionPool.closeResultSet(resultSet);
            ConnectionPool.closeStatement(statement);
        }
        return userNames;
    }

    List<String> usersArrayGetNames(List<User> activities) {
        List<String> clientNames = new ArrayList<>();
        for (int i = 0; i < activities.size(); i++) {
            clientNames.add(activities.get(i).getFirstName() + " " + activities.get(i).getSurName());
        }
        return clientNames;
    }

}