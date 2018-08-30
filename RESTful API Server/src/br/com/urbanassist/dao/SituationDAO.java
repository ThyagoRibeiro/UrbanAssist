package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Situation;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class SituationDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	private static ArrayList<Situation> createSituationList(Set<Map<Variable, Term>> result) {

		ArrayList<Situation> situationList = new ArrayList<>();
		HashMap<String, HashMap<String, String>> objMap = new HashMap<>();

		for (Map<Variable, Term> map : result) {

			String situationStr = null, attribute = null, value = null;

			for (Variable variable : map.keySet()) {

				if (variable.getName().equals("situation"))
					situationStr = map.get(variable).toString();

				if (variable.getName().equals("attribute"))
					attribute = map.get(variable).toString().replaceAll(".*#", "");

				if (variable.getName().equals("value"))
					value = map.get(variable).toString();
			}

			if (objMap.containsKey(situationStr)) {
				objMap.get(situationStr).put(attribute, value);
			} else {
				HashMap<String, String> newAttrMap = new HashMap<>();
				newAttrMap.put(attribute, value);
				objMap.put(situationStr, newAttrMap);
			}
		}

		for (java.util.Map.Entry<String, HashMap<String, String>> entry : objMap.entrySet()) {
			HashMap<String, String> objAttr = entry.getValue();

			Situation newSituation = new Situation();
			newSituation.setName(new Attribute(objAttr.get("hasNameText"), objAttr.get("hasNameAudio"),
					objAttr.get("hasNameVideo")));
			newSituation.setIdSituation(Integer.parseInt(objAttr.get("hasID")));

			situationList.add(newSituation);
		}

		return situationList;
	}

	public static void delete(int idSituation) {

		FileManager.deleteInstance("instance situation" + idSituation + " memberOf Situation");
		ontologyResolver = new OntologyResolver();
	}

	private static int getNumberResults() {
		return runQuery("?x memberOf Situation").size();
	}
	
	public static void insert(Situation situation, boolean hasID) {

		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		if (!hasID) {
			situation.setIdSituation(getNumberResults() + 1);
		}

		stringBuilder.append("\r\n\r\ninstance situation");
		stringBuilder.append(situation.getIdSituation());
		stringBuilder.append(" memberOf Situation");

		stringBuilder.append("\r\nhasID hasValue ");
		stringBuilder.append(situation.getIdSituation());

		stringBuilder.append("\r\nhasNameText hasValue \"");
		stringBuilder.append(situation.getName().getText());
		stringBuilder.append("\"");

		if (!(value = situation.getName().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasNameAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = situation.getName().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasNameVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}
		
		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
		ontologyResolver = new OntologyResolver();
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return ontologyResolver.runProgram(query);
	}

	public static ArrayList<Situation> select() {

		return createSituationList(runQuery("?situation[?attribute hasValue ?value] memberOf Situation"));
	}

	public static Situation select(int id) {

		ArrayList<Situation> situationList = createSituationList(runQuery(
				"?situation[?attribute hasValue ?value] memberOf Situation and ?situation[hasID hasValue " + id + "]"));

		if (situationList.size() > 0)
			return situationList.get(0);
		else
			return null;
	}

	public static ArrayList<Situation> selectByKeyword(String keyword) {

		ArrayList<Situation> situationList = new ArrayList<>();

		for (Situation situation : select()) {
			if (situation.getName().getText().toUpperCase().contains(keyword.toUpperCase()))
				situationList.add(situation);
		}

		return situationList;
	}

	public static void update(Situation situation) {

		delete(situation.getIdSituation());
		insert(situation, true);
	}
}
