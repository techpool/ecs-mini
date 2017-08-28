<script>
	
	function logout() {

		$.ajax({
			type: 'get',
			url: '/api/user/logout',
			success: function( response ) {
				var displayName = jQuery.parseJSON( response )[ "displayName" ];
				document.getElementById( "login-signup" ).style.display = "block";
				document.getElementById( "user-dropdown" ).style.display = "none";
				document.getElementById( "username" ).innerText = "";
			},
			error: function( response ) {
				var message = jQuery.parseJSON( response.responseText );
				var status = response.status;
				if( message["message"] != null )
					alert( "Error " + status + " : " + message["message"] ); 
				else
					alert( "Logout failed! Please try again!" );
			}
		});
	}
</script>