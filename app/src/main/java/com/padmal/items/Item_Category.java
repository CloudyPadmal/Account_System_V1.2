package com.padmal.items;

public class Item_Category {

   // Variables within
   private String Type;
   private String Category;
   private String Description;

   // Constructor
   public Item_Category (String category, String description, String type) {
      this.Category = category;
      this.Description = description;
      this.Type = type;
   }

   /**** Getters ****/
   public String getCategory () {
      return this.Category;
   }

   public String getDescription () {
      return this.Description;
   }

   public String getType() {
      return this.Type;
   }

   /**** Setters ****/
   public void setDescription (String description) {
      this.Description = description;
   }

   public void setCategory (String category) {
      this.Category = category;
   }

   public void setType (String type) {
      this.Type = type;
   }
}
