package com.expensetracker.dao;

import com.expensetracker.model.User;
import com.expensetracker.util.FirebaseConfig;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for User operations with Firebase Firestore.
 * Collection path: "users/{userId}"
 */
public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private static final String USERS_COLLECTION = "users";

    public UserDAO() {
    }

    private Firestore getDb() {
        return FirebaseConfig.getFirestore();
    }

    /**
     * Creates a new user document in Firestore.
     * @param user The user object to persist.
     * @return true if created successfully, false otherwise.
     */
    public boolean createUser(User user) {
        if (user == null || user.getUserId() == null) {
            return false;
        }

        Firestore db = getDb();
        if (db == null) {
            LOGGER.severe("Firestore is not initialized. Cannot create user.");
            return false;
        }

        try {
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getUserId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail().toLowerCase().trim());
            userData.put("passwordHash", user.getPasswordHash());
            userData.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt() : new Date());

            DocumentReference docRef = db.collection(USERS_COLLECTION).document(user.getUserId());
            ApiFuture<WriteResult> result = docRef.set(userData);
            result.get(); // Blocks until write is complete
            LOGGER.info("User created successfully with ID: " + user.getUserId());
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Finds a user by their email address.
     * @param email The user's email address.
     * @return User object if found, or null.
     */
    public User findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        Firestore db = getDb();
        if (db == null) {
            LOGGER.severe("Firestore is not initialized. Cannot find user by email.");
            return null;
        }

        try {
            CollectionReference users = db.collection(USERS_COLLECTION);
            Query query = users.whereEqualTo("email", email.toLowerCase().trim()).limit(1);
            ApiFuture<QuerySnapshot> querySnapshot = query.get();

            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            if (!documents.isEmpty()) {
                QueryDocumentSnapshot doc = documents.get(0);
                User user = new User();
                user.setUserId(doc.getString("userId"));
                user.setName(doc.getString("name"));
                user.setEmail(doc.getString("email"));
                user.setPasswordHash(doc.getString("passwordHash"));
                user.setCreatedAt(doc.getDate("createdAt"));
                return user;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by email: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Finds a user by their unique User ID.
     * @param userId The unique User ID.
     * @return User object if found, or null.
     */
    public User findUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }

        Firestore db = getDb();
        if (db == null) {
            return null;
        }

        try {
            DocumentReference docRef = db.collection(USERS_COLLECTION).document(userId);
            DocumentSnapshot snapshot = docRef.get().get();
            if (snapshot.exists()) {
                User user = new User();
                user.setUserId(snapshot.getString("userId"));
                user.setName(snapshot.getString("name"));
                user.setEmail(snapshot.getString("email"));
                user.setPasswordHash(snapshot.getString("passwordHash"));
                user.setCreatedAt(snapshot.getDate("createdAt"));
                return user;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by ID: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Checks whether an email is already registered in the system.
     */
    public boolean isEmailRegistered(String email) {
        return findUserByEmail(email) != null;
    }
}
