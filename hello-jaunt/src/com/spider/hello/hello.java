package com.spider.hello;

import java.util.Map;
import java.util.HashMap;
import org.json.simple.JSONArray;
import com.jaunt.NotFound;
import java.util.Arrays;
import java.util.ArrayList;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import org.json.simple.JSONObject;


public class hello {
  public static ArrayList<String> links = new ArrayList<String>();
//  public static JSONArray list = new JSONArray();
//  public static JSONObject obj = new JSONObject();
  
  public static void main(String args[]){
    JSONArray list = new JSONArray();
    JSONObject obj = new JSONObject();
    //Map<String, String> m = new HashMap<String, String>();
    nnt("http://sfbay.craigslist.org/search/cta");
    int counter = 0;
    
    for(String d:links) {
      //System.out.println(d);
      obj.put(counter++, tnt(d));
      //tnt(d);
      if(counter == 2){
        break;
      }
    }
    
    //obj.putAll(m);
    System.out.println(obj.toJSONString());
  }
  
  public static void nnt(String url){
    System.out.print(".");
    try{
      UserAgent userAgent = new UserAgent();                       
      userAgent.visit(url);
      
      Elements anchor = userAgent.doc.findEach("<a href>");
      
      for(Element table : anchor){
        if(table.outerHTML().contains("class=\"hdrlnk\"")){
          links.add(table.getAt("href"));
        }
      }  
      
      try {
        Element newlinks = userAgent.doc.findFirst("<a href class=\"button next\">");
        //nnt(newlinks.getAt("href"));
      } catch(Exception e){
        System.out.println("Done with all listings");
      } 
    }
    catch(JauntException e){                         
      System.err.println(e);         
    }
  }
  
  public static JSONObject tnt(String url){
    JSONArray list = new JSONArray();
    JSONObject obj = new JSONObject();
   
    UserAgent userAgent = new UserAgent();                       
    try {
      userAgent.visit(url);
    } catch (ResponseException e) {
      e.printStackTrace();
    }
    Element title;
    Elements img_src;
    Elements attrGroup;
    
    try {
      title = userAgent.doc.findFirst("<span class=\"postingtitletext\">");
      img_src = userAgent.doc.findEvery("<img>");
      attrGroup = userAgent.doc.findEvery("<div class=\"mapAndAttrs\">").findEvery("<span>");
      
      //System.out.println(title.getText());
      obj.put("title", title.getText());
      
      for(int i=1; i < img_src.size(); i++){
        //System.out.println(img_src.getElement(i).getAt("src"));
        list.add(img_src.getElement(i).getAt("src"));
      }
      
      obj.put("images", list);
      
      //System.out.println(attrGroup.size());
      
      for(int i=1; i < attrGroup.size(); i++){
        try{
          //System.out.println(attrGroup.getElement(i).getText());
          //System.out.println(attrGroup.findEvery("<b>").getElement(i).getText());
          obj.put(attrGroup.getElement(i).getText(), attrGroup.findEvery("<b>").getElement(i).getText());
        }catch(Exception e){}
      }
      
      try {
//        System.out.println(userAgent.doc.findFirst("<div data-latitude>").getAt("data-latitude"));
//        System.out.println(userAgent.doc.findFirst("<div data-longitude>").getAt("data-longitude"));
        obj.put("latitude", userAgent.doc.findFirst("<div data-latitude>").getAt("data-latitude"));
        obj.put("longitude", userAgent.doc.findFirst("<div data-longitude>").getAt("data-longitude"));
      }catch(Exception e){}
      
      try {
        //System.out.println(userAgent.doc.findFirst("<section id=\"postingbody\">").getText());
        obj.put("post_body", userAgent.doc.findFirst("<section id=\"postingbody\">").getText());
      }catch(Exception e){}
    } catch (NotFound e) {
      System.out.println("Title not found!");
    }
    
    return obj;
  }
}
