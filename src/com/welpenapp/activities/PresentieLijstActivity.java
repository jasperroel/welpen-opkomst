package com.welpenapp.activities;

import java.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.welpenapp.db.sqlite.ModelHelper;
import com.welpenapp.model.*;
import com.welpenapp.model.Aanwezigheid.Status;

public class PresentieLijstActivity extends Activity {

    private final static String TAG = "WelpenAppPresentieLijstActivity";
    private ModelHelper data;
    private ListView innerPresentieLijst;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.presentielijst);

        data = new ModelHelper(this);

        innerPresentieLijst = (ListView) findViewById(R.id.presentielijst);

        Button button = (Button) findViewById(R.id.opkomstChooserInPresentieLijst);
        createOnClickHandlerForGroupChooser(button);

        // populateList();
        // populateListFromContacts();
        // queryForContact();
        // queryForGroup();

        // TODO Hardcoded
        // Group groups = new Group(getContentResolver());
        // groups.getAllGroups();
        // List<String> names = groups.getMembersOfGroupByName("Bever");
        // populateList(names);
        // Log.d(TAG, names.toString());

        // CursorAdapter dataSource = new SimpleCursorAdapter(this,
        // R.layout.presentielijst, all, p.getColumns(),
        // new int[] { R.id.presentielijst, R.id.presentielijst });
        //
        // setListAdapter(dataSource);
        //
        // List<Person> allPerson = p.getAllFromCursor(all);
        // List<String> allNames = new LinkedList<String>();
        // for (Person person : allPerson) {
        // allNames.add(person.getName());
        // }
        //
        // setListAdapter(new ArrayAdapter<String>(this, R.layout.presentielijst,
        // allNames));
        //
        // ListView lv = getListView();

        // /// X
        // populateList();
        // innerPresentieLijst.setTextFilterEnabled(true);
        //
        // innerPresentieLijst.setOnItemClickListener(new OnItemClickListener() {
        // private Person p;
        //
        // /**
        // * <p>This will display the dialog with availability options. See getDialog().</p>
        // */
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        // // String name = "";
        // // parent.getChildAt(position);
        // TextView firstnameView = (TextView) ((LinearLayout) view).getChildAt(0);
        // TextView lastnameView = (TextView) ((LinearLayout) view).getChildAt(2);
        // String firstname = firstnameView.getText().toString();
        // String lastname = lastnameView.getText().toString();
        // // String name = ((TextView) findViewById(R.id.contactEntryText)).getText().toString();
        // // contactEntryText
        // // String name = ((TextView) view).getText().toString();
        // // String name = ((TextView) view).getText().toString();
        // p = new Person(data).get(firstname + " " + lastname);
        //
        // // TODO Hard-coded
        // Opkomst o = new Opkomst(data);
        // Opkomst opkomst = o.get(1);
        //
        // Dialog dialog = getDialog(p, opkomst);
        // dialog.show();
        // }
        // });
    }

    private void createOnClickHandlerForGroupChooser(Button button) {
        final PopupMenu menu = new PopupMenu(this, button);
        menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                return onOpkomstGekozen(item);
            }
        });

        Menu m = menu.getMenu();
        populateContactGroups(m);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menu.show();
            }
        });
    }

    public boolean populateContactGroups(Menu menu) {
        int groupId = 0;
        Group group = new Group(getContentResolver());
        Map<String, String> groups = group.getAllGroups();

        for (String id : groups.keySet()) {
            menu.add(groupId, Integer.valueOf(id), Menu.NONE, groups.get(id));
        }

        return true;
    }

    public boolean onOpkomstGekozen(MenuItem item) {
        int itemId = item.getItemId();

        Group g = new Group(getContentResolver());
        String groupName = g.getAllGroups().get(itemId + "");

        List<String> names = g.getMembersOfGroupByName(groupName);
        populateList(names);

        return true;

        // If we don't handle the menu, return false
        // return false;
    }

    public void populateList() {
        Person p = new Person(data);

        Cursor all = p.getAllAsCursor();

        // CursorAdapter dataSource = new SimpleCursorAdapter(this,
        // R.layout.presentielijst, all, p.getColumns(),
        // new int[] { R.id.presentielijst, R.id.presentielijst });

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, all,
            new String[] { Person.colFirstName, Person.colLastName }, new int[] { R.id.contactFirstNameText,
                R.id.contactLastNameText });

        innerPresentieLijst.setAdapter(adapter);
    }

    public void populateList(List<String> items) {
        // SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, items,
        // new String[] { Person.colFirstName, Person.colLastName }, new int[] { R.id.contactFirstNameText,
        // R.id.contactLastNameText });
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        innerPresentieLijst.setAdapter(aa);

    }

    private void populateListFromContacts() {
        String[] projection = { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };

        // Get a Cursor over the Contacts Provider
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);

        // Get the index(es) of the columns
        int nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
        int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);

        // Get the actual results
        String[] result = new String[cursor.getCount()];

        // Fill the result
        while (cursor.moveToNext()) {
            // Get the name & ID
            String name = cursor.getString(nameIdx);
            String id = cursor.getString(idIdx);
        }

        cursor.close();
    }

    // This is code to pick 1 particular contact from a full list
    private int PICK_CONTACT = 0;

    private void queryForContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), data.getDataString(), Toast.LENGTH_SHORT).show();
        }
    }

    // End of the "1 particular contact" code

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
