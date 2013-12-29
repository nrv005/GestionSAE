package com.example.gestionsae;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GestionBdd {
	
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "gestionsae.db";
	private static final String PRAGMA = "PRAGMA foreign_keys=ON;";
	
	private static final String TBL_DATE = "date";
	private static final String DATE_COL_ID = "did";
	private static final String DATE_COL_JOUR = "jour";
	private static final String DATE_COL_ORDER = "jour DESC";
	
	private static final String TBL_MEMBRE = "membre";
	private static final String MEMBRE_COL_ID = "mid";
	private static final String MEMBRE_COL_NOM = "nom";
	private static final String MEMBRE_COL_PRENOM = "prenom";
	private static final String MEMBRE_COL_ORDER = "nom,prenom";
	
	private static final String TBL_PRESENCE = "presence";
	private static final String PRESENCE_COL_FKDATE = "fkdate";
	private static final String PRESENCE_COL_FKNOM = "fknom";
	
	private SQLiteDatabase bdd;
	private SqlCreation maBaseSql;
	
	public GestionBdd(Context context) {
		
		maBaseSql = new SqlCreation(context, NOM_BDD, null, VERSION_BDD);
	}
	
	/**
	 * Ouvrir la base de données gestionsae.db
	 * 
	 */
	public void open() {
		
		bdd = maBaseSql.getWritableDatabase();
		bdd.execSQL(PRAGMA);
	}
	
	/**
	 * Fermer la base de données gestionsae.db
	 * 
	 */
	public void close() {
		bdd.close();
	}
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////
//Table des dates 
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * getAllDate : Obtenir toutes les dates de la table date
	 * @param void
	 * @return cursorToDate : fonction private pour obtenir un ArrayList<Date> du cursor
	 */
	public ArrayList<Date> getAllDate() {
		Cursor cAllDate = bdd.query(TBL_DATE, null, null, null, null, null, DATE_COL_ORDER);
		return cursorToDate(cAllDate);
	}
	
	
	/**
	 * cursorToDate : Transforme un cursor en ArrayList<Date>
	 * @param cDate : Cursor, des dates à renvoyer
	 * @return retAllDate : ArrayList<Date> contenant toutes les données des dates
	 */
	private ArrayList<Date> cursorToDate(Cursor cDate) {
		
		//Si la table des dates est vide
		if (cDate.getCount() == 0) return new ArrayList<Date>(0);
		
		//Récupération des données de toutes les dates
		ArrayList<Date> retAllDate = new ArrayList<Date>(cDate.getCount());
		cDate.moveToFirst();
		do {
			Date nDate = new Date();
			nDate.setId(cDate.getInt(0));
			nDate.setJour(cDate.getString(1));
			retAllDate.add(nDate);
		} while (cDate.moveToNext());
		
		//Fermer le cursor pour rendre la mémoire puis retourner le résultat
		cDate.close();
		return retAllDate;
	}
	
	
	/**
	 * setNewSeance : Creation d'un nouvelle seance dans la table date
	 * @param newSeance : String, nouvelle date
	 * @return void
	 */
	public void setNewSeance(String newSeance) {
		
		ContentValues values = new ContentValues();
		values.put(DATE_COL_JOUR, newSeance);
		bdd.insert(TBL_DATE, DATE_COL_JOUR, values);
	}
	
	
	/**
	 * getDateId : renvoie l'identifiant pour un date donnée date.id
	 * @param newSeance : String, date dont on veut obtenir l'id
	 * @return id : long, identifiant.
	 */
	public long getDateId(String newSeance) {
		
		//Préparation de la requéte
		String[] newDate = {newSeance};
		String newWhere = "jour LIKE ?";
		String[] newColonne = {DATE_COL_ID};
		Cursor c = bdd.query(TBL_DATE, newColonne, newWhere, newDate, null, null, null);
		
		//Récupération des données de la requéte
		c.moveToFirst();
		int rowId = c.getInt(0);
		
		return (long) rowId;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//Table des membres
/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * setNewMembre : Ajoute un nouveau membre dans la table correspondate
	 * @param nom : String, nom du nouveau membre
	 * @param prenom : String, prenom du nouveau membre
	 * @return void
	 */
	public void setNewMembre(String nom, String prenom) {
		
		ContentValues values = new ContentValues();
		values.put(MEMBRE_COL_NOM, nom);
		values.put(MEMBRE_COL_PRENOM, prenom);
		bdd.insert(TBL_MEMBRE, MEMBRE_COL_NOM, values);
	}
	
	/**
	 * getAllMembre : Récupère tous les membres de la base de données
	 * @param void
	 * @return cursorToMembre : fonction private pour obtenir un ArrayList du Cursor
	 */
	public ArrayList<Membre> getAllMembre() {
		
		Cursor cAllMembre = bdd.query(TBL_MEMBRE, null, null, null, null, null, MEMBRE_COL_ORDER);
		return cursorToMembre(cAllMembre);
	}
	
	
	/**
	 * cursorToMembre : Transforme un cursor en ArrayList<Membre> de membre
	 * @param cMembre : Cursor, à traiter
	 * @return ArrayList<Membre> 
	 */
	private ArrayList<Membre> cursorToMembre(Cursor cMembre) {
		
		//Si la table des membres est vide
		if (cMembre.getCount() == 0) return new ArrayList<Membre>(0);
		
		//Récupère les données de tous les membres dans un ArrayList
		ArrayList<Membre> retAllMembre = new ArrayList<Membre>(cMembre.getCount());
		cMembre.moveToFirst();
		do {
			Membre nMembre = new Membre();
			nMembre.setId(cMembre.getInt(0));
			nMembre.setNom(cMembre.getString(1));
			nMembre.setPrenom(cMembre.getString(2));
			retAllMembre.add(nMembre);
		} while (cMembre.moveToNext());
		
		//Ferme le cursor pour libérer les ressources puis renvoie l'ArrayList
		cMembre.close();
		return retAllMembre;
	}

	
	
	
/////////////////////////////////////////////////////////////////////////////////////////
//Table des presences
/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * setPresence : Insertion de la liaison date.did et membre.mid pour un membre et la séance
	 * @param idSeance : long, id de la seance dans la table date pour fkdate
	 * @param id : long, id du membre dans la table membre pour fknom;
	 * @return void
	 */
	public void setPresence(long idSeance, long idMembre) {
		
		ContentValues values = new ContentValues();
		values.put(PRESENCE_COL_FKDATE, idSeance);
		values.put(PRESENCE_COL_FKNOM, idMembre);
		bdd.insert(TBL_PRESENCE, PRESENCE_COL_FKDATE, values);
	}
	
	
	/**
	 * suppPresence : Supprime une liaison presence.fkdate et presence.fknom 
	 * @param idSeance : long, identifiant de la seance dans la table date = date.did
	 * @param idMembre : long, identifiant du membre dans la table membre = membre.mid
	 * @return void
	 */
	public void suppPresence(long idSeance, long idMembre) {
		
		//Préparation de la requète
		String where = "fkdate = ? and fknom = ?";
		//TODO Changer les fknom et fkdate en constante PRESENCE_COL_NOM et PRESENCE_COL_DATE
		String sSeance = Long.toString(idSeance);
		String sMembre = Long.toString(idMembre);
		String[] valeur = {sSeance, sMembre};
		
		bdd.delete(TBL_PRESENCE, where, valeur);
	}
	
	
	/**
	 * getPresent : Selection les membres présent pour la date donnée
	 * @param idSeance
	 * @return cursorToMembre : Transforme le Cursor en ArrayList<Membre> pour affichage
	 */
	public ArrayList<Membre> getPresent(long idSeance) {
		
		String myQuery = "SELECT membre.mid, membre.nom, membre.prenom FROM membre JOIN presence WHERE membre.mid=presence.fknom AND presence.fkdate=? ORDER BY membre.nom,membre.prenom";
		String[] argument = {String.valueOf(idSeance)};
		
		Cursor cPresent = bdd.rawQuery(myQuery, argument);
		
		return cursorToMembre(cPresent);
		}
	
	public int countPresent(long idSeance) {
		
		String[] newIdDate = {String.valueOf(idSeance)};
		String newWhere = "fkdate LIKE ?";
		String[] newColone = {PRESENCE_COL_FKDATE};
		Cursor cPresent = bdd.query(TBL_PRESENCE, newColone, newWhere, newIdDate, null, null, null);
		cPresent.moveToFirst();
		
		int result = cPresent.getCount();
		
		return result;
	}
}
