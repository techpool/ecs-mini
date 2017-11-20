<!DOCTYPE html>
<html lang="${lang}">

	<head>
		<#assign mainPage="pratilipi-2016-page">
		<#include "meta/Head.ftl">
	</head>
	
	<body>
		<dom-module id="pratilipi-2016-page">
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
							<div style='padding: 2px; overflow: hidden'>
								<pratilipi-android-launch></pratilipi-android-launch>
								<div id="androidLaunchBottom">
									<div class='secondary-500' style="padding: 20px;">
										<img src="{{ image }}" style="width: 100%;" />
									</div>
								</div>
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
						is: 'pratilipi-2016-page',

						behaviors: [
							Polymer.IronResizableBehavior
						],

						listeners: {
							'iron-resize': '_onIronResize'
						},

						properties: {
							lastScrollTop: { type: Number, value: 0 },
							image: { type: Image }
						},

						_onIronResize: function() {
							var windowsize = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
							if( windowsize > 560 )
								this.image = "http://public.pratilipi.com/year-in-review-2016/high-res/YearInReview-${lang}.jpg";
							else
								this.image = "http://public.pratilipi.com/year-in-review-2016/low-res/YearInReview-${lang}.jpg";
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
		<pratilipi-2016-page></pratilipi-2016-page>
		<#include "meta/Foot.ftl">
	</body>
</html>