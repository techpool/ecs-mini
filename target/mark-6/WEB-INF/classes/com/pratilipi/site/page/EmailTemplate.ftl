<!DOCTYPE html>
<html lang="${lang}">
	<head>
		<title>${ title }</title>
		<script src="https://0.ptlp.co/resource-all/jquery.knockout.boostrap.js" type="text/javascript"></script>
		<link rel='stylesheet' href='https://1.ptlp.co/third-party/bootstrap-3.3.4/css/bootstrap.min.css'>
	</head>
	<body>
		<style>
			*[data-key] {
				border: 2px solid #107fe5;
				padding: 4px;
				border-radius: 4px;
				outline: none;
			}
		</style>
		<div style="max-width: 600px; margin: auto; border: 1px solid; padding: 12px;">
			${ body }
		</div>
		<button style="margin: 32px auto; display: block;" onClick="saveChanges()" class="btn btn-primary">Save Changes</button>
		<script>
			$( document ).ready( function() {
				$( '[data-key]' ).each( function( index ) {
					$(this).attr( 'contenteditable','true' );
				});
				$( '[subject]' ).each( function( index ) {
					$(this).css( 'display', 'block' );
					$(this).before( "<span>Subject</span>" );
				});
				$( "a" ).click( function() { return false; } );
			});
			function saveChanges() {
				var data = {};
				$( '[data-key]' ).each( function( index ) {
					data[ $( this ).attr( "data-key" ) ] = $( this ).text().replace( /<!--.+?-->/g, "" ).trim();
				});

				$.ajax({

					type: 'post',
					url: '/api/i18n',

					data: { 
						'language': "${ language }", 
						'group': "EMAIL",
						'keyValues': JSON.stringify( data )
					},

					success: function( response ) {
						alert( "success" );
						window.location.reload();
					},

					error: function( response ) {
						console.log( "fail" );
					}

				});
			}
		</script>
	</body>
</html>