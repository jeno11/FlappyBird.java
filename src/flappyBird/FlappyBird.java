package flappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
    public static FlappyBird flappyBird;

    public final int Width = 800;
    public final int Height = 800;

    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public int ticks, yMotion, score;
    public boolean gameOver, started;
    public Random random;

    public FlappyBird(){
        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        random = new Random();

        jFrame.add(renderer);
        jFrame.setTitle("Flappy Bird");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(Width, Height);
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setResizable(false);
        jFrame.setVisible(true);

        bird = new Rectangle(Width/2 - 10, Height/2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    public void addColumn(boolean start){
        int space = 300;
        int width = 100;
        int height = 50 + random.nextInt(300);

        if (start){
            columns.add(new Rectangle(Width + width + columns.size() * 300, Height - 120 - height, width, height));
            columns.add(new Rectangle(Width + width + (columns.size() - 1) * 300, 0, width, Height - height - space));
        } else{
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, Height - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, Height - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.red.darker().darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump(){
        if (gameOver){
            bird = new Rectangle(Width/2 - 10, Height/2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        } if (!started){
            started = true;
        } else if (!gameOver) {
            if (yMotion > 0){
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 5;
        ticks++;

        if (started){
            for (int i = 0; i < columns.size(); i++){
                Rectangle column = columns.get(i);
                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15){
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++){
                Rectangle column = columns.get(i);

                if (column.x + column.width < 0){
                    columns.remove(column);

                    if (column.y == 0){
                        addColumn(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle column : columns){
                if (column.y == 0 && bird.x + bird.width/2 > column.x + column.width/2 - 10 && bird.x + bird.width/2 < column.x + column.width/2 +10){
                    score++;
                }

                if (column.intersects(bird)){
                    gameOver = true;

                    if (bird.x <= column.x){
                        bird.x = column.x - bird.width;
                    } else {
                        if (column.y != 0){
                            bird.y = column.y - bird.height;
                        } else if (bird.y < column.height) {
                            bird.y = column.height;
                        }
                    }
                }
            }

            if (bird.y > Height - 120 || bird.y < 0){
                gameOver = true;
            }

            if (bird.y + yMotion >= Height -120){
                bird.y = Height - 120 - bird.height;
                gameOver = true;
            }
        }

        renderer.repaint();
    }

    public void repaint(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, Width, Height);

        g.setColor(Color.lightGray);
        g.fillRect(0, Height - 120, Width, 120);

        g.setColor(Color.red.darker());
        g.fillRect(0, Height - 120, Width, 20);

        g.setColor(Color.cyan);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : columns){
            paintColumn(g, column);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));

        if (!started){
            g.drawString("Click to Start!", 75, Height/2 - 50);
        }

        if (gameOver){
            g.drawString("Game Over!", 100, Height/2 - 50);
        }

        if (!gameOver && started){
            g.drawString(String.valueOf(score), Width/2 - 25, 100);
        }
    }

    public static void main(String[] args){flappyBird = new FlappyBird();}

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            jump();
        }
    }


}
