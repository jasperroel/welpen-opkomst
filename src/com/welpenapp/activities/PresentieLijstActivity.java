package com.welpenapp.activities;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.welpenapp.db.sqlite.ModelHelper;
import com.welpenapp.model.Aanwezigheid;
import com.welpenapp.model.Aanwezigheid.Status;
import com.welpenapp.model.Opkomst;
import com.welpenapp.model.Person;

public class PresentieLijstActivity extends ListActivity {

    ModelHelper data;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new ModelHelper(this);

        Person p = new Person(data);
        Opkomst o = new Opkomst(data);

        // TODO Hard-coded
        final Opkomst opkomst = o.get(1);

        Cursor all = p.getAllAsCursor();

        CursorAdapter dataSource = new SimpleCursorAdapter(this,
            R.layout.presentielijst, all, p.getColumns(),
            new int[] { R.id.presentielijst, R.id.presentielijst });

        setListAdapter(dataSource);

        List<Person> allPerson = p.getAllFromCursor(all);
        List<String> allNames = new LinkedList<String>();
        for (Person person : allPerson) {
            allNames.add(person.getName());
        }

        setListAdapter(new ArrayAdapter<String>(this, R.layout.presentielijst,
            allNames));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
            private Person p;

            /**
             * <p>This will display the dialog with availability options. See getDialog().</p>
             */
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                String name = ((TextView) view).getText().toString();
                p = new Person(data).get(name);
                Dialog dialog = getDialog(p, opkomst);
                dialog.show();
            }
        });
    }

    public Dialog getDialog(final Person p, final Opkomst o) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String date = DateFormat.format("E, MMMM dd, yyyy", o.getDate()).toString();
        builder.setTitle(p.getName() + " voor " + date);
        final CharSequence[] dialogOptions = Aanwezigheid.getStatusValues();

        int selectedItem = -1;
        try {
            Aanwezigheid a = new Aanwezigheid(data).get(p, o);
            selectedItem = a.getStatus().ordinal();
        } catch (RuntimeException e) {
            // NOOP - this happens if there is no item...
        }

        builder.setSingleChoiceItems(dialogOptions, selectedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                Toast.makeText(getApplicationContext(), p.getName() + " is " + dialogOptions[item], Toast.LENGTH_SHORT)
                    .show();

                // Figure out the right enum :/
                Status s = Status.getByValue(dialogOptions[item].toString());
                // Cool, update the DB!
                Aanwezigheid a = new Aanwezigheid(data);
                a.add(p, o, s);

                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
