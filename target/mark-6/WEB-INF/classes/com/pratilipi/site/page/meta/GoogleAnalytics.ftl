<#-- For Google Analytics tracking -->

<script language="javascript">

	(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	ga('create', 'UA-53742841-2', 'pratilipi.com');
    ga('require', 'displayfeatures');
	<#if user.getState() != 'GUEST'>
		ga('set', 'userId', '${ ga_userId }');
	</#if>
	ga('set', 'dimension1', '${ ga_userId }');
	ga('set', 'dimension2', '${ ga_website }');
	ga('set', 'dimension3', '${ ga_websiteMode }');
	ga('set', 'dimension4', '${ ga_websiteVersion }');

	ga('send', 'pageview');


	function ga_CALV( category, action, location, value ) {
    	if( category == null || action == null || location == null || value == null ) {
    		console.log( "ga_CALV". category, action, location, value );
    		return;
    	}
    	ga( 'send', 'event', category, action, location, value );
    	return true;
    }

    function ga_CAL( category, action, location ) {
    	if( category == null || action == null || location == null ) {
    		console.log( "ga_CAL", category, action, location );
    		return;
    	}
    	console.log( "ga_CAL", category, action, location );
    	ga( 'send', 'event', category, action, location );
        return true;
    }

    function ga_CA( category, action ) {
    	if( category == null || action == null ) {
    		console.log( "ga_CA", category, action );
    		return;
    	}
    	ga_CAL( category, action, "${ ga_location }" );
    }

    function ga_CAV( category, action, value ) {
    	if( category == null || action == null || value == null ) {
            console.log( "ga_CAV", category, action );
            return;
        }
        ga_CALV( category, action, "${ ga_location }", value );
    }

</script>
