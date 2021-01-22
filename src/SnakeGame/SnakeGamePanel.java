package SnakeGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.*;


public class SnakeGamePanel extends JPanel implements ActionListener {

    static final int Screen_Width = 600;
    static final int Screen_Height = 600;
    static final int Unit_Size = 25;
    static final int Game_Units = (Screen_Width*Screen_Height)/Unit_Size;
    static final int DELAY = 75;

    //Array holding coordinates (body parts) of the snake.
    final int x[] = new int[Game_Units];
    final int y[] = new int[Game_Units];

    int bodyParts = 7;
    int foodEaten;
    int foodx;
    int foody;
    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    //constructor
    SnakeGamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(Screen_Width,Screen_Height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        gameStart();
    }

    //Game methods
    public void gameStart(){
        newFood();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    //generate a new food for the snake each time method is called.
    public void newFood(){
        foodx = random.nextInt((int)(Screen_Width/Unit_Size))*Unit_Size;
        foody = random.nextInt((int)(Screen_Height/Unit_Size))*Unit_Size;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (running){

            /*for(int i=0; i<Screen_Height/Unit_Size; i++){
                g.drawLine(i*Unit_Size, 0, i*Unit_Size, Screen_Height);
                g.drawLine(0, i*Unit_Size, Screen_Width, i*Unit_Size);
            }*/

            g.setColor(Color.cyan);
            g.fillOval(foodx, foody, Unit_Size, Unit_Size);

            //drawing body parts of the snake.
            for (int i = 0; i<bodyParts; i++){
                if (i == 0){ //snake head.
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], Unit_Size, Unit_Size);
                } else {
                    //g.setColor(Color.magenta);
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], Unit_Size, Unit_Size);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Player Score: "+foodEaten,(Screen_Width-metrics.stringWidth("Player Score: "+foodEaten))/2,g.getFont().getSize());
        }else {
            gameOver(g);
        }
    }

    public void move(){

        for (int i = bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0] - Unit_Size;
                break;

            case 'D':
                y[0] = y[0] + Unit_Size;
                break;

            case 'L':
                x[0] = x[0] - Unit_Size;
                break;

            case 'R':
                x[0] = x[0] + Unit_Size;
                break;
        }
    }

    public void chekFood(){
        if ((x[0] == foodx) && (y[0] == foody) ){
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollisions(){
        //checks if head touches body.
        for (int i=bodyParts; i>0; i--){
            if ((x[0] == x[1] && y[0] == y[i])){
                running = false;
            }
        }
        //if head touches the 'L' border.
        if (x[0]<0){
            running = false;
        }

        //if head touches the 'R' border.
        if (x[0]>Screen_Width){
            running = false;
        }

        //if head touches the 'Top' border.
        if (y[0]<0){
            running = false;
        }

        //if head touches the 'Bottom' border.
        if (y[0]>Screen_Height){
            running = false;
        }

        if (!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Display player score.
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Player Score: "+foodEaten,(Screen_Width-metrics1.stringWidth("Player Score: "+foodEaten))/2,g.getFont().getSize());

        //Display Game Over screen.
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(Screen_Width-metrics2.stringWidth("Game Over"))/2,Screen_Height/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            chekFood();
            checkCollisions();
        }
        repaint();
    }

    //inner class
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent a){
            //player controlling the snake
            switch(a.getExtendedKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
