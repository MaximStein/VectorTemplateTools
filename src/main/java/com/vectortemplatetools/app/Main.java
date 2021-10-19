package com.vectortemplatetools.app;

import java.awt.image.BufferedImage;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import javafx.fxml.FXMLLoader;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	
	private Group root;
	
	@Override
	public void start(Stage primaryStage) {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AppView.fxml"));

			Parent root =  fxmlLoader.load();
			Scene scene = new Scene(root, 900, 700);
		//	scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public class BufferedImageTranscoder extends ImageTranscoder {
	    private BufferedImage img = null;
	    @Override
	    public BufferedImage createImage(int width, int height) {
	        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
	    }
	    @Override
	    public void writeImage(BufferedImage img, TranscoderOutput to) throws TranscoderException {
	        this.img = img;
	    }
	    public BufferedImage getBufferedImage() {
	        return img;
	    }
	}
}