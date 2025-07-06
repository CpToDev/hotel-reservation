package com.reservation.db;

import com.reservation.model.User;

import java.io.*;
import java.util.*;

public class UserRepository {
    private static final String FILE = "users.txt";

    public static Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String email = parts[0];
                    String passwordHash = parts[1];
                    users.put(email, new User(email, passwordHash));
                }
            }
        } catch (IOException e) {
            // ignore file not found or empty
        }
        return users;
    }

    public static void saveUsers(Map<String, User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (User user : users.values()) {
                bw.write(user.getEmail() + "," + user.getPasswordHash());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}