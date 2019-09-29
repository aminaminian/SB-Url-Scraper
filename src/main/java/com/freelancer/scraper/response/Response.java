package com.freelancer.scraper.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class Response {
	
	@Getter @Setter
	private String status;
	
	@Getter @Setter
	private List<Url> urls;
	
	public static class Url {
				
		@Getter @Setter
		private String urlString;
		
		public Url(String url){
			this.urlString = url;
		}
	}
	
}
