package com.oau.nhif.jspexample.web;

import com.oau.nhif.client.NhifApiClient;
import com.oau.nhif.client.model.CardAuthorizationRequest;
import com.oau.nhif.client.model.CardAuthorizationResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizeServletTest {

    private HttpServletRequest mockReq(
            String cardNo, String fpCode, String visitTypeID, String imageData,
            NhifApiClient client) {
        HttpServletRequest req = mock(HttpServletRequest.class);
        ServletContext ctx = mock(ServletContext.class);
        when(req.getServletContext()).thenReturn(ctx);
        when(ctx.getAttribute(NhifClientContextListener.CLIENT_ATTR)).thenReturn(client);
        when(req.getParameter("cardNo")).thenReturn(cardNo);
        when(req.getParameter("fpCode")).thenReturn(fpCode);
        when(req.getParameter("visitTypeID")).thenReturn(visitTypeID);
        when(req.getParameter("imageData")).thenReturn(imageData);
        when(req.getParameter("referralNo")).thenReturn("");
        when(req.getParameter("remarks")).thenReturn("Biometric verified visit");
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(anyString())).thenReturn(rd);
        return req;
    }

    @Test
    void forwardsToResultOnApproval() throws Exception {
        NhifApiClient client = mock(NhifApiClient.class);
        CardAuthorizationResponse resp = new CardAuthorizationResponse();
        resp.setAuthorizationStatus("APPROVED");
        resp.setAuthorizationNo("AUTH-123");
        resp.setFullName("Jane Doe");
        when(client.authorizeCardWithBiometric(any()))
            .thenReturn(CompletableFuture.completedFuture(resp));

        HttpServletRequest req = mockReq("101502314766", "R_INDEX", "1", "BASE64DATA==", client);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);

        new AuthorizeServlet().doPost(req, httpResp);

        ArgumentCaptor<CardAuthorizationRequest> cap = ArgumentCaptor.forClass(CardAuthorizationRequest.class);
        verify(client).authorizeCardWithBiometric(cap.capture());
        assertEquals("101502314766", cap.getValue().getCardNo());
        assertEquals("FINGERPRINT",  cap.getValue().getBiometricMethod());
        assertEquals("R_INDEX",      cap.getValue().getFpCode());
        assertEquals("BASE64DATA==", cap.getValue().getImageData());
        assertEquals(1,              cap.getValue().getVisitTypeID());

        verify(req).setAttribute("response", resp);
        verify(req).getRequestDispatcher("/WEB-INF/jsp/result.jsp");
    }

    @Test
    void returns503WhenClientNotConfigured() throws Exception {
        HttpServletRequest req = mockReq("101502314766", "R_INDEX", "1", "BASE64DATA==", null);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);

        new AuthorizeServlet().doPost(req, httpResp);

        verify(httpResp).sendError(eq(503), contains("NHIF_"));
    }

    @Test
    void returns400OnMissingImageData() throws Exception {
        NhifApiClient client = mock(NhifApiClient.class);
        HttpServletRequest req = mockReq("101502314766", "R_INDEX", "1", "", client);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);

        new AuthorizeServlet().doPost(req, httpResp);

        verify(httpResp).sendError(eq(400), contains("imageData"));
        verify(client, never()).authorizeCardWithBiometric(any());
    }

    @Test
    void returns400OnUnknownFpCode() throws Exception {
        NhifApiClient client = mock(NhifApiClient.class);
        HttpServletRequest req = mockReq("101502314766", "THUMB", "1", "BASE64DATA==", client);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);

        new AuthorizeServlet().doPost(req, httpResp);

        verify(httpResp).sendError(eq(400), contains("fpCode"));
    }
}
