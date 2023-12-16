import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {

  // Image variables
  PImage imgBackground;
  PImage imgSnowflake;
  PImage imgPlayer;
  PImage imgPlayerFlipped;
  PImage imgHeart;

  // Snowflake variables
  float[] snowflakeY = new float[25];
  float[] snowflakeX = new float[25];
  boolean[] snowflakeHideStatus = new boolean[25];

  // Player variables
  float fltPlayerX;
  float fltPlayerY;
  float fltPlayerSpeedX;
  double dblPlayerSpeedY;
  double dblGravity;
  boolean blnFacingRight;

  // Game variables
  int intSnowflakeSpeed;
  int intLives;
  int intScore;

  public void settings() {

    size(1458, 657);
  }

  public void setup() {
    
    // Load images
    imgBackground = loadImage("background.png");
    imgSnowflake = loadImage("snowflake.png");
    imgPlayer = loadImage("player.png");
    imgPlayerFlipped = loadImage("playerflipped.png");
    imgHeart = loadImage("heart.png");

    // Resize images
    imgSnowflake.resize(imgSnowflake.width / 20, imgSnowflake.height / 20);
    imgPlayer.resize(imgPlayer.width / 10, imgPlayer.height / 10);
    imgPlayerFlipped.resize(imgPlayerFlipped.width / 10, imgPlayerFlipped.height / 10);
    imgHeart.resize(imgHeart.width / 10, imgHeart.height / 10);

    // Set random snowflake positions
    for (int i = 0; i < snowflakeY.length; i++) {
      snowflakeY[i] = random(height) - height;
      snowflakeX[i] = random(width);
    }

    // Initialize game variables
    intSnowflakeSpeed = 1;
    dblGravity = 0.2;
    intLives = 5;
    intScore = 0;
    
    // Set player position
    fltPlayerX = width / 2 - imgPlayer.width / 2;
    fltPlayerY = height - imgPlayer.height;

  }

  public void draw() {

    // Draw background
    background(imgBackground);

    // Text settings
    textSize(50);
    fill(0);

    // Game over screen
    if (intLives == 0) {

      text("Game Over", width / 2 - 100, height / 2);
      text("Your final Score: " + intScore, width / 2 - 200, height / 2 + 50);
      text("Press R to play again", width / 2 - 200, height / 2 + 100);
      noLoop();
    }

    // Game screen
    else {

      snowflake();
      movement();
      lives();

      // Display score
      intScore ++;
      text("Score: " + intScore, width - 300, 50);
    }
  } 

  /*
   * Draws the snowflakes and moves them down the screen
   * @author Joshua Yin
   * @param none
   * @return void
   */
  public void snowflake() {

    for (int i = 0; i < snowflakeY.length; i++) {

      // Move snowflakes down the screen
      snowflakeY[i] += intSnowflakeSpeed;

      if (snowflakeY[i] > height) {
        snowflakeX[i] = random(width);
        snowflakeY[i] = 0;
        snowflakeHideStatus[i] = false;
      }

      // Draw snowflakes if they are not hidden
      if (!snowflakeHideStatus[i]) {
        image(imgSnowflake, snowflakeX[i], snowflakeY[i]);
      }
    }
  }

  /*
   * Moves and draws the player
   * @author Joshua Yin
   * @param none
   * @return void
   */
  public void movement() {

    // Left-right movement
    if (keyPressed) {
      if (key == 'd') {
        fltPlayerSpeedX = 5;
        blnFacingRight = true;
      }
      else if (key == 'a' || keyCode == LEFT) {
        fltPlayerSpeedX = -5;
        blnFacingRight = false;
      }
    }
    else {
      fltPlayerSpeedX = 0;
    }

    // If the player is not on the ground, it falls
    if (fltPlayerY < height - imgPlayer.height) {
      dblPlayerSpeedY += dblGravity;
    }
    // Jumping
    else if ((keyPressed && key == 'w') || (keyPressed && key == ' ')) {
        dblPlayerSpeedY = -7;
    }
    // If the player is on the ground, then it stops falling
    else {
      dblPlayerSpeedY = 0;
    }
    fltPlayerX += fltPlayerSpeedX;
    fltPlayerY += dblPlayerSpeedY;

    // Collision, prevents the player from going off the screen
    if (fltPlayerX < 0) {
      fltPlayerX = 0;
    }
    else if (fltPlayerX > width - imgPlayer.width) {
      fltPlayerX = width - imgPlayer.width;
    }

    // Draws player with correct orientation
    if (blnFacingRight) {
      image(imgPlayer, fltPlayerX, fltPlayerY);
    }
    else {
      image(imgPlayerFlipped, fltPlayerX, fltPlayerY);
    }
  }
  
  /*
   * Draws the hearts and checks for collisions between the player and snowflakes
   * @author Joshua Yin
   * @param none
   * @return void
   */
  public void lives() {
    
    // Draw hearts
    for (int i = 0; i < intLives; i++) {
      image(imgHeart, 10 + (i * imgHeart.width), 10);
    }

    // If the player collides with a snowflake, they lose a life and the snowflake disappears
    for (int i = 0; i < snowflakeX.length; i++) {
      if (!snowflakeHideStatus[i] && fltPlayerX < snowflakeX[i] + imgSnowflake.width &&
          fltPlayerX + imgPlayer.width > snowflakeX[i] &&
          fltPlayerY < snowflakeY[i] + imgSnowflake.height &&
          fltPlayerY + imgPlayer.height > snowflakeY[i]) {

        intLives--;
        snowflakeHideStatus[i] = true;
      }
    }
  }

  /*
   * Changes the speed of the snowflakes when the up and down arrow keys are pressed and Restarts the game when the r key is pressed
   * @author Joshua Yin
   * @param none
   * @return void
   */
  public void keyPressed() {

    if (keyCode == DOWN) {
      intSnowflakeSpeed += 1;
    } 
    else if (keyCode == UP) {
      intSnowflakeSpeed -= 1;
    }
    
    if (key == 'r') {
      setup();
      loop();
    }
  }

  /*
   * Hides snowflakes when they are clicked on by the mouse
   * @author Joshua Yin
   * @param none
   * @return void
   */
  public void mousePressed() {
    
    for (int i = 0; i < snowflakeX.length; i++) {

      if (mouseX < snowflakeX[i] + imgSnowflake.width &&
          mouseX > snowflakeX[i] &&
          mouseY < snowflakeY[i] + imgSnowflake.height &&
          mouseY > snowflakeY[i]) {

        snowflakeHideStatus[i] = true;
      }
    }
  }
}
