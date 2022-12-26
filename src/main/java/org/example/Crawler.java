package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;

public class Crawler {

    private HashSet<String> urlLink;
    private int MAX_DEPTH = 2;
    public Connection connection;
    public  Crawler()
    {
        //Setup the Connection to mySql
        connection = DatabaseConnection.getConnection();
        urlLink = new HashSet<String>();
    }

    public void getPageTextandLink(String url, int depth)
    {
        if(!urlLink.contains(url))
        {
            if (urlLink.add(url))
            {
                System.out.println(url);
            }

            try
            {
                //Parsing HTML object to java Document Object
                Document document = Jsoup.connect(url).timeout(5000).get();
                //Get text from Document object
                String text =  document.text().length()<501?document.text():document.text().substring(0,500);
                //print Text
                System.out.println(text);

                PreparedStatement preparedStatement = connection.prepareStatement("Insert into pages values(?,?,?)");
                preparedStatement.setString(1,document.title());
                preparedStatement.setString(2,url);
                preparedStatement.setString(3,text);
                preparedStatement.executeUpdate();
                //Increase Depth
                depth++;
                //if Depth is greater than max then return
                if (depth > MAX_DEPTH)
                {
                    return;
                }
                Elements avaliableLinksOnPage = document.select("a[href]");
                for (Element currentLink : avaliableLinksOnPage)
                {
                    getPageTextandLink(currentLink.attr("abs:href"),depth);
                }

            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            catch (SQLException sqlException)
            {
                sqlException.printStackTrace();
            }

        }
    }


    public static void main(String[] args)
    {
        Crawler crawler = new Crawler();
        crawler.getPageTextandLink("https://www.javatpoint.com/",0);
    }
}