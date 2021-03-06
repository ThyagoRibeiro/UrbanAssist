package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.urbanassist.dao.LocalityDAO;
import br.com.urbanassist.dao.ResponsibleDAO;
import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class EditLocality
 */
@WebServlet("/EditLocality")
public class EditLocality extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditLocality() {
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

		JSONObject jsonObject = ServletManager.readJSON(request).getJSONObject("locality");

		JSONObject jsonName = jsonObject.getJSONObject("name");
		JSONObject jsonDescription = jsonObject.getJSONObject("description");

		Locality locality = new Locality();
		locality.setID(jsonObject.getInt("idLocality"));
		locality.setName(
				new Attribute(jsonName.getString("text"), jsonName.getString("audio"), jsonName.getString("video")));
		locality.setDescription(new Attribute(jsonDescription.getString("text"), jsonDescription.getString("audio"),
				jsonDescription.getString("video")));

		if (!jsonObject.isNull("locality"))
			locality.setIsContainedIn(LocalityDAO.selectByKeyword(jsonObject.getString("locality")).get(0));
		else
			locality.setIsContainedIn(null);

		if (!jsonObject.isNull("responsible"))
			locality.setResponsible(ResponsibleDAO.selectByKeyword(jsonObject.getString("responsible")).get(0));
		else
			locality.setResponsible(null);
		
		LocalityDAO.update(locality);
		ServletManager.successResponse(response);
	}

}
