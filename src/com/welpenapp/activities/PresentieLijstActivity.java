package com.welpenapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.welpenapp.model.ModelHelper;
import com.welpenapp.model.Person;

public class PresentieLijstActivity extends ListActivity {

    ModelHelper data;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new ModelHelper(this);

        Person p = new Person(data);

        Cursor hathi = p.getAsCursor(1);

        CursorAdapter dataSource = new SimpleCursorAdapter(this,
            R.layout.presentielijst, hathi, p.getColumnNames(),
            new int[] { R.id.presentielijst, R.id.presentielijst });

        setListAdapter(dataSource);

        String[] names = { p.getFromCursor(hathi).getName() };
        setListAdapter(new ArrayAdapter<String>(this, R.layout.presentielijst,
            names));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
            private Person p;

            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                String name = ((TextView) view).getText().toString();
                p = new Person(data).get(1);

                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(),
                    name, Toast.LENGTH_SHORT).show();

                Dialog dialog = getDialog(p);
                dialog.show();
            }
        });
    }

    final CharSequence[] dialogOptions = { "Aanwezig", "Afwezig (met kennisgeving)", "Afwezig (zonder kennisgeving)" };

    public Dialog getDialog(final Person p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(p.getName());
        // builder.setItems(dialogOptions, new DialogInterface.OnClickListener() {
        builder.setSingleChoiceItems(dialogOptions, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                Toast.makeText(getApplicationContext(), p.getName() + " is " + dialogOptions[item], Toast.LENGTH_SHORT)
                    .show();
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
