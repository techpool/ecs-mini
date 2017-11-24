<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-static-page">
		<#include "meta/Head.ftl">
		<style>
			main {
				padding-top: 54px;
			}
		</style>
	</head>
	
	<body>
		<dom-module id="pratilipi-static-page">
			<template>
				<pratilipi-user user='{{ user }}' user-data='${ userJson }'></pratilipi-user>
				<pratilipi-write pratilipi-types='${ pratilipiTypesJson }'></pratilipi-write>
				<pratilipi-alert></pratilipi-alert>
				<div class="header-pos">
		   			<pratilipi-header language-map='${ languageMap }' user='[[ user ]]'></pratilipi-header>
		   		</div>
		   		<main>
					<div class="parent-container margin-top-bottom">
						<div class="container">
							<pratilipi-navigation
								class='pull-left hidden-xs hidden-sm'
								navigation-list='${ navigationListJson }'
								></pratilipi-navigation>
							<#-- Navigation & Search bar for extra-small & small screens. -->
							<pratilipi-navigation-drawer with-backdrop navigation-list='${ navigationListJson }'></pratilipi-navigation-drawer>
							<div class='secondary-500' style='padding:20px; overflow:hidden'>
								<h3>${ staticTitle }</h3>
								<div style="text-align: justify;">${ content }</div>
							</div>
						</div>
					</div>
				</main>
				<footer>
    				<pratilipi-footer></pratilipi-footer>
				</footer>
				<div class="scroll-top-button">
					<a id="scrollToTop" on-click="scrollToTop"><paper-fab mini noink style="background: #c0c0c0;" icon="icons:arrow-upward"></paper-fab></a>
				</div>
			</template>
			<script>
				HTMLImports.whenReady(function () {
					Polymer({
						is: 'pratilipi-static-page',
						
						properties: {
							lastScrollTop: { type: Number, value: 0 }
						},
						
						scrollToTop: function() {
							$( 'html, body' ).animate( { scrollTop : 0 },800 );
						},
						
						ready: function() {
							jQuery( '#scrollToTop' ).css( "display", "none" );
						},
						
						scrollHandler: function( st ) {
							if( st > this.lastScrollTop || st < 100 )
								jQuery( '#scrollToTop' ).fadeOut();
							else
								jQuery( '#scrollToTop' ).fadeIn();
							this.lastScrollTop = st;
						}
					});
				});
			</script>
		</dom-module>
		<pratilipi-static-page></pratilipi-static-page>
		<#include "meta/Foot.ftl">
	</body>
</html>