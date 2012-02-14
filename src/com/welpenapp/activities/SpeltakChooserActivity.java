package com.welpenapp.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>Je speltak uitkiezen kan hier.</p>
 * 
 * <p>TODO! Deze instelling moet worden opgeslagen en onthouden.</p>
 * 
 * @author Jasper Roel
 * 
 */
public class SpeltakChooserActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] speltakken = getResources().getStringArray(R.array.speltakken);

        setListAdapter(new ArrayAdapter<String>(this, R.layout.speltak_chooser, speltakken));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                    Toast.LENGTH_SHORT).show();
            }
        });

    }
}
