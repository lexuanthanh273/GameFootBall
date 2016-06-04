package com.randd.bongdavn.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.randd.bongdavn.models.Case;
import com.randd.bongdavn.models.Customer;
import com.randd.bongdavn.models.Question;
import com.randd.bongdavn.models.Ward;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Main on 16/01/16.
 */
public class DBManager {
    private static final String DB_PATH =
            Environment.getDataDirectory().getPath() + "/data/com.randd.bongdavn/databases";
    private static final String DB_NAME = "Question.db";
    private static final String TAG = "DBManager";
    String SQL_GET_QUESTIONS =
            "SELECT * FROM (SELECT * FROM tblQuestion Where Level = '1' ORDER BY RANDOM() LIMIT 1) " +
                    "UNION " +
                    "SELECT * FROM (SELECT * FROM tblQuestion Where Level = '2' ORDER BY RANDOM() LIMIT 1) ";
//                    "UNION " +
//                    "SELECT * FROM (SELECT * FROM tblQuestion Where Level = '3' ORDER BY RANDOM() LIMIT 1) ";
    private Context context;
    private SQLiteDatabase sqlDB;
    private ArrayList<Question> listQuestions = new ArrayList<>();
    private ArrayList<Case>     listCase = new ArrayList<>();
    private ArrayList<Ward>     listWard = new ArrayList<>();
    private ArrayList<Customer> listCustomer = new ArrayList<>();

    public DBManager(Context context) {
        this.context = context;
        copyDB();
        Log.e("FUCK PATH", DB_PATH);
    }

    private void copyDB() {
        try {
            new File(DB_PATH).mkdir();
            File file = new File(DB_PATH + "/" + DB_NAME);
            if (file.exists())
                return;
            file.createNewFile();

            InputStream input = context.getAssets().open(DB_NAME);
            FileOutputStream output = new FileOutputStream(file);
            int len;
            byte buff[] = new byte[1024];
            len = input.read(buff);
            while (len > 0) {
                output.write(buff, 0, len);
                len = input.read(buff);
            }
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDB() {
        if (sqlDB == null || !sqlDB.isOpen())
            sqlDB = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDB() {
        assert sqlDB != null;
        sqlDB.close();
        sqlDB = null;
    }

    public ArrayList<Question> getQuestions() {
        listQuestions.clear();
        openDB();
        Cursor cursor = sqlDB.rawQuery(SQL_GET_QUESTIONS, null);
        if (cursor == null) {
            return null;
        }

        String  QuestionName, TrueCase;
        int ID, Level;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ID = cursor.getInt(0);
            Level = cursor.getInt(1);
            QuestionName = cursor.getString(2);
            TrueCase = cursor.getString(3);
            Question question = new Question(ID, Level, QuestionName, TrueCase);
            listQuestions.add(question);
            cursor.moveToNext();
        }
        cursor.close();
        closeDB();
        return listQuestions;
    }

    public ArrayList<Case> getCases(int IDQuestion) {
        listCase.clear();
        openDB();
        Cursor cursor = sqlDB.rawQuery(SQLConst.SQL_GET_CASE + IDQuestion, null);
        if (cursor == null) {
            return null;
        }

        String  CaseName;
        int ID, IDTabQuestion, result;
        byte [] CaseImage;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ID = cursor.getInt(0);
            IDTabQuestion = cursor.getInt(3);
            CaseName = cursor.getString(2);
            CaseImage = cursor.getBlob(1);
            result = cursor.getInt(4);
            Case aCase = new Case(ID, IDTabQuestion, CaseName, CaseImage, result);
            listCase.add(aCase);
            cursor.moveToNext();
        }
        cursor.close();
        closeDB();
        return listCase;
    }

    public boolean savePerfomation(String name, String sdt, String email, String wardName) {
        openDB();
        ContentValues contentValues=new ContentValues();
        boolean resultl;
        contentValues.put("Name", name);
        contentValues.put("Phone", sdt);
        contentValues.put("Email",email);
        contentValues.put("WardName", wardName);
        resultl =sqlDB.insert("tblCustomer", null, contentValues) > 0;
        Log.e("FUCK SAVE CUSTOMER: ", resultl + "");
        closeDB();
        ArrayList<Customer> listCustomers;
        listCustomers = getAllCustomer();
        assert listCustomers != null;
        for (int i = 0;  i < listCustomers.size(); i++) {
            Log.e("FUCK CUSTOMER", listCustomers.get(i).toString());
        }
        return resultl;
    }

    private ArrayList<Customer> getAllCustomer() {
        listCustomer.clear();
        openDB();
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM tblCustomer", null);
        if (cursor == null) {
            return null;
        }

        String  name, email, phone, wardName;
        int ID;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ID = cursor.getInt(0);
            name = cursor.getString(1);
            phone = cursor.getString(2);
            email = cursor.getString(3);
            wardName = cursor.getString(4);
            Customer customer = new Customer(ID, name, phone, email, wardName);
            listCustomer.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        closeDB();
        return listCustomer;
    }

    public void upDateAllWard(int bong, int ao, int but, int caseIP6, int balo, int mu, int aoMua) {
        int countRow = 0;

        if (bong != -1) {
            if (upDateWard("Bóng đá", bong))
                countRow+= 1;
        }
        if (ao != -1)
            if (upDateWard("Áo phông", ao))
                countRow +=1;
        if (but != -1)
            if(upDateWard("Bút bi", but))
                countRow +=1;
        if (caseIP6 != -1)
            if(upDateWard("Case Iphone 6", caseIP6))
                countRow +=1;
        if (balo != -1)
            if(upDateWard("Balo dây rút", balo))
                countRow +=1;
        if (mu != -1)
            if(upDateWard("Mũ", mu))
                countRow +=1;
        if (aoMua != -1)
            if(upDateWard("Áo mưa", aoMua))
                countRow +=1;
        Log.e("FUCK COUNT UPDATE: ", countRow + "");
    }

    private boolean upDateWard(String name, int count) {
        openDB();
        ContentValues cont = new ContentValues();
        cont.put("Count", count);
        boolean result =  sqlDB.update("tdbWard", cont, "Name = ?", new String[] {name}) > 0;
        closeDB();
        return result;

    }

    public boolean updateQuayThuong(int ID) {
        openDB();
        Cursor cursor = sqlDB.rawQuery("SELECT Count FROM tdbWard where ID = " + ID, null);
        if (cursor == null || cursor.getCount() <= 0) {
            closeDB();
            return false;
        }
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        ContentValues cont = new ContentValues();
        cont.put("Count", count - 1);
        boolean result =  sqlDB.update("tdbWard", cont, "ID = " + ID, null) > 0;
        closeDB();
        return result;
    }

    public ArrayList<Ward> getAllWard() {
        listWard.clear();
        openDB();
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM tdbWard", null);
        if (cursor == null) {
            return null;
        }

        String  name;
        int ID, count;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ID = cursor.getInt(0);
            name = cursor.getString(1);
            count = cursor.getInt(2);
            Ward ward = new Ward(ID, name, count);
            listWard.add(ward);
            cursor.moveToNext();
        }
        cursor.close();
        closeDB();
        return listWard;
    }

    public boolean checkAccount(String user, String password) {
        openDB();
        String sql = "Select Username, Password from tblUser where (Username = '"
                + user
                + "') and (Password = '"
                + password + "')";
        Cursor cursor = sqlDB.rawQuery(sql, null);
        boolean result =  cursor.getCount() > 0;
        closeDB();
        return result;
    }

}
