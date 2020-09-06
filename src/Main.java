import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.sun.javafx.css.FontFace;



/**  
 * @Title: Main.java
 * @Package 
 * @author doublexi
 * @date 2020年7月26日
 * @version V1.0  
 */

/**
 * @ClassName: Main
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author doublexi
 * @date 2020年7月26日
 * @since JDK 1.8
 */
public class Main extends Application {
	ExecutorService pool = Executors.newFixedThreadPool(20);
	ArrayList<String> list=new ArrayList(10000);
	public static void main(String[] args) throws InterruptedException {
//		//AsciiImageCreator.create("微信图片_20200626134048.jpg", "result.txt");
//		boolean videoImage = FFMPEG.getVideoImage("",40.006,"");
//		System.out.println(videoImage);
//		//TimeUnit.SECONDS.sleep(180);
//		ExecutorService pool = Executors.newFixedThreadPool(20);
//		task tt=new task(1, 7000);
//		pool.execute(tt);
		
	launch(args);
		
	}

	/* (非 Javadoc)
	 * 
	 * 
	 * @param primaryStage
	 * @throws Exception
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox hbox=new HBox();
		Button play=new Button("播放");
		Button play2=new Button("命令行播放");
		Button videoToImage=new Button("视频转图片");
		Button imageToTXT=new Button("图片转文本");
		Button readTXT=new Button("装载文本");
		ProgressBar bar = new ProgressBar();
		hbox.getChildren().add(play);
		hbox.getChildren().add(play2);
		hbox.getChildren().add(videoToImage);
		hbox.getChildren().add(imageToTXT);
		hbox.getChildren().add(readTXT);
		hbox.getChildren().add(bar);
		Scene node=new Scene(hbox);
		primaryStage.setScene(node);
		primaryStage.setHeight(100);
		primaryStage.setWidth(600);
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent arg0) {
		    	System.exit(0);
		   }
		});
		
		
		play.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Stage stage=new Stage();
				HBox hbox=new HBox();
//				Text text=new Text();
				TextArea text=new TextArea();
				text.setPrefSize(1920, 1080);
				text.setFont(Font.font("monospaced",FontWeight.BLACK,6));
				hbox.getChildren().add(text);
				Scene scene=new Scene(hbox);
				stage.setScene(scene);
				stage.setHeight(1080);
				stage.setWidth(1920);
				stage.setResizable(false);
				stage.show();
		
				
				ShowTextTask scheduel=new ShowTextTask(list, text);
				scheduel.setPeriod(Duration.millis(45));
				scheduel.start();
			 
			}
		});
		videoToImage.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				boolean videoImage = FFMPEG.getVideoImage("","");
				System.out.println(videoImage);
			}
		});
		imageToTXT.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				File file=new File("image");
				int length = file.listFiles().length;
				CreateTextTask task=new CreateTextTask(1, length);
				pool.execute(task);
			}
		});
		readTXT.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				
				//
				
				Task task=new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						// TODO Auto-generated method stub
						File file=new File("txt");
						int length = file.listFiles().length;
						System.out.println(length);
						
						for(int i=1;i<=length;i++) {
							try {
								FileInputStream in=new FileInputStream("txt/"+i+".txt");
								byte[] data=new byte[in.available()];
								in.read(data);
								list.add(new String(data));
								System.out.println("加载第"+i+"个");
								updateProgress(i, length);
								in.close();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						
						return null;
					}
				};
				
				bar.progressProperty().bind(task.progressProperty());
				pool.execute(task);
			}
		});
		
		
		play2.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				list.forEach(new Consumer<String>() {

					@Override
					public void accept(String t) {
						// TODO Auto-generated method stub
						System.out.println(t);
						try {
							TimeUnit.MILLISECONDS.sleep(45);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
			}
		});
		
		
		
		
		
	}
}

class CreateTextTask implements Runnable{
	private int start;
	private int end;
	
public CreateTextTask(int start,int end) {
	this.start=start;
	this.end=end;
}
	@Override
	public void run() {
		for(;start<=end;start++) {
			AsciiImageCreator.create("image/"+start+".jpg", "txt/"+start+".txt");
			
		}
		
	}
	
}


class ShowTextTask extends ScheduledService<String>{
	int key=0;
	List<String> data;
	TextArea text;
	ShowTextTask schedule=this;
	public ShowTextTask(List<String> data,TextArea text) {
		this.data=data;
		this.text=text;
	}
	@Override
	protected Task<String> createTask() {
		Task<String> task=new Task<String>() {
			
			

			@Override
			protected void updateValue(String value) {
				
				super.updateValue(value);
				text.setText(value);
			}

			@Override
			protected String call() throws Exception {
				if(key<data.size()) {
					String string = data.get(key);
					key++;
					return string;
					
				}
				schedule.cancel();
				return "";
			}
		};
		
		
		return task;
	}
	
	
	
}