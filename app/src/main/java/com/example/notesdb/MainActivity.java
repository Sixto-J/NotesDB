package com.example.notesdb;

    import android.content.Context;
    import android.database.Cursor;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner my_spinner;
    Button btnAdd;
    Button btnVeure;
    EditText inputLabel;
    EditText inputComentari;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        my_spinner = findViewById(R.id.spinner);
        btnAdd =  findViewById(R.id.crear);
        btnVeure = findViewById(R.id.veure);
        inputLabel = findViewById(R.id.edit_titol);
        inputComentari = findViewById(R.id.edit_comentari);

        databaseHandler = new DatabaseHandler(this);

        my_spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String label = inputLabel.getText().toString();
                String text = inputComentari.getText().toString();

                if (label.trim().length() > 0 && text.trim().length() > 0) {
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                    db.insert(label, text);

                    // making input filed text to blank
                    inputLabel.setText("");
                    inputComentari.setText("");

                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);
                    // loading spinner with newly added data
                    loadSpinnerData();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter label name",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnVeure.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            mostrarComentari();

        }

        });



        Button btnDelete = findViewById(R.id.eliminar);
        btnDelete.setOnClickListener(new View.OnClickListener() {

          @Override
              public void onClick(View arg0) {
              int selectedItemId = my_spinner.getSelectedItemPosition();
              Log.d("POSICION", "Selected Item Position: " + String.valueOf(selectedItemId));
              databaseHandler.deleteRow(selectedItemId);
              loadSpinnerData();

               }
        });

    }




    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        List<String> labels = databaseHandler.getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        my_spinner.setAdapter(dataAdapter);
    }

    private void mostrarComentari(){
        logCursorData();

        List<String> comentaris = databaseHandler.getAllComentaris();
        String comentari_localitzat = comentaris.get(
                my_spinner.getSelectedItemPosition());
        EditText comentari = findViewById(R.id.edit_veure);
        comentari.setText(comentari_localitzat);

    }

    // Metodo para mostrar columnas
    private void logCursorData() {
        Cursor cursor = databaseHandler.getAllData();
        if (cursor != null) {
            // Get the number of columns
            int columnCount = cursor.getColumnCount();
            Log.d("CursorData", "Column Count: " + columnCount);

            // Iterate through the rows
            while (cursor.moveToNext()) {
                StringBuilder rowData = new StringBuilder();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i); // or cursor.getInt(i), etc. based on the data type
                    rowData.append(columnName).append(": ").append(columnValue).append(", ");
                }
                Log.d("CursorData", "Row Data: " + rowData.toString());
            }
            cursor.close(); // Close the cursor
        } else {
            Log.d("CursorData", "Cursor is null or empty.");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}