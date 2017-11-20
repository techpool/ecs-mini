<!doctype html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="description" content="A front-end template that helps you build fast, modern mobile web apps.">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
		<title>${ title }</title>
		<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.grey-orange.min.css">
		<style>
			.demo-card-wide {
				margin: auto;
				cursor: pointer;
				-webkit-transition: all 0.2s ease;
				-moz-transition: all 0.2s ease;
				-o-transition: all 0.2s ease;
				transition: all 0.2s ease;
			}
			.demo-card-wide:hover{
				z-index:1000;
				box-shadow:rgba(0, 0, 0, 0.3) 0 16px 16px 0;
				-webkit-box-shadow:rgba(0, 0, 0, 0.3) 0 16px 16px 0;
				-moz-box-shadow:rgba(0, 0, 0, 0.3) 0 16px 16px 0;
			}
			.demo-card-wide.mdl-card {
				width: 512px;
			}
			.demo-card-wide > .mdl-card__title {
				color: #fff;
				height: 176px;
			}
			.demo-card-wide > .mdl-card__menu {
				color: #fff;
			}
			.authors {
				background: url('http://public.pratilipi.com/images/authors.jpg') center / cover;
			}
			.emails {
				background: url('http://public.pratilipi.com/images/emails.png') center / cover;
			}
			.translations {
				background: url('http://public.pratilipi.com/images/translations.jpg') bottom / cover;
			}
			.notifications {
			 	background: url('http://public.pratilipi.com/images/notifications.png') bottom / cover; 
			}
		</style>
	</head>
	<body>
		<div style="padding: 64px 0px;">
			<div class="mdl-grid">
				<div class="mdl-cell mdl-cell--6-col">
					<div class="demo-card-wide mdl-card mdl-shadow--2dp authors" onclick="window.location='/admin/authors';">
						<div class="mdl-card__title"></div>
						<div class="mdl-card__actions mdl-card--border">
							<span class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" style="color: #000; font-weight: 700;">
								List all Authors | Add Author | Edit Author Info
							</span>
						</div>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--6-col">
					<div class="demo-card-wide mdl-card mdl-shadow--2dp emails" onclick="window.location='/admin/email-templates';">
						<div class="mdl-card__title"></div>
						<div class="mdl-card__actions mdl-card--border">
							<span class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" style="color: #333; font-weight: 700;">
								Configure Email Templates
							</span>
						</div>
					</div>
				</div>

				<div class="mdl-cell mdl-cell--12-col" style="min-height: 32px;"></div>

				<div class="mdl-cell mdl-cell--6-col">
					<div class="demo-card-wide mdl-card mdl-shadow--2dp translations" onclick="window.location='/admin/translations';">
						<div class="mdl-card__title"></div>
						<div class="mdl-card__actions mdl-card--border">
							<span class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" style="color: #333; font-weight: 700;">
								Translations
							</span>
						</div>
					</div>
				</div>
				<div class="mdl-cell mdl-cell--6-col">
					<div class="demo-card-wide mdl-card mdl-shadow--2dp notifications" onclick="window.location='/admin/batch-process';">
						<div class="mdl-card__title"></div>
						<div class="mdl-card__actions mdl-card--border">
							<span class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" style="color: #333; font-weight: 700;">
								Notifications
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script src="https://code.getmdl.io/1.3.0/material.min.js"></script>
	</body>
</html>
