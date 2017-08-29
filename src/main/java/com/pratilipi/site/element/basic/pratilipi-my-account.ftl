<script>
	function goBack() {
		if( getUrlParameter( "ret" ) != null )
			window.location.href =  getUrlParameter( "ret" );
		else
			window.location.href = "/";
	}	
	function logout() {
		$.ajax({
			type: 'get',
			url: '/api/user/logout',
			success: function( response ) {
				if( getUrlParameter( "ret" ) != null )
					window.location.href =  getUrlParameter( "ret" );
				else
					window.location.href = "/";
			},
			error: function () {
				alert( "Logout Failed!" );
			}
		});
	}
	
	function sendVerificationEmail(emailId) {
			$.ajax({
			type: 'post',
			url: '/api/user/email',
            data: { 
					email: emailId,
					sendEmailVerificationMail: true,        		
            	   },
			success: function( response ) {
				location.reload();
			},
			error: function () {
				alert( "Email Verification Failed!" );
			}
		});
	}
</script>

<div class="pratilipi-shadow secondary-500 box align-text-center">	
	<div class="pull-left">
		<a style="cursor: pointer;" onclick="goBack()">
			<div class="sprites-icon black-arrow-left-icon"></div>
	  	</a>
	</div> 
	<div class="center-heading">
		<h5 class="pratilipi-no-margin pratilipi-red">
			${ _strings.user_my_account	 }
		</h5>
	</div>                  
	<div class="clearfix"></div>
	<hr class="pratilipi-margin-top-2">
	<#if user.getEmail() ??> <#-- Facebook Users might not have an e-mail id -->
		<h3 style="margin-bottom: 25px;">${ user.getEmail() }</h3>
	</#if>
	<div style="margin-bottom: 5px;">
		<#if user.isEmailVerified() == true >
			<button class="pratilipi-red-background-button">${ _strings.edit_account_email_verified } &nbsp;<span class="glyphicon glyphicon-ok" aria-hidden="true"></span></button>		
		<#else>
			<button class="pratilipi-red-background-button" onclick="sendVerificationEmail( '${ user.getEmail() }' )">${ _strings.edit_account_verify_email }</button>
		</#if>
	</div>
	<a class="pratilipi-red-button" href="/updatepassword">${ _strings.user_reset_password}</a>
	<div style="margin-top: 5px;"><a class="pratilipi-red-background-button" onclick="logout()">${ _strings.user_sign_out}</a></div>			
</div>