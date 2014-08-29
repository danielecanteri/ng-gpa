package com.acme.xgpa.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;

import com.acme.xgpa.controllers.Carico;
import com.acme.xgpa.controllers.Job;
import com.acme.xgpa.controllers.Task;

public class AppServiceMock implements AppService {

	public List<Carico> elencoCarichi(Cookie[] cookies) throws IOException,
			HttpException {

		List<Carico> result = new ArrayList<Carico>();
		Carico carico = new Carico();
		carico.setData("07/04/2014");
		carico.setJob("PARC140112");
		carico.setOre("8");
		result.add(carico);
		return result;
	}

	public List<Job> elencoJob(Cookie[] cookies) throws HttpException,
			IOException {

		List<Job> jobs = new ArrayList<Job>();

		for (int i = 0; i < 10; i++) {
			Job job = new Job();
			job.setCodice("PARC14000" + i);
			job.setDescrizione("Descrizione del job " + job.getCodice());
			jobs.add(job);
		}

		return jobs;
	}

	public List<Task> elencoTask(Cookie[] cookies, String job)
			throws HttpException, IOException {
		List<Task> tasks = new ArrayList<Task>();
		for (int i = 0; i < 6; i++) {
			Task task = new Task();
			task.setTask("Task job " + job);
			task.setSubtask("00" + i);
			task.setAttivita("AM");
			tasks.add(task);
		}
		return tasks;
	}

	public String doLogin(Cookie[] cookies, String login, String password)
			throws HttpException, IOException {
		return StringUtils.equalsIgnoreCase(login, "error") ? "KO" : "OK";
	}

	public void salvaCarico(Cookie[] cookies, String date, String job,
			String task, String subtask, String attivita, String ore)
			throws HttpException, IOException {
		// TODO Auto-generated method stub

	}

}
