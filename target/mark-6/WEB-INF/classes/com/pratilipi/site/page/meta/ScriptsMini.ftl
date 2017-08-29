<script>
	function triggerOperaMiniGaEvents( isOpera, isOperaExtreme ) {
		if ( isOpera ) {
			triggerGlobalGAEvent( 'detect', 'detectBrowser', 'isOperaMini', 1 );
		}
		if ( isOperaExtreme ) {
			triggerGlobalGAEvent( 'detect', 'detectBrowser', 'isOperaExtreme', 1 );
		}						 
	}
	
	function detectOperaMini() {
		<#-- Check if current browser is Opera -->
		var isOpera = window.opera | window.opr | ( navigator.userAgent.indexOf(' OPR/') > -1 ) | ( navigator.userAgent.indexOf(' Coast/') > -1 ) | ( navigator.userAgent.indexOf(' OPiOS/') > -1 ) | (navigator.userAgent.indexOf('Opera Mini/') > -1);
	
		<#-- Check if the Opera browser is Opera Mini or Opera Mobile in regular mode (called High Savings Mode) -->
		var isOperaHigh = (navigator.userAgent.indexOf('OPR/') > -1) && (navigator.userAgent.indexOf('Mobile') > -1) && (navigator.userAgent.indexOf('Presto/') < 0);
	
		<#-- Check if the Opera browser is Opera Mini in Extreme Savings Mode -->
		var isOperaExtreme = (navigator.userAgent.indexOf('Opera Mini/') > -1) && (navigator.userAgent.indexOf('Presto/') > -1);		  
	  	
		triggerOperaMiniGaEvents( isOpera, isOperaExtreme );
	}
	
	$( document ).ready(function() {
		detectOperaMini();
	});
</script>