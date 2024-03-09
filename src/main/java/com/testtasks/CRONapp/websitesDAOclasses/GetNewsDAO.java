package com.testtasks.CRONapp.websitesDAOclasses;

import java.util.List;

import com.testtasks.CRONapp.entities.News;

import reactor.core.publisher.Mono;

public interface GetNewsDAO {
	public List<News> parseBBCnews();
}
