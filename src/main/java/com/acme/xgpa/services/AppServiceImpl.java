package com.acme.xgpa.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.acme.xgpa.controllers.Carico;
import com.acme.xgpa.controllers.Job;
import com.acme.xgpa.controllers.Task;

import static com.acme.xgpa.services.Constants.COD_UTE_LOGIN;
import static com.acme.xgpa.services.Constants.GPA_SVL_NAME;

public class AppServiceImpl implements AppService {

    public List<Carico> elencoCarichi(Cookie[] cookies) throws IOException,
			HttpException {
		HttpClient client = new HttpClient();

		client.getState().addCookies(cookies);

		List<Carico> carichi = new ArrayList<Carico>();

		DateTime now = new DateTime();
		DateTime day = now.withDayOfMonth(1).withDayOfWeek(1);
		if (day.getMonthOfYear() < now.getMonthOfYear()) {
			day = day.plusDays(7);
		}
		while (day.getMonthOfYear() == now.getMonthOfYear()) {

			PostMethod post = new PostMethod(Constants.URL
					+ "RAPPORTINO_SETTIMANALE");
			post.addParameter("LIVELLO_UTENTE", "99");
			post.addParameter("COD_JOB", "");
			post.addParameter("CONVERSAZIONE", "");
			post.addParameter("STEP", "");
			post.addParameter("COMANDO", "");
			post.addParameter("OPERATION", "");
			post.addParameter("LINK_1",
					"AM_ELENCO_ATTIVITA%3FCOD_JOB%3DPLSS5002A");
			post.addParameter("COD_UTE_LOGIN", COD_UTE_LOGIN);
			post.addParameter("LIV_UTE_LOGIN", "99");
			post.addParameter("GPA_SVL_NAME", GPA_SVL_NAME);
			post.addParameter("CAMPI_DISABILITATI", "");
			post.addParameter("ANNI_SELECT", "" + day.getYear());
			post.addParameter("MESE_SEL", "" + day.getMonthOfYear());
			post.addParameter(
					"SETTIMANA_SELECT",
					StringUtils.leftPad("" + day.getDayOfMonth(), 2, "0")
							+ "/"
							+ StringUtils.leftPad("" + day.getMonthOfYear(), 2,
									"0") + "/" + day.getYear());

			// ANNI_SELECT=2014&MESE_SEL=3&SETTIMANA_SELECT=10%2F03%2F2014

			System.out.println(post.getParameter("SETTIMANA_SELECT"));
			client.executeMethod(post);

			String responseBodyAsString = post.getResponseBodyAsString();
			System.out.println(responseBodyAsString);
			Document document = Jsoup.parse(responseBodyAsString);

			Elements tablesReport = document.getElementsByAttributeValue("id",
					"rpt_settimanale");
			if (tablesReport.size() > 1) {
				Element table = tablesReport.get(1);
				System.out.println(table);
				Elements rows = table.getElementsByTag("tr");
				for (Element element : rows) {
					Elements tds = element.getElementsByTag("td");
					if (tds.size() > 1) {
						Carico carico = new Carico();
						carico.setData(tds.get(2).text());
						carico.setJob(tds.get(3).text());
						carico.setOre(tds.get(8).text());
						carichi.add(carico);
					}
				}
			}

			day = day.plusDays(7);
		}

		return carichi;
	}

	public List<Job> elencoJob(Cookie[] cookies) throws HttpException,
			IOException {
		HttpClient client = new HttpClient();
		client.getState().addCookies(cookies);

		GetMethod get = new GetMethod(Constants.URL + "RAPPORTINO_SETTIMANALE");
		client.executeMethod(get);

		String body = get.getResponseBodyAsString();
		System.out.println(body);

		String elenco = StringUtils.substringBefore(
				StringUtils.substringAfter(body, "elencoJob = ["), "];");
		System.out.println("SUB " + elenco);
		elenco = StringUtils.substringBeforeLast(
				StringUtils.substringAfter(elenco, "\""), "\" ");
		System.out.println(elenco);
		StrTokenizer strTokenizer = new StrTokenizer(elenco, "\" , \"");
		List<String> tokenList = strTokenizer.getTokenList();

		List<Job> jobs = new ArrayList<Job>();
		for (int i = 0; i < tokenList.size(); i += 3) {
			Job job = new Job();
			job.setCodice(tokenList.get(i));
			job.setDescrizione(tokenList.get(i + 1));
			jobs.add(job);
		}

		return jobs;
	}

	public List<Task> elencoTask(Cookie[] cookies, String job)
			throws HttpException, IOException {
		System.out.println("elenco task [" + job + "]");
		HttpClient client = new HttpClient();
		client.getState().addCookies(cookies);

		GetMethod get = new GetMethod(Constants.URL + "SELEZIONE_TSK?INS_JOB="
				+ job);
		client.executeMethod(get);
		String body = get.getResponseBodyAsString();
		Pattern pattern = Pattern.compile(
				".*javascript: seleziona\\('(.*)', '(.*)', '(.*)'\\);.*",
				Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(body);
		Document document = Jsoup.parse(body);
		List<Task> tasks = new ArrayList<Task>();
		while (matcher.find()) {
			Task task = new Task();
			task.setTask(matcher.group(1));
			task.setSubtask(matcher.group(2));
			task.setAttivita(matcher.group(3));
			Elements elementsContainingText = document.select(
					"tr:contains(" + task.getTask() + " - " + task.getSubtask()
							+ ")").select("td:eq(1)");
			// TODO rimuovere primo pezzo da descrizione?
			task.setDescrizione(elementsContainingText.get(0).text());
			tasks.add(task);
		}
		return tasks;
	}

	public String doLogin(Cookie[] cookies, String login, String password)
			throws HttpException, IOException {
		HttpClient client = new HttpClient();

		client.getState().addCookies(cookies);

		PostMethod post = new PostMethod(Constants.URL);
		post.addParameter("LOGON_COD_UTE_LOGIN", login);
		post.addParameter("PWD_UTE_LOGIN", password);
		post.addParameter("AZIONE", "LOGIN");
		post.addParameter("LIVELLO_UTENTE", "");
		post.addParameter("CONVERSAZIONE", "");
		post.addParameter("STEP", "LOGIN");
		post.addParameter("COMANDO", "ESEGUI_LOGIN");
		post.addParameter("LINK1", "AM_ELENCO_ATTIVITA%3FCOD_JOB%3DPLSS5002A");
		post.addParameter("LIV_UTE_LOGIN", "");
		System.out.println(post);
		System.out.println(post.getPath());
		int executeMethod = client.executeMethod(post);
		System.out.println(post.getResponseBodyAsString());
		return loginSuccessful(post.getResponseBodyAsString()) ? "OK" : "KO";
	}

	private boolean loginSuccessful(String responseBodyAsString) {
		Document document = Jsoup.parse(responseBodyAsString);
		Elements elementsByTag = document.getElementsByTag("title");
		System.out.println(elementsByTag);
		System.out.println(elementsByTag.get(0));
		System.out.println(elementsByTag.get(0).text());
		System.out.println(elementsByTag.get(0).text().contains("Menù GPA"));
		return elementsByTag.get(0).text().contains("Menù GPA");
	}

	public void salvaCarico(Cookie[] cookies, String date, String job,
			String task, String subtask, String attivita, String ore)
			throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(Constants.URL
				+ "RAPPORTINO_SETTIMANALE");

		post.addParameter("ANNI_SELECT", "2014");
		post.addParameter("MESE_SEL", "3");
		post.addParameter("SETTIMANA_SELECT", "17/03/2014");
		post.addParameter("INS_DATA", date);
		post.addParameter("INS_JOB", job);
		post.addParameter("INS_TASK", task);
		post.addParameter("INS_SUB_TASK", subtask);
		post.addParameter("INS_ATTIVITA", attivita);
		post.addParameter("INS_ORA_INIZIO_STRAO", "");
		post.addParameter("INS_ORE", ore);
		post.addParameter("INS_NOTE", "-");
		post.addParameter("INS_DES_JOB", "AM Sviluppo Applicazioni web Vita");
		post.addParameter("STEP", "INSERIMENTO_RPT");
		post.addParameter("COMANDO", "INSERIMENTO_SAV");
		post.addParameter("CONVERSAZIONE", "");
		post.addParameter("AZIONE", "");
		post.addParameter("INS_STATO", "A");
		post.addParameter("TMR.COD_UTENTE_VS", COD_UTE_LOGIN);
		post.addParameter("TMR.INS_LUOGO_LAV", "A");
		post.addParameter("COD_UTE_LOGIN", COD_UTE_LOGIN);
		post.addParameter("LIV_UTE_LOGIN", "99");
		post.addParameter("INS_ORA_INIZIO_STRAO_OLD", "");
		post.addParameter("INS_ANNO_OLD", "");
		post.addParameter("INS_MESE_OLD", "");
		post.addParameter("INS_GIORNO_OLD", "");
		post.addParameter("INS_JOB_OLD", "");
		post.addParameter("INS_TASK_OLD", "");
		post.addParameter("INS_SUB_TASK_OLD", "");
		post.addParameter("INS_ATTIVITA_OLD", "");
		post.addParameter("FLG_AGGIORNA", "");
		post.addParameter("FLG_INSERISCI", "");
		post.addParameter("COD_OPT_SELECTED", "");
		post.addParameter("MODIFICA", "");
		post.addParameter("INSERIMENTO", "");
		post.addParameter("RIS_CPY", "");
		post.addParameter("FINE_ST", "23/03/2014");

		client.executeMethod(post);
		System.out.println("salvato!");
		String responseBodyAsString = post.getResponseBodyAsString();
		System.out.println(responseBodyAsString);

		Document document = Jsoup.parse(responseBodyAsString);
		Elements elementsByAttributeValue = document
				.getElementsByAttributeValue("class", "msgerr");
		if (elementsByAttributeValue.size() > 0) {
			System.out.println("ERRORE: "
					+ elementsByAttributeValue.get(0).text());
		}
	}
}
