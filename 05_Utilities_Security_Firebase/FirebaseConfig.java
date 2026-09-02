package com.expensetracker.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Firebase Firestore Configuration and Initialization Utility.
 * 
 * Securely loads credentials from:
 * 1. Environment variable: FIREBASE_CONFIG_PATH
 * 2. System property: firebase.config.path
 * 3. Classpath: /firebase-service-account.json
 * 4. Application root / working directory: firebase-service-account.json
 * 
 * Fails gracefully with informative setup guidance if configuration is absent.
 */
public class FirebaseConfig {
    private static final Logger LOGGER = Logger.getLogger(FirebaseConfig.class.getName());
    private static Firestore firestoreInstance;
    private static boolean initialized = false;
    private static String initializationError = null;

    private FirebaseConfig() {
        // Private constructor for singleton utility
    }

    /**
     * Initializes FirebaseApp and Firestore instance with thread safety.
     */
    public static synchronized void initialize() {
        if (initialized && firestoreInstance != null) {
            return;
        }

        try {
            if (!FirebaseApp.getApps().isEmpty()) {
                firestoreInstance = FirestoreClient.getFirestore();
                initialized = true;
                LOGGER.info("Firebase already initialized. Firestore client attached successfully.");
                return;
            }

            InputStream serviceAccountStream = getServiceAccountInputStream();

            if (serviceAccountStream == null) {
                initializationError = "Firebase service account JSON not found. " +
                        "Please set the FIREBASE_CONFIG_PATH environment variable or place firebase-service-account.json in the classpath.";
                LOGGER.log(Level.WARNING, "--------------------------------------------------");
                LOGGER.log(Level.WARNING, initializationError);
                LOGGER.log(Level.WARNING, "--------------------------------------------------");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            FirebaseApp.initializeApp(options);
            firestoreInstance = FirestoreClient.getFirestore();
            initialized = true;
            initializationError = null;
            LOGGER.info("Firebase Firestore initialized successfully!");

        } catch (Exception e) {
            initializationError = "Failed to initialize Firebase: " + e.getMessage();
            LOGGER.log(Level.SEVERE, initializationError, e);
        }
    }

    /**
     * Resolves the InputStream for the Firebase Service Account JSON file.
     */
    private static InputStream getServiceAccountInputStream() {
        try {
            // 1. Check environment variable
            String envPath = System.getenv("FIREBASE_CONFIG_PATH");
            if (envPath != null && !envPath.trim().isEmpty()) {
                File file = new File(envPath.trim());
                if (file.exists() && file.isFile()) {
                    LOGGER.info("Loading Firebase credentials from FIREBASE_CONFIG_PATH: " + envPath);
                    return new FileInputStream(file);
                }
            }

            // 2. Check system property
            String sysProp = System.getProperty("firebase.config.path");
            if (sysProp != null && !sysProp.trim().isEmpty()) {
                File file = new File(sysProp.trim());
                if (file.exists() && file.isFile()) {
                    LOGGER.info("Loading Firebase credentials from system property: " + sysProp);
                    return new FileInputStream(file);
                }
            }

            // 3. Check classpath
            InputStream cpStream = FirebaseConfig.class.getResourceAsStream("/firebase-service-account.json");
            if (cpStream != null) {
                LOGGER.info("Loading Firebase credentials from classpath /firebase-service-account.json");
                return cpStream;
            }

            // 4. Check working directory / local root
            File localFile = new File("firebase-service-account.json");
            if (localFile.exists() && localFile.isFile()) {
                LOGGER.info("Loading Firebase credentials from local file: " + localFile.getAbsolutePath());
                return new FileInputStream(localFile);
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error reading Firebase credentials stream: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Retrieves the active Firestore database instance.
     */
    public static Firestore getFirestore() {
        if (!initialized || firestoreInstance == null) {
            initialize();
        }
        return firestoreInstance;
    }

    public static boolean isInitialized() {
        return initialized && firestoreInstance != null;
    }

    public static String getInitializationError() {
        return initializationError;
    }
}
