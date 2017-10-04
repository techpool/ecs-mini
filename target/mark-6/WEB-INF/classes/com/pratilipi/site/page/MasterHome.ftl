<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="A platform to discover, read and share your favorite stories, poems and books in a language, device and format of your choice.">
	<title>Read Hindi, Gujarati and Tamil Stories, Poems and Books</title>
	<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
	<style>
		@import url(https://fonts.googleapis.com/css?family=Montserrat);
		html, body, div, span, applet, object, iframe,
		h1, h2, h3, h4, h5, h6, p, blockquote, pre,
		a, abbr, acronym, address, big, cite, code,
		del, dfn, em, img, ins, kbd, q, s, samp,
		small, strike, strong, sub, sup, tt, var,
		b, u, i, center,
		dl, dt, dd, ol, ul, li,
		fieldset, form, label, legend,
		table, caption, tbody, tfoot, thead, tr, th, td,
		article, aside, canvas, details, embed,
		figure, figcaption, footer, header, hgroup,
		menu, nav, output, ruby, section, summary,
		time, mark, audio, video {
			margin: 0;
			padding: 0;
			border: 0;
			font-size: 100%;
			font: inherit;
			vertical-align: baseline; 
		}
		
		/* HTML5 display-role reset for older browsers */
		article, aside, details, figcaption, figure,
		footer, header, hgroup, menu, nav, section {
			display: block; 
		}
		
		body {
			line-height: 1; 
			font-family: Avenir-Black, Montserrat, Helvetica;
			font-weight: 600;
		}
		body * {
			font-family: inherit;
		}
		button {
			cursor: pointer;
		}
		ol, ul {
			list-style: none; 
		}
		
		blockquote, q {
			quotes: none; 
		}
		
		blockquote:before, blockquote:after,
		q:before, q:after {
			content: '';
			content: none; 
		}
		
		table {
			border-collapse: collapse;
			border-spacing: 0; 
		}
		
		
		.container {
			width: 100%;
			padding: 0px; 
		}
		
		.wrapper {
			width: 100%;
		}
		
		.active {
			display: block !important; 
		}
		
		.inactive {
			display: none !important; 
		}
		
		a {
			text-decoration: none; 
		}
		
		.clearfix:after {
			content: "";
			display: block;
			clear: both; 
		}
		
		* {
			box-sizing: border-box; 
		}
		
		.landing-page .pratilipi-banner {
			position: relative;
			z-index: 1;
			padding: 10px;
			text-align: center;
			min-height: 520px;
			overflow: hidden;
		}
		
		.landing-page .pratilipi-banner .pratilipi-background {
			position: absolute;
			z-index: -1;
			top: 0;
			bottom: 0;
			left: 0;
			right: 0;
			background-color: lightgrey;
			opacity: 0.72;
			width: 100%;
			height: 100%;
			background-repeat: no-repeat;
		}
		@media only screen and (max-width: 640px) {
			.landing-page .pratilipi-banner .pratilipi-background {
				background: url(https://www.ptlp.co/resource-all/home-page/pratilipi-banner-compressed-mobile.jpg) no-repeat 0 0;
				background-size: cover;
				background-position-x: -128px;
			}
		}
		@media only screen and (min-width: 641px) {
			.landing-page .pratilipi-banner .pratilipi-background {
				background: url(https://www.ptlp.co/resource-all/home-page/pratilipi-banner-compressed.jpg) no-repeat 0 0;
				background-size: cover;
				background-position: top;
			}
		}
		.landing-page .pratilipi-banner .login-btn,
		.landing-page .pratilipi-banner .signup-btn {
			font-family: Montserrat, Helvetica;
			border: 2px solid #FFFFFF;
			border-radius: 7px;
			font-size: 16px;
			line-height: 18px;
			color: #FFFFFF;
			padding: 8px 24px;
		    padding-top: 10px;
			outline: none;
			background: transparent;
		}
		.landing-page .pratilipi-banner .user-dropdown button {
			background: transparent;
			padding: 0px 25px;
			font-size: 20px;
			line-height: 40px;
			font-weight: 200;
			margin: 0;
			outline: none; 
			border: none;
			color: #FFFFFF;
		}
		.landing-page .pratilipi-banner .user-dropdown .username {
			font-size: 18px;
			font-family: Montserrat, Helvetica;
		}
		.landing-page .pratilipi-banner .user-dropdown .dropdown ul.dropdown-menu {
			right: -48px;
			background-color: transparent;
			border: none;
			-webkit-box-shadow: none;
			box-shadow: none;
		}
		.landing-page .pratilipi-banner .user-dropdown .dropdown ul.dropdown-menu > li > a,
		.landing-page .pratilipi-banner .user-dropdown .dropdown ul.dropdown-menu > li > a:hover,
		.landing-page .pratilipi-banner .user-dropdown .dropdown ul.dropdown-menu > li > a:focus {
			color: #FFFFFF;
			background-color: transparent;
			border: none;
			-webkit-box-shadow: none;
			box-shadow: none;
			cursor: pointer;
		}
		.landing-page .pratilipi-banner .user-dropdown .open > .dropdown-menu {
			animation-name: slidenavAnimation;
			animation-duration:.5s;
			animation-iteration-count: 1;
			animation-timing-function: ease;
			animation-fill-mode: forwards;
			-webkit-animation-name: slidenavAnimation;
			-webkit-animation-duration:.5s;
			-webkit-animation-iteration-count: 1;
			-webkit-animation-timing-function: ease;
			-webkit-animation-fill-mode: forwards;
			-moz-animation-name: slidenavAnimation;
			-moz-animation-duration:.5s;
			-moz-animation-iteration-count: 1;
			-moz-animation-timing-function: ease;
			-moz-animation-fill-mode: forwards;
		}
		@keyframes slidenavAnimation {
			from {
				opacity: 0;
			}
			to {
				opacity: 1;
			}
		}
		@-webkit-keyframes slidenavAnimation {
			from {
				opacity: 0;
			}
			to {
				opacity: 1;
			}
		}	
		
		.landing-page .pratilipi-banner .content-wrapper {
			padding: 64px 0px; 
		}
		.landing-page .pratilipi-banner .content-wrapper .description {
			margin: 12px auto;
			font-family: Montserrat, Helvetica;
		}
		.landing-page .pratilipi-banner .content-wrapper .description .bg-white {
			background-color: #fff;
			color: #000000;
			border: 2px solid transparent;
			letter-spacing: 0.66px;
			overflow: hidden;
		}
		@media only screen and (max-width: 512px) {
			.landing-page .pratilipi-banner .content-wrapper .description-mobile {
				display: block;
				width: 97%;
			}
			.landing-page .pratilipi-banner .content-wrapper .description-widescreen {
				display: none;
			}
			.landing-page .pratilipi-banner .content-wrapper .description div.bg-white {
				display: block;
				margin-top: 10px;
				margin-bottom: 12px;
				border-radius: 16px;
				font-size: 18px;
				line-height: 22px;
				text-align: center;
				padding: 12px 5px;
				opacity: 0.6;
			}
		}
		@media only screen and (min-width: 512px) {
			.landing-page .pratilipi-banner .content-wrapper .description-mobile {
				display: none;
			}
			.landing-page .pratilipi-banner .content-wrapper .description-widescreen {
				display: block;
			}
			.landing-page .pratilipi-banner .content-wrapper .description h5.bg-white {
				display: inline-block;
				margin: 2px 10px;
				padding: 2px 5px; 
				font-size: 18px;
				opacity: 0.80;
			}
		}
		.landing-page .pratilipi-banner .content-wrapper .heading-wrapper h2 {
			display: inline-block;
			white-space: nowrap;
			font-weight: 400;
			letter-spacing: 1.13px;
			margin-bottom: 4px;
			color: #FFFFFF;
			text-shadow: 4px 1px 4px rgba(0,0,0,0.50);
		}
		
		@media only screen and (max-width: 840px) {
			.landing-page .pratilipi-banner .content-wrapper .heading-wrapper {
				font-size: 28px;
				margin-top: 24px;
			}
		}
		@media only screen and (min-width: 840px) {
			.landing-page .pratilipi-banner .content-wrapper .heading-wrapper {
				font-size: 42px;
				margin-bottom: 48px;
			}
		}
		.landing-page .pratilipi-banner .content-wrapper .logo {
			display: block;
			text-align: center;
		}
		.landing-page .pratilipi-banner .content-wrapper .logo .pratilipi {
			display: inline-block;
			width: 75px;
			height: 75px;
			border-radius: 50%;
			background-image: url("https://www.ptlp.co/resource-all/home-page/pratilipi_logo.png");
			background-repeat: no-repeat;
			background-size: cover; 
		}
		.landing-page .pratilipi-banner .content-wrapper .logo h1 {
			display: inline-block;
			margin-top: 16px;
			vertical-align: top;
			font-weight: 400;
			font-size: 36px;
			color: #FFFFFF;
			letter-spacing: 1.21px;
			text-shadow: 0px 2px 4px rgba(0,0,0,0.50);
		}
		.landing-page .pratilipi-banner .content-wrapper button.start-reading-btn, 
		.landing-page .pratilipi-banner .content-wrapper button.start-reading-btn:hover, 
		.landing-page .pratilipi-banner .content-wrapper button.start-reading-btn:focus {
			opacity: 0.85;
			background: #D0021B;
			box-shadow: 0px 2px 4px 0px rgba(0,0,0,0.50);
			border-radius: 7px;
			font-size: 18px;
			color: #FFFFFF;
			letter-spacing: 0px;
			outline: none;
			margin: 10px;
		    border: 2px solid transparent;
		    padding: 10px 48px;
		}
		.landing-page .wrapper {
			margin: auto;
			display: flex;
			flex-direction: row;
			flex-flow: row wrap;
			justify-content: space-between;
		}
		.landing-page .wrapper ul.tiles-container, .landing-page .wrapper .tiles-container-mobile {
			margin: auto;
			padding:10px;
			display: block;
		}
		.landing-page .wrapper ul.tiles-container li.image {
			display: inline-block;
			width: 24%;
			padding: 7px;
			overflow: hidden;
			margin-bottom: 0px;
		}
		.landing-page .wrapper .tiles-container-mobile {
			padding-left: 24px;
			padding-right: 24px;
		}
		.landing-page .wrapper .tiles-container-mobile a.language-button {
			-moz-box-shadow: 1px 1px 1px -1px #555555;
			-webkit-box-shadow: 1px 1px 1px -1px #555555;
			box-shadow: 1px 1px 1px -1px #555555;
			background-color: #F9F9F9;
			-moz-border-radius: 28px;
			-webkit-border-radius: 28px;
			border-radius: 28px;
			border: 1px solid #333;
			display: inline-block;
			cursor: pointer;
			color: #333;
			font-size: 18px;
			padding: 12px 16px;
			text-align: center;
			text-decoration: none;
			margin-bottom: 24px;
		}
		.landing-page .wrapper .tiles-container-mobile a.language-button:hover {
			background-color: transparent;
		}
		.landing-page .wrapper .tiles-container-mobile a.language-button:active {
			position: relative;
			top: 1px;
		}
		
		
		@media only screen and (min-width: 769px) {
			.landing-page .wrapper ul.tiles-container {
				width: 100%;
				text-align: center;
			}
			.landing-page .wrapper ul.tiles-container li.image a .tiles {
				background-size: cover; 
				background-position: top;
				height: 300px;
			}
			.landing-page .wrapper .tiles-container-mobile {
				display: none;
			} 
		}
		
		@media only screen and (max-width: 768px) {
			.landing-page .wrapper ul.tiles-container {
				display: none;
			}
			.landing-page .wrapper .tiles-container-mobile {
				display: block;
			}
		}
		
		
		.landing-page .wrapper a {
			width: 100%;
			margin: 0; 
		}
		.landing-page .wrapper a .tiles {
			width: 100%;
			padding-bottom: 100%;
			background-color: #ebebeb;
			background-repeat: no-repeat;
			background-size: cover;
			background-position: center;
			opacity: 0.6; 
		}
		.landing-page .wrapper a .tiles:hover {
			opacity: 1; 
			transition: opacity .5s;
			-webkit-transition: opacity .5s;
		}
		.landing-page .wrapper a .tiles .language {
			text-decoration: none;
			float: left;
			margin-top: 30px;
			background: rgba(255, 255, 255, 0.8);
			text-align: center;
			padding: 20px;
			font-size: 18px;
			color: #000;
			display: inline-block; 
		}
		.landing-page .notify-me-wrapper {
			padding: 20px 5px;
			text-align: center;
			background-color: #333;
		}
		.landing-page .notify-me-wrapper .notify-elements {
			margin: 0 auto; 
		}
		.landing-page .notify-me-wrapper .notify-elements h3 {
			color: #fff;
			font-size: 24px;
			margin: 16px;
			line-height: 28px;
			font-weight: 400; 
		}
		.landing-page .notify-me-wrapper .notify-loader {
			display: none;
		    margin: 0 auto;
		    border: 2px solid #D0021B;
		    border-radius: 50%;
		    border-top: 2px solid #333;
		    width: 28px;
		    height: 28px;
		    margin-top: 36px;
		    -webkit-animation: spin 1s linear infinite;
		    animation: spin 1s linear infinite;
		}
		@-webkit-keyframes spin {
		  0% { -webkit-transform: rotate(0deg); }
		  100% { -webkit-transform: rotate(360deg); }
		}
		@keyframes spin {
		  0% { transform: rotate(0deg); }
		  100% { transform: rotate(360deg); }
		}
		.landing-page .notify-me-wrapper .notify-message {
			margin: 0 auto;
			display: none;
		}
		.landing-page .notify-me-wrapper .notify-message h2 {
			color: #fff;
			font-size: 26px;
			line-height: 32px;
			margin: 16px;
			font-weight: 400; 
		}
		.landing-page .notify-me-wrapper .notify-elements .language-selection {
			background-color: #ebebeb;
			color: #555555;
			font-size: 16px;
			font-weight: 100;
			float: none;
			height: 35px; 
		}
		.landing-page .notify-me-wrapper .notify-elements .input-field {
			color: #555555;
			font-size: 16px;
			border-radius: 4px;
			height: 35px;
			background-color: #ebebeb;
			padding-left: 10px; 
		}
		.landing-page .notify-me-wrapper .notify-elements .notify-me-btn,
		.landing-page .notify-me-wrapper .notify-message .notify-me-btn {
			border: 2px solid transparent;
			padding: 0px 25px;
			font-size: 16px;
			line-height: 32px;
			font-weight: 200;
			outline: none;
			background: #D0021B;
			box-shadow: 0px 2px 4px 0px rgba(0,0,0,0.50);
			border-radius: 7px;
			color: #FFFFFF;
		}
		@media only screen and (max-width: 599px) {
			.landing-page .notify-me-wrapper .notify-elements .language-selection {
				display: block;
				width: 90%;
				margin-left: auto;
				margin-right: auto;
				margin-bottom: 12px;
			}
			.landing-page .notify-me-wrapper .notify-elements .input-field {
				display: block;
				width: 90%;
				margin-bottom: 12px;
				margin-left: auto;
				margin-right: auto;
			}
			.landing-page .notify-me-wrapper .notify-elements .notify-me-btn,
			.landing-page .notify-me-wrapper .notify-message .notify-me-btn {
				display: block;
				margin-left: auto;
				margin-right: auto;
			}
		}
		@media only screen and (min-width: 600px) {
			.landing-page .notify-me-wrapper .notify-elements .language-selection {
				display: inline-block;
				margin: 10px 0px;
				margin-left: 8px;
				width: 200px;
			}
			.landing-page .notify-me-wrapper .notify-elements .input-field {
				display: inline-block;
				width: 300px;
				margin-left: 16px;
				margin-right: 16px;
			}
			.landing-page .notify-me-wrapper .notify-elements .notify-me-btn,
			.landing-page .notify-me-wrapper .notify-message .notify-me-btn {
				display: inline-block;
				margin: 10px;
			}
		}
		.landing-page .pratilipi-footer {
			font-family: Montserrat, Helvetica;
			padding: 20px;
			background-color: #ebebeb; 
		}
		.landing-page .pratilipi-footer p {
			margin-top: 12px;
			font-size: 15px;
		}
		.landing-page .pratilipi-footer .about-pratilipi {
			position: relative;
			color: #666;
			padding-top: 10px;
		}
		.landing-page .pratilipi-footer .about-pratilipi h5 {
			font-size: 18px;
			margin-bottom: 10px;
			color: inherit; 
		}
		.landing-page .pratilipi-footer .about-pratilipi span {
			display: block;
		}
		.landing-page .pratilipi-footer .about-pratilipi a {
			display: inline-block;
		}
		.landing-page .pratilipi-footer .about-pratilipi span {
			font-size: 14px;
			line-height: 1.5;
			display: block;
			color: #666;
			margin-bottom: 4px;
		}
		.landing-page .pratilipi-footer .about-pratilipi .copyrights {
			font-size: 14px;
			position: absolute;
			bottom: 0; 
		}
		@media only screen and (max-width: 599px) {
			.landing-page .pratilipi-footer .about-pratilipi {
				display: block;
				margin-left: auto;
				margin-right: auto;
				text-align: center;
				padding-bottom: 20px;
			}
			.landing-page .pratilipi-footer .about-part-3 {
				display: block;
				margin-left: auto;
				margin-right: auto;
				text-align: center;
				padding-bottom: 20px;
			}
			.landing-page .pratilipi-footer p {
				text-align: center;
			}
		}
		@media only screen and (min-width: 600px) {
			.landing-page .pratilipi-footer .about-pratilipi {
				display: inline-block;
				width: 50%;
				text-align: left;
				border-right: 1px solid gray;
				padding-bottom: 40px;
				padding-right: 12px;
			}
			.landing-page .pratilipi-footer .about-part-3 {
				margin-top: 5px;
				position: absolute;
				display: inline-block;
				width: 45%;
				padding-left: 20px;
				padding-top: 4px;
			}
		}
		.landing-page .pratilipi-footer .about-part-3 {
			color: #666;
			font-size: 16px;
		}
		.landing-page .pratilipi-footer .about-part-3 span {
			display: block;
			line-height: 1.3em;
			margin-bottom: 16px; 
		}
		.landing-page .pratilipi-footer .about-part-3 .social-media-links {
			white-space: nowrap;
		}
		.landing-page .pratilipi-footer .about-part-3 .social-media-links img {
			width: 20px;
		}
		.landing-page .pratilipi-footer .about-part-3 a {
			color: #666;
			margin-right: 16px; 
		}
		.android-logo img {
			max-width: 128px;
			width: 128px;
			z-index: 64456;
			opacity: 1;
		}
		@media only screen and (max-width: 768px) {
			.android-logo {
				margin-left: 20px;
				float: none;
				display: block;
			}
		}
		@media only screen and (min-width: 769px) {
			.android-logo {
				float: right;
				margin-top: 0;
				margin-right: -86px;
			}
		}
	</style>
</head>

<body>
	<div class="container">
		<div class="landing-page">
			<div class="pratilipi-banner clearfix">

				<div class="pratilipi-background"></div>

				<div class="content-wrapper">

					<div class="heading-wrapper">
						<h2>1,000,000 Readers.&nbsp;</h2><h2>9,000+ Writers.&nbsp;</h2><h2>8 Languages.&nbsp;</h2><h2>1 Platform.</h2>
					</div>

					<div class="logo">
						<div class="pratilipi"></div>
						<h1>
							pratilipi
							<#if basicMode>
								<#assign playstore_url = "https://play.google.com/store/apps/details?id=com.pratilipi.mobile.android&referrer=utm_source%3Dpratilipi_mini_web%26utm_medium%3Dweb_app_badge%26utm_campaign%3Dapp_download">
							<#else>
								<#assign playstore_url = "https://play.google.com/store/apps/details?id=com.pratilipi.mobile.android&referrer=utm_source%3Dpratilipi_main_web%26utm_medium%3Dweb_app_badge%26utm_campaign%3Dapp_download">
							</#if>
							<a class="android-logo" target="_blank" href="${ playstore_url }">
								<img src="https://www.ptlp.co/resource-all/image/google-play-badge.png" />
							</a>
						</h1>
					</div>

					<div class="description description-mobile">
						<div class="bg-white">Read great stories and write your own on world's largest platform for Indian languages</div>
					</div>

					<div class="description description-widescreen">
						<h5 class="bg-white">Read great stories and write your own on</h5>
						<br>
						<h5 class="bg-white">world's largest platform for Indian languages</h5>
					</div>

					<button type="button" class="start-reading-btn" onClick="startReading()">START READING</button>

				</div>

			</div>

			<div class="wrapper" id="wrapper">
				<ul class="tiles-container" id="tiles-container">
					<li class="image">
						<a href="http://hindi.pratilipi.com/">
							<div class="tiles" style="background-image:url('https://www.ptlp.co/resource-all/home-page/pratilipi-hindi-compressed-s.jpg');">
								<span class="language">हिंदी</span>
							</div>
						</a>
					</li>
					<li class="image">
						<a href="http://tamil.pratilipi.com/">
							<div class="tiles" style="background-image:url('https://www.ptlp.co/resource-all/home-page/pratilipi-tamil-compressed-s.jpg');">
								<span class="language">தமிழ்</span>
							</div>
						</a>
					</li>
					<li class="image">
						<a href="http://malayalam.pratilipi.com/">
							<div class="tiles" style="background-image:url('https://www.ptlp.co/resource-all/home-page/pratilipi-malayalam-compressed-s.jpg');">
								<span class="language">മലയാളം</span>
							</div>
						</a>
					</li>
					<li class="image">
						<a href="http://bengali.pratilipi.com/">
							<div class="tiles" style="background-image:url('https://www.ptlp.co/resource-all/home-page/pratilipi-bengali-compressed-s.jpg'); background-size: cover; background-position: top;">
								<span class="language">বাংলা</span>
							</div>
						</a>
					</li>
					<li class="image">
						<a href="http://telugu.pratilipi.com/">
							<div class="tiles" style="background-image:url('https://www.ptlp.co/resource-all/home-page/pratilipi-telugu-compressed-s.jpg');">
								<span class="language">తెలుగు</span>
							</div>
						</a>
					</li>
					<li class="image">
						<a href="http://gujarati.pratilipi.com/">
							<div class="tiles" style="background-image:url('https://www.ptlp.co/resource-all/home-page/pratilipi-gujarati-compressed-s.jpg'); background-size: cover; background-position: top;">
								<span class="language">ગુજરાતી</span>
							</div>
						</a>
					</li>
					<li class="image">
						<a href="http://marathi.pratilipi.com/">
							<div class="tiles" style="background-image:url('https://www.ptlp.co/resource-all/home-page/pratilipi-marathi-compressed-s.jpg');">
								<span class="language">मराठी</span>
							</div>
						</a>
					</li>
					<li class="image">
						<a href="http://kannada.pratilipi.com/">
							<div class="tiles" style="background-image:url('https://www.ptlp.co/resource-all/home-page/pratilipi-kannada-compressed-s.jpg');">
								<span class="language">ಕನ್ನಡ</span>
							</div>
						</a>
					</li> 
				</ul>

				<div class="tiles-container-mobile" id="tiles-container-mobile">
					<a class="language-button" href="http://hindi.pratilipi.com/">
						<span>हिंदी</span>
					</a>
					<a class="language-button" href="http://tamil.pratilipi.com/">
						<span>தமிழ்</span>
					</a>
					<a class="language-button" href="http://malayalam.pratilipi.com/">
						<span class="language">മലയാളം</span>
					</a>
					<a class="language-button" href="http://bengali.pratilipi.com/">
						<span class="language">বাংলা</span>
					</a>
					<a class="language-button" href="http://telugu.pratilipi.com/">
						<span class="language">తెలుగు</span>
					</a>
					<a class="language-button" href="http://gujarati.pratilipi.com/">
						<span class="language">ગુજરાતી</span>
					</a>
					<a class="language-button" href="http://marathi.pratilipi.com/">
						<span class="language">मराठी</span>
					</a>
					<a class="language-button" href="http://kannada.pratilipi.com/">
						<span class="language">ಕನ್ನಡ</span>
					</a>
				</div>

			</div>

			<div class="notify-me-wrapper" id="notify-me-wrapper">

				<div class="notify-elements" id="notify-elements">
					<h3>Be the first to know when your language gets added</h3>

					<select id="mailingListLanguage" name="Language" class="language-selection">
						<option value="none" selected disabled>Language</option>
						<option value="LAUNCH_ANNOUNCEMENT_URDU">		Urdu		</option>
						<option value="LAUNCH_ANNOUNCEMENT_ODIA">		Odia		</option>
						<option value="LAUNCH_ANNOUNCEMENT_PUNJABI">	Punjabi		</option>
						<option value="LAUNCH_ANNOUNCEMENT_ASSAMESE">	Assamese	</option>
						<option value="LAUNCH_ANNOUNCEMENT_MAITHILI">	Maithili	</option>
						<option value="LAUNCH_ANNOUNCEMENT_BHOJPURI">	Bhojpuri	</option>
						<option value="LAUNCH_ANNOUNCEMENT_OTHER">		Any Other	</option>
					</select>
					<input class="input-field" type="text" name="mailingListLanguageInput" id="mailingListLanguageInput" placeholder="Language">
					<input class="input-field" type="email" name="mailingListEmail" id="mailingListEmail" placeholder="Email" onKeyPress="mailingList( event, this )">
					<button class="notify-me-btn" onClick="mailingList()">NOTIFY ME!</button>
				</div>
				
				<div id="notify-loader" class="notify-loader"></div>

				<div class="notify-message" id="notify-message">
					<h2 id="notify-message-text"></h2>
					<button class="notify-me-btn" onClick="addSubscription()">ADD ANOTHER SUBSCRIPTION</button>
				</div>
			</div>
			<div class="pratilipi-footer">
				<div class="about-pratilipi">
					<h5>Pratilipi</h5>
					<span>Jai&nbsp;Plaza&nbsp;Elite, #1064, 1st&nbsp;Floor, 7th&nbsp;A&nbsp;Main, 3rd&nbsp;Block&nbsp;Koramangala, Bengaluru, Karnataka&nbsp;-&nbsp;560095</span>
					<span>CIN:&nbsp;U72200KA2015PTC079230</span>
					<br/>
					<a href="mailto:${ _strings.footer_contact_email }"><span>${ _strings.footer_contact_email }</span></a>
					<br/>
					<a href="tel:${ _strings.footer_contact_number?replace(' ', '') }"><span>${ _strings.footer_contact_number }</span></a>
				</div>
				<div class="about-part-3">
					<span>Follow us on Social Media :</span>
					<div class="social-media-links">
						<a href="https://www.facebook.com/Pratilipidotcom" target="_blank"><img src="https://www.ptlp.co/resource-all/icon/svg/facebook-grey.svg" /></a>
						<a href="https://twitter.com/TeamPratilipi" target="_blank"><img src="https://www.ptlp.co/resource-all/icon/svg/twitter-grey.svg" /></a>
						<a href="https://plus.google.com/+PratilipiTeam" target="_blank"><img src="https://www.ptlp.co/resource-all/icon/svg/google-plus-grey.svg" /></a>
						<a href="https://www.linkedin.com/company/pratilipi" target="_blank"><img src="https://www.ptlp.co/resource-all/icon/svg/linkedin-grey.svg" /></a>
						<a href="https://www.quora.com/topic/Pratilipi-company" target="_blank"><img src="https://www.ptlp.co/resource-all/icon/svg/quora.svg" /></a>
					</div>
				</div>
				<p class="copyrights">&copy;&nbsp;2017 Nasadiya Tech. Pvt. Ltd.</p>
			</div>
		</div>
	</div>

	<script src='https://www.ptlp.co/third-party/jquery-2.1.4/jquery-2.1.4.min.js'></script>
	<script>
		$( document ).ready(function() {
			<#-- Setting tiles min-height -->
			jQuery( '.wrapper' ).css( "min-height", jQuery( window ).height() + "px" );
			<#-- Setting banner height -->
			if( $( window ).width() < 768 ) {
				jQuery( '.pratilipi-banner' ).height( jQuery( window ).height() );
			} else {
				var diff = ( jQuery( window ).height() - jQuery( '#tiles-container' ).height() ) / 2;
				jQuery( '.pratilipi-banner' ).height( jQuery( window ).height() - 20 - diff - 108 );
				jQuery( '.pratilipi-banner' ).css( "max-height", Math.max( jQuery( window ).height(), jQuery( '.content-wrapper' ).height() + 108 ) + "px" );
			}
			<#-- Hiding the language input on ready -->
			document.getElementById( 'mailingListLanguageInput' ).style.display = "none";
			document.getElementById( "mailingListLanguage" ).addEventListener( "change", onMailingListLanguageChange, false );
		});
		function onMailingListLanguageChange() {
			var val = document.getElementById( 'mailingListLanguage' ).value;
			if( val == "LAUNCH_ANNOUNCEMENT_OTHER" ) {
				document.getElementById( 'mailingListLanguageInput' ).style.display = "inline-block";
			} else {
				document.getElementById( 'mailingListLanguageInput' ).style.display = "none";
			}
		}
		function validateEmail( email ) {
			var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
			return re.test(email);
		}
		function startReading() {
			jQuery('html, body').animate( { scrollTop: jQuery( '#wrapper' ).offset().top }, 500 );
		}
		function addSubscription() {
			document.getElementById( "notify-elements" ).style.display = "block";
			document.getElementById( "notify-message" ).style.display = "none";
		}
		function mailingList( e, input ) {
			if( e != null ) {
				var code = ( e.keyCode ? e.keyCode : e.which );
				if( code != 13 )
					return;
			}
			var mailingList = document.getElementById( 'mailingListLanguage' ).value;
			var comment = mailingList == "LAUNCH_ANNOUNCEMENT_OTHER" ? 
					document.getElementById( 'mailingListLanguageInput' ).value.trim() : "";

			var email = document.getElementById( 'mailingListEmail' ).value;
			var passed = true;
			
			if( mailingList == "none" ) {
				jQuery( '#mailingListLanguage' ).css( { "border": '#FF0000 1px solid' } );
				passed = false;
			} else {
				jQuery( '#mailingListLanguage' ).css( { "border": 'none' } );
			}

			if( mailingList == "LAUNCH_ANNOUNCEMENT_OTHER" && comment == "" ) {
				jQuery( '#mailingListLanguageInput' ).css( { "border": '#FF0000 1px solid' } );
				passed = false;
			} else {
				jQuery( '#mailingListLanguage' ).css( { "border": 'none' } );
			}

			if( email.trim() == "" || !validateEmail( email ) ) {
				jQuery( '#mailingListEmail' ).css( { "border": '#FF0000 1px solid' } );
				passed = false;
			} else {
				jQuery( '#mailingListEmail' ).css( { "border": 'none' } );
			}
			
			if( !passed )
				return;

			<#-- Spinner active -->
			jQuery( '#notify-me-wrapper' ).height( jQuery( '#notify-me-wrapper' ).height() );
			document.getElementById( "notify-elements" ).style.display = "none";
			document.getElementById( 'notify-loader' ).style.display = "block";

			var body = { 
					'email': email, 
					'mailingList': mailingList,
			};
			if( comment != "" )
					body[ 'comment' ] = comment;

			$.ajax({
				type: 'post',
				url: '/api/mailinglist/subscribe',
				data: body,
				success: function( response ) {
					document.getElementById( 'notify-loader' ).style.display = "none";
					document.getElementById( 'mailingListLanguage' ).value = "none";
					document.getElementById( 'mailingListLanguageInput' ).style.display = "none";
					document.getElementById( 'mailingListEmail' ).value = null;
					document.getElementById( "notify-message-text" ).innerText = "Thank You! You will be notified when we launch the language!";
					document.getElementById( "notify-message" ).style.display = "block";
				},
				error: function( response ) {
					var messageJson = jQuery.parseJSON( response.responseText );
					var message = "";
					if( messageJson["message"] != null )
						message = messageJson["message"];
					else
						message = "Failed due to some reason! Please try again!";

					document.getElementById( 'notify-loader' ).style.display = "none";
					document.getElementById( "notify-message-text" ).innerText = message;
					document.getElementById( "notify-message" ).style.display = "block";
				}
			});
		}
	</script>
	<#include "meta/GoogleAnalytics.ftl">
</body>

</html>
