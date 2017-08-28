<#macro compress_single_line>
<#local captured><#compress><#nested></#compress></#local>
${ captured?replace( "^\\s+|\\s+$|\\n|\\r", " ", "rm" ) }
</#macro>

<#macro add_backslashes>
<#local captured><#compress><#nested></#compress></#local>
${ "`" + captured?replace("\\n", "", 'r') + "`" }
</#macro>

<#macro register element>
	<script>
		ko.components.register( '${ element }', {
			<#attempt>viewModel: <@compress_single_line><#include "../pwa-elements/${ element }/${ element }.js"></@compress_single_line>,<#recover></#attempt>
		    <#attempt>template: <@add_backslashes><#include "../pwa-elements/${ element }/${ element }.html"></@add_backslashes><#recover></#attempt>
		});
	</script>
</#macro>

<#macro script src>
	<script>
		<@compress_single_line><#include "../pwa-scripts/${ src }"></@compress_single_line>
	</script>
</#macro>
