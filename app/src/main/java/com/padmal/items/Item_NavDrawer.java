package com.padmal.items;

public class Item_NavDrawer {

   // Variables within
   private String title;
   private int icon;

   // Constructor
   public Item_NavDrawer (String title, int icon) {
      this.title = title;
      this.icon = icon;
   }

   /**** Getters ****/
   public String getTitle(){
      return this.title;
   }

   public int getIcon(){
      return this.icon;
   }

   /**** Setters ****/
   public void setTitle(String title){
      this.title = title;
   }

}
