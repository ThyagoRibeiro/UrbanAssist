package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.urbanassist.dao.AttrClassDAO;
import br.com.urbanassist.dao.LocalityDAO;
import br.com.urbanassist.dao.ResponsibleDAO;
import br.com.urbanassist.dao.SituationDAO;
import br.com.urbanassist.dao.ThingDAO;
import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class EditThing
 */
@WebServlet("/EditThing")
public class EditThing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditThing() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request).getJSONObject("thing");

		JSONObject jsonName = jsonObject.getJSONObject("name");
		JSONObject jsonDescription = jsonObject.getJSONObject("description");
		JSONObject jsonMessage = jsonObject.getJSONObject("message");

		Thing thing = new Thing();
		thing.setID(jsonObject.getInt("idThing"));
		thing.setName(
				new Attribute(jsonName.getString("text"), jsonName.getString("audio"), jsonName.getString("video")));
		thing.setDescription(new Attribute(jsonDescription.getString("text"), jsonDescription.getString("audio"),
				jsonDescription.getString("video")));
		thing.setMessage(new Attribute(jsonMessage.getString("text"), jsonMessage.getString("audio"),
				jsonMessage.getString("video")));
		thing.setDisplay(new Attribute(jsonObject.getString("display"), "", ""));

		if (!jsonObject.isNull("attrClass"))
			thing.setAttrClass(AttrClassDAO.selectByKeyword(jsonObject.getString("attrClass")).get(0));
		else
			thing.setAttrClass(null);

		if (!jsonObject.isNull("locality"))
			thing.setLocality(LocalityDAO.selectByKeyword(jsonObject.getString("locality")).get(0));
		else
			thing.setLocality(null);

		if (!jsonObject.isNull("responsible"))
			thing.setResponsible(ResponsibleDAO.selectByKeyword(jsonObject.getString("responsible")).get(0));
		else
			thing.setResponsible(null);

		if (!jsonObject.isNull("situation"))
			thing.setSituation(SituationDAO.selectByKeyword(jsonObject.getString("situation")).get(0));
		else
			thing.setSituation(null);

		ThingDAO.update(thing);
		ServletManager.successResponse(response);
	}

}
