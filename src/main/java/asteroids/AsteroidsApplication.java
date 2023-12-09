package asteroids;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;







public class AsteroidsApplication extends Application{
    
    public static int WIDTH = 300;
    public static int HEIGHT = 200;

    public void start(Stage window) throws Exception{
        Pane layout = new Pane();
        layout.setPrefSize(WIDTH, HEIGHT);
        Text text = new Text(10,20,"Points: 0");
        AtomicInteger points = new AtomicInteger();
        
        Ship ship = new Ship(WIDTH/2,HEIGHT/2);  
        List<Asteroid> asteroids = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            asteroids.add(new Asteroid(random.nextInt(WIDTH / 3),random.nextInt(HEIGHT)));            
        }
        
        layout.getChildren().add(ship.getCharacter());         
        asteroids.forEach(asteroid->
            layout.getChildren().add(asteroid.getCharacter())
        );   
        
        layout.getChildren().add(text);
        
        Scene scene = new Scene(layout); 
        
        HashMap<KeyCode, Boolean> pressedKeys = new HashMap<>();
        
        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);                    
        });
                
       scene.setOnKeyReleased(event ->{
           pressedKeys.put(event.getCode(), Boolean.FALSE);
       });       
       
       List<Projectile> projectiles = new ArrayList<>();
        
        new AnimationTimer(){
            
            public void handle(long now){
                
                if(pressedKeys.getOrDefault(KeyCode.LEFT, Boolean.FALSE)){
                    ship.turnLeft();
                }
                
                if(pressedKeys.getOrDefault(KeyCode.RIGHT, Boolean.FALSE)){
                    ship.turnRight();
                }   
                
                if(pressedKeys.getOrDefault(KeyCode.UP, Boolean.FALSE)){
                    ship.accelerate();
                }
                
                if(pressedKeys.getOrDefault(KeyCode.SPACE, Boolean.FALSE) && projectiles.size() < 3){
                    Projectile projectile = new Projectile((int)ship.getCharacter().getTranslateX(),(int)ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);
                    
                    projectile.accelerate();
                    projectile.setMovementent(projectile.getMovement().normalize().multiply(3));
                    
                    layout.getChildren().add(projectile.getCharacter());
                }
                
                ship.move(); 
                asteroids.forEach(asteroid->asteroid.move());
                projectiles.forEach(projectile->projectile.move());
                
//                projectiles.forEach(projectile->{
//                        List<Asteroid> collitions = asteroids.stream()
//                                                        .filter(asteroid -> asteroid.collide(projectile))
//                                                        .collect(Collectors.toList());
//                
//                        collitions.stream().forEach(collition->{
//                            asteroids.remove(collition);
//                            layout.getChildren().remove(collition.getCharacter());
//                        });                
//                });
                
//                List<Projectile> projectilesToRemove = projectiles.stream().filter(projectile->{
//                    List<Asteroid> collitions = asteroids.stream()
//                                                    .filter(asteroid->asteroid.collide(projectile))
//                                                    .collect(Collectors.toList());
//                    
//                    if(collitions.isEmpty()){
//                        return false;
//                    }
//                    
//                    collitions.stream().forEach(collition->{
//                        asteroids.remove(collition);
//                        layout.getChildren().remove(collition.getCharacter());                    
//                    });
//                    
//                    return true;
//                                
//                
//                }).collect(Collectors.toList());
//                
//                projectilesToRemove.stream().forEach(projectile->{
//                    layout.getChildren().remove(projectile.getCharacter());
//                    projectiles.remove(projectile);
//                });

                   projectiles.stream().forEach(projectile->{
                       asteroids.stream().forEach(asteroid->{
                           if(projectile.collide(asteroid)){
                               projectile.setAlive(false);
                               asteroid.setAlive(false);
                           }
                           if(!projectile.isAlive()){
                               text.setText("Points: " + points.addAndGet(1000));
                           }
                       });
                   });
                   
                   projectiles.stream().filter(projectile->!projectile.isAlive()
                   ).forEach(projectile->layout.getChildren().remove(projectile.getCharacter()));
                   
                   projectiles.removeAll(projectiles.stream()
                           .filter(projectile->!projectile.isAlive())
                           .collect(Collectors.toList())
                           );
                   
                   asteroids.stream().filter(asteroid->!asteroid.isAlive())
                                      .forEach(asteroid->{
                                          layout.getChildren().remove(asteroid.getCharacter());
                                      });
                   asteroids.removeAll(asteroids.stream().filter(asteroid->!asteroid.isAlive())
                                                          .collect(Collectors.toList()));
                
                //text.setText("Points: " + points.incrementAndGet());
                   
                asteroids.forEach(asteroid->{
                    if(ship.collide(asteroid)){
                        stop();
                    }
                }); 
                
                if(Math.random() < 0.005){
                    Asteroid asteroid = new Asteroid(WIDTH,HEIGHT);
                    if(!asteroid.collide(ship)){
                        asteroids.add(asteroid);
                        layout.getChildren().add(asteroid.getCharacter());
                    }
                }
                
            }                        
        }.start();
                        
        window.setTitle("Asteroids!");
        window.setScene(scene);
        window.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    public static int partsCompleted() {
        // State how many parts you have completed using the return value of this method
        
        return 4;
    }

}
