<style type="text/css">
	button.fb-button {
		margin: 0 auto;
		margin-bottom: 12px;
		background-color: #3b5998;
		color: #FFFFFF;
		border: none;
		border-radius: 2px;
		white-space: nowrap;
		outline: none;
		padding: 2px;
		width: 320px;
	}
	button.fb-button span.icon {
		display: inline-block;
		background-image: url('https://0.ptlp.co/resource-all/icon/png/fb-logo-64.png');
		background-size: contain;
		vertical-align: middle;
		width: 36px;
		height: 36px;
		float: left;
	}
	button.fb-button span.button-text {
		display: inline-block;
		vertical-align: middle;
		text-align: center;
		font-size: 14px;
		font-weight: bold;
		line-height: 36px;
	}
</style>

<script>
	
	var facebookLoginOnFlight = false;
	
	function facebookLogin() {
		if( facebookLoginOnFlight ) return;
		FB.login( function( response ) {
			if( response == null || response.authResponse == null ) {
				facebookLoginOnFlight = false;
				return;
			}
			facebookLoginOnFlight = true;
			$.ajax({
		
				type: 'post',
				url: '/api/user/login/facebook',

				data: { 
					'fbUserAccessToken': response.authResponse.accessToken,
					'language': '${ language }'
				},
				
				success: function( response ) {
					facebookLoginOnFlight = false;
					if( getUrlParameters().ret != null )
						window.location.href = decodeURIComponent( getUrlParameters().ret );
					else
						window.location.href = "/";
				},
				
				error: function( response ) {
					facebookLoginOnFlight = false;
					var message = jQuery.parseJSON( response.responseText );
					var status = response.status;

					if( message["message"] != null )
						alert( "Error " + status + " : " + message["message"] ); 
					else
						alert( "Invalid Credentials" );
				}
			});
		}, { scope: 'public_profile,email,user_birthday' } );
	}
</script>