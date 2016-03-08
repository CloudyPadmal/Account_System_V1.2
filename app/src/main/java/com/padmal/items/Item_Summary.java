package com.padmal.items;

public class Item_Summary {

   // Variables within
   private String category;
   private Double total;

   // Constructor
   public Item_Summary (String Category, Double Total) {
      this.category = Category;
      this.total = Total;
   }

   /**** Getters ****/
   public String getCategory () {
      return this.category;
   }

   public Double getTotal () {
      return this.total;
   }

   /**** Setters ****/
   public void setCategory (String category) {
      this.category = category;
   }

}
