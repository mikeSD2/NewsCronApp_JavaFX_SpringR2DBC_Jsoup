package com.testtasks.CRONapp.javaFxPart;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.testtasks.CRONapp.javaFxPart.JavaFxView.StageReadyEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
	//path to the file with UIelements
	@Value("classpath:/UIelements.fxml")
	private Resource resource;
	//title. the string from application.properties will be injected in it
	private String appTitle;
	//we need to give JavaFX the beans it needs
	private ApplicationContext appContext;
	//so at first we need to inject appContext where it configured
	public StageInitializer(
			@Value("${spring.app.title}") String appTitle,
			ApplicationContext appContext) {
		this.appTitle = appTitle;
		this.appContext = appContext;
	}
	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		try {
			//Get stage to customize it as we want
			Stage stage = (Stage)event.getSource();
			//We need to pass the file with UI elementa to the scene
			//so we do it via Parent object
			FXMLLoader fxmlloader = new FXMLLoader(resource.getURL());
			//below we set the way JavaFX will get Beans it needs from appContext
			fxmlloader.setControllerFactory(someBean -> appContext.getBean(someBean));
			Parent parent = fxmlloader.load();
			//Scene is the place where we plae our elements
			Scene scene = new Scene(parent);
			stage.setScene(scene);
			//set title
			stage.setTitle(appTitle);
			//prohibit resizing
			stage.setResizable(false);
			//Show scene
			stage.show();
		} catch (IOException e) {
			e.printStackTrace(); //FOR MEEEE watch tutor for how to handle it in production code
		}
	}
}
