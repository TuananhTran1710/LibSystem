package com.jmc.libsystem.QueryDatabase;

import com.jmc.libsystem.Information.Admin;
import com.jmc.libsystem.Information.User;
import com.jmc.libsystem.Models.DatabaseDriver;
import com.jmc.libsystem.Views.AccountType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryAccountData {
    // User Section
    public static ResultSet getDataForLogin(String email, String password, AccountType type) {
        ResultSet resultSet = null;
        try {
            String tableName = "user";
            if (type == AccountType.ADMIN) tableName = "admin";
            String query = "SELECT * FROM " + tableName + " WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    //lam the nay tuc la tren csdl, cac email phai khac nhau (PK) --> Can chinh lai table trong csdl
    public static ResultSet getUserDataForSignUp(String email, String id) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM user WHERE email = ?";
            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet getDataForForgetPassword(String email, String id, AccountType type) {
        ResultSet resultSet = null;
        try {
            String query;
            if (type == AccountType.ADMIN) {
                query = "SELECT * FROM admin WHERE email = ? and admin_id = ?";
            } else query = "SELECT * FROM user WHERE email = ? and user_id = ?";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, id);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getCountUser(){
        ResultSet resultSet = null;
        try {
            String query = "SELECT COUNT(*) as count FROM user;";

            PreparedStatement preparedStatement = DatabaseDriver.getConn().prepareStatement(query);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    //cập nhật thông tin user, dùng cho user profile
    public static void updateUserInfo(User user) {
        String query = "UPDATE user SET fullName = ?, email = ? WHERE user_id = ?";
        try (PreparedStatement stmt = DatabaseDriver.getConn().prepareStatement(query)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //cập nhật mật khẩu user, dùng cho user profile
    public static void updateUserPassword(User user) {
        String query = "UPDATE user SET password = ?";
        try (PreparedStatement stmt = DatabaseDriver.getConn().prepareStatement(query)) {
            stmt.setString(1, user.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //kiểm tra xem email đã tồn tại chưa, dùng cho user profile
    public static boolean isUserEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement stmt = DatabaseDriver.getConn().prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //cập nhật thông tin admin, dùng cho admin profile
    public static void updateAdminInfo(Admin admin) {
        String query = "UPDATE admin SET fullName = ?, email = ? WHERE admin_id = ?";
        try (PreparedStatement stmt = DatabaseDriver.getConn().prepareStatement(query)) {
            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //cập nhật mật khẩu admin, dùng cho admin profile
    public static void updateAdminPassword(Admin admin) {
        String query = "UPDATE admin SET password = ?";
        try (PreparedStatement stmt = DatabaseDriver.getConn().prepareStatement(query)) {
            stmt.setString(1, admin.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //kiểm tra xem email đã tồn tại chưa, dùng cho admin profile
    public static boolean isAdminEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM admin WHERE email = ?";
        try (PreparedStatement stmt = DatabaseDriver.getConn().prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
