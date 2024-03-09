package com.testtasks.CRONapp.services;

import java.util.List;
import com.testtasks.CRONapp.entities.News;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NewsService {
	public List<News> parseNews();
	public void saveTodaysNews(List<News> todaysNews);
	public Flux<News> getNewsForTimePeriod(String period);
	public void refreshNewsTable();
	public void updateNews(final News news);
	public void deleteNotTodaysNews();
}
