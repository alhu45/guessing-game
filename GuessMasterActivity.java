//Alan Hu 20352082

package com.example.guessmaster;

//All the libraries
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.Random;
import android.content.DialogInterface;

public class GuessMasterActivity extends AppCompatActivity {
    private TextView entityName;
    private TextView ticketsum;
    private Button guessButton;
    private EditText userIn;
    private Button btnclearContent;
    private ImageView entityImage;
    String answer;

    private int numOfEntities;
    private Entity currentEntity;
    private Entity[] entities;
    private int numOfTickets = 0;
    String entName; //Stores Entity Name
    int entityid = 0;
    private int totalTicketNum = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Total tickets won from user
        ticketsum = (TextView) findViewById(R.id.ticket);

        //Image of entity
        entityImage = (ImageView) findViewById(R.id.entityImage);

        //Name of entity
        entityName = (TextView) findViewById(R.id.entityName);

        //Button for inputting guess from user
        userIn = (EditText) findViewById(R.id.guessinput);

        //Defining the buttons created in the XML file
        guessButton = (Button) findViewById(R.id.btnGuess);

        //Button to clear
        btnclearContent = (Button) findViewById(R.id.btnClear);

        //Adding all the entities
        Politician jTrudeau = new Politician("Justin Trudeau", new Date("December", 25, 1971), "Male", "Liberal", 0.25);////
        Singer cDion = new Singer("Celine Dion", new Date("March", 30, 1961), "Female", "La voix du bon Dieu", new Date("November", 6, 1981), 0.5);////
        Person myCreator = new Person("My Creator", new Date("September", 1, 2000),"Female", 1);////
        Country usa = new Country("United States", new Date("July", 4, 1776), "Washinton D.C.", 0.1);////

        new GuessMasterActivity();

        //Adding entities to play the game
        addEntity(usa);
        addEntity(myCreator);
        addEntity(jTrudeau);
        addEntity(cDion);
        changeEntity();

        welcomeToGame(currentEntity);

        //When clicked, game starts
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method that changes the entity here
                playGame(currentEntity);
            }
        });

        //When clicked, new entity is to be guessed
        btnclearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method that changes the entity here
                changeEntity();
            }
        });
    }

    //Methods from previous files
    public GuessMasterActivity() {
        numOfEntities = 0;
        entities = new Entity[10];
        numOfTickets = 0;
    }
    public int genRandomEntityId() {
        Random randomNumber = new Random();
        return randomNumber.nextInt(numOfEntities);
    }
    public void addEntity(Entity entity) {
        entities[numOfEntities++] = entity.clone();
    }
    public void playGame(int entityId) {
        Entity entity = entities[entityId];
        playGame(entity);
    }
    public void playGame() {
        int entityId = genRandomEntityId();
        playGame(entityId);
    }

    //The play game method, changes were done so it would display messages rather than printing them on terminal
    public void playGame(Entity entity) {

        //Name of the entity to be guessed in the entityName textview
        entityName.setText(entity.getName());

        //Get Input from the EditText
        answer = userIn.getText().toString();
        answer = answer.replace("\n", "").replace("\r", "");
        Date date = new Date(answer);
        answer = answer.replace("\n", "").replace("\r", "");

        //If it is the wrong date, displays notification that says try later date
        if (date.precedes(entity.getBorn())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GuessMasterActivity.this);
            builder.setTitle("Incorrect");
            builder.setMessage("Try a later date.");
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //User clicks the OK button, dismisses message
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        //If it is the wrong date, displays notification that says try earlier date
        } else if (entity.getBorn().precedes(date)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GuessMasterActivity.this);
            builder.setTitle("Incorrect");
            builder.setMessage("Try an earlier date.");
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //User clicks the OK button
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        //If the date is right, adds tickets and tells user they won
        } else {
            //Adds tickets to total tickets
            numOfTickets = entity.getAwardedTicketNumber();
            totalTicketNum += numOfTickets;
            String total = (new Integer(totalTicketNum)).toString();

            //Alerts user they've won
            AlertDialog.Builder builder = new AlertDialog.Builder(GuessMasterActivity.this);
            builder.setTitle("You won");
            builder.setMessage("BINGO! " + entity.closingMessage());


            //Continues the game
            builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    //Adds the awarded ticket number to the total and show a toast message
                    totalTicketNum += entity.getAwardedTicketNumber();
                    Toast.makeText(GuessMasterActivity.this, "You won " + entity.getAwardedTicketNumber() + " tickets!", Toast.LENGTH_SHORT).show();
                    ContinueGame();
                }
            });
            ticketsum.setText("Total Tickets: " + total);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //Method to continue the game
    public void ContinueGame() {
        userIn.getText().clear();
        changeEntity();
    }

    //Changes the entity on the screen and game
    public void changeEntity() {
        //Clears guess
        userIn.setText("");

        //Generate a random entity ID
        entityid = genRandomEntityId();

        //Get the new entity using the random ID
        Entity newEntity = entities[entityid];

        entName = newEntity.getName();
        //Update the frontend with the new entity's information
        entityName.setText(entName);

        //Update image
        imageSetter();

        //Display a message to the user about the change
        Toast.makeText(this, "New entity loaded! Try guessing again.", Toast.LENGTH_SHORT).show();

        //Makes the current entity to the new entity
        currentEntity = newEntity;
    }

    //Getting images, called in the changeEntity method
    public void imageSetter() {
        String name = entName;
        switch (name) {
            case "United States":
                entityImage.setImageResource(R.drawable.usaflag);
                break;
            case "Justin Trudeau":
                entityImage.setImageResource(R.drawable.justint);
                break;
            case "Celine Dion":
                entityImage.setImageResource(R.drawable.celidion);
                break;
            default:
                entityImage.setImageResource(R.drawable.bot);
                break;
        }
    }
    
    public void welcomeToGame(Entity entity) {
        AlertDialog.Builder welcomeAlert = new AlertDialog.Builder(GuessMasterActivity.this);
        welcomeAlert.setTitle("GuessMaster Game v3");
        welcomeAlert.setMessage(entity.welcomeMessage());
        welcomeAlert.setCancelable(false); // This disables the ability to cancel the alert by clicking outside of it

        welcomeAlert.setNegativeButton("START GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Game is Starting...Enjoy", Toast.LENGTH_SHORT).show();
            }
        });

        // Creating and showing the dialog
        AlertDialog dialog = welcomeAlert.create();
        dialog.show();
    }
}