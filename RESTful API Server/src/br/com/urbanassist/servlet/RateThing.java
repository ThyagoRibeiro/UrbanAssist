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
import br.com.urbanassist.model.Thing;
import br.com.urbanassist.model.User;
import br.com.urbanassist.util.ServletManager;

@WebServlet("/rateThing")
public class RateThing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RateThing() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject = ServletManager.readJSON(request);
		User user = new Gson().fromJson(jsonObject.get("user").toString(), User.class);
		Thing thing = new Gson().fromJson(jsonObject.get("thing").toString(), Thing.class);
		int rate = jsonObject.getInt("rate");

		UserDAO.rateThing(user, thing, rate);
		
		ServletManager.successResponse(response);
	}

}
