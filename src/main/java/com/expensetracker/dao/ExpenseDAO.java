package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import com.expensetracker.util.FirebaseConfig;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Expense operations with Firebase Firestore.
 * Sub-collection path: "users/{userId}/expenses/{expenseId}"
 * 
 * Strict User Isolation: Every query and write is scoped under the specific user's document.
 */
public class ExpenseDAO {
    private static final Logger LOGGER = Logger.getLogger(ExpenseDAO.class.getName());
    private static final String USERS_COLLECTION = "users";
    private static final String EXPENSES_SUBCOLLECTION = "expenses";

    public ExpenseDAO() {
    }

    private Firestore getDb() {
        return FirebaseConfig.getFirestore();
    }

    private CollectionReference getUserExpensesRef(String userId) {
        Firestore db = getDb();
        if (db == null || userId == null || userId.trim().isEmpty()) {
            return null;
        }
        return db.collection(USERS_COLLECTION).document(userId).collection(EXPENSES_SUBCOLLECTION);
    }

    /**
     * Adds a new expense record for a user.
     */
    public boolean addExpense(Expense expense) {
        if (expense == null || expense.getUserId() == null) {
            return false;
        }

        CollectionReference expensesRef = getUserExpensesRef(expense.getUserId());
        if (expensesRef == null) {
            LOGGER.severe("Firestore is not initialized or invalid userId: " + expense.getUserId());
            return false;
        }

        try {
            String expenseId = expense.getId();
            if (expenseId == null || expenseId.trim().isEmpty()) {
                expenseId = UUID.randomUUID().toString();
                expense.setId(expenseId);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", expenseId);
            data.put("userId", expense.getUserId());
            data.put("amount", expense.getAmount());
            data.put("description", expense.getDescription());
            data.put("category", expense.getCategory());
            data.put("date", expense.getDate()); // YYYY-MM-DD
            data.put("notes", expense.getNotes() != null ? expense.getNotes() : "");
            data.put("createdAt", expense.getCreatedAt() != null ? expense.getCreatedAt() : new Date());

            DocumentReference docRef = expensesRef.document(expenseId);
            ApiFuture<WriteResult> result = docRef.set(data);
            result.get(); // Wait for completion
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding expense for user " + expense.getUserId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves an expense by its unique ID for a specific user.
     */
    public Expense getExpenseById(String userId, String expenseId) {
        CollectionReference expensesRef = getUserExpensesRef(userId);
        if (expensesRef == null || expenseId == null || expenseId.trim().isEmpty()) {
            return null;
        }

        try {
            DocumentReference docRef = expensesRef.document(expenseId);
            DocumentSnapshot snapshot = docRef.get().get();
            if (snapshot.exists()) {
                return mapDocumentToExpense(snapshot);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting expense " + expenseId + ": " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Retrieves all expenses for a user, sorted by date descending.
     */
    public List<Expense> getAllExpensesByUser(String userId) {
        List<Expense> list = new ArrayList<>();
        CollectionReference expensesRef = getUserExpensesRef(userId);
        if (expensesRef == null) {
            return list;
        }

        try {
            ApiFuture<QuerySnapshot> future = expensesRef.orderBy("date", Query.Direction.DESCENDING).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                Expense exp = mapDocumentToExpense(doc);
                if (exp != null) {
                    list.add(exp);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error querying expenses for user " + userId + ": " + e.getMessage(), e);
            // Fallback without ordering in case index is building
            try {
                ApiFuture<QuerySnapshot> fallbackFuture = expensesRef.get();
                List<QueryDocumentSnapshot> documents = fallbackFuture.get().getDocuments();
                for (QueryDocumentSnapshot doc : documents) {
                    Expense exp = mapDocumentToExpense(doc);
                    if (exp != null) {
                        list.add(exp);
                    }
                }
                list.sort((a, b) -> {
                    if (a.getDate() == null || b.getDate() == null) return 0;
                    return b.getDate().compareTo(a.getDate());
                });
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Fallback expense retrieval also failed: " + ex.getMessage(), ex);
            }
        }
        return list;
    }

    /**
     * Retrieves expenses for a specific month and year.
     * @param month 1-12
     * @param year e.g. 2026
     */
    public List<Expense> getExpensesByMonthAndYear(String userId, int month, int year) {
        List<Expense> allExpenses = getAllExpensesByUser(userId);
        List<Expense> filtered = new ArrayList<>();
        for (Expense exp : allExpenses) {
            if (exp.getMonth() == month && exp.getYear() == year) {
                filtered.add(exp);
            }
        }
        return filtered;
    }

    /**
     * Updates an existing expense record.
     */
    public boolean updateExpense(Expense expense) {
        if (expense == null || expense.getId() == null || expense.getUserId() == null) {
            return false;
        }

        CollectionReference expensesRef = getUserExpensesRef(expense.getUserId());
        if (expensesRef == null) {
            return false;
        }

        try {
            DocumentReference docRef = expensesRef.document(expense.getId());
            Map<String, Object> data = new HashMap<>();
            data.put("amount", expense.getAmount());
            data.put("description", expense.getDescription());
            data.put("category", expense.getCategory());
            data.put("date", expense.getDate());
            data.put("notes", expense.getNotes() != null ? expense.getNotes() : "");

            ApiFuture<WriteResult> result = docRef.update(data);
            result.get();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating expense " + expense.getId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deletes an expense by ID for a specific user.
     */
    public boolean deleteExpense(String userId, String expenseId) {
        CollectionReference expensesRef = getUserExpensesRef(userId);
        if (expensesRef == null || expenseId == null || expenseId.trim().isEmpty()) {
            return false;
        }

        try {
            DocumentReference docRef = expensesRef.document(expenseId);
            ApiFuture<WriteResult> result = docRef.delete();
            result.get();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting expense " + expenseId + ": " + e.getMessage(), e);
            return false;
        }
    }

    private Expense mapDocumentToExpense(DocumentSnapshot doc) {
        try {
            Expense expense = new Expense();
            expense.setId(doc.getString("id") != null ? doc.getString("id") : doc.getId());
            expense.setUserId(doc.getString("userId"));
            Double amt = doc.getDouble("amount");
            if (amt == null) {
                Long longAmt = doc.getLong("amount");
                amt = longAmt != null ? longAmt.doubleValue() : 0.0;
            }
            expense.setAmount(amt);
            expense.setDescription(doc.getString("description"));
            expense.setCategory(doc.getString("category"));
            expense.setDate(doc.getString("date"));
            expense.setNotes(doc.getString("notes"));
            expense.setCreatedAt(doc.getDate("createdAt"));
            return expense;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error mapping document to Expense: " + e.getMessage(), e);
            return null;
        }
    }
}
