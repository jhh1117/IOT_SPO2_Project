package com.pelkan.tab;

/**
 * Created by admin on 2016-02-03.
 */
public class Product {
    private String q_id;

    private String img_url;

    private String title;

    private String content;

    public void setQ_id(String q_id){this.q_id = q_id;}

    public void setURL(String img_url){this.img_url=img_url;}

    public void setTitle(String title){this.title=title;}

    public void setContent(String content){this.content=content;}

    public String getQ_id(){return q_id;}

    public String getURL(){return img_url;}

    public String getTitle(){return title;}

    public String getContent(){return content;}
}