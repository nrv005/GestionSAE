package com.example.gestionsae;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class PresenceActivity extends Activity{
	
	GestionBdd saedb = new GestionBdd(this);
	ArrayList<Membre> listMembre = new ArrayList<Membre>();
	ArrayList<Membre> listPresent = new ArrayList<Membre>();
	ListView listViewMembre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pesence);
		
		
		saedb.open();
		listMembre = saedb.getAllMembre();
		
		afficheMembre(listMembre);
		
		
		/**
		 * Button.setOnClickListener : Gestion du bouton "Nouveau Membre"
		 * On clique sur le bouton pour afficher une boite de dialogue "R.layout.new_membre"
		 * On obtient le nom et prénom, que l'on rajoute dans la table membre nom,prenom
		 */
		Button newMembre = (Button) findViewById(R.id.btPresenceNewMembre);
		newMembre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				LayoutInflater inflater = getLayoutInflater();
				final View dialogLayout = inflater.inflate(R.layout.new_membre, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(PresenceActivity.this);
				builder.setView(dialogLayout);
				
				builder.setPositiveButton("Enregister", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						EditText newNom = (EditText) dialogLayout.findViewById(R.id.editNom);
						String nom = newNom.getText().toString();
						EditText newPrenom = (EditText) dialogLayout.findViewById(R.id.editPrenom);
						String prenom = newPrenom.getText().toString();
						saedb.setNewMembre(nom, prenom);
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
		listViewMembre = (ListView) findViewById(R.id.listPresence);
		listViewMembre.setAdapter(adaptMembre);
	}
	
	

}
