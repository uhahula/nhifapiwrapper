package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.model.CardVerifier;
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
        VisitTypeCache visitCache = VisitTypeCacheProvider.get(req.getServletContext());
        List<VisitType> visitTypes = (visitCache == null) ? List.of() : visitCache.get();

        CardVerifierCache verifierCache = CardVerifierCacheProvider.get(req.getServletContext());
        List<CardVerifier> verifiers = (verifierCache == null) ? List.of() : verifierCache.get();

        NhifConfig cfg = (NhifConfig) req.getServletContext().getAttribute(NhifClientContextListener.CONFIG_ATTR);

        req.setAttribute("visitTypes", visitTypes);
        req.setAttribute("verifiers", verifiers);
        req.setAttribute("facilityCode", cfg == null ? "" : (cfg.facilityCode() == null ? "" : cfg.facilityCode()));
        req.setAttribute("defaultCardTypeID", cfg == null ? null : cfg.defaultCardTypeID());
        req.setAttribute("fpCodes", List.of(
            "R_THUMB", "R_INDEX", "R_MIDDLE", "R_RING", "R_LITTLE",
            "L_THUMB", "L_INDEX", "L_MIDDLE", "L_RING", "L_LITTLE"));
        req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
    }
}
