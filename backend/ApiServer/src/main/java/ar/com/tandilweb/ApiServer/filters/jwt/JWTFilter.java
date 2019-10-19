package ar.com.tandilweb.ApiServer.filters.jwt;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ar.com.tandilweb.persistence.domain.Sessions;
import ar.com.tandilweb.persistence.repository.SessionsRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JWTFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);

	public SessionsRepository sessionsRepository;

	public void destroy() {

	}

	class ExceptionJWT extends Exception {
		
		private static final long serialVersionUID = 1L;

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		if(httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
			httpResponse.setStatus(200); // forbidden
		} else {
			try {
				Pattern r = Pattern.compile("Bearer<([^>]+)>");
				if(httpRequest.getHeader("authorization") == null) throw new Exception("No authorization tag finded in header.");
				Matcher m = r.matcher(httpRequest.getHeader("authorization"));
				String jwToken = null;
				if(!m.find()) throw new Exception("Invalid Bearer.");
				jwToken = m.group(1);
				String identity = httpRequest.getHeader("identity");
				if(identity == null) throw new ExceptionJWT();
				Sessions session = sessionsRepository.findById(Long.parseLong(identity));
				if(session == null) throw new ExceptionJWT();
				String key = new String(Base64.getEncoder().encode(session.getJwt_passphrase().getBytes()));
				Jws<Claims> jt = Jwts.parser().setSigningKey(key).parseClaimsJws(jwToken);
				Claims data = jt.getBody();
				if(!identity.equals(data.getSubject())) throw new ExceptionJWT();
				JWTUnpackedData uD = new JWTUnpackedData();
				uD.setExpiration(data.getExpiration());
				uD.setId(data.getId());
				uD.setIssuedAt(data.getIssuedAt());
				uD.setIssuer(data.getIssuer());
				uD.setNotBefore(data.getNotBefore());
				uD.setSubject(data.getSubject());
				httpRequest.setAttribute("jwtParsed", uD);
				httpRequest.setAttribute("jwtTrusted", true);
				httpRequest.setAttribute("jwtSessionOrigin", session);
				filterChain.doFilter(httpRequest, httpResponse);
			} catch (ExceptionJWT e) {
				log.info("Error in jwt validation", e);
				httpResponse.setHeader("Authorization-Requested", "jwt.0.14.1");
				httpResponse.setStatus(400); // bad request
			} catch(Exception e) {
				log.info("Error getting Authorization", e);
				httpResponse.setHeader("Authorization-Requested", "jwt.0.14.1");
				httpResponse.setStatus(403); // forbidden
			}
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Filter JWT Initialized");
		sessionsRepository = (SessionsRepository)WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).
			    getBean(SessionsRepository.class);
	}

}
