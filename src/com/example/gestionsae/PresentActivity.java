package com.example.gestionsae;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PresentActivity extends Activity {
	
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
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflatMenu = getMenuInflater();
		inflatMenu.inflate(R.menu.present_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menuModif:
			
			Intent selection = new Intent(PresentActivity.this, SelectionActivity.class);
			selection.putExtra("id_seance", idSeance);
			selection.putExtra("j_seance", jourSeance);
			startActivity(selection);
			
			return true;
		}
		return false;
	}
}
