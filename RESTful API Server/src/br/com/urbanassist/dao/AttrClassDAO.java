package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;
import org.w3c.dom.Attr;

import br.com.urbanassist.model.AttrClass;
import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Situation;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;

public class AttrClassDAO {

	private static OntologyResolver ontologyResolver = new OntologyResolver();

	private static ArrayList<AttrClass> createAttrClassList(Set<Map<Variable, Term>> result) {

		ArrayList<AttrClass> classList = new ArrayList<>();
		HashMap<String, HashMap<String, String>> objMap = new HashMap<>();

		for (Map<Variable, Term> map : result) {

			String classStr = null, attribute = null, value = null;

			for (Variable variable : map.keySet()) {

				if (variable.getName().equals("class"))
					classStr = map.get(variable).toString();

				if (variable.getName().equals("attribute"))
					attribute = map.get(variable).toString().replaceAll(".*#", "");

				if (variable.getName().equals("value"))
					value = map.get(variable).toString();
			}

			if (objMap.containsKey(classStr)) {
				objMap.get(classStr).put(attribute, value);
			} else {
				HashMap<String, String> newAttrMap = new HashMap<>();
				newAttrMap.put(attribute, value);
				objMap.put(classStr, newAttrMap);
			}
		}

		for (java.util.Map.Entry<String, HashMap<String, String>> entry : objMap.entrySet()) {
			HashMap<String, String> objAttr = entry.getValue();

			AttrClass newAttrClass = new AttrClass();
			newAttrClass.setName( new Attribute(objAttr.get("hasNameText"), objAttr.get("hasNameAudio"),
					objAttr.get("hasNameVideo")));
			newAttrClass.setIdClass(Integer.parseInt(objAttr.get("hasID")));

			classList.add(newAttrClass);
		}

		return classList;
	}

	public static void delete(int idClass) {
		
		FileManager.deleteInstance("instance class" + idClass + " memberOf Class");
		ontologyResolver = new OntologyResolver();
	}
	
	private static int getNumberResults() {
		return runQuery("?x memberOf Class").size();
	}

	public static void insert(AttrClass attrClass, boolean hasID) {

		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		if (!hasID) {
			attrClass.setIdClass(getNumberResults() + 1);
		}

		stringBuilder.append("\r\n\r\ninstance class");
		stringBuilder.append(attrClass.getIdClass());
		stringBuilder.append(" memberOf Class");

		stringBuilder.append("\r\nhasID hasValue ");
		stringBuilder.append(attrClass.getIdClass());

		stringBuilder.append("\r\nhasNameText hasValue \"");
		stringBuilder.append(attrClass.getName().getText());
		stringBuilder.append("\"");

		if (!(value = attrClass.getName().getAudio()).equals("")) {

			stringBuilder.append("\r\nhasNameAudio hasValue \"");
			stringBuilder.append(value);
			stringBuilder.append("\"");
		}

		if (!(value = attrClass.getName().getVideo()).equals("")) {

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
	
	public static ArrayList<AttrClass> select() {

		ArrayList<AttrClass> attrClassList = createAttrClassList(
				runQuery("?class[?attribute hasValue ?value] memberOf Class"));
		
		return attrClassList;
	}
	
	public static AttrClass select(int id) {

		ArrayList<AttrClass> thingList = createAttrClassList(
				runQuery("?class[?attribute hasValue ?value] memberOf Class and ?class[hasID hasValue " + id + "]"));

		if (thingList.size() > 0)
			return thingList.get(0);
		else
			return null;
	}

	public static ArrayList<AttrClass> selectByKeyword(String keyword) {

		ArrayList<AttrClass> attrList = new ArrayList<>();

		for (AttrClass attrClass : select()) {
			if (attrClass.getName().getText().toUpperCase().contains(keyword.toUpperCase()))
				attrList.add(attrClass);
		}

		return attrList;
	}

	public static void update(AttrClass attrClass) {

		delete(attrClass.getIdClass());
		insert(attrClass, true);
	}
}
