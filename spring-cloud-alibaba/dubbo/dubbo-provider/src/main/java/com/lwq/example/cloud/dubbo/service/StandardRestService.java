package com.lwq.example.cloud.dubbo.service;

import static com.lwq.example.cloud.dubbo.util.LoggerUtils.log;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lwq
 * @date 2021/6/16 0016
 */
@DubboService(version = "1.0.0", protocol = {"dubbo", "rest"})
@Path("/")
public class StandardRestService implements RestService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	@Path("param")
	@GET
	public String param(@QueryParam("param") String param) {
		log("/param", param);
		return param;
	}

	@Override
	@Path("params")
	@POST
	public String params(@QueryParam("a") int a, @QueryParam("b") String b) {
		log("/params", a + b);
		return a + b;
	}

	@Override
	@Path("headers")
	@GET
	public String headers(@HeaderParam("h") String header,
	                      @HeaderParam("h2") String header2, @QueryParam("v") Integer param) {
		String result = header + " , " + header2 + " , " + param;
		log("/headers", result);
		return result;
	}

	@Override
	@Path("path-variables/{p1}/{p2}")
	@GET
	public String pathVariables(@PathParam("p1") String path1,
	                            @PathParam("p2") String path2, @QueryParam("v") String param) {
		String result = path1 + " , " + path2 + " , " + param;
		log("/path-variables", result);
		return result;
	}

	// @CookieParam does not support : https://github.com/OpenFeign/feign/issues/913
	// @CookieValue also does not support

	@Override
	@Path("form")
	@POST
	public String form(@FormParam("f") String form) {
		return String.valueOf(form);
	}

	@Override
	@Path("request/body/map")
	@POST
	@Produces(APPLICATION_JSON_VALUE)
	public User requestBodyMap(Map<String, Object> data,
	                           @QueryParam("param") String param) {
		User user = new User();
		user.setId(((Integer) data.get("id")).longValue());
		user.setName((String) data.get("name"));
		user.setAge((Integer) data.get("age"));
		log("/request/body/map", param);
		return user;
	}

	@Path("request/body/user")
	@POST
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, Object> requestBodyUser(User user) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", user.getId());
		map.put("name", user.getName());
		map.put("age", user.getAge());
		return map;
	}

}
