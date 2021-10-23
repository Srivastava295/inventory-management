package com.ecommerce.inventorymanagement.filters;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;

import com.ecommerce.inventorymanagement.constants.Constants;
import com.ecommerce.inventorymanagement.constants.StatusConstants;
import com.ecommerce.inventorymanagement.util.CommonUtil;
import com.ecommerce.inventorymanagement.util.ConfigMgrUtil;
import com.ecommerce.inventorymanagement.util.JwtUtil;

//Filter to check authentication of the users
public class AuthenticationFilter implements Filter {

	ConfigMgrUtil cfg;

	private static final Logger LOG = Logger.getLogger(AuthenticationFilter.class.getName());

	public AuthenticationFilter(ConfigMgrUtil cfg) {
		super();
		this.cfg = cfg;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		LOG.log(Level.INFO, "Inside Authentication filter. Request info :" + req.getRequestURL());
		// Getting authorization header
		String jwtHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
		if (jwtHeader==null || jwtHeader.isEmpty()) {
			// Authorization header is missing
			JSONObject errorResponse = CommonUtil.getResponseJson(StatusConstants.AUTH_FAILED, Constants.FAILURE,
					StatusConstants.AUTH_TOKEN_MISSING, null);
			((HttpServletResponse) response).setHeader("Content-Type", "application/json");
			((HttpServletResponse) response).setStatus(401);
			response.getWriter().print(errorResponse);
			return;
		} else {
			String jwt = jwtHeader.split(" ")[1];
			Map<String, Object> claimsFromJWT = JwtUtil.getClaimsFromJWT(jwt,
					cfg.getPropertyValueAsString(Constants.JWT_SECRET_KEY));
			if(claimsFromJWT==null){
				JSONObject errorResponse = CommonUtil.getResponseJson(StatusConstants.AUTH_FAILED, Constants.FAILURE,
						StatusConstants.TAMPERED_JWT, null);
				((HttpServletResponse) response).setHeader("Content-Type", "application/json");
				((HttpServletResponse) response).setStatus(401);
				response.getWriter().print(errorResponse);
				return;
			}else{
				String email = (String) claimsFromJWT.get("email");
				// check if JWT token is expired
				if (!JwtUtil.isExpired(claimsFromJWT)) {
					request.setAttribute("email", email);
					chain.doFilter(request, response);
				} else {
					// JWT is expired
					JSONObject errorResponse = CommonUtil.getResponseJson(StatusConstants.AUTH_FAILED, Constants.FAILURE,
							StatusConstants.AUTH_TOKEN_EXPIRED, null);
					((HttpServletResponse) response).setHeader("Content-Type", "application/json");
					((HttpServletResponse) response).setStatus(401);
					response.getWriter().print(errorResponse);
					return;
			}
			

			}
		}

	}

}
