package com.welpenapp.model;

import java.util.*;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * <p>This represents a group in your phone.</p>
 * 
 * <p>We use this to keep track of what {@link Person}s are in a {@link Speltak}. (?)</p>
 * 
 * @author Jasper Roel
 * 
 */
public class Group {

    private final static String TAG = "com.welpenapp.model.Group";

    private final ContentResolver cr;

    public Group(ContentResolver cr) {
        this.cr = cr;
    }

    /**
     * 
     * @param name The name of a group in the Contacts API.
     * @return A list of Display names of contacts in the group
     */
    public List<String> getMembersOfGroupByName(String name) {
        List<String> result = new LinkedList<String>();
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_GROUP_URI, name);

        String[] projection = new String[] { ContactsContract.Contacts._ID };

        Cursor idCursor = getContentResolver().query(lookupUri, projection, null, null, null);
        Log.d(TAG, "cursor Count:" + idCursor.getCount());

        while (idCursor.moveToNext()) {
            int idIdx = idCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            String id = idCursor.getString(idIdx);
            String _name = getContactName(id);
            if (null == _name) {
                // Try again, but without the mimetype
                _name = getContactNameSimplified(id);
                if (null == _name) {
                    Log.w(TAG, "name is null for id " + id + "?!");
                    continue;
                }
            }
            result.add(_name);
        }

        idCursor.close();

        return result;
    }

    /**
     * 
     * @param contactId a valid Contact ID
     * @return the Display Name of a contact
     */
    private String getContactName(String contactId) {
        String where = ContactsContract.Data.CONTACT_ID + " = " + contactId + " AND " + ContactsContract.Data.MIMETYPE
            + " = '" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' ";

        String[] projection = new String[] { ContactsContract.Data.DISPLAY_NAME }; // ,
                                                                                   // ContactsContract.CommonDataKinds.Phone.NUMBER
                                                                                   // };

        Cursor dataCursor = getContentResolver()
            .query(ContactsContract.Data.CONTENT_URI, projection, where, null, null);

        int nameIdx = dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
        // int phoneIdx = dataCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

        if (dataCursor.moveToFirst()) {
            String _name = dataCursor.getString(nameIdx);
            // String _number = dataCursor.getString(phoneIdx);
            Log.d(TAG, "contact ID [" + contactId + "]:" + _name);
            return _name;
        }

        return null;
    }

    /**
     * <p>TODO Move to Person.</p>
     * 
     * @param contactId
     * @return
     */
    private String getContactNameSimplified(String contactId) {
        String where = ContactsContract.Data.CONTACT_ID + " = " + contactId;

        String[] projection = new String[] { ContactsContract.Data.DISPLAY_NAME }; // ,
                                                                                   // ContactsContract.CommonDataKinds.Phone.NUMBER
                                                                                   // };

        Cursor dataCursor = getContentResolver()
            .query(ContactsContract.Data.CONTENT_URI, projection, where, null, null);

        int nameIdx = dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME);
        // int phoneIdx = dataCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

        if (dataCursor.moveToFirst()) {
            String _name = dataCursor.getString(nameIdx);
            // String _number = dataCursor.getString(phoneIdx);
            Log.d(TAG, "contact ID [" + contactId + "]:" + _name);
            return _name;
        }

        return null;
    }

    /**
     * <p>Request all groups from Contacts, ask the user which group he wants to use, return the _ID for that group.</p>
     * 
     * @return Map<String, String> key = ID, value = name
     */
    public Map<String, String> getAllGroups() {
        String[] projection = { ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };

        // Get a Cursor over the Contacts Provider
        Cursor cursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, projection, null, null, null);

        // Get the index(es) of the columns
        int titleIdx = cursor.getColumnIndexOrThrow(ContactsContract.Groups.TITLE);
        int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Groups._ID);

        // Get the actual results
        Map<String, String> results = new HashMap<String, String>();

        // Fill the result
        while (cursor.moveToNext()) {
            // Get the name & ID
            String id = cursor.getString(idIdx);
            String title = cursor.getString(titleIdx);
            results.put(id, title);

            Log.d(TAG, "id:" + id + "title:" + title);
        }

        cursor.close();

        return results;

    }

    /**
     * <p>Mimics the behavior of a screen.</p>
     * 
     * <p>PS: This is a hack until I know a better way :-).</p>
     * 
     * @return
     */
    private ContentResolver getContentResolver() {
        return this.cr;
    }

}
