package commands.implementations.admin;

import commands.BasicCommand;
import constants.MessageConstants;
import constants.Parameters;
import constants.PathPageConstants;
import entities.Activity;
import manager.ConfigManagerPages;
import org.apache.log4j.Logger;
import services.ActivityService;
import services.AdminService;
import services.UserService;
import tag.MyTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: This describes actions of registration new user.
 * <p>
 * Created by Yaroslav Bodyak on 11.12.2018.
 */
public class CreateActivityCommand implements BasicCommand {
    private static final Logger logger = Logger.getLogger(CreateActivityCommand.class);

    /**
     * This method describes the adding new activities logic.
     * The method uses methods of the RequestParameterIdentifier and AdminService.
     *
     * @param request - request which will be processed.
     * @return - a page which user will be directed to.
     */
    @Override
    public String execute(HttpServletRequest request) {
        String page;
        HttpSession session = request.getSession(false);
        Activity activity = AdminService.getInstance().geActivityFromRequest(request);
        try {
            if (AdminService.getInstance().areFieldsFilled(request)) {
                if (ActivityService.getInstance().isUniqueActivity(activity)) {
                    ActivityService.getInstance().createActivityDB(activity);
                    List<Activity> activityAdminList = ActivityService.getInstance().getAllActivities();
                    MyTag.activityList = activityAdminList;
                    UserService.getInstance().setAttributeToSession(activityAdminList, session);
                    page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ADMIN_PAGE_PATH);
                    logger.info(MessageConstants.SUCCESS_CREATION);
                } else {
                    request.setAttribute(Parameters.OPERATION_MESSAGE, MessageConstants.ACTIVITY_EXISTS);
                    page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ADMIN_PAGE_PATH);
                }
            } else {
                request.setAttribute(Parameters.OPERATION_MESSAGE, MessageConstants.EMPTY_FIELDS_ACTIVITY);
                page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ADMIN_PAGE_PATH);
            }
        } catch (SQLException e) {
            request.setAttribute(Parameters.ERROR_DATABASE, MessageConstants.DATABASE_ACCESS_ERROR);
            page = ConfigManagerPages.getInstance().getProperty(PathPageConstants.ERROR_PAGE_PATH);
            logger.error(MessageConstants.DATABASE_ACCESS_ERROR);
        }
        return page;
    }
}