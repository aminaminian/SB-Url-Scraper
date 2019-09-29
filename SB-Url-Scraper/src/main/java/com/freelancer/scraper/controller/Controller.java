package com.freelancer.scraper.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.freelancer.scraper.exception.UrlException;
import com.freelancer.scraper.response.Response;
import com.freelancer.scraper.response.Response.Url;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class Controller {
	
	@Value("${search.url}")
	private String searchUrl;
	
	
	
	static String RECORDS_FOUND = "FOUND";
	static String RECORDS_NOT_FOUND = "RECORDS-NOT-FOUND";
	
	@RequestMapping(value="/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> scrapper(@RequestParam(value = "search") String search) {
		
		log.info("URL: {}{} ", searchUrl, search);
		
		Response responses = null;
		
		try {
			Document doc = Jsoup.connect(searchUrl + search).timeout(6000).get();
			
			Elements li = doc.select("li.b_algo");
			
			if(li.isEmpty())
				return ResponseEntity.status(HttpStatus.OK).body(Response.builder().status("RECORDS_NOT_FOUND").build());
			
			Elements div = li.select("div.b_caption");
			
			List<Url> urls = new ArrayList<Url>();
			
			for (Element element : div.select("div.b_attribution cite")) {
				urls.add(new Url(element.text()));
			}
			
			responses = Response.builder().status("RECORDS_FOUND").urls(urls).build();
		
		} catch (IOException e) {
			throw new UrlException(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}
}
