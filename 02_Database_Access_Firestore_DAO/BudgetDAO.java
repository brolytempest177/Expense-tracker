package com.expensetracker.dao;

import com.expensetracker.model.Budget;
import com.expensetracker.util.FirebaseConfig;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Budget operations with Firebase Firestore.
 * Sub-collection path: "users/{userId}/budgets/{budgetId}"
 * Document ID format: "{year}-{month}" (e.g., "2026-9")
 */
public class BudgetDAO {
    private static final Logger LOGGER = Logger.getLogger(BudgetDAO.class.getName());
    private static final String USERS_COLLECTION = "users";
    private static final String BUDGETS_SUBCOLLECTION = "budgets";

    public BudgetDAO() {
    }

    private Firestore getDb() {
        return FirebaseConfig.getFirestore();
    }

    private CollectionReference getUserBudgetsRef(String userId) {
        Firestore db = getDb();
        if (db == null || userId == null || userId.trim().isEmpty()) {
            return null;
        }
        return db.collection(USERS_COLLECTION).document(userId).collection(BUDGETS_SUBCOLLECTION);
    }

    /**
     * Sets or updates a monthly budget for a user.
     */
    public boolean setOrUpdateBudget(Budget budget) {
        if (budget == null || budget.getUserId() == null) {
            return false;
        }

        CollectionReference budgetsRef = getUserBudgetsRef(budget.getUserId());
        if (budgetsRef == null) {
            return false;
        }

        try {
            String docId = budget.getYear() + "-" + budget.getMonth();
            budget.setId(docId);

            Map<String, Object> data = new HashMap<>();
            data.put("id", docId);
            data.put("userId", budget.getUserId());
            data.put("month", budget.getMonth());
            data.put("year", budget.getYear());
            data.put("amount", budget.getAmount());
            data.put("updatedAt", budget.getUpdatedAt() != null ? budget.getUpdatedAt() : new Date());

            DocumentReference docRef = budgetsRef.document(docId);
            ApiFuture<WriteResult> result = docRef.set(data);
            result.get();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting budget for user " + budget.getUserId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves the budget for a specific month and year.
     */
    public Budget getBudget(String userId, int month, int year) {
        CollectionReference budgetsRef = getUserBudgetsRef(userId);
        if (budgetsRef == null) {
            return null;
        }

        try {
            String docId = year + "-" + month;
            DocumentReference docRef = budgetsRef.document(docId);
            DocumentSnapshot snapshot = docRef.get().get();
            if (snapshot.exists()) {
                return mapDocumentToBudget(snapshot);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting budget for " + year + "-" + month + ": " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Retrieves all configured budgets for a user.
     */
    public List<Budget> getAllBudgets(String userId) {
        List<Budget> list = new ArrayList<>();
        CollectionReference budgetsRef = getUserBudgetsRef(userId);
        if (budgetsRef == null) {
            return list;
        }

        try {
            ApiFuture<QuerySnapshot> future = budgetsRef.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                Budget b = mapDocumentToBudget(doc);
                if (b != null) {
                    list.add(b);
                }
            }
            list.sort((a, b) -> {
                if (a.getYear() != b.getYear()) {
                    return Integer.compare(b.getYear(), a.getYear());
                }
                return Integer.compare(b.getMonth(), a.getMonth());
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all budgets for user " + userId + ": " + e.getMessage(), e);
        }
        return list;
    }

    private Budget mapDocumentToBudget(DocumentSnapshot doc) {
        try {
            Budget budget = new Budget();
            budget.setId(doc.getId());
            budget.setUserId(doc.getString("userId"));
            Long monthVal = doc.getLong("month");
            budget.setMonth(monthVal != null ? monthVal.intValue() : 0);
            Long yearVal = doc.getLong("year");
            budget.setYear(yearVal != null ? yearVal.intValue() : 0);

            Double amt = doc.getDouble("amount");
            if (amt == null) {
                Long longAmt = doc.getLong("amount");
                amt = longAmt != null ? longAmt.doubleValue() : 0.0;
            }
            budget.setAmount(amt);
            budget.setUpdatedAt(doc.getDate("updatedAt"));
            return budget;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error mapping document to Budget: " + e.getMessage(), e);
            return null;
        }
    }
}
