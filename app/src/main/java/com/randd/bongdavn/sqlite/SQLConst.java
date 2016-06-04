package com.randd.bongdavn.sqlite;

/**
 * Created by Thanh Le on 09/05/16.
 */
public class SQLConst {
    public static final String SQL_GET_QUESTIONS =
            "SELECT * FROM (SELECT * FROM tblQuestion Where Level = '1' ORDER BY RANDOM() LIMIT 1) " +
            "UNION " +
            "SELECT * FROM (SELECT * FROM tblQuestion Where Level = '2' ORDER BY RANDOM() LIMIT 1) " +
            "UNION " +
            "SELECT * FROM (SELECT * FROM tblQuestion Where Level = '3' ORDER BY RANDOM() LIMIT 1) ";
    public static final String SQL_GET_CASE =
            "SELECT * FROM tblCase Where IDQuestion = ";
}
