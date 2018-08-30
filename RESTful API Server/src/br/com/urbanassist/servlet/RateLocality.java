package br.com.urbanassist.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import br.com.urbanassist.dao.UserDAO;
import br.com.urbanassist.model.Locality;
import br.com.urbanassist.model.User;
import br.com.urbanassist.util.ServletManager;

@WebServlet("/rateLocality")
public class RateLocality extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RateLocality() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request);
		User user = new Gson().fromJson(jsonObject.get("user").toString(), User.class);
		Locality locality = new Gson().fromJson(jsonObject.get("locality").toString(), Locality.class);
		int rate = jsonObject.getInt("rate");

		UserDAO.rateLocality(user, locality, rate);
		
		ServletManager.successResponse(response);
	}

}
