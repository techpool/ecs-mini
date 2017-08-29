package com.pratilipi.api.impl.pratilipi.shared;

import java.util.List;

import com.pratilipi.api.shared.GenericRequest;

public class PostPratilipiProcessRequest extends GenericRequest {

	private Long pratilipiId;
	private List<Long> pratilipiIdList;

	private Boolean validateData;
	private Boolean processData;
	private Boolean processContent;
	private Boolean processContentDoc;
	private Boolean updateReviewsDoc;
	private Boolean updateStats;
	private Boolean updateUserPratilipiStats;
	

	public Long getPratilipiId() {
		return pratilipiId;
	}

	public List<Long> getPratilipiIdList() {
		return pratilipiIdList;
	}


	public boolean validateData() {
		return validateData == null ? false : validateData;
	}
	
	public boolean processData() {
		return processData == null ? false : processData;
	}
	
	public boolean processContent() {
		return processContent == null ? false : processContent;
	}

	public boolean processContentDoc() {
		return processContentDoc == null ? false : processContentDoc;
	}

	public boolean updateReviewsDoc() {
		return updateReviewsDoc == null ? false : updateReviewsDoc;
	}

	public boolean updateStats() {
		return updateStats == null ? false : updateStats;
	}

	public boolean updateUserPratilipiStats() {
		return updateUserPratilipiStats == null ? false : updateUserPratilipiStats;
	}

}
