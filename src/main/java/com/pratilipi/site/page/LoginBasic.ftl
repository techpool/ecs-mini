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
	            max-width: 600px;
	            margin-left: auto;
	            margin-right: auto;
	            padding: 20px;
	        }
    	</style>
    	
    	<script type="text/javascript">
    		function register() {
				if( getUrlParameters().ret != null )
					window.location.href = "/register" + "?ret=" + getUrlParameters().ret;
				else
					window.location.href = "/register" + "?ret=/";
			}
			function forgotPassword() {
				window.location.href = "/forgot-password";
			}
			function login() {
				var email = $( '#inputEmail' ).val();
				var password = $( '#inputPassword' ).val();
				if( email == null || email.trim() == "" ) {
					<#-- Throw message - Please Enter Email -->
					alert( "Please Enter your Email" );
					return;
				}
				if( password == null || password.trim() == "" ) {
					<#--  Throw message - Please Enter Password -->
					alert( "Please Enter your Password" );
					return;
				}
				if( ! validateEmail( email ) ) {
					<#--  Throw message - Email is not valid -->
					alert( "Please Enter a valid Email" );
					return;
				}
				jQuery( '#loginButton' ).prop( 'disabled', true );
				$.ajax({
				
					type: 'post',
					url: '/api/user/login',

					data: { 
						'email': email, 
						'password': password
					},
					
					success: function( response ) {
						jQuery( '#loginButton' ).prop( 'disabled', false );
						if( getUrlParameters().ret != null )
							window.location.href = decodeURIComponent( getUrlParameters().ret );
						else
							window.location.href = "/"; 
					},
					
					error: function( response ) {
						jQuery( '#loginButton' ).prop( 'disabled', false );
						var message = jQuery.parseJSON( response.responseText );
						var status = response.status;

						if( message["email"] != null ) 
							alert( "Error " + status + " : " + message["email"] );
						else if( message["password"] != null ) 
							alert( "Error " + status + " : " + message["password"] );
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
		                <h4 style="text-align: center;">${ _strings.user_sign_in_to_pratilipi }</h4>
		            </div>
					<div class="text-center">
						<button class="fb-button" onClick="facebookLogin()">
							<span class="icon"></span>
							<span class="button-text">${ _strings.user_sign_in_with_facebook }</span>
						</button>
					</div>
					<div class="text-center">
						<#-- Listener created for the button -->
						<button id="googleLoginButton" class="google-button">
							<span class="icon"></span>
							<span class="button-text">${ _strings.user_sign_in_with_google }</span>
						</button>
					</div>
	
		            <form id="userLoginForm" class="form-horizontal" action="javascript:void(0);">
		                <div class="form-group">
		                    <label for="inputEmail" class="col-sm-2 control-label">Email</label>
		                    <div class="col-sm-10">
		                        <input name="email" type="email" class="form-control" id="inputEmail" placeholder="${ _strings.user_email }">
		                    </div>
		                </div>
		                <div class="form-group">
		                    <label for="inputPassword" class="col-sm-2 control-label">Password</label>
		                    <div class="col-sm-10">
		                        <input name="password" type="password" class="form-control" id="inputPassword" placeholder="${ _strings.user_password }">
		                    </div>
		                </div>
		                <div class="form-group" style="margin: 25px auto; text-align: center;">
		                	<button id="loginButton" class="pratilipi-dark-blue-button" onclick="login()">${ _strings.user_sign_in }</button>
		                </div>
		            </form>
		            
		            <div style="text-align: center;">
		            	<button class="pratilipi-new-blue-button" onclick="register()">${ _strings.user_sign_up_for_pratilipi }</button>
		            </div>
		            
		            <div style="text-align: center; margin-top: 25px; margin-bottom: 25px;">
		            	<button class="pratilipi-new-blue-button" onclick="forgotPassword()">${ _strings.user_forgot_password }</button>
		            </div>
	
		        </div>
		        
			</div>
		</div>
		<#include "../element/basic/pratilipi-footer.ftl">
		<#-- Style and Scripts for facebook login and google login -->
		<#include "../element/basic/pratilipi-facebook-login.ftl">
		<#include "../element/basic/pratilipi-google-login.ftl">
	</body>
	
</html>