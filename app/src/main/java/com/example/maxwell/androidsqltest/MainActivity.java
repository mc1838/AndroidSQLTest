package com.example.maxwell.androidsqltest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.example.maxwell.androidsqltest.MESSAGE";

    Button btnSelect;
    Button btnInsert;
    Button btnUpdate;
    Button btnDelete;
    Button btnLogin; //loginButton
    Button btnRegister; //registerButton
    CheckBox chkDM; //DMcheckBox
    TextView testResult;
    boolean isDM = false;
    String connectionString = "jdbc:jtds:sqlserver://csc450.database.windows.net:1433/" +
        "testdb;" +
        "user=mc1838@csc450;password=Project450;";
    Connection connection = null;

    // Stored set of test SQL query strings:
    String SQLSelectTest = "SELECT * FROM [dbo].[DungeonMaster]";
    String SQLInsertTest = "INSERT INTO [dbo].[DungeonMaster] ([Game_IDs])"
            + " VALUES ('1,2,3')";
    String SQLUpdateTest = "UPDATE [dbo].[DungeonMaster]"
            + " SET [Game_IDs] = '4,5,6'"
            + " WHERE [Game_IDs] = '1,2,3';";
    String SQLDeleteTest = "DELETE FROM [dbo].[DungeonMaster] "
            + "WHERE [Game_IDs] = '4,5,6'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSelect = (Button) findViewById(R.id.btnSelect); //init button to actual button in layout
        btnSelect.setOnClickListener(MainActivity.this);

        btnInsert = (Button) findViewById(R.id.btnInsert); //init button to actual button in layout
        btnInsert.setOnClickListener(MainActivity.this);

        btnUpdate = (Button) findViewById(R.id.btnUpdate); //init button to actual button in layout
        btnUpdate.setOnClickListener(MainActivity.this);

        btnDelete = (Button) findViewById(R.id.btnDelete); //init button to actual button in layout
        btnDelete.setOnClickListener(MainActivity.this);

        btnLogin = (Button) findViewById(R.id.loginButton); //init button to actual button in layout
        btnLogin.setOnClickListener(MainActivity.this);

        btnRegister = (Button) findViewById(R.id.registerButton); //init button to actual button in layout
        btnRegister.setOnClickListener(MainActivity.this);

        chkDM = (CheckBox) findViewById(R.id.DMcheckBox);
        chkDM.setOnClickListener(MainActivity.this);

        testResult = (TextView) findViewById(R.id.testResult);

    }
    public void onClick(View view)
    {
        if (view == btnSelect)
        {
            String result = connectSelect(SQLSelectTest);

            Intent intent = new Intent(this, DisplayResultsActivity.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity(intent);
        }

        else if (view == btnInsert)
        {
            String result = "";

            connectExecute(SQLInsertTest);
            result = connectSelect(SQLSelectTest);
            Intent intent = new Intent(this, DisplayResultsActivity.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity(intent);
        }

        else if (view == btnUpdate)
        {
            String result = "";

            connectExecute(SQLUpdateTest);
            result = connectSelect(SQLSelectTest);
            Intent intent = new Intent(this, DisplayResultsActivity.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity(intent);
        }

        else if (view == btnDelete)
        {
            String result = "";

            connectExecute(SQLDeleteTest);
            result = connectSelect(SQLSelectTest);
            Intent intent = new Intent(this, DisplayResultsActivity.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity(intent);
        }

        else if (view == btnLogin)
        {
            //Login actions
        }

        else if (view == btnRegister)
        {
            //Register actions
        }

        else if (view == chkDM)
        {
            //Set DM flag!
            if (chkDM.isChecked())
            {
                isDM = true;
                testResult.setText("DM true!");
            }

            else
            {
                isDM = false;
                testResult.setText("DM false!");
            }
        }
    } //end onClick

    /**
     * connectSelect - Select SQL statement connection,
     *  modified for Android.
     * @param SQLQuery
     * @return result - the resulting data from Select stmt.
     */
    @SuppressLint("NewApi")
    public String connectSelect(String SQLQuery)
    {
        // Allow all forms of Networking threading
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String result = "";
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionString);
        }

        catch (Exception e)
        {
            Log.w("Error with connection: ", e.getMessage());
            Toast.makeText(this, "getC "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQLQuery))
        {
            ResultSetMetaData rsmd = resultSet.getMetaData(); //needed for column data/indices
            int columns = rsmd.getColumnCount();
            result += "Select Stmt Result for Table:\n";
            String columnNames = "";
            for (int i=1; i<=columns; i++)
            {
                columnNames += rsmd.getColumnLabel(i) + "\t";
            }

            result += columnNames + "\n";
            result += "============================\n";

            while (resultSet.next())
            {
                for (int i=1; i<=columns; i++)
                {
                    result += resultSet.getString(i) + "\t"; //each col. item, tabbed
                }

            }

            connection.close();
            statement.close();
            resultSet.close();
        }

        catch (Exception e)
        {
            Log.w("Error with connection: ", e.getMessage());
            Toast.makeText(this, "exec " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    /**
     * connectExecute - Execute a SQL statement which
     * returns no result, such as Insert/Update/Delete.
     * @param SQLQuery - the SQL statement to execute
     */
    @SuppressLint("NewApi")
    public void connectExecute(String SQLQuery)
    {
        // Allow all forms of Networking threading
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(connectionString);
        }

        catch (Exception e)
        {
            Log.w("Error with connection: ", e.getMessage());
            Toast.makeText(this, "getC "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try (Statement exstatement = connection.createStatement())
        {
            exstatement.execute(SQLQuery);

            connection.close();
            exstatement.close();
        }

        catch (Exception e)
        {
            Log.w("Error with connection: ", e.getMessage());
            Toast.makeText(this, "exec " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
