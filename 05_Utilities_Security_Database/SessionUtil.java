package com.expensetracker.util;

import com.expensetracker.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session utility to handle user authentication state and session attributes.
 */
public class SessionUtil {
    public static final String SESSION_USER = "LOGGED_IN_USER";
    public static final String SESSION_USER_ID = "LOGGED_IN_USER_ID";
    public static final String FLASH_SUCCESS = "FLASH_SUCCESS";
    public static final String FLASH_ERROR = "FLASH_ERROR";

    private SessionUtil() {}

    /**
     * Stores user details in session upon successful authentication.
     */
    public static void setLoggedInUser(HttpServletRequest request, User user) {
        if (request == null || user == null) return;
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_USER, user);
        session.setAttribute(SESSION_USER_ID, user.getUserId());
    }

    /**
     * Retrieves the currently logged-in user from session.
     */
    public static User getLoggedInUser(HttpServletRequest request) {
        if (request == null) return null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object userObj = session.getAttribute(SESSION_USER);
            if (userObj instanceof User) {
                return (User) userObj;
            }
        }
        return null;
    }

    /**
     * Retrieves the currently logged-in user's unique ID.
     */
    public static String getLoggedInUserId(HttpServletRequest request) {
        if (request == null) return null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object idObj = session.getAttribute(SESSION_USER_ID);
            if (idObj instanceof String) {
                return (String) idObj;
            }
            User user = getLoggedInUser(request);
            if (user != null) {
                return user.getUserId();
            }
        }
        return null;
    }

    /**
     * Checks if a user is currently authenticated in the session.
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        return getLoggedInUserId(request) != null;
    }

    /**
     * Destroys the current HTTP session on logout.
     */
    public static void logout(HttpServletRequest request) {
        if (request == null) return;
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Sets a temporary flash message (e.g. success or error) in the session.
     */
    public static void setFlashMessage(HttpServletRequest request, String type, String message) {
        if (request == null) return;
        HttpSession session = request.getSession(true);
        if ("success".equalsIgnoreCase(type)) {
            session.setAttribute(FLASH_SUCCESS, message);
        } else {
            session.setAttribute(FLASH_ERROR, message);
        }
    }
}
