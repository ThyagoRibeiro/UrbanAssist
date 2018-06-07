package br.com.urbanassist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Variable;

import br.com.urbanassist.model.Responsible;
import br.com.urbanassist.model.User;
import br.com.urbanassist.util.Constants;
import br.com.urbanassist.util.FileManager;
import br.com.urbanassist.model.Attribute;

public class ResponsibleDAO {

	private static String attrsQuery(String id) {

		if (id == null)
			id = "?ID";

		StringBuilder allAttr = new StringBuilder();

		// allAttr.append("?objeto[hasID hasValue ");
		// allAttr.append(id);
		// allAttr.append("] and ?objeto[hasNameText hasValue ?nameText]");
		// allAttr.append(" and ?objeto[hasNameAudio hasValue ?nameAudio]");
		// allAttr.append(" and ?objeto[hasNameVideo hasValue ?nameVideo]");
		// allAttr.append(" and ?objeto[hasDescriptionText hasValue ?descriptionText]");
		// allAttr.append(" and ?objeto[hasDescriptionAudio hasValue
		// ?descriptionAudio]");
		// allAttr.append(" and ?objeto[hasDescriptionVideo hasValue
		// ?descriptionVideo]");
		// allAttr.append(" and ?objeto[hasMessageText hasValue ?messageText]");
		// allAttr.append(" and ?objeto[hasMessageAudio hasValue ?messageAudio]");
		// allAttr.append(" and ?objeto[hasMessageVideo hasValue ?messageVideo]");
		// allAttr.append(" and ?objeto[hasAlertText hasValue ?alertText]");
		// allAttr.append(" and ?objeto[hasAlertAudio hasValue ?alertAudio]");
		// allAttr.append(" and ?objeto[hasAlertVideo hasValue ?alertVideo]");
		// allAttr.append(" and ?objeto[hasDisplayText hasValue ?displayText]");
		// allAttr.append(" and ?objeto[hasDisplayAudio hasValue ?displayAudio]");
		// allAttr.append(" and ?objeto[hasDisplayVideo hasValue ?displayVideo]");
		// allAttr.append(" and ?objeto[isContainedIn hasValue ?locality]");
		// allAttr.append(" and ?objeto[hasResponsible hasValue ?responsible]");
		// allAttr.append(" and ?objeto[hasSituation hasValue ?situation]");
		// allAttr.append(" and ?objeto[hasCoord hasValue ?coord]");

		return allAttr.toString();
	}

	private static ArrayList<Responsible> createResponsibleList(Set<Map<Variable, Term>> result) {

		ArrayList<Responsible> rspList = new ArrayList<>();
		HashMap<String, String> attrsMap = new HashMap<>();

		Responsible responsibly = new Responsible();
		for (Map<Variable, Term> map : result) {

			for (Variable key : map.keySet()) {
				attrsMap.put(key.toString(), map.get(key).toString());
			}

			responsibly.setName(new Attribute(attrsMap.get("?nameText").toString(),
					attrsMap.get("?nameAudio").toString(), attrsMap.get("?nameVideo").toString()));
			responsibly.setEmail(new Attribute(attrsMap.get("?emailText").toString(),
					attrsMap.get("?emailAudio").toString(), attrsMap.get("?emailVideo").toString()));
			responsibly.setPhone(new Attribute(attrsMap.get("?phoneText").toString(),
					attrsMap.get("?phoneAudio").toString(), attrsMap.get("?phoneVideo").toString()));

			rspList.add(responsibly);
		}

		return rspList;
	}

	public static void main(String[] args) {

		Responsible responsible = new Responsible();
		responsible.setName(new Attribute("Thyago", "", ""));
		
		insert(responsible);
	}

	private static Set<Map<Variable, Term>> runQuery(String query) {
		return OntologyResolver.getInstance().runProgram(Constants.ONTOLOGY_FILE, query);
	}

	public static ArrayList<Responsible> select() {
		return createResponsibleList(runQuery("?x memberOf Responsible"));
	}

	public static Responsible select(String id) {
		return createResponsibleList(runQuery(attrsQuery(id))).get(0);
	}

	public void delete() {
		// TODO Auto-generated method stub

	}

	public static void insert(Responsible responsible) {

		StringBuilder stringBuilder = new StringBuilder();

		String value = "";

		stringBuilder.append("\r\n\r\ninstance responsible");
		stringBuilder.append(getNumberResults() + 1);
		stringBuilder.append(" memberOf Responsible");

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
		}

		if (!(value = responsible.getEmail().getVideo()).equals("")) {

			stringBuilder.append("\r\nhasEmailVideo hasValue \"");
			stringBuilder.append(value);
		}

		FileManager.writeString(Constants.ONTOLOGY_FILE, stringBuilder.toString(), true);
	}

	private static int getNumberResults() {
		return runQuery("?x memberOf Responsible").size();
	}

	public void update() {
		// TODO Auto-generated method stub
	}
	


}
