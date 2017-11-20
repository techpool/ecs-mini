<script>
	function isMobile() {
		return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test( navigator.userAgent );
	}
	
	function isAndroid() {
		return /Android/i.test( navigator.userAgent );
	}
	function setCookie( name, value, days, path ) {
		if( days ) {
			var date = new Date();
			date.setTime( date.getTime() + ( days * 24 * 60 * 60 * 1000 ) );
		}	
		var expires = days ? "; expires=" + date.toGMTString() : "";
		document.cookie = name + "=" + value + "; path=" + path + expires;
	}
	function getCookie( cname ) {
		var name = cname + "=";
		var ca = document.cookie.split( ';' );
		for( var i = 0; i < ca.length; i++ ) {
			var c = ca[i];
			while( c.charAt(0) == ' ' ) c = c.substring( 1 );
			if( c.indexOf( name ) == 0 ) return c.substring( name.length, c.length );
		}
		return null;
	}
	function validateEmail( email ) {
		if( email == null ) return false;
		var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return re.test( email );
	}
	function validatePhone( phone ) {
		if( phone == null ) return false;
		var pattern = /^[0-9]{10}$/;
		return pattern.test( phone );
	}
	function convertDate( date ) {
		var d = new Date( date );
		function day(d) { return (d < 10) ? '0' + d : d; }
		function month(m) { var months = ['${ _strings.month_jan }','${ _strings.month_feb }','${ _strings.month_mar }',
		                                  '${ _strings.month_apr }','${ _strings.month_may }','${ _strings.month_jun }',
		                                  '${ _strings.month_jul }','${ _strings.month_aug }','${ _strings.month_sep }',
		                                  '${ _strings.month_oct }','${ _strings.month_nov }','${ _strings.month_dec }']; 
		                                  return months[m]; }
		return [ day( d.getDate() ), month( d.getMonth() ), d.getFullYear() ].join(' ');
	}
	function convertDateWithMinutes( date ) {
		var d = new Date( date );
		function weekday(d) { var days= [ "${ _strings.day_sun }", "${ _strings.day_mon }", "${ _strings.day_tue }", 
											"${ _strings.day_wed }", "${ _strings.day_thu }", "${ _strings.day_fri }", 
											"${ _strings.day_sat }" ]; return days[d]; }
		function day(d) { return (d < 10) ? '0' + d : d; }
		function getHoursMin( h, m ) {
			var suffix = ( h >= 12 ) ? '${ _strings.meridiem_pm }' : '${ _strings.meridiem_am }';
			var hours = h;
			hours = ( hours > 12 ) ? hours - 12 : hours;
			hours = ( hours == '00' ) ? 12 : hours;
			return ( hours + ":" + ( (m < 10) ? '0' + m : m ) + " " + suffix ); 
		}
		function month(m) { var months = ['${ _strings.month_jan }','${ _strings.month_feb }','${ _strings.month_mar }',
		                                  '${ _strings.month_apr }','${ _strings.month_may }','${ _strings.month_jun }',
		                                  '${ _strings.month_jul }','${ _strings.month_aug }','${ _strings.month_sep }',
		                                  '${ _strings.month_oct }','${ _strings.month_nov }','${ _strings.month_dec }']; return months[m]; }
		return [ weekday( d.getDay() ), day( d.getDate() ), month( d.getMonth() ), d.getFullYear(), getHoursMin( d.getHours(), d.getMinutes() ) ].join(' ');
	}
	function getUrlParameter( key ) {
	   if( key = ( new RegExp( '[?&]' +encodeURIComponent( key ) + '=([^&]*)' ) ).exec( location.search ) )
	      return decodeURIComponent( key[1] );
		return null;
	}

	function getParameterByName(name, url) {
	    if (!url) {
	      url = window.location.href;
	    }
	    name = name.replace(/[\[\]]/g, "\\$&");
	    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
	        results = regex.exec(url);
	    if (!results) return null;
	    if (!results[2]) return '';
	    return decodeURIComponent(results[2].replace(/\+/g, " "));
	}
	function getUrlParameters() {
		var str = decodeURI( location.search.substring(1) ), 
			res = str.split("&"), 
			retObj = {};
		for( var i = 0; i < res.length; i++ ){
			var key = res[i].substring( 0, res[i].indexOf( '=' ) );
			var value = res[i].substring( res[i].indexOf( '=' ) + 1 );
			retObj[ key ] = value;
		}
		if( retObj[""] != null ) delete retObj[""];
		return retObj;
	}
	function abbrNum( n, d ) {
		if( n < 1000 ) return n;
		d = d != null ? d : 2;
		x = ( '' + n ).length, p = Math.pow, d = p( 10,d );
		x -= x % 3;
		return Math.round( n * d / p ( 10, x ) ) / d + " kMGTPE" [ x / 3 ]; 
	}
	function commaSeparatedNumber(x) {
	    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	}
	function gotoShare( pageUrl, name, utmSource, utmCampaign ) {
	
		var language = "${ language }".toLowerCase();
		var url = "https://" + language + ".pratilipi.com" + pageUrl +  "?utm_source=" + utmSource + "&utm_campaign=" + utmCampaign;
		window.location.href = ( "/share?url=" + encodeURIComponent(url) + "&name=" + name );
	}	
</script>