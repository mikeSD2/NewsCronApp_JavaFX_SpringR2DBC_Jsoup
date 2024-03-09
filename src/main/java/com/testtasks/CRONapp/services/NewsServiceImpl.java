package com.testtasks.CRONapp.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.testtasks.CRONapp.entities.News;
import com.testtasks.CRONapp.repos.NewsRepository;
import com.testtasks.CRONapp.websitesDAOclasses.GetNewsDAO;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NewsServiceImpl implements NewsService {
	//This app is written to parse only forbes now. But if we want to Externalize it
	//to parse another websites, we can add another methods in DAO object.
	//This object hides all low level parsing so dont see it in service. 
	@Autowired
	GetNewsDAO parseNewsdao;
	//inject repositories.
	@Autowired
	NewsRepository newsRepository;
	
//	This is the sheduled. It parses and stores parsed data to db every 20 minutes
	//and cleares old data
	@Scheduled(fixedDelay = 1200000)
	@Async //its asynchronous, so javafx thread is not blocked
	public void refreshNewsTable() {
		saveTodaysNews(parseNews());
		deleteNotTodaysNews();
		
	}
	//method to call parsing method we want.
	//as it was noticed there could be methods to parse another websites in GetNewsDAO
	//not only forbes, so we can pass name of website we need in parseNews() as a parameter
	//and call parsing method we need. its about Exteranlisibility.
	@Override
	public List<News> parseNews() {
		return parseNewsdao.parseBBCnews();
	}
	//this mwthod is to save new news in db and to update existing news in db.
	//also this method deletes not today's news
	@Override
	public void saveTodaysNews(List<News> todaysNews) {
		//
		for(int i=todaysNews.size()-1; i>=0; i--) {
			updateNews(todaysNews.get(i));
		}
	}
	
	//method to delete Not Today's News
	@Override
	public void deleteNotTodaysNews() {
		newsRepository.deleteNotTodaysNews().subscribe();
	}
	//method to update or save new news in db.
    public void updateNews(final News news) {
//    	it checks if news is present in db and if it is, it updates news, if not it adds new news
        newsRepository.existsById(news.getHeadline()).subscribe(isPresent -> {
			if(isPresent) {
				newsRepository.save(news).subscribe(updatedNews -> {
//							System.out.println(updatedNews);
				});
			}
			else {
				News lambdanews = new News(news.getHeadline(),
						news.getDescription(),news.getPublicationTime());
				lambdanews.setAsNew();
				newsRepository.save(lambdanews).subscribe(sss -> {
//					System.out.println(sss.getHeadline());
				});
			}
		});
    }
    //this method is to get news based on passed period
	@Override
	public Flux<News> getNewsForTimePeriod(String period) {
		Instant from;
		Instant to;
		switch (period) {
	        case "morning": 
	        	from = Timestamp.valueOf(LocalDate.now() + " 06:00:01").toInstant();
	        	to = Timestamp.valueOf(LocalDate.now() + " 12:00:00").toInstant();
	        	return newsRepository.getNewsForPeriod(from, to);
	        case "day": 
	        	from = Timestamp.valueOf(LocalDate.now() + " 12:00:01").toInstant();
	        	to = Timestamp.valueOf(LocalDate.now() + " 18:00:00").toInstant();
	        	return newsRepository.getNewsForPeriod(from, to);
	        case "evening": 
	        	from = Timestamp.valueOf(LocalDate.now() + " 18:00:01").toInstant();
	        	to = Timestamp.valueOf(LocalDate.now() + " 23:59:59").toInstant();
	        	return newsRepository.getNewsForPeriod(from, to);
	        default:
	        	return null;
		}
	}

}
