package com.testtasks.CRONapp.javaFxPart;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import com.testtasks.CRONapp.CroNappApplication;
import com.testtasks.CRONapp.javaFxPart.JavaFxView.StageReadyEvent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
//this class launches our JavaFx app
public class JavaFxView extends Application {
	private ConfigurableApplicationContext appCont;
	
	@Override
	public void init() {
		//get application context
		appCont = new SpringApplicationBuilder(CroNappApplication.class).run();
	}

	@Override
	public void stop(){
		//close application context and JavaFx
		appCont.close();
		Platform.exit();
	}

	@Override
	public void start(Stage primaryStage){
		//Inside primaryStage is something that we wont to show in the window.
		//When we ready to show something, so the stage is ready, we publish event with stage and 
		//another classes can have access to this stage.
		appCont.publishEvent(new StageReadyEvent(primaryStage));
	}
	
	public static class StageReadyEvent extends ApplicationEvent {

		public StageReadyEvent(Stage stage) {
			super(stage);
		}
		
	}
}
