package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.urbanassist.dao.SituationDAO;
import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Situation;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class InsertSituation
 */
@WebServlet("/InsertSituation")
public class InsertSituation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertSituation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request).getJSONObject("situation");
		
		JSONObject jsonName = jsonObject.getJSONObject("name");
		Situation situation = new Situation();
		situation.setName(new Attribute(jsonName.getString("text"), jsonName.getString("audio"), jsonName.getString("video")));
		
		SituationDAO.insert(situation, false);
		ServletManager.successResponse(response);
	}

}
