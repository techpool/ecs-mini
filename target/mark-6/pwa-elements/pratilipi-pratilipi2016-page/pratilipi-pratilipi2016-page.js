function() {
	var windowsize = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
	this.image = "http://public.pratilipi.com/year-in-review-2016/" + ( windowsize < 768 ? "low-res" : "high-res" ) + "/YearInReview-${lang}.jpg";
}