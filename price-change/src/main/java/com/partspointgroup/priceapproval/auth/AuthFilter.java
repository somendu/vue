package com.partspointgroup.priceapproval.auth;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Intercepts requests:
 *
 * - when a apitoken parameter appears, it is stored on the session
 * - whenever an apitoken is available, it is kept in ApiTokenHolder for this request
 *
 * @author edgardegraaff
 *
 */
public class AuthFilter implements Filter {
	private static final String SESSION_ATTRIBUTE = "apitoken";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(
			ServletRequest request,
			ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// new API token received?
		String apiToken = request.getParameter("apitoken");

		if (apiToken != null) {
			// set it as session variable
			((HttpServletRequest) request).getSession(true).setAttribute(SESSION_ATTRIBUTE, apiToken);
		} else {
			// otherwise use the one on the session
			HttpSession session = ((HttpServletRequest) request).getSession(false);
			if (session != null) {
				apiToken = (String) session.getAttribute(SESSION_ATTRIBUTE);
			}
		}

		if (apiToken != null) {
			// make token available during this request
			try {
				ApiTokenHolder.set(apiToken);
				chain.doFilter(request, response);
			} finally {
				ApiTokenHolder.remove();
			}
		} else {
			chain.doFilter(request, response);
		}
	}
}
