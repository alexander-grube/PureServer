package org.example.utils;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ResultSetUtil {

    public static ArrayList<String> getArrayListFromResultSet(ResultSet resultSet, String columnName) throws SQLException {
        Array sqlArray = resultSet.getArray(columnName);
        if (sqlArray == null) {
            return new ArrayList<>();
        }
        String[] stringArray = (String[]) sqlArray.getArray();
        return new ArrayList<>(Arrays.asList(stringArray));
    }
}