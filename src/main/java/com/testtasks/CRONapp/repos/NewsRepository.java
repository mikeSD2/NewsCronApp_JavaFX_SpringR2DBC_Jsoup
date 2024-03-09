package com.testtasks.CRONapp.repos;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.testtasks.CRONapp.entities.News;

import javafx.collections.ObservableList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//this is reactive repository. we should subscribe on everything it returns.
@Repository
public interface NewsRepository extends R2dbcRepository<News, String> {
//	this hql query deletes all news from db where publication time is less than todays date.
	@Modifying
	@Transactional
	@Query("delete from News n where DATE(n.publication_time) < DATE(NOW())")
	Mono<Long> deleteNotTodaysNews();
//	this query gets news from db for period between dates in parameters
	@Query("select * from News n where n.publication_time between :from and :to order by n.publication_time")
	Flux<News> getNewsForPeriod(Instant from, Instant to);
}