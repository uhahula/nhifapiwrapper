package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.model.CardAuthorizationResponse;
import com.oau.nhif.client.model.VerifyCardRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/authorize")
public class AuthorizeServlet extends HttpServlet {

    private static final List<String> VALID_FP_CODES = List.of(
        "R_THUMB", "R_INDEX", "R_MIDDLE", "R_RING", "R_LITTLE",
        "L_THUMB", "L_INDEX", "L_MIDDLE", "L_RING", "L_LITTLE"
    );

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        NhifApiClient client = (NhifApiClient) req.getServletContext().getAttribute(NhifClientContextListener.CLIENT_ATTR);
        if (client == null) {
            resp.sendError(503, "Server not configured - check NHIF_* env vars");
            return;
        }
        NhifConfig cfg = (NhifConfig) req.getServletContext().getAttribute(NhifClientContextListener.CONFIG_ATTR);

        String cardNo       = req.getParameter("cardNo");
        String fpCode       = req.getParameter("fpCode");
        String imageData    = req.getParameter("imageData");
        String visitIdStr   = req.getParameter("visitTypeID");
        String verifierStr  = req.getParameter("verifierID");
        String cardTypeStr  = req.getParameter("cardTypeID");
        String referralNo   = req.getParameter("referralNo");
        String remarks      = req.getParameter("remarks");
        String mobileNo     = req.getParameter("mobileNo");
        String wcfNotificationNo = req.getParameter("wcfNotificationNo");
        String facilityCode = req.getParameter("facilityCode");

        if (imageData == null || imageData.isEmpty()) {
            resp.sendError(400, "missing imageData (did the local scanner capture fail?)");
            return;
        }
        if (!VALID_FP_CODES.contains(fpCode)) {
            resp.sendError(400, "invalid fpCode: " + fpCode);
            return;
        }
        int visitTypeID;
        try { visitTypeID = Integer.parseInt(visitIdStr); }
        catch (Exception e) { resp.sendError(400, "invalid visitTypeID"); return; }

        Integer verifierID = parseIntOrNull(verifierStr);
        Integer cardTypeID = parseIntOrNull(cardTypeStr);
        if (cardTypeID == null && cfg != null) cardTypeID = cfg.defaultCardTypeID();
        if (facilityCode == null || facilityCode.isEmpty()) {
            facilityCode = (cfg == null) ? null : cfg.facilityCode();
        }

        VerifyCardRequest request = new VerifyCardRequest();
        request.setCardNo(cardNo);
        request.setVerifierID(verifierID);
        request.setCardTypeID(cardTypeID);
        request.setBiometricMethod("FINGERPRINT");
        request.setFpCode(fpCode);
        request.setImageData(imageData);
        request.setVisitTypeID(visitTypeID);
        request.setReferralNo(referralNo == null ? "" : referralNo);
        request.setRemarks(remarks == null ? "" : remarks);
        request.setMobileNo(emptyToNull(mobileNo));
        request.setWcfNotificationNo(emptyToNull(wcfNotificationNo));
        request.setFacilityCode(facilityCode);

        try {
            CardAuthorizationResponse response = client.verifyCardForAuthorization(request).get();
            req.setAttribute("response", response);
            req.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("exception", e);
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    private static Integer parseIntOrNull(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; }
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}
