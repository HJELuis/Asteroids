/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

/**
 *
 * @author luise
 */

import javafx.scene.shape.Polygon;
import javafx.geometry.Point2D;

public class Ship extends Character{
    
    public Ship(int x, int y){
        super(new Polygon(-5, -5, 10, 0 ,-5, 5),x, y);
    }    
}