package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.model.User;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class UserDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	private static ArrayList<User> createUserList(Set<Map<Variable, Term>> result) {

		ArrayList<User> usrList = new ArrayList<>();
		HashMap<String, HashMap<String, String>> objMap = new HashMap<>();

		for (Map<Variable, Term> map : result) {

			String userStr = null, attribute = null, value = null;

			for (Variable variable : map.keySet()) {

				if (variable.getName().equals("user"))
					userStr = map.get(variable).toString();

				if (variable.getName().equals("attribute"))
					attribute = map.get(variable).toString().replaceAll(".*#", "");

				if (variable.getName().equals("value"))
					value = map.get(variable).toString();
			}

			if (objMap.containsKey(userStr)) {
				objMap.get(userStr).put(attribute, value);
			} else {
				HashMap<String, String> newAttrMap = new HashMap<>();
				newAttrMap.put(attribute, value);
				objMap.put(userStr, newAttrMap);
			}
		}

		for (java.util.Map.Entry<String, HashMap<String, String>> entry : objMap.entrySet()) {
			HashMap<String, String> objAttr = entry.getValue();

			User newUser = new User();

			newUser.setName(new Attribute(objAttr.get("hasNameText"), objAttr.get("hasNameAudio"),
					objAttr.get("hasNameVideo")));
			newUser.setMidia(Integer.parseInt(objAttr.get("hasMidia")));

			usrList.add(newUser);
		}

		return usrList;
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return ontologyResolver.runProgram(query);
	}

	public static ArrayList<User> select() {
		return createUserList(runQuery("?user[?attribute hasValue ?value] memberOf User"));
	}

	public static User select(String id) {
		return createUserList(
				runQuery("?user[?attribute hasValue ?value] memberOf User and ?user[hasID hasValue " + id + "]"))
						.get(0);
	}

	public static void delete() {
		// TODO Auto-generated method stub

	}

	public static void insert(User user) {

		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		stringBuilder.append("\r\n\r\ninstance user");
		stringBuilder.append(getNumberResults() + 1);
		stringBuilder.append(" memberOf User");

		stringBuilder.append("\r\nhasNameText hasValue \"");
		stringBuilder.append(user.getName().getText());
		stringBuilder.append("\"");

		if (!(value = user.getName().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasNameAudio hasValue \"");
			stringBuilder.append(value);
		}

		if (!(value = user.getName().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasNameVideo hasValue \"");
			stringBuilder.append(value);
		}

		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
	}

	private static int getNumberResults() {
		return runQuery("?x memberOf User").size();
	}

	public static void update(User user) {
		// TODO Auto-generated method stub

	}

	public static void rateLocality(User user, Locality locality, int rate) {

		StringBuilder sb = new StringBuilder();
		sb.append("\r\n\r\nrelationInstance RateLocality(user");
		sb.append(user.getID());
		sb.append(", locality");
		sb.append(locality.getID());
		sb.append(", ");
		sb.append(rate);
		sb.append(")");

		FileManager.writeString(Constants.ONTOLOGY_FILE, sb.toString(), true);
	}

	public static HashMap<Integer, Integer> getRatedThings(User user) {

		return createRatedMap(
				runQuery("RateThing(?user, ?thing, ?rate) and ?user[hasID hasValue " + user.getID() + "]"));
	}

	public static HashMap<Integer, Integer> getRatedLocalities(User user) {

		return createRatedMap(
				runQuery("RateLocality(?user, ?locality, ?rate) and ?user[hasID hasValue " + user.getID() + "]"));
	}

	private static HashMap<Integer, Integer> createRatedMap(Set<Map<Variable, Term>> result) {

		HashMap<Integer, Integer> ratedMap = new HashMap<>();
		int rated = 0, value = 0;

		for (Map<Variable, Term> map : result) {

			for (Variable key : map.keySet()) {
				if (!key.toString().equals("?user")) {
					if (key.toString().equals("?rate"))
						value = Integer.parseInt(map.get(key).toString());
					else
						rated = Integer.parseInt(
								map.get(key).toString().replaceAll(".*thing", "").replaceAll(".*locality", ""));
				}
			}
			ratedMap.put(rated, value);
		}

		return ratedMap;
	}

	public static void rateThing(User user, Thing thing, int rate) {

		StringBuilder sb = new StringBuilder();
		sb.append("\r\n\r\nrelationInstance RateThing(user");
		sb.append(user.getID());
		sb.append(", thing");
		sb.append(thing.getID());
		sb.append(", ");
		sb.append(rate);
		sb.append(")");

		FileManager.writeString(Constants.ONTOLOGY_FILE, sb.toString(), true);
	}

}
