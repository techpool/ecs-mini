<#macro compress_single_line>
<#local captured><#compress><#nested></#compress></#local>
${ captured?replace( "^\\s+|\\s+$|\\n|\\r", " ", "rm" ) }
</#macro>
<#if stage=="prod">
<@compress_single_line><#include "${ templateName }"></@compress_single_line>
<#else>
<#include "${ templateName }">
</#if>