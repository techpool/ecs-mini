<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#include "meta/HeadBasic.ftl">
		<style type="text/css">
	        .box{
	            text-overflow: hidden;
	            overflow: hidden;
	        }
	        .btn-default {
	            border-radius: 1px;
	        }
	        .form-control {
	            border-radius: 1px;
	        }
	        .form-horizontal {
	            max-width: 100%;
	            padding: 20px;
	        }
    	</style>
    	
    	<script type="text/javascript">
			function resetPassword() {
			
				var email = $( '#inputEmail' ).val();
				
				if( email == null || email.trim() == "" ) {
					<#--  Throw message - Please Enter Email -->
					alert( "Please Enter your Email" );
					return;
				}
				
				if( ! validateEmail( email ) ) {
					<#--  Throw message - Email is not valid -->
					alert( "Please Enter a valid Email" );
					return;
				}

				jQuery( '#passwordResetButton' ).prop( 'disabled', true );

				$.ajax({
				
					type: 'post',
					url: '/api/user/email',

					data: { 
						'email': email, 
						'sendPasswordResetMail': true
					},
					
					success: function( response ) {
						jQuery( '#passwordResetButton' ).prop( 'disabled', false );
						alert( "${ _strings.password_reset_request_success }" );
						window.location.href = "/"; 
					},
					
					error: function( response ) {
						var message = jQuery.parseJSON( response.responseText );
						var status = response.status;
						jQuery( '#passwordResetButton' ).prop( 'disabled', false );
						if( message["email"] != null ) 
							alert( "Error " + status + " : " + message["email"] );
						else if( message["message"] != null )
							alert( "Error " + status + " : " + message["message"] ); 
						else
							alert( "Invalid Email!" );
					}
				});
			}
    	</script>
	</head>

	<body>
		<#include "../element/basic/pratilipi-header.ftl">
		<div class="parent-container">
			<div class="container">

				<div class="secondary-500 pratilipi-shadow box" style="min-height: 370px;">
		            <div style="margin: 20px auto; text-align: center;">
		                <h4 style="text-align: center;">${ _strings.user_forgot_password }</h4>
		            </div>
		            
		            <p class="text-muted">${ _strings.user_reset_password_help }</p>
		            <form id="userPasswordResetForm" class="form-horizontal" action="javascript:void(0);">
		                <div class="form-group">
		                    <label for="inputEmail" class="col-sm-2 control-label">Email</label>
		                    <div class="col-sm-10">
		                        <input name="email" type="email" class="form-control" id="inputEmail" placeholder="${ _strings.user_email }">
		                    </div>
		                </div>
		                <div class="form-group" style="margin: 25px auto; text-align: center;">
		                	<button id="passwordResetButton" class="pratilipi-dark-blue-button" onclick="resetPassword()">${ _strings.user_reset_password }</button>
		                </div>
		            </form>
		            
		            <div style="text-align: center;">
		            	<a class="link" href="/">${ _strings.back }</a>
		            </div>
		            
		        </div>
				
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>