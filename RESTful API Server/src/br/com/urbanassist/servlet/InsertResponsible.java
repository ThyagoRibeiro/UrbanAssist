package br.com.urbanassist.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.com.urbanassist.dao.ResponsibleDAO;
import br.com.urbanassist.model.Attribute;
import br.com.urbanassist.model.Responsible;
import br.com.urbanassist.util.ServletManager;

/**
 * Servlet implementation class InsertResponsible
 */
@WebServlet("/InsertResponsible")
public class InsertResponsible extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertResponsible() {
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

		JSONObject jsonObject = ServletManager.readJSON(request).getJSONObject("responsible");
		
		JSONObject jsonName = jsonObject.getJSONObject("name");
		JSONObject jsonEmail = jsonObject.getJSONObject("email");
		JSONObject jsonPhone = jsonObject.getJSONObject("phone");
		
		Responsible resp = new Responsible();
		resp.setName(new Attribute(jsonName.getString("text"), jsonName.getString("audio"), jsonName.getString("video")));
		resp.setEmail(new Attribute(jsonEmail.getString("text"), jsonEmail.getString("audio"), jsonEmail.getString("video")));
		resp.setPhone(new Attribute(jsonPhone.getString("text"), jsonPhone.getString("audio"), jsonPhone.getString("video")));
		
		ResponsibleDAO.insert(resp, false);
		ServletManager.successResponse(response);
	}

}
