package com.example.gestionsae;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PresentActivity extends Activity{
	
	GestionBdd saedb = new GestionBdd(this);
	long idSeance;
	String jourSeance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extra = this.getIntent().getExtras();
		if (extra != null) {
			idSeance = extra.getLong("idSeance");
			jourSeance = extra.getString("jourSeance");
		}
		
		setContentView(R.layout.activity_present);
		
		saedb.open();
		
		/**
		 * Afficher la liste de Présent pour la date donnée jourSeance
		 * utiliser un simple_list_item_1, qui n'est pas sélectionnable.
		 */
		ArrayList<Membre> listPresent = saedb.getPresent(idSeance);
		ArrayAdapter<Membre> adaptPresent = new ArrayAdapter<Membre>(this, android.R.layout.simple_list_item_1, listPresent);
		ListView listViewPresent = (ListView) findViewById(R.id.listPresent);
		listViewPresent.setAdapter(adaptPresent);
		
		
		
		//TODO Terminer la possibilité de modification lors d'un appui long sur la vue
		/**
		 * Un long click sur la view affiche un boite de dialogue pour valider la possibilité de modifier
		 * la liste des présents pour cette séance.
		 */
		listViewPresent.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				Toast.makeText(getApplicationContext(), "Appuie long", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		
		listViewPresent.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				Log.i("Test", "Appuis long sur la view");
				Toast.makeText(getApplicationContext(), "Appuie long par listvie", Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}
}
