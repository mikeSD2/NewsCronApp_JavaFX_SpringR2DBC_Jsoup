package com.testtasks.CRONapp.javaFxPart.controllers;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.testtasks.CRONapp.entities.News;
import com.testtasks.CRONapp.repos.NewsRepository;
import com.testtasks.CRONapp.services.NewsService;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import reactor.core.Disposable;



@Component
public class MainController implements Initializable {
	//insert service bean to use service methods
	@Autowired
	private NewsService serviceclass;
	//in this list staores extracted data from DB for needed period
	List<News> newsForSelectedPeriod = new ArrayList<News>();
	//this variable is to change articles by clicking buttons.
	int currentListElement = 0;
	//Elements from fxml. their name must match with names in fxml
	@FXML
	private ChoiceBox<String> selectPeriod;
	@FXML
    private Text headline,description, publTime;
	@FXML
    private Button next, previous;
	//this method executes when app is started
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//here we are getting news for period stored in selectPeriod.getValue().
		//subscribe creates new thread and news getting is happening there, so the javafx thread is not blocked.
		serviceclass.getNewsForTimePeriod(selectPeriod.getValue()).subscribe(news -> {
			//store every gotten news in list
			newsForSelectedPeriod.add(news);
			//set state of test fields.(yeah i could find better place to place it)
			changeTestFieldsState();
		});
	}
//	this method executes on ChoiceBox change to store and show news in list for period we need
	@FXML
    public void getNewsForPeriodClicked(Event e) {
					newsForSelectedPeriod.clear();
					currentListElement=0;
			serviceclass.getNewsForTimePeriod(selectPeriod.getValue()).subscribe(news -> {
				newsForSelectedPeriod.add(news);
				changeTestFieldsState();
			});
    }
	//method that executes on next news button press.
	//it shows newt news.
	@FXML
	public void getNext(Event e) {
		if(currentListElement == newsForSelectedPeriod.size()-1)
			return;
		currentListElement++;
		changeTestFieldsState();
    }
	//method that executes on previous news button press.
	//it shows previous news.
	@FXML
	public void getPrev(Event e) {
		if(currentListElement == 0)
			return;
		currentListElement--;
		changeTestFieldsState();
    }
	//here we set state of textfields
	public void changeTestFieldsState() {
		try {
			headline.setText(newsForSelectedPeriod.get(currentListElement).getHeadline());
			description.setText(newsForSelectedPeriod.get(currentListElement).getDescription());
			publTime.setText(String.valueOf(newsForSelectedPeriod.get(currentListElement).getPublicationTime()));
		} catch (IndexOutOfBoundsException e) {}
	}
	}

