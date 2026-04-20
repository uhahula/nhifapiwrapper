package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.model.VisitType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("")
public class IndexServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        VisitTypeCache cache = VisitTypeCacheProvider.get(req.getServletContext());
        List<VisitType> visitTypes = (cache == null) ? List.of() : cache.get();
        req.setAttribute("visitTypes", visitTypes);
        req.setAttribute("fpCodes", List.of(
            "R_THUMB", "R_INDEX", "R_MIDDLE", "R_RING", "R_LITTLE",
            "L_THUMB", "L_INDEX", "L_MIDDLE", "L_RING", "L_LITTLE"));
        req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
    }
}
