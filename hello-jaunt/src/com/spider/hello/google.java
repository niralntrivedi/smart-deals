package com.spider.hello;

import com.jaunt.*;
public class google{
  public static void main(String[] args){
    try{
        UserAgent userAgent = new UserAgent();         //create new userAgent (headless browser)
        userAgent.visit("http://google.de");          //visit google
        userAgent.doc.apply("schmetterlinge");            //apply form input (starting at first editable field)
        userAgent.doc.submit();         //click submit button labelled "Google Search"


        Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");  //find search result links
        for(Element link : links) System.out.println(link.getAt("href"));           //print results

        if(userAgent.doc.nextPageLinkExists()) {
            userAgent.visit(userAgent.doc.nextPageLink().getHref());
            Elements newlinks = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");
            System.out.println("\nPage 2:");
            for(Element link : newlinks) System.out.println(link.getAt("href"));
        }
    }
    catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
      System.err.println(e);
    }
  }
}