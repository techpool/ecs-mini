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
				var name = $( '#inputName' ).val();
				var email = $( '#inputEmail' ).val();
				var password = $( '#inputPassword' ).val();
				if( name == null || name.trim() == "" ) {
					<#--  Throw message - Please Enter Name -->
					alert( "Please Enter your Name" );
					return;
				}
				if( email == null || email.trim() == "" ) {
					<#--  Throw message - Please Enter Email -->
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
				if( !document.getElementById( "acceptTerms" ).checked ) {
					alert( "Please accept to our terms!" );
					return;
				}
				jQuery( '#registerButton' ).prop( 'disabled', true );
				$.ajax({
				
					type: 'post',
					url: '/api/user/register',

					data: {
						'name': name,
						'email': email, 
						'password': password,
						'language': "${ language }"
					},
					
					success: function( response ) {
						jQuery( '#registerButton' ).prop( 'disabled', false );
						if( getUrlParameters().ret != null )
							window.location.href = decodeURIComponent( getUrlParameters().ret );
						else
							window.location.href = "/";
					},
					
					error: function ( response ) {
						var message = jQuery.parseJSON( response.responseText );
						var status = response.status;
						jQuery( '#registerButton' ).prop( 'disabled', false );
						if( message["name"] != null ) 
							alert( "Error " + status + " : " + message["name"] );
						else if( message["email"] != null ) 
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
		                <h4 style="text-align: center;">${ _strings.user_sign_up_for_pratilipi }</h4>
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

		            <form id="userRegisterForm" class="form-horizontal" action="javascript:void(0);">
		            	<div class="form-group">
		                    <label for="inputName" class="col-sm-2 control-label">Name</label>
		                    <div class="col-sm-10">
		                        <input name="name" type="text" class="form-control" id="inputName" placeholder="${ _strings.user_full_name }">
		                    </div>
		                </div>
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
		                <div style="padding: 12px 0px;">
						<input style="position: absolute; margin-top: 8px;" type="checkbox" name="acceptTerms" id="acceptTerms">
							<p style="margin-left: 24px; text-align: center;">
								${ _strings.register_part_1 }
								<a class="pratilipi-blue" style="white-space: nowrap; font-size: 16px;" href="/privacy-policy">${ _strings.footer_privacy_policy }</a>
								&nbsp;${ _strings.register_part_2 }&nbsp;
								<a class="pratilipi-blue" style="white-space: nowrap; font-size: 16px;" href="/terms-of-service">${ _strings.footer_terms_of_service }</a>
								&nbsp;${ _strings.register_part_3 }
							</p>
						</div>
		                <div class="form-group" style="margin: 0 auto; text-align: center;">
		                	<button id="registerButton" class="pratilipi-dark-blue-button" onclick="register()">${ _strings.user_sign_up }</button>
		                </div>
		            </form>
					<div style="text-align: center; margin-bottom: 20px;">
		            	<a class="link" href="/">${ _strings.back }</a>
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