package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Responsible;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class ResponsibleDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	private static ArrayList<Responsible> createResponsibleList(Set<Map<Variable, Term>> result) {

		ArrayList<Responsible> rspList = new ArrayList<>();
		HashMap<String, HashMap<String, String>> objMap = new HashMap<>();

		for (Map<Variable, Term> map : result) {

			String rspStr = null, attribute = null, value = null;

			for (Variable variable : map.keySet()) {

				if (variable.getName().equals("responsible"))
					rspStr = map.get(variable).toString();

				if (variable.getName().equals("attribute"))
					attribute = map.get(variable).toString().replaceAll(".*#", "");

				if (variable.getName().equals("value"))
					value = map.get(variable).toString();
			}

			if (objMap.containsKey(rspStr)) {
				objMap.get(rspStr).put(attribute, value);
			} else {
				HashMap<String, String> newAttrMap = new HashMap<>();
				newAttrMap.put(attribute, value);
				objMap.put(rspStr, newAttrMap);
			}
		}

		for (java.util.Map.Entry<String, HashMap<String, String>> entry : objMap.entrySet()) {
			HashMap<String, String> objAttr = entry.getValue();

			Responsible newResponsible = new Responsible();

			newResponsible.setID(Integer.parseInt(objAttr.get("hasID")));
			newResponsible.setName(new Attribute(objAttr.get("hasNameText"), objAttr.get("hasNameAudio"),
					objAttr.get("hasNameVideo")));
			newResponsible.setEmail(new Attribute(objAttr.get("hasEmailText"), objAttr.get("hasEmailAudio"),
					objAttr.get("hasEmailVideo")));
			newResponsible.setPhone(new Attribute(objAttr.get("hasPhoneText"), objAttr.get("hasPhoneAudio"),
					objAttr.get("hasPhoneVideo")));

			rspList.add(newResponsible);
		}

		return rspList;
	}

	public static void delete(int idResponsible) {

		FileManager.deleteInstance("instance responsible" + idResponsible + " memberOf Responsible");
		ontologyResolver = new OntologyResolver();
	}

	private static int getNumberResults() {
		return runQuery("?x memberOf Responsible").size();
	}

	public static void insert(Responsible responsible, boolean hasID) {

		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		if (!hasID) {
			responsible.setID(getNumberResults() + 1);
		}
		
		stringBuilder.append("\r\n\r\ninstance responsible");
		stringBuilder.append(responsible.getID());
		stringBuilder.append(" memberOf Responsible");

		stringBuilder.append("\r\nhasID hasValue ");
		stringBuilder.append(responsible.getID());
		
		stringBuilder.append("\r\nhasNameText hasValue \"");
		stringBuilder.append(responsible.getName().getText());
		stringBuilder.append("\"");

		if (!(value = responsible.getName().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasNameAudio hasValue \"");
			stringBuilder.append(value);
		}

		if (!(value = responsible.getName().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasNameVideo hasValue \"");
			stringBuilder.append(value);
		}

		if (!(value = responsible.getPhone().getText()).equals("")) {

			stringBuilder.append("\r\nhasTelefoneText hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = responsible.getPhone().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasTelefoneAudio hasValue \"");
			stringBuilder.append(value);
		}

		if (!(value = responsible.getPhone().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasTelefoneVideo hasValue \"");
			stringBuilder.append(value);
		}

		if (!(value = responsible.getEmail().getText()).equals("")) {

			stringBuilder.append("\r\nhasEmailText hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = responsible.getEmail().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasEmailAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = responsible.getEmail().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasEmailVideo hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
		ontologyResolver = new OntologyResolver();
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return ontologyResolver.runProgram(query);
	}

	public static ArrayList<Responsible> select() {
		return createResponsibleList(runQuery("?responsible[?attribute hasValue ?value] memberOf Responsible"));
	}

	public static Responsible select(int id) {

		ArrayList<Responsible> responsibleList = createResponsibleList(runQuery(
				"?responsible[?attribute hasValue ?value] memberOf Responsible and ?responsible[hasID hasValue " + id
						+ "]"));

		if (responsibleList.size() > 0)
			return responsibleList.get(0);
		else
			return null;
	}

	public static ArrayList<Responsible> selectByKeyword(String keyword) {

		ArrayList<Responsible> respList = new ArrayList<>();

		for (Responsible resp : select()) {
			if (resp.getName().getText().toUpperCase().contains(keyword.toUpperCase()))
				respList.add(resp);
		}

		return respList;
	}

	public static void update(Responsible resp) {
		
		delete(resp.getID());
		insert(resp, true);
	}

}
