package com.padmal.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.padmal.items.Item_Category;
import com.padmal.items.Item_Record;
import com.padmal.items.Item_Summary;

import java.text.NumberFormat;
import java.util.ArrayList;

public class Helper_DB extends SQLiteOpenHelper {

   private static final String DATABASE_NAME = "Accounts.db";

   private static final String TABLE_INCOME = "Income";
   private static final String TABLE_EXPENSE = "Expense";
   private static final String TABLE_CATEGORY = "Category";

   private static final String COLUMN_ID = "ID";
   private static final String COLUMN_TYPE = "Type";
   private static final String COLUMN_DATE = "Date";
   private static final String COLUMN_AMOUNT = "Amount";
   private static final String COLUMN_CATEGORY = "Category";
   private static final String COLUMN_DESCRIPTION = "Description";

   private static final int VERSION_NUMBER = 1;

   public Helper_DB (Context context) {

      super(context, DATABASE_NAME, null, VERSION_NUMBER);
   }

   @Override
   public void onCreate (SQLiteDatabase db) {

      // Income Table
      db.execSQL("CREATE TABLE " + TABLE_INCOME + " (" +
            COLUMN_ID + " INTEGER, " +
            COLUMN_DATE + " DATE, " +
            COLUMN_CATEGORY + " TEXT, " +
            COLUMN_AMOUNT + " REAL)");

      // Expense Table
      db.execSQL("CREATE TABLE " + TABLE_EXPENSE + " (" +
            COLUMN_ID + " INTEGER, " +
            COLUMN_DATE + " DATE, " +
            COLUMN_CATEGORY + " TEXT, " +
            COLUMN_AMOUNT + " REAL)");

      // Category Table
      db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
              COLUMN_TYPE + " TEXT, " +
              COLUMN_CATEGORY + " TEXT PRIMARY KEY, " +
              COLUMN_DESCRIPTION + " TEXT)");
   }

   @Override
   public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

      db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);

      onCreate(db);
   }

   /*******************************************************************************************
    * Insert Methods
    ******************************************************************************************/

   public boolean insertNewEntry (String Table_Name, Integer ID, String Date, String Category, String Amount) {

      if ((Date == null) || (Amount == null) || Date.equalsIgnoreCase("") || Amount.equalsIgnoreCase("")) {
         return false;
      }
      else {

         long check_values;
         Double D_Amount = Double.parseDouble(Amount);
         // Create a database object
         SQLiteDatabase DB = this.getWritableDatabase();
         // Create a content value
         ContentValues Entry_Cache = new ContentValues();

         // Generate cache line
         Entry_Cache.put(COLUMN_ID, ID);
         Entry_Cache.put(COLUMN_DATE, Date);
         Entry_Cache.put(COLUMN_CATEGORY, Category);
         Entry_Cache.put(COLUMN_AMOUNT, D_Amount);

         DB.beginTransaction();
         try {
            // Insert the cache line to the table
            check_values = DB.insert(Table_Name, null, Entry_Cache);
            DB.setTransactionSuccessful();
         } finally {DB.endTransaction();}

         // .insert method returns number of entries it inserted, so if it's -1 there's an error
         return check_values != - 1;
      }
   }

   public boolean insertNewCategory (String Type, String Category, String Description) {

      if (Category.equalsIgnoreCase("") || Description.equalsIgnoreCase("")) {
         return false;
      }
      else {

         long check_value;
         // Create a database object
         SQLiteDatabase DB = this.getWritableDatabase();
         // Create a content value
         ContentValues Entry_Cache = new ContentValues();

         // Generate cache line
         Entry_Cache.put(COLUMN_TYPE, Type);
         Entry_Cache.put(COLUMN_CATEGORY, Category);
         Entry_Cache.put(COLUMN_DESCRIPTION, Description);

         DB.beginTransaction();
         try {
            // Insert the cache line to the table
            check_value = DB.insert(TABLE_CATEGORY, null, Entry_Cache);
            DB.setTransactionSuccessful();
         } finally {DB.endTransaction();}

         return check_value != - 1;
      }
   }

   /*******************************************************************************************
    * Update Methods
    ******************************************************************************************/

   public Integer updateRecord (String Table_Name, String Date_Stamp, String Category_Stamp, Integer new_ID, String new_Date, String new_Category, String new_Amount) {

      int return_value;
      Double N_Amount;
      // Convert Amount into a Double
      try {
         N_Amount = Double.parseDouble(new_Amount);
      } catch (NumberFormatException e) {
         N_Amount = 0.00;
      }

      // Create a database object
      SQLiteDatabase DB = this.getWritableDatabase();
      // Generate content values
      ContentValues Entry_Cache = new ContentValues();
      // Put values to content values
      Entry_Cache.put(COLUMN_ID, new_ID);
      Entry_Cache.put(COLUMN_DATE, new_Date);
      Entry_Cache.put(COLUMN_CATEGORY, new_Category);
      Entry_Cache.put(COLUMN_AMOUNT, N_Amount);

      DB.beginTransaction();
      try {
         return_value = DB.update(
                 Table_Name,
                 Entry_Cache,
                 COLUMN_DATE + " = ? AND " + COLUMN_CATEGORY + " = ?",
                 new String[] {Date_Stamp, Category_Stamp});

         DB.setTransactionSuccessful();
      } finally {DB.endTransaction();}

      return return_value;
   }

   public void updateUpdatedCategoryRecord(String Table_Name, String Category_Stamp, String new_Category) {

      // Create a database object
      SQLiteDatabase DB = this.getWritableDatabase();
      // Generate content values
      ContentValues Entry_Cache = new ContentValues();
      // Put values to content values
      Entry_Cache.put(COLUMN_CATEGORY, new_Category);

      DB.beginTransaction();
      try {
         DB.update(
                 Table_Name,
                 Entry_Cache,
                 COLUMN_CATEGORY + " = ?",
                 new String[] {Category_Stamp});

         DB.setTransactionSuccessful();
      } finally {DB.endTransaction();}
   }

   public Integer updateCategory (String Category_ID, String new_Type, String new_Category, String new_Description) {

      if (new_Category.equalsIgnoreCase("") || new_Description.equalsIgnoreCase("")) {
         return - 1;
      }
      else {

         // Create a database object
         SQLiteDatabase DB = this.getWritableDatabase();
         // Generate content values
         ContentValues Entry_Cache = new ContentValues();
         // Put values to content values
         Entry_Cache.put(COLUMN_TYPE, new_Type);
         Entry_Cache.put(COLUMN_CATEGORY, new_Category);
         Entry_Cache.put(COLUMN_DESCRIPTION, new_Description);

         return DB.update(
                 "Category",
                 Entry_Cache,
                 COLUMN_CATEGORY + "=?",
                 new String[] {Category_ID});
      }
   }

   /*******************************************************************************************
    * Delete Methods
    ******************************************************************************************/

   public Integer deleteRecord (String Table_Name, String Date, String Category, String Amount) {

      // Create a database object
      SQLiteDatabase DB = this.getWritableDatabase();

      // return the delete statement
      String command = COLUMN_DATE + " = ? AND " + COLUMN_CATEGORY + " = ? AND " + COLUMN_AMOUNT + " = ?";
      String[] locator = {Date, Category, Amount};

      return DB.delete(Table_Name, command, locator);
   }

   public Integer deleteCategory (String Category_to_delete) {

      // Create a database object
      SQLiteDatabase DB = this.getWritableDatabase();

      // return the delete statement
      String command = COLUMN_CATEGORY + " = ? ";
      // Where clause
      String[] locator = {Category_to_delete};

      return DB.delete(TABLE_CATEGORY, command, locator);
   }

   /*******************************************************************************************
    * Array Lists
    ******************************************************************************************/

   public ArrayList<Item_Record> getAllRecords_ArrayList (String Table_Name) {

      // Create an array list
      ArrayList<Item_Record> List_Of_Records = new ArrayList<>();
      // Create a database object
      SQLiteDatabase DB = this.getReadableDatabase();

      // Create a cursor file we get from executing this above command
      Cursor crsr = DB.query(
            Table_Name,
            new String[] {COLUMN_DATE, COLUMN_CATEGORY, COLUMN_AMOUNT},
            null, null, null, null, COLUMN_ID + " DESC");

      crsr.moveToFirst();

      while (! crsr.isAfterLast()) {

         Double amount = crsr.getDouble(crsr.getColumnIndex(COLUMN_AMOUNT));
         NumberFormat nf = NumberFormat.getNumberInstance();
         nf.setMinimumFractionDigits(2); nf.setMaximumFractionDigits(2);

         // Add that to the array list
         List_Of_Records.add(new Item_Record(
               crsr.getString(crsr.getColumnIndex(COLUMN_DATE)),
               crsr.getString(crsr.getColumnIndex(COLUMN_CATEGORY)),
               Double.parseDouble(nf.format(amount).replace(",", ""))));

         // Go to the next row
         crsr.moveToNext();
      }

      // Closes database and cursor and return the list
      crsr.close(); DB.close();
      return List_Of_Records;
   }

   public ArrayList<Item_Category> getAllCategoriesAndDescriptionsOfThisType_ArrayList (String Cat_Type) {

      // Create an array list
      ArrayList<Item_Category> List_Of_Records = new ArrayList<>();
      // Create a database object
      SQLiteDatabase DB = this.getReadableDatabase();

      // Create a cursor file we get from executing this above command
      Cursor crsr = DB.query(
            TABLE_CATEGORY,
            new String[] {COLUMN_TYPE, COLUMN_CATEGORY, COLUMN_DESCRIPTION},
            COLUMN_TYPE + " = ?",
            new String[] {Cat_Type}, null, null,
            COLUMN_CATEGORY);

      crsr.moveToFirst();

      while (! crsr.isAfterLast()) {
         // Generate a Item_Category record of the current row and add that to the array list
         List_Of_Records.add(new Item_Category(
               crsr.getString(crsr.getColumnIndex(COLUMN_CATEGORY)),
               crsr.getString(crsr.getColumnIndex(COLUMN_DESCRIPTION)),
               crsr.getString(crsr.getColumnIndex(COLUMN_TYPE))));
         // Go to the next row
         crsr.moveToNext();
      }

      // Close the cursor and return the list
      crsr.close(); DB.close();
      return List_Of_Records;
   }

   public ArrayList<String> getListOfThisTypeCategories_ArrayList (String Cat_Type) {

      // Create an array list
      ArrayList<String> List_Of_Records = new ArrayList<>();
      // Create a database object
      SQLiteDatabase DB = this.getReadableDatabase();

      // Create a cursor file we get from executing this above command
      Cursor crsr = DB.query(
            TABLE_CATEGORY,
            new String[] {COLUMN_CATEGORY},
            COLUMN_TYPE + " = ?",
            new String[] {Cat_Type}, null, null,
            COLUMN_CATEGORY);

      crsr.moveToFirst();

      while (! crsr.isAfterLast()) {
         // Generate a string record of the current row and add that to the array list
         List_Of_Records.add(crsr.getString(crsr.getColumnIndex(COLUMN_CATEGORY)));
         // Go to the next row
         crsr.moveToNext();
      }

      // Close the cursor and return the list
      crsr.close(); DB.close();
      return List_Of_Records;
   }

   public ArrayList<Item_Category> getAllCategoriesWithDescription_ArrayList () {

      // Create an array list
      ArrayList<Item_Category> List_Of_Categories = new ArrayList<>();
      // Create a database object
      SQLiteDatabase DB = this.getReadableDatabase();

      // Create a cursor file we get from executing this above command
      Cursor crsr_to_process = DB.query(
            TABLE_CATEGORY,
            new String[] {COLUMN_TYPE, COLUMN_CATEGORY, COLUMN_DESCRIPTION},
            null, null, null, null, COLUMN_CATEGORY);

      crsr_to_process.moveToFirst();
      while (! crsr_to_process.isAfterLast()) {
         // Generate a record of the current row and add that to the array list
         List_Of_Categories.add(new Item_Category(
               crsr_to_process.getString(crsr_to_process.getColumnIndex(COLUMN_CATEGORY)),
               crsr_to_process.getString(crsr_to_process.getColumnIndex(COLUMN_DESCRIPTION)),
               crsr_to_process.getString(crsr_to_process.getColumnIndex(COLUMN_TYPE))));

         // Go to the next row
         crsr_to_process.moveToNext();
      }

      // Closes database and cursor and return the list
      crsr_to_process.close(); DB.close();
      return List_Of_Categories;
   }

   public ArrayList<Item_Summary> getTotalOfEachCategory_ArrayList (String Table_Name) {

      // Create an array list of Item_Summary type
      ArrayList<Item_Summary> List_Of_Records_Summary = new ArrayList<>();
      // Create a database object
      SQLiteDatabase DB = this.getReadableDatabase();

      // Create a cursor file we get from executing this above command
      Cursor crsr_to_process = DB.query(
            Table_Name,
            new String[] {COLUMN_CATEGORY, "SUM(" + COLUMN_AMOUNT + ")"},
            null, null, COLUMN_CATEGORY,
            null, COLUMN_CATEGORY);

      crsr_to_process.moveToFirst();
      while (! crsr_to_process.isAfterLast()) {

         Double amount = crsr_to_process.getDouble(crsr_to_process.getColumnIndex("SUM(" + COLUMN_AMOUNT + ")"));
         NumberFormat nf = NumberFormat.getNumberInstance();
         nf.setMinimumFractionDigits(2); nf.setMaximumFractionDigits(2);

         // Generate a record of the current row and add that to the array list
         List_Of_Records_Summary.add(new Item_Summary(
               crsr_to_process.getString(crsr_to_process.getColumnIndex(COLUMN_CATEGORY)),
               Double.parseDouble(nf.format(amount).replace(",", ""))));

         // Go to the next row
         crsr_to_process.moveToNext();
      }

      // Closes the Database and cursor and return the list
      crsr_to_process.close(); DB.close();
      return List_Of_Records_Summary;
   }

   /*******************************************************************************************
    * Specific Records
    ******************************************************************************************/

   public String getCategoryDescription_String (String Category_String) {

      // Create a database object
      SQLiteDatabase DB = this.getReadableDatabase();

      // Create a cursor file we get from executing this above command
      Cursor crsr = DB.query(
              TABLE_CATEGORY,
              new String[]{COLUMN_DESCRIPTION},
              COLUMN_CATEGORY + "=?",
              new String[]{Category_String}, null, null, null);

      crsr.moveToFirst();

      // Generate a string record of the current row
      String send_this = crsr.getString(0);

      // Closes database and cursor and return the string
      crsr.close(); DB.close();
      return send_this;
   }

   public Double getTableTotals_String (String Table_name) {

      // Create a database object
      SQLiteDatabase DB = this.getReadableDatabase();
      // Create a cursor file we get from executing this above command
      Cursor crsr = DB.query(
              Table_name,
              new String[]{"SUM(" + COLUMN_AMOUNT + ")"},
              null, null, null, null, null);

      // Move to the first entry
      crsr.moveToFirst();

      // Generate a string record of the current row
      Double total = Double.parseDouble(crsr.getString(0));

      // Closes the cursor and Database and return the value
      crsr.close(); DB.close();
      return total;
   }
}