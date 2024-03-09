package com.testtasks.CRONapp;

import java.time.ZoneId;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.testtasks.CRONapp.javaFxPart.JavaFxView;

import io.r2dbc.spi.ConnectionFactory;
import javafx.application.Application;

//These are some CONCLUSIONS.
//As you can see everithing is okay with modularity in this project.
//We follow MVC pattern, so we have controller (javafx controller), view (fxml) and model(repo and anothe things),
//we have service layer, dao class to hide some complicated logic.
//We thought about extensibility. We handled exceptions (but not everywhere to be honest :) ).
//Everithing is reactive in our app so nothing is lagging.
//We made a pretty nice interface. And everithing is commented.
@EnableWebFlux
@EnableScheduling
@SpringBootApplication
public class CroNappApplication {
	//bean to execute sql query every time we start the project to create the table where news should be stored
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

      ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
      initializer.setConnectionFactory(connectionFactory);
      initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

      return initializer;
    }
	public static void main(String[] args) {
		Application.launch(JavaFxView.class, args);
	}

}
