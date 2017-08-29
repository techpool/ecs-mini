<style type="text/css">
	button.google-button {
		margin: 0 auto;
		margin-bottom: 12px;
		background-color: #4285F4;
		color: #FFFFFF;
		border: none;
		border-radius: 2px;
		white-space: nowrap;
		outline: none;
		padding: 2px;
		width: 320px;
	}
	button.google-button span.icon {
		display: inline-block;
		background-image: url('https://0.ptlp.co/resource-all/icon/png/g-logo.png');
		background-size: contain;
		vertical-align: middle;
		width: 36px;
		height: 36px;
		float: left;
	}
	button.google-button span.button-text {
		display: inline-block;
		vertical-align: middle;
		text-align: center;
		font-size: 14px;
		font-weight: bold;
		line-height: 36px;
	}
</style>

<script src="https://apis.google.com/js/platform.js"></script>
<script>

	var googleLoginOnFlight = false;

	function googleLogin( idToken ) {
		if( googleLoginOnFlight ) return;
		googleLoginOnFlight = true;

		$.ajax({

			type: 'post',
			url: '/api/user/login/google',

			data: { 
				'googleIdToken': idToken
			},

			success: function( response ) {
				googleLoginOnFlight = false;
				window.location.href = getUrlParameters().ret != null ? 
									decodeURIComponent( getUrlParameters().ret ) : "/"; 
			},

			error: function( response ) {
				googleLoginOnFlight = false;
				var message = jQuery.parseJSON( response.responseText );
				var status = response.status;

				if( message["message"] != null )
					alert( "Error " + status + " : " + message["message"] ); 
				else
					alert( "Invalid Credentials" );
			}

		});

	}

	gapi.load( 'auth2', function() {

		auth2 = gapi.auth2.init({
			client_id: '659873510744-kfim969enh181h4gbctffrjg5j47tfuq.apps.googleusercontent.com',
			cookiepolicy: 'single_host_origin',
		});

		auth2.attachClickHandler( document.querySelector( '#googleLoginButton' ), {},
			function( googleUser ) {
				googleLogin( googleUser.getAuthResponse().id_token );
			}, function( error ) {
				alert( JSON.stringify( error, undefined, 2 ) );
		});

	});

</script>