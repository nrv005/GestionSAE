package com.example.gestionsae;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
		
		TextView txtDate = (TextView) findViewById(R.id.txtPresentDate);
		txtDate.setText(jourSeance + " - " + idSeance);
		
		saedb.open();
		
		/**
		 * Afficher la liste de Présent pour la date donnée jourSeance
		 * utiliser un simple_list_item_1, qui n'est pas sélectionnable.
		 */
		ArrayList<Membre> listPresent = saedb.getPresent(idSeance);
		ArrayAdapter<Membre> adaptPresent = new ArrayAdapter<Membre>(this, android.R.layout.simple_list_item_1, listPresent);
		ListView listViewPresent = (ListView) findViewById(R.id.listPresent);
		listViewPresent.setAdapter(adaptPresent);
	}
	
	
	/**
	 * onCreateOptionsMenu
	 * Affichage d'un menu lors de l'appui sur le bouton menu
	 * @return void
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflatMenu = getMenuInflater();
		inflatMenu.inflate(R.menu.present_menu, menu);
		return true;
	}
	
	/**
	 * onOptionsItemSelected
	 * Gestion des différents menu de la page présence.
	 * @return void
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menuModif:
			
			//Modification des membres présent pour une date vers l'activité SelectionActivity
			Intent selection = new Intent(PresentActivity.this, SelectionActivity.class);
			selection.putExtra("idSeance", idSeance);
			selection.putExtra("jourSeance", jourSeance);
			startActivity(selection);
			
			return true;
		}
		return false;
	}
}
