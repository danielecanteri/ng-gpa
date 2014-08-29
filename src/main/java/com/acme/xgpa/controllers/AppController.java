package com.acme.xgpa.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acme.xgpa.services.AppService;
import com.acme.xgpa.services.Constants;

@Controller
@Order(1)
public class AppController {

	@Autowired
	private AppService appService;

	@RequestMapping(value = "/app/home")
	@ResponseBody
	public String page() throws HttpException, IOException {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(Constants.URL);

		int executeMethod = client.executeMethod(get);
		return get.getResponseBodyAsString();
	}

	@RequestMapping(value = "/app/getcookie")
	@ResponseBody
	public Cookie getCookie(HttpSession session) throws HttpException,
			IOException {
		HttpClient client = new HttpClient();
		System.out.println("cookie policy "
				+ client.getParams().getCookiePolicy());
		GetMethod get = new GetMethod(Constants.URL);
		int executeMethod = client.executeMethod(get);
		System.out.println("result " + executeMethod);
		Cookie[] cookies = client.getState().getCookies();
		System.out.println("cook");
		System.out.println(cookies);
		System.out.println(cookies.length);
		for (Cookie cookie : cookies) {
			System.out.println("cookies: " + cookie);
		}

		Header[] responseHeaders = get.getResponseHeaders();
		for (Header header : responseHeaders) {
			System.out.println(header.getName() + "=" + header.getValue());
		}

		session.setAttribute("__GPA_COOKIES__", cookies);
		return client.getState().getCookies()[0];

	}

	@RequestMapping(value = "/app/login")
	@ResponseBody
	public String login(HttpSession session, String login, String password)
			throws HttpException, IOException {

		Cookie[] cookies = getNewCookie(session);

		return appService.doLogin(cookies, login, password);
	}

	public static void main(String[] args) {
		DateTime now = new DateTime();
		DateTime day = now.withDayOfMonth(1);
		System.out.println(day);
		day = day.withDayOfWeek(1);
		System.out.println(day);
	}

	@RequestMapping(value = "/app/test")
	@ResponseBody
	public List<Carico> test(HttpSession session, String login, String password)
			throws HttpException, IOException {
		Cookie[] cookies = getExistingCookie(session);
		System.out.println(cookies);

		return appService.elencoCarichi(cookies);
	}

	@RequestMapping(value = "/app/save")
	@ResponseBody
	public Carico save(HttpSession session,
			@RequestBody Request<FormNuovoCarico> formNuovoCarico)
			throws HttpException, IOException {
		System.out.println("save!!");
		System.out.println("save!! date "
				+ formNuovoCarico.getParams().getDate());
		System.out
				.println("save!! job " + formNuovoCarico.getParams().getJob());
		System.out.println("save!! task "
				+ formNuovoCarico.getParams().getTask());
		System.out.println("save!! subtask "
				+ formNuovoCarico.getParams().getSubtask());
		System.out.println("save!! attivita "
				+ formNuovoCarico.getParams().getAttivita());
		System.out
				.println("save!! ore " + formNuovoCarico.getParams().getOre());

		if (true)
			return null;

		Cookie[] cookies = getExistingCookie(session);
		System.out.println(cookies);

		appService.salvaCarico(cookies, formNuovoCarico.getParams().getDate(),
				formNuovoCarico.getParams().getJob(), formNuovoCarico
						.getParams().getTask(), formNuovoCarico.getParams()
						.getSubtask(), formNuovoCarico.getParams()
						.getAttivita(), formNuovoCarico.getParams().getOre());

		return null;
	}

	@RequestMapping(value = "/app/elencoJob")
	@ResponseBody
	public List<Job> save(HttpSession session) throws HttpException,
			IOException {
		Cookie[] cookies = getExistingCookie(session);
		return appService.elencoJob(cookies);

	}

	@RequestMapping(value = "/app/elencoTask")
	@ResponseBody
	public List<Task> elencoTask(HttpSession session, String job)
			throws HttpException, IOException {
		Cookie[] cookies = getExistingCookie(session);
		return appService.elencoTask(cookies, job);

	}

	private Cookie[] getNewCookie(HttpSession session) throws HttpException,
			IOException {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(Constants.URL);
		client.executeMethod(get);
		Cookie[] cookies = client.getState().getCookies();
		session.setAttribute("__GPA_COOKIES__", cookies);
		return cookies;
	}

	private Cookie[] getExistingCookie(HttpSession session) {
		return (Cookie[]) session.getAttribute("__GPA_COOKIES__");
	}

}
