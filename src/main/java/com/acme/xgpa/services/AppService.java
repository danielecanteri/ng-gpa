package com.acme.xgpa.services;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpException;

import com.acme.xgpa.controllers.Carico;
import com.acme.xgpa.controllers.Job;
import com.acme.xgpa.controllers.Task;

public interface AppService {

	List<Carico> elencoCarichi(Cookie[] cookies) throws IOException,
			HttpException;

	List<Job> elencoJob(Cookie[] cookies) throws HttpException, IOException;

	List<Task> elencoTask(Cookie[] cookies, String job) throws HttpException,
			IOException;

	String doLogin(Cookie[] cookies, String login, String password)
			throws HttpException, IOException;

	void salvaCarico(Cookie[] cookies, String date, String job, String task,
			String subtask, String attivita, String ore) throws HttpException,
			IOException;

}
