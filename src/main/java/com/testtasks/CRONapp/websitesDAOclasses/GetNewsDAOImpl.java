package com.testtasks.CRONapp.websitesDAOclasses;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.testtasks.CRONapp.entities.News;

@Component
public class GetNewsDAOImpl implements GetNewsDAO {
//	method to parse forbes news using Jsoup
	@Override
	public List<News> parseBBCnews() {
		List<News> todaysNews = new ArrayList<News>();
		try {
			System.out.println("parser "+ Thread.currentThread().getName());
			//connect to the page with all breaking news
			Document allNewsPage = Jsoup.connect("https://www.forbes.com/news/?sh=7802be093690").get();
			Document oneOfTheNews;
//			get spans with date news posted
			Elements els1 = allNewsPage.select("div.COe59 span._9u4PrQql");
			int numberOfTodaysNews = 0;
			for(Element e1 : els1) {
				News singleNews = new News();
				String timePosted = e1.text();
				DateTime dateTime = new DateTime();
//				if this string with date contains minutes, we minus these minutes from current time 
				if(timePosted.contains("minute")) {
					dateTime = dateTime.minusMinutes(Integer.parseInt(timePosted.substring(0,2).trim()));
				}
//				if this string with date contains hours, we minus these hours from current time 
				else if(timePosted.contains("hour")) {
					dateTime = dateTime.minusHours(Integer.parseInt(timePosted.substring(0,2).trim()));
				}
				else {
					break;
				}
				//here we check if gotten datetime less than current time and if it is
				//we go to the next one cycle iteration, so we shoulnt not store this datetime
				//to db beacause we need only todays news.
				if(dateTime.toInstant().isBefore(LocalDate.now().toDateMidnight())) {
					continue;
				}
				//if it less we store it in objects we want to save in database
				singleNews.setPublicationTime(Instant.ofEpochMilli(dateTime.getMillis()));
			    numberOfTodaysNews++;
				todaysNews.add(singleNews);
			}
			//here we get Headline and Description
			Elements els2 = allNewsPage.select("div.NJl37fev");
			for(int i=0; i<numberOfTodaysNews; i++) {
//				go to the specific page with news
				String href = els2.get(i).select("a").attr("href");
				oneOfTheNews = Jsoup.connect(href).get();
//				get headline
				String headline = oneOfTheNews.select("h1").text();
				Elements els3 = oneOfTheNews
						.select("div.article-body h2:not(:contains(Further Reading)),p:not([class])");
				String descriptionToDB = "";
				//here we make full description string 
				for(Element e1 : els3) {
					descriptionToDB += e1.text()+"\n\n";
				}
//				store headlines and descriptions in objects we want to store in db
				todaysNews.get(i).setHeadline(headline);
				todaysNews.get(i).setDescription(descriptionToDB);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return list of today's news
		return todaysNews;
	}
//	here we can create another methods to parse another news websites
}
