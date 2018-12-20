package constants;

/**
 * Description: This class contains the sql-queries to the current database. The queries are formatted in constant view.
 * <p>
 * Created by Yaroslav Bodyak on 11.12.2018.
 */
public class QueriesDB {

    /*Queries to work with user database table.*/
    public static final String CHECK_AUTHORIZATION = "SELECT login, password FROM user WHERE login = ? AND password = ?";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM user JOIN user_type ON user.user_type_id = \n" +
            "    user_type.id  WHERE login = ? ";
    public static final String GET_USER_BY_ID = "SELECT * FROM user WHERE id = ?";
    public static final String ADD_USER_CLIENT = "INSERT INTO user (first_name, sur_name, login, password, user_type_id)" +
            "VALUES (?, ?, ?, ?, ?)";
    public static final String DELETE_USER_BY_ID = "DELETE FROM user WHERE id = ?";
    public static final String UPDATE_USER_BY_ID = "UPDATE user SET first_name = ?, sur_name = ? " +
            "login = ?, password = ?, user_type = ?  WHERE id = ?";
    public static final String GET_ALL_USERS = "SELECT * FROM user";
    /*Queries to work with userType database table.*/
    public static final String ADD_USER_TYPE = "INSERT INTO user_type (name) VALUES (?);";
    public static final String UPDATE_USER_TYPE_BY_ID = "UPDATE user_type SET name=? WHERE id=?;";
    public static final String DELETE_USER_TYPE_BY_ID = "DELETE FROM user_type WHERE id = ?";
    public static final String GET_USER_TYPE_BY_ID = "SELECT * FROM user_type WHERE id = ?";
    public static final String GET_ALL_USERS_TYPE = "SELECT * FROM user_type";
    public static final String GET_USER_TYPE_BY_TYPE = "SELECT * FROM user_type WHERE name = ?";

    /*Queries to work with activity database table.*/
    public static final String ADD_ACTIVITY = "INSERT INTO activity (name) VALUES (?);";
    public static final String UPDATE_ACTIVITY_BY_ID = "UPDATE activity SET name=? WHERE id=?;";
    public static final String DELETE_ACTIVITY_BY_ID = "DELETE FROM activity WHERE id = ?";
    public static final String GET_ACTIVITY_BY_ID = "SELECT * FROM activity WHERE id = ?";
    public static final String GET_ALL_ACTIVITIES = "SELECT * FROM activity";
    /*Queries to work with tracking database table.*/
    public static final String DELETE_TRACKING_BY_USER_ID = "DELETE FROM tracking WHERE use_id = ?";
    public static final String DELETE_TRACKING_BY_ID = "DELETE FROM tracking WHERE id = ?";
    public static final String GET_TRACKING_BY_ID = "SELECT * FROM tracking JOIN user ON tracking.user_id = user.id\n" +
            "    JOIN activity ON tracking.activity_id = activity.id \n" +
            "    JOIN status ON tracking.status_id = status.id \n" +
            "    JOIN user_type ON user.user_type_id = user_type.id WHERE id = ?";
    public static final String UPDATE_TRACKING_STATUS_AND_TIME_BY_ID = "UPDATE tracking SET status_id = ?, time = ? " +
            "WHERE id=?;";
    public static final String UPDATE_TRACKING_STATUS_BY_ID = "UPDATE tracking SET status_id = ? WHERE id=?;";
    public static final String ADD_TRACKING = "INSERT INTO tracking (user_id, activity_id, status_id, time)" +
            "VALUES (?, ?, ?, ?)";
    public static final String GET_ALL_TRACKING = "SELECT * FROM tracking JOIN user ON tracking.user_id = user.id\n" +
            "    JOIN activity ON tracking.activity_id = activity.id \n" +
            "    JOIN status ON tracking.status_id = status.id \n" +
            "    JOIN user_type ON user.user_type_id = user_type.id ;";

}
