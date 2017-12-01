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

			function updatePassword() {
			
				<#if !passwordResetFromMail??>
					var password = $( '#inputPassword' ).val();
					if( password == null || password.trim() == "" ) {
						<#--  Throw message - Please Enter Current Password -->
						alert( "Please Enter the current password!" );
						return;
					}
				</#if>


				var newPassword = $( '#inputNewPassword' ).val();
				var newPassword2 = $( '#inputNewPassword2' ).val();
				
				if( newPassword == null || newPassword.trim() == "" ) {
					<#--  Throw message - Please Enter new Password -->
					alert( "Please Enter the new password!" );
					return;
				}
				
				if( newPassword2 == null || newPassword2.trim() == "" ) {
					<#--  Throw message - Please Enter new password again -->
					alert( "Please Enter the new password again!" );
					return;
				}
				
				if( newPassword != newPassword2 ) {
					alert( "Passwords doesn't match!" );
					return;
				}

				jQuery( '#passwordUpdateButton' ).prop( 'disabled', true );

				$.ajax({
				
					type: 'post',
					url: '/api/user/passwordupdate',

					data: {
						<#if passwordResetFromMail??>
							'email' : "${ email }",
							'verificationToken': "${ verificationToken }",
						<#else>
							'password': password,
						</#if>
						'newPassword': newPassword
					},
					
					success: function( response ) {
						jQuery( '#passwordUpdateButton' ).prop( 'disabled', false );
						alert( "${ _strings.password_change_success }" );
						window.location.href = "/"; 
					},
					
					error: function ( response ) {
						var message = jQuery.parseJSON( response.responseText );
						var status = response.status;
						jQuery( '#passwordUpdateButton' ).prop( 'disabled', false );
						if( message["email"] != null ) 
							alert( "Error " + status + " : " + message["email"] );
						else if( message["password"] != null ) 
							alert( "Error " + status + " : " + message["password"] );
						else if( message["newPassword"] != null )
							alert( "Error " + status + " : " + message["newPassword"] );
						else if( message["message"] != null )
							alert( "Error " + status + " : " + message["message"] ); 
						else
							alert( "Invalid Credentials" );
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
		                <h3 style="text-align: center; font-size: 20px;">${ _strings.user_reset_password }</h3>
		            </div>
		            
		            <form id="userPasswordUpdateForm" class="form-horizontal" action="javascript:void(0);">
		            	<#if !passwordResetFromMail??>
		            		<div class="form-group">
			                    <label for="inputPassword" class="col-sm-2 control-label">Current password</label>
			                    <div class="col-sm-10">
			                        <input name="password" type="password" class="form-control" id="inputPassword" placeholder="${ _strings.user_current_password }">
			                    </div>
			                </div>
		            	</#if>
		                <div class="form-group">
		                    <label for="inputNewPassword" class="col-sm-2 control-label">New password</label>
		                    <div class="col-sm-10">
		                        <input name="newPassword" type="password" class="form-control" id="inputNewPassword" placeholder="${ _strings.user_new_password }">
		                    </div>
		                </div>
		                <div class="form-group">
		                    <label for="inputNewPassword2" class="col-sm-2 control-label">Confirm password</label>
		                    <div class="col-sm-10">
		                        <input name="newPassword2" type="password" class="form-control" id="inputNewPassword2" placeholder="${ _strings.user_confirm_password }">
		                    </div>
		                </div>
		                <div class="form-group" style="margin: 25px auto; text-align: center;">
		                	<button id="passwordUpdateButton" class="pratilipi-dark-blue-button" onclick="updatePassword()">${ _strings.user_reset_password }</button>
		                </div>
		            </form>
		            
		            <div style="text-align: center;">
		            	<a href="/">${ _strings.back }</a>
		            </div>
		            
		        </div>
				
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
	</body>
	
</html>