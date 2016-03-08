package com.padmal.items;

public class Item_Record {

   // Variables within
   private String Date;
   private String Category;
   private Double Amount;

   // Constructor
   public Item_Record (String date, String category, Double amount) {

      this.Date = date;
      this.Category = category;
      this.Amount = amount;
   }

   /**** Getters ****/
   public String getDate () {
      return this.Date;
   }

   public String getCategory () {
      return this.Category;
   }

   public Double getAmount () {
      return this.Amount;
   }

   /**** Setters ****/
   public void setDate (String date) {
      this.Date = date;
   }

   public void setCategory (String category) {
      this.Category = category;
   }

   public void setAmount (Double amount) {
      this.Amount = amount;
   }
}
