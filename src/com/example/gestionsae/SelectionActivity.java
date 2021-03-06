package com.example.gestionsae;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SelectionActivity extends Activity{
	
	GestionBdd saedb = new GestionBdd(this);
	ArrayList<Membre> listMembre = new ArrayList<Membre>();
	ArrayList<Membre> listPresent = new ArrayList<Membre>();
	ListView listViewMembre;
	TextView txtDate;
	
	long idSeance;
	String jourSeance;
	int nbrPresent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * Récupération des données de l'Activité précédente
		 * idSeance : long, correspond à l'identifiant de la seance dans la table date = date.did
		 * jSeance : long, correspond à la date de la séance dans la table date = date.jour
		 */
		Bundle extra = this.getIntent().getExtras();
		if (extra != null) {
			idSeance = extra.getLong("idSeance");
			jourSeance = extra.getString("jourSeance");
			
		}
		
		setContentView(R.layout.activity_selection);
		
		saedb.open();
		listMembre = saedb.getAllMembre();
		listPresent = saedb.getPresent(idSeance);
		
		
		//Affiche la date de la séance et le nombre de présent
		txtDate = (TextView) findViewById(R.id.txtSelectionDate);
		nbrPresent = saedb.countPresent(idSeance);
		txtDate.setText(jourSeance + " - " + nbrPresent);
		
		afficheMembre(listMembre);
		affichePresent(listPresent);
		
		/**
		 * setOnItemClick : Gestion de la sélection des membres
		 * Cocher : on ajoute l'entrée dans la table présence avec fkdate, fknom
		 * Décocher : on supprime l'entrée dans la table présence.
		 */
		listViewMembre.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
				Membre nMembre = new Membre();
				nMembre = listMembre.get(position);
				int idMembre = nMembre.getId();
				
				if (listViewMembre.isItemChecked(position)) {
					saedb.setPresence(idSeance, idMembre);
					
					nbrPresent = saedb.countPresent(idSeance);
					txtDate.setText(jourSeance + " - " + nbrPresent);
				}
				else {
					saedb.suppPresence(idSeance, idMembre);

					nbrPresent = saedb.countPresent(idSeance);
					txtDate.setText(jourSeance + " - " + nbrPresent);
				}
			}
		});
		
		/**
		 * Button.setOnClickListener : Gestion du bouton "Nouveau Membre"
		 * On clique sur le bouton pour afficher une boite de dialogue "R.layout.new_membre"
		 * On obtient le nom et prénom, que l'on rajoute dans la table membre nom,prenom
		 */
		Button newMembre = (Button) findViewById(R.id.btSelectionNewMembre);
		newMembre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				LayoutInflater inflater = getLayoutInflater();
				final View dialogLayout = inflater.inflate(R.layout.new_membre, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
				builder.setView(dialogLayout);
				
				builder.setPositiveButton("Enregister", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						EditText newNom = (EditText) dialogLayout.findViewById(R.id.editNom);
						String nom = newNom.getText().toString();
						EditText newPrenom = (EditText) dialogLayout.findViewById(R.id.editPrenom);
						String prenom = newPrenom.getText().toString();
						saedb.setNewMembre(nom, prenom);
						
						listMembre = saedb.getAllMembre();
						afficheMembre(listMembre);
						affichePresent(listPresent);
					}
				});
				
				builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				builder.show();
			}
		});
	}

	/**
	 * afficheMembre : Affiche la liste des membres
	 * @param listMembre : ArrayList<Membre> liste des membres de la table membre.
	 * @return void
	 */
	private void afficheMembre(ArrayList<Membre> listMembre2) {
		ArrayAdapter<Membre> adaptMembre = new ArrayAdapter<Membre>(this, android.R.layout.simple_list_item_multiple_choice, listMembre2);
		listViewMembre = (ListView) findViewById(R.id.listSelection);
		listViewMembre.setAdapter(adaptMembre);
	}
	
	/**
	 * affichePresent : Coche la case des membres présent à cette séance
	 * @param lPresent : ArrayList<Membre> liste des membres présent pour la date
	 * @return void
	 */
	private void affichePresent(ArrayList<Membre> lPresent) {
		
		//
		int index = 0, position = 0;
		
		while (index < lPresent.size()) {
			
			int idPresent = lPresent.get(index).getId();
			boolean boucle = true;
			
			while (boucle && position < listMembre.size()) {
				
				int idMembre = listMembre.get(position).getId();
				position ++;
				
				if (idPresent == idMembre) {
					listViewMembre.setItemChecked(position - 1, true);
					boucle = false;
				}
			}
			index ++;
		}
	}
	
	/**
	 * onKeyDown : Gestion de la touche retour
	 * En cas d'appuie sur la touche retour, on revient sur la liste des séances.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent main = new Intent(SelectionActivity.this, MainActivity.class);
			startActivity(main);
		}
		return true;
	}

}
